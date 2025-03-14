<script setup lang="ts">
import { ref, nextTick, onBeforeUnmount, inject, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElScrollbar } from 'element-plus'
import { createSession, getHistory, getUserSessions, sendMessageNoStream, updateSessionTitle, CHAT_STREAM_RAG, CHAT_STREAM_DEFAULT } from '../../api/chat'
import { getRiskSignals } from '../../api/risk_signal'
import { parseCurrencyName, parseTime } from '../../utils'
import { Plus, ChatLineRound, ChatLineSquare, ArrowUp } from '@element-plus/icons-vue'
import MarkdownIt from 'markdown-it'
import { fetchEventSource } from '@microsoft/fetch-event-source';

type SessionRecord = {
  id: number
  userId: number
  title: string
  createTime: string
  updateTime: string
}

type ChatRecord = {
  id?: number
  createTime?: string
  userId: number
  direction: boolean
  sessionId: number
  content: string
}

const router = useRouter()
// 用户id, 从sessionStorage获取
const userId = ref<number>(Number(sessionStorage.getItem('userId')))
// 当前会话id, 默认为0, 即无对话
const currentSessionId = ref<number>(0)
const currentSessionTitle = ref('')
// 所有会话
const sessions = ref<SessionRecord[]>([])
// 当前会话的聊天记录
const messages = ref<ChatRecord[]>([])
// 所有风险信号
const riskSignals = ref<any[]>([])
// 当前输入框内容
const inputMessage = ref('')
// 加载中
const isLoading = ref(false)
// Markdown 渲染器
const md = new MarkdownIt();
// 流式输出
const streamOutput = ref(true)
const isStreaming = ref(false)
const abortController = ref<AbortController | null>(null)
// 知识库开关
const knowledgeBaseEnabled = ref(false)
// 模型选择
const selectedModel = ref('deepseek-r1')
const availableModels = [
  { label: 'DeepSeek-R1 (默认)', value: 'deepseek-r1' },
  { label: 'QWQ Plus Latest', value: 'qwq-plus-latest' },
  { label: 'DeepSeek V3', value: 'deepseek-v3' },
  { label: 'DeepSeek-R1 Distill QWen-32B', value: 'deepseek-r1-distill-qwen-32b' }
]

// 窗口监听
const viewport = inject('viewport', {
  isMobile: ref(false),
  viewportWidth: ref(0),
  breakpoints: { md: 768 }
})
const sidebarOpen = ref(true)

const inputDisabled = computed(() => {
  return isLoading.value || isStreaming.value || currentSessionId.value === 0
})

const messagesContainer = ref<InstanceType<typeof ElScrollbar> | null>(null)

onMounted(() => {
  if (viewport.isMobile.value) {
    sidebarOpen.value = false
  }
})

onBeforeUnmount(() => {
  if (abortController.value) {
    abortController.value.abort()
    abortController.value = null
  }
})

const loadData = async () => {
  getUserSessions(userId.value).then((res) => {
    if (res.data.code === '000') {
      // 加载所有会话, 按更新时间倒序排序
      sessions.value = res.data.result.sort((a: SessionRecord, b: SessionRecord) =>
        b.updateTime.localeCompare(a.updateTime)
      )
    } else {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg
      })
    }
  })
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
loadData()

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
      sessions.value.unshift({ id: newSessionId, userId: userId.value, title: '新会话', createTime: new Date().toISOString(), updateTime: new Date().toISOString() })
      currentSessionId.value = newSessionId
      currentSessionTitle.value = '新会话'
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
    if (viewport.isMobile.value) {
      sidebarOpen.value = false
    }
  })
}
// 选择会话
const handleSelectSession = (sessionId: number) => {
  if (currentSessionId.value === sessionId) return
  currentSessionId.value = sessionId
  loadChatHistory(sessionId)
  currentSessionTitle.value = sessions.value.find(session => session.id == sessionId)?.title || ''
  if (viewport.isMobile.value) {
    sidebarOpen.value = false
  }
}

