<!-- 返回示例：
如果需要在前端显示thought的内容 
{
    "output": {
        "thoughts": [
            {
                "thought": "好的，我现在需要处理用户的问题：“你是谁？”首先，用户是在询问我的身份，我需要用简单明了的方式回答，同时保持友好。根据之前的历史记录，用户可能已经知道我是DeepSeek-R1，但可能需要更详细的介绍。\n\n我应该先明确回答我是由深度求索公司开发的智能助手DeepSeek-R1。然后，可能需要补充我的功能和用途，比如帮助回答问题、提供信息等。还要注意保持口语化，避免过于正式，让用户感觉自然。\n\n接下来，我需要检查是否有必要提到当前时间，但用户的问题并不涉及时间，所以可能不需要。不过，之前的回复中系统时间显示是2025年，这可能是一个测试环境的时间设置，但用户的问题不相关，所以不用提及。\n\n然后，我要确保回答简洁，同时覆盖用户可能关心的点，比如我的开发公司、功能、如何帮助用户等。最后，以友好的语气结束，邀请用户提问。需要避免使用任何Markdown格式，保持纯文本，自然分段。\n\n可能还需要考虑用户是否有后续问题，所以保持回答开放，鼓励用户继续交流。检查是否有拼写错误或语法错误，确保回答流畅准确。",
                "action_type": "reasoning",
                "response": "好的，我现在需要处理用户的问题：“你是谁？”首先，用户是在询问我的身份，我需要用简单明了的方式回答，同时保持友好。根据之前的历史记录，用户可能已经知道我是DeepSeek-R1，但可能需要更详细的介绍。\n\n我应该先明确回答我是由深度求索公司开发的智能助手DeepSeek-R1。然后，可能需要补充我的功能和用途，比如帮助回答问题、提供信息等。还要注意保持口语化，避免过于正式，让用户感觉自然。\n\n接下来，我需要检查是否有必要提到当前时间，但用户的问题并不涉及时间，所以可能不需要。不过，之前的回复中系统时间显示是2025年，这可能是一个测试环境的时间设置，但用户的问题不相关，所以不用提及。\n\n然后，我要确保回答简洁，同时覆盖用户可能关心的点，比如我的开发公司、功能、如何帮助用户等。最后，以友好的语气结束，邀请用户提问。需要避免使用任何Markdown格式，保持纯文本，自然分段。\n\n可能还需要考虑用户是否有后续问题，所以保持回答开放，鼓励用户继续交流。检查是否有拼写错误或语法错误，确保回答流畅准确。",
                "action_name": "思考过程",
                "action": "reasoning"
            }
        ],
        "finish_reason": "stop",
        "session_id": "41bb8c9b3d6649eb8bac64874fce950c",
        "text": "我是由中国的深度求索（DeepSeek）公司开发的智能助手DeepSeek-R1。我擅长通过思考来帮您解答复杂的数学，代码和逻辑推理等理工类问题，并能用更贴近自然的口语化风格与您交流。有什么我可以帮您的吗？"
    },
    "usage": {
        "models": [
            {
                "output_tokens": 301,
                "model_id": "deepseek-r1",
                "input_tokens": 32
            }
        ]
    },
    "request_id": "ff90f771-70db-9324-ba48-cb1236dacc93"
} -->

<script setup lang="ts">
import { ref, computed, nextTick, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createSession, getHistory, ChatRecord } from '../api/chat'
import { getRiskSignals } from '../api/risk_signal'
import { parseCurrencyName } from '../utils'
import { Plus, ChatLineRound, Position } from '@element-plus/icons-vue'
import axios from 'axios'
import MarkdownIt from 'markdown-it'

const router = useRouter()
// 用户id, 从sessionStorage获取
const userId = ref<number>(Number(sessionStorage.getItem('userId')))
// 当前会话id, 从sessionStorage获取
const currentSessionId = ref<number>(Number(sessionStorage.getItem('currentSessionId')))
// 当前会话的所有聊天记录
const messages = ref<ChatRecord[]>([])
// 所有会话
const sessions = ref<{ id: number; title: string }[]>([])
// 所有风险信号
const riskSignals = ref<any[]>([])
// 当前输入框内容
const inputMessage = ref('')
// 加载中
const isLoading = ref(false)

