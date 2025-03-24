<script setup lang="ts">
import { ref, nextTick, onBeforeUnmount, inject, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElScrollbar, ElMessageBox } from 'element-plus'
import { createSession, getHistory, getUserSessions, sendMessageNoStream, updateSessionTitle, deleteSession, CHAT_STREAM_RAG, CHAT_STREAM_DEFAULT } from '../../api/chat'
import { getRiskSignals } from '../../api/risk_signal'
import { parseCurrencyName, parseTime } from '../../utils'
import { Plus, ChatLineRound, ChatLineSquare, ArrowUp, Edit, Delete } from '@element-plus/icons-vue'
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
// 添加编辑会话标题功能
const isEditing = ref(false)
const editingSessionId = ref<number | null>(null)
const editingTitle = ref('')

const handleEditSession = (event: Event, session: SessionRecord) => {
  // 阻止事件冒泡，避免触发 select 事件
  event.stopPropagation()

  editingSessionId.value = session.id
  editingTitle.value = session.title
  isEditing.value = true

  // 使用nextTick确保DOM已更新后聚焦输入框
  nextTick(() => {
    const inputEl = document.getElementById(`session-title-input-${session.id}`)
    if (inputEl) {
      inputEl.focus()
    }
  })
}

const saveSessionTitle = async () => {
  if (!editingSessionId.value || !editingTitle.value.trim()) {
    isEditing.value = false
    return
  }

  try {
    const res = await updateSessionTitle(userId.value, editingSessionId.value, editingTitle.value)

    if (res.data.code === '000') {
      const sessionIndex = sessions.value.findIndex(s => s.id === editingSessionId.value)
      if (sessionIndex !== -1) {
        sessions.value[sessionIndex].title = editingTitle.value

        // 如果正在编辑的是当前会话，更新当前会话标题
        if (currentSessionId.value === editingSessionId.value) {
          currentSessionTitle.value = editingTitle.value
        }
      }
      ElMessage.success('标题已更新')
    } else {
      ElMessage.error(res.data.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新标题失败:', error)
    ElMessage.error('更新失败，请重试')
  } finally {
    isEditing.value = false
    editingSessionId.value = null
  }
}

const cancelTitleEdit = () => {
  isEditing.value = false
  editingSessionId.value = null
}

// 添加删除会话功能
const handleDeleteSession = async (event: Event, sessionId: number) => {
  // 阻止事件冒泡
  event.stopPropagation()

  try {
    await ElMessageBox.confirm(
      '确定要删除这个会话吗？此操作不可恢复。',
      '删除会话',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const res = await deleteSession(sessionId, userId.value)

    if (res.data.code === '000') {
      // 从会话列表中移除
      sessions.value = sessions.value.filter(s => s.id !== sessionId)

      // 如果删除的是当前会话，重置当前会话
      if (currentSessionId.value === sessionId) {
        currentSessionId.value = 0
        currentSessionTitle.value = ''
        messages.value = []
      }

      ElMessage.success('会话已删除')
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除会话失败:', error)
      ElMessage.error('删除失败，请重试')
    }
  }
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

  // 构建查询参数，正确编码所有值
  const params = new URLSearchParams({
    sessionId: currentSessionId.value.toString(),
    message: messageToSend, // URLSearchParams 会自动编码
    userId: userId.value.toString(),
    modelName: selectedModel.value
  });
  try {
    let thought = '';
    let content = '';
    await fetchEventSource(`${CHAT_STREAM}?${params.toString()}`, {
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
// 预设问题列表
const suggestionQuestions = [
  '如何为小微企业设计针对外汇波动的套期保值方案？',
  '如何量化风险控制措施的成本收益比？',
  '汇率波动对企业短期现金流的影响如何量化？',
  '风险偏好差异如何影响企业选择风险自留与风险转移的决策边界？'
]

// 处理点击预设问题
interface SuggestionQuestion {
  question: string;
}

const insertSuggestion = (question: SuggestionQuestion['question']) => {
  inputMessage.value = question
  // 如果已选中会话，自动聚焦输入框
  if (currentSessionId.value !== 0) {
    nextTick(() => {
      const inputEl = document.querySelector('.chat-input .el-textarea__inner')
      if (inputEl) (inputEl as HTMLInputElement).focus()
    })
  } else {
    // 如果没有选中会话，自动创建新会话
    handleCreateSession().then(() => {
      inputMessage.value = question
    })
  }
}

// 添加显示欢迎卡片的计算属性
const showWelcomeCard = computed(() => {
  return currentSessionId.value === 0 || (currentSessionId.value !== 0 && messages.value.length === 0)
})
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
    <!-- 桌面端侧边栏 - 修改会话列表项 -->
    <el-aside v-if="!viewport.isMobile.value" class="chat-aside" :width="sidebarOpen ? '220px' : '0'">
      <!-- 头部不变 -->
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
            <!-- 非编辑状态 -->
            <template v-if="!(isEditing && editingSessionId === session.id)">
              <div class="session-item">
                <div class="session-info">
                  <el-icon>
                    <ChatLineRound />
                  </el-icon>
                  <span class="session-title">{{ session.title === '' ? '新会话' : session.title }}</span>
                </div>
                <div class="session-actions" @click.stop>
                    <el-button type="text" size="small" @click="(e: Event) => handleEditSession(e, session)"
                    class="action-button">
                    <el-icon>
                      <Edit />
                    </el-icon>
                    </el-button>
                  <el-button type="text" size="small" @click="(e: Event) => handleDeleteSession(e, session.id)"
                    class="action-button delete-button">
                    <el-icon>
                      <Delete />
                    </el-icon>
                  </el-button>
                </div>
              </div>
            </template>

            <!-- 编辑状态 -->
            <template v-else>
              <div class="session-edit">
                <el-input v-model="editingTitle" size="small" :id="`session-title-input-${session.id}`"
                  @keyup.enter="saveSessionTitle" @keyup.esc="cancelTitleEdit" @click.stop />
                <div class="edit-actions">
                  <el-button type="text" size="small" @click="saveSessionTitle" class="action-button">
                    确定
                  </el-button>
                  <el-button type="text" size="small" @click="cancelTitleEdit" class="action-button">
                    取消
                  </el-button>
                </div>
              </div>
            </template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 移动端侧边栏 - 同样需要修改 -->
    <el-drawer v-else v-model="sidebarOpen" :with-header="false" direction="ltr">
      <div class="chat-aside-header">
        <el-button type="primary" @click="handleCreateSession" :icon="Plus" :loading="isLoading" round />
      </div>
      <el-scrollbar>
        <el-menu :default-active="String(currentSessionId)" @select="handleSelectSession" class="session-list">
          <el-menu-item v-for="session in sessions" :key="session.id" :index="String(session.id)"
            :class="{ 'is-active': session.id === currentSessionId }">
            <!-- 非编辑状态 -->
            <template v-if="!(isEditing && editingSessionId === session.id)">
              <div class="session-item">
                <div class="session-info">
                  <el-icon>
                    <ChatLineRound />
                  </el-icon>
                  <span class="session-title">{{ session.title === '' ? '新会话' : session.title }}</span>
                </div>
                <div class="session-actions" @click.stop>
                  <el-button type="text" size="small" @click="(e: Event) => handleEditSession(e, session)"
                    class="action-button">
                    <el-icon>
                      <Edit />
                    </el-icon>
                  </el-button>
                  <el-button type="text" size="small" @click="(e: Event) => handleDeleteSession(e, session.id)"
                    class="action-button delete-button">
                    <el-icon>
                      <Delete />
                    </el-icon>
                  </el-button>
                </div>
              </div>
            </template>

            <!-- 编辑状态 -->
            <template v-else>
              <div class="session-edit">
                <el-input v-model="editingTitle" size="small" :id="`session-title-input-mobile-${session.id}`"
                  @keyup.enter="saveSessionTitle" @keyup.esc="cancelTitleEdit" @click.stop />
                <div class="edit-actions">
                  <el-button type="text" size="small" @click="saveSessionTitle" class="action-button">
                    确定
                  </el-button>
                  <el-button type="text" size="small" @click="cancelTitleEdit" class="action-button">
                    取消
                  </el-button>
                </div>
              </div>
            </template>
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
        <el-scrollbar v-if="currentSessionId != 0 && messages.length > 0" class="chat-messages" ref="messagesContainer">
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
        <!-- 将现有的 no-session 替换为以下内容 -->
        <div v-else-if="showWelcomeCard" class="welcome-container">
          <div class="welcome-card">
            <div class="welcome-header">
  <img src="/logo_with_name.png" alt="RiskHunter AI" class="welcome-logo">
  <p class="welcome-greeting">你好，我是RiskHunter AI!<br>你可以问我有关外汇风险管理的任何问题</p>
</div>
            <div class="suggestion-cards">
              <div v-for="(question, index) in suggestionQuestions" :key="index" class="suggestion-card"
                @click="insertSuggestion(question)">
                <p>{{ question }}</p>
                <el-icon>
                  <ArrowUp />
                </el-icon>
              </div>
            </div>
          </div>
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
                :style="{ 'width': viewport.isMobile.value ? '100px' : '180px' }" :disabled="inputDisabled"
                :suffix-icon="ArrowUp">
                <el-option v-for="model in availableModels" :key="model.value" :label="model.label"
                  :value="model.value">
                </el-option>
              </el-select>
              <el-switch v-model="streamOutput" :inactive-text="viewport.isMobile.value ? '标准' : '标准输出'"
                :active-text="viewport.isMobile.value ? '流式' : '流式输出'" :disabled="isLoading" />
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
  height: calc(100vh - 3.5rem - 42px);
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

/* 欢迎卡片样式 */
.welcome-container {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.welcome-card {
  max-width: 800px;
  width: 100%;
  background-color: var(--el-bg-color);
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 32px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.welcome-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 24px;
}

.welcome-logo {
  max-width: 280px;
  height: auto;
}

.welcome-greeting {
  font-size: 1.4rem;
  color: var(--el-text-color-primary);
  margin: 0;
  line-height: 1.6;
}

.suggestion-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.suggestion-card {
  background-color: rgba(106, 86, 198, 0.05);
  border: 1px solid rgba(106, 86, 198, 0.2);
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.suggestion-card:hover {
  background-color: rgba(106, 86, 198, 0.1);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(106, 86, 198, 0.15);
}

.suggestion-card p {
  margin: 0;
  color: var(--el-text-color-primary);
  font-size: 1rem;
  line-height: 1.5;
  flex: 1;
}

.suggestion-card .el-icon {
  color: #6a56c6;
  font-size: 16px;
  margin-left: 12px;
  opacity: 0.7;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .welcome-card {
    padding: 20px;
  }

  .welcome-logo {
    max-width: 220px;
  }

  .welcome-greeting {
    font-size: 1.2rem;
  }

  .suggestion-cards {
    grid-template-columns: 1fr;
  }

  .suggestion-card {
    padding: 12px;
  }

  .suggestion-card p {
    font-size: 0.9rem;
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

/* 添加新样式 */
.session-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  overflow: hidden;
}

.session-info {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
  overflow: hidden;
}

.session-title {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-left: 8px;
}

.session-actions {
  display: none;
  align-items: center;
  gap: 4px;
}

.el-menu-item:hover .session-actions,
.el-menu-item.is-active .session-actions {
  display: flex;
}

.action-button {
  padding: 2px 4px;
  margin: 0;
  height: auto;
  color: var(--el-text-color-secondary);
}

.action-button:hover {
  color: var(--el-color-primary);
}

.delete-button:hover {
  color: var(--el-color-danger);
}

.session-edit {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding-right: 4px;
}

.edit-actions {
  display: flex;
  gap: 4px;
  white-space: nowrap;
}

/* 确保输入框不会太宽 */
.session-edit .el-input {
  flex: 1;
  min-width: 0;
}

/* 添加紫色主题按钮样式 */
:deep(.el-button--primary) {
  background-color: #6a56c6;
  border-color: #6a56c6;
}

:deep(.el-button--primary:hover),
:deep(.el-button--primary:focus) {
  background-color: #7b68d7;
  border-color: #7b68d7;
}

:deep(.el-button--primary.is-disabled) {
  background-color: rgba(106, 86, 198, 0.5);
  border-color: rgba(106, 86, 198, 0.5);
}

/* 确保开关组件匹配紫色主题 */
:deep(.el-switch.is-checked .el-switch__core) {
  background-color: #6a56c6;
  border-color: #6a56c6;
}

/* 选中的菜单项样式 */
:deep(.el-menu-item.is-active) {
  color: #6a56c6;
  background-color: rgba(106, 86, 198, 0.1);
}

/* 增强删除/编辑按钮的可见性 */
.action-button:hover {
  color: #6a56c6;
  background-color: rgba(106, 86, 198, 0.1);
  border-radius: 4px;
}

.delete-button:hover {
  color: var(--el-color-danger);
  background-color: rgba(245, 108, 108, 0.1);
}
</style>