// 加载聊天历史记录
const loadChatHistory = async (sessionId: number) => {
  if (!userId.value) return
  isLoading.value = true
  getHistory(sessionId, userId.value).then(res => {
    if (res.data.code === '000') {
      messages.value = res.data.result.map((msg: ChatRecord) => ({
      ...msg,
      content: replaceRefTags(msg.content)
      }))
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
};

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

  // 更新会话
  updateSession(messageToSend)

  if (streamOutput.value) {
    await handleStreamMessage(messageToSend)
  } else {
    await handleNoStreamMessage(messageToSend)
  }
}

// 非流式消息处理
const handleNoStreamMessage = async (messageToSend: string) => {
  isLoading.value = true
  sendMessageNoStream({
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

// 流式消息处理 Get请求 适配后端
const handleStreamMessage = async (messageToSend: string) => {
  if (abortController.value) {
    abortController.value.abort()
    abortController.value = null
  }

  isStreaming.value = true;
  isLoading.value = true;

  // 创建初始的AI消息条目
  const aiMessageIndex = messages.value.length;
  messages.value.push({
    content: '',
    direction: false,
    userId: userId.value,
    sessionId: currentSessionId.value
  });
  scrollToBottom()

  const ctrl = new AbortController();
  abortController.value = ctrl;

  // 根据 knowledgeBaseEnabled 状态设置 CHAT_STREAM 的值
  const CHAT_STREAM = knowledgeBaseEnabled.value ? CHAT_STREAM_RAG : CHAT_STREAM_DEFAULT;

  try {
    let thought = '';
    let content = '';
    await fetchEventSource(`${CHAT_STREAM}?sessionId=${currentSessionId.value}&message=${messageToSend}&userId=${userId.value}&modelName=${selectedModel.value}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      signal: ctrl.signal,
      openWhenHidden: true,
      onmessage(ev) {
        if (ev.data.startsWith('<thought>')) {
          const thoughtContent = ev.data.replace('<thought>', '').replace('</thought>', '')
          thought += thoughtContent
          messages.value[aiMessageIndex].content = replaceRefTags('<thought>' + thought + '</thought>')
        } else {
          content += ev.data
          if (thought !== '') {
            messages.value[aiMessageIndex].content = replaceRefTags('<thought>' + thought + '</thought>' + content)
          } else {
            messages.value[aiMessageIndex].content = replaceRefTags(content)
          }
        }
        scrollToBottom()
      },
      onclose() {
        isLoading.value = false;
        isStreaming.value = false;
        abortController.value = null;
        loadChatHistory(currentSessionId.value)
      },
      onerror(err) {
        ElMessage.error('请求失败')
        messages.value.pop()
        ctrl.abort()
        isLoading.value = false;
        isStreaming.value = false;
        abortController.value = null;
        throw err
      },
    });
  } catch (err) {
    if (abortController.value) abortController.value.abort()
  } finally {
    isLoading.value = false;
    isStreaming.value = false;
    abortController.value = null;
  }
};

// 终止流式消息
const handleStopStream = () => {
  if (abortController.value) {
    abortController.value.abort()
    abortController.value = null
    isStreaming.value = false
  }
}

// 更新会话
const updateSession = (message: string) => {
  const sessionIndex = sessions.value.findIndex(s => s.id === currentSessionId.value)
  if (sessionIndex === -1) return

  sessions.value[sessionIndex].updateTime = new Date().toISOString()
  sessions.value = sessions.value.sort((a: SessionRecord, b: SessionRecord) =>
    b.updateTime.localeCompare(a.updateTime)
  )
  if (messages.value.length === 1) {
    // 第一次发送信息时需更新会话标题
    const trimmed = message.trim()
    const title = trimmed.length > 5
      ? trimmed.substring(0, 5) + '...'
      : trimmed
    sessions.value[sessionIndex].title = title
    currentSessionTitle.value = title
  }
  updateSessionTitle(userId.value, currentSessionId.value, sessions.value[sessionIndex].title).then(res => {
    if (res.data.code !== '000') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

// 插入风险信号文本
const handleInsertRiskSignal = (signal: any) => {
  if (!signal) return

  const signalText = `风险信号: 基准货币: ${parseCurrencyName(signal.baseCurrency)}, 报价货币: ${parseCurrencyName(signal.targetCurrency)}, 时间: ${parseTime(signal.time)}, ` +
    `EMP: ${signal.emp}, 汇率: ${signal.exchangeRate}, 利率: ${signal.interestRate}, 外汇储备: ${signal.fxReserves}.`
  inputMessage.value += inputMessage.value ? `\n${signalText}` : signalText
}

// 消息解析
const parseThoughtContent = (content: string) => {
  const thoughtMatch = content.match(/<thought>([\s\S]*?)<\/thought>/)
  return thoughtMatch ? thoughtMatch[1].trim().replace(/\n/g, '<br>') : ''
}

const parseResponseContent = (content: string) => {
  return content.replace(/<thought>[\s\S]*?<\/thought>/, '').trim()
}

// 替换 <ref> 标签为 Markdown 脚注格式
const replaceRefTags = (text: string) => {
  return text.replace(/<ref>\[(.*?)\]<\/ref>/g, '[$1]');
}


const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      const scrollbar = messagesContainer.value.$el.querySelector('.el-scrollbar__wrap');
      if (scrollbar) {
        scrollbar.scrollTop = scrollbar.scrollHeight;
      }
    }
  })
}

</script>

<template>
  <el-container>
    <!-- 桌面端侧边栏 -->
    <el-aside v-if="!viewport.isMobile.value" class="chat-aside" :width="sidebarOpen ? '220px' : '0'">
      <div class="chat-aside-header">
        <el-button type="primary" @click="handleCreateSession" :loading="isLoading" round>
          <el-icon>
            <Plus />
          </el-icon>新对话
        </el-button>
        <el-button plain round @click="sidebarOpen = !sidebarOpen" :icon="ChatLineSquare" class="sidebar-toggle" />
      </div>
      <el-scrollbar class="chat-sessions">
        <el-menu :default-active="String(currentSessionId)" @select="handleSelectSession" class="session-list">
          <el-menu-item v-for="session in sessions" :key="session.id" :index="String(session.id)"
            :class="{ 'is-active': session.id === currentSessionId }">
            <el-icon>
              <ChatLineRound />
            </el-icon>
            <span>{{ session.title === '' ? '新会话' : session.title }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-drawer v-else v-model="sidebarOpen" :with-header="false" direction="ltr">
      <div class="chat-aside-header">
        <el-button type="primary" @click="handleCreateSession" :icon="Plus" :loading="isLoading" round />
      </div>
      <el-scrollbar>
        <el-menu :default-active="String(currentSessionId)" @select="handleSelectSession" class="session-list">
          <el-menu-item v-for="session in sessions" :key="session.id" :index="String(session.id)"
            :class="{ 'is-active': session.id === currentSessionId }">
            <el-icon>
              <ChatLineRound />
            </el-icon>
            <span>{{ session.title }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-drawer>

    <el-main>
      <!-- 右侧聊天主区域 -->
      <div class="chat-main">
        <!-- 标题栏 -->
        <div class="chat-main-header">
          <el-button v-if="!sidebarOpen || viewport.isMobile.value" plain round @click="sidebarOpen = !sidebarOpen"
            :icon="ChatLineSquare" class="sidebar-toggle" />
          <h2 v-show="currentSessionId != 0"> {{ currentSessionTitle === '' ? '新会话' : currentSessionTitle }} </h2>
        </div>

        <!-- 消息区 -->
        <el-scrollbar v-if="currentSessionId != 0" class="chat-messages" ref="messagesContainer">
          <div v-for="(msg, index) in messages" :key="index">
            <el-row :justify="msg.direction ? 'end' : 'start'">
              <el-col>
                <el-card shadow="never" :class="['message-bubble', msg.direction ? 'user-message' : 'ai-message']"
                  body-style="padding:12px 16px; display: inline-block">
                  <template v-if="!msg.direction">
                    <div v-if="msg.content.includes('<thought>')">
                      <!-- 思考过程 -->
                      <div class="thought-bubble">
                        <div v-html="parseThoughtContent(msg.content)"></div>
                      </div>
                      <!-- 正式回复 -->
                      <div class="md-content" v-html="md.render(replaceRefTags(parseResponseContent(msg.content)))">
                      </div>
                    </div>
                    <div v-else class="md-content" v-html="md.render(replaceRefTags(msg.content))"></div>
                  </template>
                  <div v-else v-html="msg.content"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-scrollbar>
        <div v-else class="no-session">
          <el-icon class="empty-icon">
            <ChatLineRound />
          </el-icon>
          <p class="empty-text">{{ currentSessionId == 0 ? '请选择或新建会话' : '您可以询问任何外汇风险相关问题' }}</p>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input">
          <div class="input-row">
            <el-dropdown trigger="click" @command="handleInsertRiskSignal" placement="top">
              <el-button type="primary" :icon="Plus" plain circle :disabled="inputDisabled" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-for="signal in riskSignals" :key="signal.id" :command="signal">
                    {{ parseCurrencyName(signal.baseCurrency) }}/{{ parseCurrencyName(signal.targetCurrency) }}-{{
                      parseTime(signal.time) }}
                  </el-dropdown-item>
                  <el-dropdown-item v-if="riskSignals.length === 0" disabled>
                    无可用风险信号
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <el-input v-model="inputMessage" type="textarea" :rows="3" resize="none" placeholder="输入您的问题..."
              @keyup.enter="handleSendMessage" :disabled="inputDisabled">
            </el-input>
          </div>

          <div class="input-footer">
            <div class="input-tips">
              {{ viewport.isMobile.value ? '' : '输入问题后按 Enter 发送，或点击发送按钮' }}
            </div>
            <div class="button-group">
              <el-select v-if="streamOutput" v-model="selectedModel" 
                :style="{ 'width': viewport.isMobile.value ? '100px' : '180px' }"
                :disabled="inputDisabled"
                :suffix-icon="ArrowUp">
                <el-option v-for="model in availableModels" :key="model.value" :label="model.label"
                  :value="model.value">
                </el-option>
              </el-select>
              <el-switch v-model="streamOutput" :inactive-text="viewport.isMobile.value ? '标准' : '标准输出'"
                :active-text="viewport.isMobile.value ? '流式' : '流式输出'"
                :disabled="isLoading" />
              <el-checkbox v-model="knowledgeBaseEnabled" :disabled="isLoading">知识库</el-checkbox>
              <el-button v-if="isStreaming" type="danger" @click="handleStopStream">中止</el-button>
              <el-button v-else type="primary" @click="handleSendMessage" :loading="isLoading"
                :disabled="inputDisabled">发送</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
:deep(.el-aside) {
  max-height: calc(100vh - 3.5rem);
  transition: width 0.2s ease-in-out;
}

:deep(.el-main) {
  height: calc(100vh - 3.5rem);
  padding: 0;
}

:deep(.el-drawer) {
  width: 220px !important;
  height: 100vh !important;
  overflow: hidden;
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

:deep(.el-drawer__body) {
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-aside {
  height: 100%;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.chat-aside-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem 1rem;
}

.chat-sessions {
  flex: 1;
  overflow-y: auto;
}


.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: var(--el-bg-color-page);
}

.chat-main-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 1rem 1rem;
  border-bottom: 1px solid var(--el-border-color-light);

  h2 {
    margin: 0;
    font-size: 18px;
    color: var(--el-text-color-primary);
  }
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
}

.message-bubble {
  width: fit-content;
  max-width: 80%;
  margin: 8px 0;
  border-radius: 12px;
  transition: all 0.3s;
}

.thought-bubble {
  background-color: var(--el-border-color-lighter);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 16px;
  font-size: 0.9em;
  color: var(--el-text-color-secondary);
}

.user-message {
  background-color: var(--el-color-primary-light-9);
  border: 1px solid var(--el-color-primary-light-7);
  margin-left: auto;
}

.ai-message {
  background-color: var(--el-bg-color);
  border: 1px solid var(--el-border-color-light);
  margin-right: auto;
}

.md-content :deep(pre) {
  background-color: var(--el-fill-color-light);
  padding: 12px;
  border-radius: 8px;
  margin: 8px 0;
}

.md-content :deep(code) {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.9em;
}

.no-session {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;

  .empty-icon {
    font-size: 3rem;
    color: var(--el-text-color-placeholder);
    opacity: 0.6;
    animation: float 3s ease-in-out infinite;
  }

  .empty-text {
    color: var(--el-text-color-secondary);
    font-size: 1.2rem;
    letter-spacing: 2px;
  }
}

.chat-input {
  padding: 20px;
  border-top: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.input-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button-group {
  display: flex;
  align-items: center;
  gap: 16px;
}

.input-tips {
  font-size: 0.8em;
  color: var(--el-text-color-secondary);
}
</style>