const messagesContainer = ref<HTMLElement | null>(null)
//导入Markdown-it
const md = new MarkdownIt();

// 获取当前会话标题
const currentSessionTitle = computed(() => {
  const session = sessions.value.find(s => s.id === currentSessionId.value)
  return session?.title || ''
})


// 流式输出相关变量
const useStreamOutput = ref(true) // 是否使用流式输出
const abortController = ref<AbortController | null>(null) // 用于取消请求的控制器
const isStreaming = ref(false) // 是否正在流式输出中
// 创建一个缓冲区变量
let buffer = '';

// 在组件销毁前取消未完成的流式请求
onBeforeUnmount(() => {
  if (abortController.value) {
    abortController.value.abort()
    abortController.value = null
  }
})
loadSeesion()
loadData()
// 加载会话
async function loadSeesion() {
  const savedSessions = localStorage.getItem('chatSessions')
  if (savedSessions) {
    sessions.value = JSON.parse(savedSessions)
    const lastSessionId = localStorage.getItem('currentSessionId')
    if (lastSessionId) {
      currentSessionId.value = Number(lastSessionId)
      await loadChatHistory(currentSessionId.value)
    }
  } else if (sessions.value.length === 0) {
    handleCreateSession()
  }
}

function loadData() {
  getRiskSignals().then((res) => {
    if (res.data.code === '000') {
      riskSignals.value = res.data.result.records
    } else {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg
      })
    }
  })
}

// 创建新会话
const handleCreateSession = async () => {
  if (!userId.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  isLoading.value = true

  createSession(userId.value).then(res => {
    if (res.data.code === '000') {
      const newSessionId = res.data.result

      const newSession = {
        id: newSessionId,
        // 以日期为标题
        title: `新对话 ${new Date().toLocaleString('zh-CN', { month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric' })}`
      }

      sessions.value = [newSession, ...sessions.value]
      localStorage.setItem('chatSessions', JSON.stringify(sessions.value))

      currentSessionId.value = newSessionId
      localStorage.setItem('currentSessionId', String(newSessionId))

      messages.value = []
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  }).finally(() => {
    isLoading.value = false
  })
}

// 加载聊天历史记录
const loadChatHistory = async (sessionId: number) => {
  if (!userId.value) return;
  isLoading.value = true;
  getHistory(sessionId, userId.value).then(res => {
    if (res.data.code === '000') {
      // 处理消息中的思考块
      const processedMessages = res.data.result.map((message: ChatRecord) => {
        // 只处理AI的回复，用户的消息保持不变
        if (!message.direction) {
          // 提取所有思考块
          const thoughtRegex = /<thought>([\s\S]*?)<\/thought>/g;
          let match;
          let thoughtContent = '';
          let normalContent = message.content;

          // 收集所有思考内容
          while ((match = thoughtRegex.exec(message.content)) !== null) {
            thoughtContent += match[1];
            // 从原内容中移除思考块
            normalContent = normalContent.replace(match[0], '');
          }

          if (thoughtContent) {
            // 如果有思考内容，添加到消息的开头
            message.content = `<em class="text-gray-500 italic">${thoughtContent}</em>` +
              (normalContent.trim() ? '\n' : '') +
              md.render(normalContent.trim());
          } else {
            // 没有思考内容，使用原始内容
            message.content = md.render(normalContent);
          }
        }
        return message;
      });

      messages.value = processedMessages;
      scrollToBottom();
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      });
    }
  }).finally(() => {
    isLoading.value = false;
  });
};
// 选择会话
const handleSelectSession = (sessionId: number) => {
  if (currentSessionId.value === sessionId) return
  currentSessionId.value = sessionId
  localStorage.setItem('currentSessionId', String(sessionId))
  loadChatHistory(sessionId)
}

