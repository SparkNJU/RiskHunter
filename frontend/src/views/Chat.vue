<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createSession, sendMessage, getHistory, type ChatRecord } from '../api/chat'
import { getRiskSignals } from '../api/risk_signal'
import { parseCurrencyName } from '../utils'
import { Plus, ChatLineRound, Position } from '@element-plus/icons-vue'

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

// 获取当前会话标题
const currentSessionTitle = computed(() => {
  const session = sessions.value.find(s => s.id === currentSessionId.value)
  return session?.title || ''
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
  if (!userId.value) return
  isLoading.value = true
  getHistory(sessionId, userId.value).then(res => {
    if (res.data.code === '000') {
      messages.value = res.data.result
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

  // 发送信息
  sendMessage({
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
    <div class="w-64 border-r border-gray-200 flex flex-col">
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
      <div ref="messagesContainer" class="flex-1 overflow-y-auto p-4 bg-white">
        <div v-if="messages.length === 0" class="h-full flex flex-col items-center justify-center text-gray-500">
          <el-icon :size="48" class="mb-4">
            <ChatLineRound />
          </el-icon>
          <p class="text-lg">开始一个新对话</p>
          <p class="text-sm mt-2">您可以询问任何金融风险相关问题</p>
        </div>

        <div v-else>
          <div v-for="(message, index) in messages" :key="index" class="mb-6">
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
                <div class="text-gray-800 whitespace-pre-wrap">{{ message.content }}</div>
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

      <div class="border-t border-gray-200 p-4">
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
                {{ parseCurrencyName(signal.baseCurrency) }}/{{ parseCurrencyName(signal.targetCurrency) }}-{{ formatDate(signal.time) }}
                </el-dropdown-item>
                <el-dropdown-item v-if="riskSignals.length === 0" disabled>
                  无可用风险信号
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <!-- 输入框 -->
          <div class="relative flex-1">
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
        <div class="text-xs text-gray-500 mt-2 text-center">
          输入问题后按 Enter 发送，或点击发送按钮
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>