// 发送信息
const handleSendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value || !userId.value || !currentSessionId.value) return

  // 更新聊天框
  messages.value.push({
    content: inputMessage.value,
    direction: true,
    userId: userId.value,
    sessionId: currentSessionId.value
  })

  // 清空输入框
  const messageToSend = inputMessage.value
  inputMessage.value = ''
  scrollToBottom()

  // 修改会话标题
  if (messages.value.length === 1) {
    updateSessionTitle(messageToSend)
  }

  isLoading.value = true

  // 根据配置选择使用流式输出或普通输出
  if (useStreamOutput.value) {
    await handleStreamMessage(messageToSend)
  } else {
    await handleNormalMessage(messageToSend)
  }
}

// 普通消息发送模式（保留原有逻辑）
const handleNormalMessage = async (messageToSend: string) => {
  // 发送信息
  axios.post('/api/chat/noStream', {
    sessionId: currentSessionId.value,
    message: messageToSend,
    userId: userId.value
  }).then(res => {
    if (res.data.code === '000') {
      messages.value.push({
        content: res.data.result,
        direction: false,
        userId: userId.value,
        sessionId: currentSessionId.value
      })
      scrollToBottom()
    } else {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  }).finally(() => {
    isLoading.value = false
  })
}


// 流式消息处理// 流式消息处理
const handleStreamMessage = async (messageToSend: string) => {
  try {
    // 创建一个空的AI回复消息
    const aiMessageIndex = messages.value.length
    messages.value.push({
      content: '',
      direction: false,
      userId: userId.value,
      sessionId: currentSessionId.value
    })
    scrollToBottom()

    // 创建AbortController用于可能的取消操作
    abortController.value = new AbortController()
    isStreaming.value = true

    // 使用EventSource处理SSE流
    // 修改为后端实际提供的路径
    //const eventSource = new EventSource(`http://localhost:8080/api/chat/stream?sessionId=${currentSessionId.value}&userId=${userId.value}&message=${encodeURIComponent(messageToSend)}`)
    const eventSource = new EventSource(`http://47.96.147.149:8080/api/chat/stream?sessionId=${currentSessionId.value}&userId=${userId.value}&message=${encodeURIComponent(messageToSend)}`)

    // 创建一个单独的思考过程区域
    let currentThoughtBlock = ''
    eventSource.onmessage = (event) => {
      // 接收到消息时的处理
      let chunk = event.data; // 去除多余的换行符

      if (chunk) {

        // 检查是否是思考过程
        if (chunk.startsWith('<thought>')) {
          //<thought>xxx</thought>
          const thoughtContent = chunk.substring(9, chunk.length - 10);

          // 累积思考内容，添加到当前思考块
          currentThoughtBlock += thoughtContent;

          // 更新思考块，使用更优化的方法
          // 检查是否已经有思考块
          if (messages.value[aiMessageIndex].content.includes('<em class="text-gray-500 italic">')) {
            // 已有思考块，替换思考块内容
            messages.value[aiMessageIndex].content = messages.value[aiMessageIndex].content.replace(
              /<em class="text-gray-500 italic">[\s\S]*?<\/em>/,
              `<em class="text-gray-500 italic">${currentThoughtBlock}</em>`
            );
          } else {
            // 没有思考块，添加新的思考块（只在开头和结尾添加一次换行符）
            const normalContent = messages.value[aiMessageIndex].content;
            messages.value[aiMessageIndex].content = normalContent +
              (normalContent.trim() ? '\n' : '') +
              `<em class="text-gray-500 italic">${currentThoughtBlock}</em>`;
            scrollToBottom();
          }
        } else {
          buffer += chunk

          // 尝试检测是否完整的Markdown段落
          const lines = buffer.split('\n')
          if (lines[lines.length - 1].startsWith('```') || lines.length > 2) {
            // 当检测到代码块结束或积累到多行时进行渲染
            messages.value[aiMessageIndex].content =
              md.render(buffer) +
              (currentThoughtBlock ? `<em>${currentThoughtBlock}</em>` : '')
            buffer = ''
            scrollToBottom()
          } else {
            // 临时显示原始内容（可选）
            messages.value[aiMessageIndex].content += chunk
            scrollToBottom()
          }
        }
      }
    };
    eventSource.onerror = () => {
      // 发生错误或流结束
      eventSource.close()
      isStreaming.value = false
      isLoading.value = false
      abortController.value = null
      // 处理剩余缓冲区内容
      if (buffer.length > 0) {
        messages.value[aiMessageIndex].content += md.render(buffer)
        buffer = ''
        scrollToBottom()
      }
    }

    // 添加取消监听
    if (abortController.value) {
      abortController.value.signal.addEventListener('abort', () => {
        eventSource.close()
      })
    }

  } catch (error) {
    if ((error as Error).name === 'AbortError') {
      ElMessage({
        message: '请求已取消',
        type: 'info',
        center: true,
      })
    } else {
      ElMessage({
        message: `请求失败: ${(error as Error).message}`,
        type: 'error',
        center: true,
      })

      // 如果流式请求失败，尝试使用普通方式重新发送
      await handleNormalMessage(messageToSend)
    }
    scrollToBottom()
  } finally {
    abortController.value = null
    isStreaming.value = false
    isLoading.value = false
  }
}

// 取消当前流式请求
const cancelStreamRequest = () => {
  if (abortController.value && isStreaming.value) {
    abortController.value.abort()
    abortController.value = null
    isStreaming.value = false
    isLoading.value = false

    ElMessage({
      message: '已取消响应',
      type: 'info',
      center: true,
    })
  }
}



// 更新会话标题
const updateSessionTitle = (message: string) => {
  const trimmed = message.trim()
  const title = trimmed.length > 20
    ? trimmed.substring(0, 20) + '...'
    : trimmed

  const sessionIndex = sessions.value.findIndex(s => s.id === currentSessionId.value)
  if (sessionIndex !== -1) {
    sessions.value[sessionIndex].title = title
    localStorage.setItem('chatSessions', JSON.stringify(sessions.value))
  }
}

// 插入风险信号文本
const handleInsertRiskSignal = (signal: any) => {
  if (!signal) return

  const signalText = `风险信号: 基准货币: ${parseCurrencyName(signal.baseCurrency)}, 报价货币: ${parseCurrencyName(signal.targetCurrency)}, 时间: ${formatDate(signal.time)}, ` +
    `EMP: ${signal.emp}, 汇率: ${signal.exchangeRate}, 利率: ${signal.interestRate}, 外汇储备: ${signal.fxReserves}.`
  inputMessage.value += inputMessage.value ? `\n${signalText}` : signalText
}

// 格式化时间
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

</script>
<template>
  <div class="flex h-screen bg-white">
    <!-- 侧边栏 -->
    <div class="sidebar border-r border-gray-200 flex flex-col">
      <!-- 创建新的对话 -->
      <div class="p-4">
        <el-button type="primary" class="w-full flex items-center justify-center" @click="handleCreateSession"
          :disabled="isLoading">
          <el-icon class="mr-2">
            <Plus />
          </el-icon> 新的对话
        </el-button>
      </div>

      <!-- 会话列表 -->
      <div class="flex-1 overflow-y-auto">
        <div v-for="session in sessions" :key="session.id"
          class="px-3 py-2 hover:bg-gray-100 cursor-pointer flex items-center"
          :class="{ 'bg-blue-50': session.id === currentSessionId }" @click="handleSelectSession(session.id)">
          <el-icon class="mr-2 text-gray-500">
            <ChatLineRound />
          </el-icon>
          <div class="truncate">{{ session.title || '新对话' }}</div>
        </div>
      </div>
    </div>

    <!-- 主界面 -->
    <div class="flex-1 flex flex-col">
      <!-- 标题 -->
      <div class="border-b border-gray-200 p-4 flex justify-between items-center">
        <h1 class="text-xl font-medium">
          {{ currentSessionTitle || '智能体' }}
        </h1>
      </div>

      <!-- 信息区 -->
      <div ref="messagesContainer" class="messages-container overflow-y-auto p-4 bg-white">
        <div v-if="messages.length === 0" class="h-full flex flex-col items-center justify-center text-gray-500">
          <el-icon :size="48" class="mb-4">
            <ChatLineRound />
          </el-icon>
          <p class="text-lg">开始一个新对话</p>
          <p class="text-sm mt-2">您可以询问任何外汇风险相关问题</p>
        </div>

        <div v-else>
          <div v-for="(message, index) in messages" :key="index" class="mb-4">
            <!-- 用户信息 -->
            <div v-if="message.direction" class="flex">
              <div class="flex-shrink-0 mr-3">
                <el-avatar :size="36" class="bg-blue-500">U</el-avatar>
              </div>
              <div class="flex-1">
                <div class="font-medium mb-1">您</div>
                <div class="text-gray-800 whitespace-pre-wrap">{{ message.content }}</div>
              </div>
            </div>

            <!-- 智能体信息 -->
            <div v-else class="flex">
              <div class="flex-shrink-0 mr-3">
                <el-avatar :size="36" class="bg-purple-500">AI</el-avatar>
              </div>
              <div class="flex-1">
                <div class="font-medium mb-1">RiskHunter AI</div>
                <!-- 使用 v-html 渲染带有样式的内容 -->
                <div class="text-gray-800 whitespace-pre-wrap" v-html="message.content"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-if="isLoading" class="flex items-center mt-4">
          <el-avatar :size="36" class="bg-purple-500 mr-3">AI</el-avatar>
          <el-skeleton :rows="2" animated />
        </div>
      </div>

      <!-- 输入框和流式输出开关容器 -->
      <div class="fixed-bottom-container fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 p-4">
        <div class="flex space-x-2">
          <!-- 风险信号 -->
          <el-dropdown trigger="click" @command="handleInsertRiskSignal" placement="top">
            <el-button type="primary" plain circle>
              <el-icon>
                <Plus />
              </el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="signal in riskSignals" :key="signal.id" :command="signal">
                  {{ parseCurrencyName(signal.baseCurrency) }}/{{ parseCurrencyName(signal.targetCurrency) }}-{{
                    formatDate(signal.time) }}
                </el-dropdown-item>
                <el-dropdown-item v-if="riskSignals.length === 0" disabled>
                  无可用风险信号
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <!-- 输入框 -->
          <div class="relative flex-1 ">
            <el-input v-model="inputMessage" type="textarea" :rows="3" resize="none" placeholder="输入您的问题..."
              class="pr-12" :disabled="isLoading || !currentSessionId"
              @keydown.enter.exact.prevent="handleSendMessage" />
            <el-button class="absolute bottom-2 right-2"
              :disabled="isLoading || !inputMessage.trim() || !currentSessionId" type="primary" circle
              @click="handleSendMessage">
              <el-icon>
                <Position />
              </el-icon>
            </el-button>
          </div>
        </div>
        <!-- 添加了流式输出开关 -->
        <div class="mt-2 flex justify-between items-center">
          <div class="text-xs text-gray-500">
            输入问题后按 Enter 发送，或点击发送按钮
          </div>

          <div class="flex items-center">
            <!-- 流式输出开关 -->
            <el-switch v-model="useStreamOutput" inactive-text="标准输出" active-text="流式输出" class="mr-4 "
              :disabled="isLoading" />

            <!-- 取消按钮，仅在流式输出时显示 -->
            <el-button v-if="isStreaming" size="small" type="danger" @click="cancelStreamRequest">
              取消响应
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

:deep(.text-gray-500) {
  color: #6B7280;
  font-style: italic;
  display: block;
  margin: 4px 0;
  /* 减少 margin */
  padding: 8px;
  /* 减少 padding */
  background-color: #F3F4F6;
  border-radius: 6px;
  border-left: 3px solid #9CA3AF;
  font-size: 0.95em;
  white-space: pre-wrap;
}
.sidebar {
  width: 16rem; /* 默认宽度 */
}

@media (max-width: 768px) {
  .sidebar {
    width: 12rem; /* 中等屏幕宽度 */
  }
}

@media (max-width: 640px) {
  .sidebar {
    width: 8rem; /* 小屏幕宽度 */
  }
}

.messages-container {
  height: calc(100vh - 275px);
  /* 减小消息显示区域的高度 */
}
</style>