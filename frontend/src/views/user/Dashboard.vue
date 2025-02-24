<script setup lang="ts">
import { ref, computed } from 'vue'
import { userInfo, userInfoUpdate } from '../../api/user.ts'
import { parseRole, parseTime } from "../../utils"
import { router } from '../../router'
import { User, UserFilled, Calendar, Phone, LocationInformation, Lock } from '@element-plus/icons-vue'

const role = ref(1);
const username = ref('test')
const phone = ref('13800000000')
const address = ref('中国江苏省南京市栖霞区南京大学软件学院')
const regTime = ref('')

const newName = ref('')

const displayInfoCard = ref(true)

const password = ref('')
const confirmPassword = ref('')

const hasPasswordInput = computed(() => password.value !== '')
const isPasswordValid = computed(() => password.value.length >= 6)
const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordMatching = computed(() => password.value == confirmPassword.value)
const changeDisabled = computed(() => {
  return !(hasConfirmPasswordInput.value && isPasswordMatching.value)
})

getUserInfo()

function getUserInfo() {
  userInfo().then(res => {
    role.value = res.data.result.role
    username.value = res.data.result.username
    phone.value = res.data.result.phone
    regTime.value = parseTime(res.data.result.createTime)
    newName.value = username.value
  })
}

function updateInfo() {
  userInfoUpdate({
    username: newName.value,
    password: undefined,
    address: address.value,
  }).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        customClass: 'customMessage',
        type: 'success',
        message: '更新成功！',
      })
      getUserInfo()
    } else if (res.data.code === '400') {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}

function updatePassword() {
  userInfoUpdate({
    username: undefined,
    password: password.value,
  }).then(res => {
    if (res.data.code === '000') {
      password.value = ''
      confirmPassword.value = ''
      ElMessageBox.alert(
        `请重新登录`,
        '修改成功',
        {
          customClass: "customDialog",
          confirmButtonText: '跳转到登录',
          type: "success",
          showClose: false,
          roundButton: true,
          center: true
        }).then(() => router.push({ path: "/login" }))
    } else if (res.data.code === '400') {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
      password.value = ''
      confirmPassword.value = ''
    }
  })
}
</script>


<template>
  <el-main class="main-container">
    <!-- 个人信息卡片 -->
    <el-card class="aside-card" shadow="hover">
      <div class="avatar-area">
        <el-avatar :icon="UserFilled" :size="80" class="user-avatar" />
        <span class="avatar-text">欢迎您，{{ username }}</span>
      </div>

      <el-divider />

      <el-descriptions :column="1" border title="个人信息">
        <template #extra>
          <el-button type="primary" @click="displayInfoCard = !displayInfoCard">
            <span v-if="displayInfoCard">修改密码</span>
            <span v-else>修改个人信息</span>
          </el-button>
        </template>

        <el-descriptions-item>
          <template #label>
            <div style="display: flex; align-items: center; gap: 8px">
              <el-icon>
                <User />
              </el-icon>
              <span>身份</span>
            </div>
          </template>
          <el-tag>{{ parseRole(role) }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div style="display: flex; align-items: center; gap: 8px">
              <el-icon>
                <Phone />
              </el-icon>
              <span>联系电话</span>
            </div>
          </template>
          {{ phone }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div style="display: flex; align-items: center; gap: 8px">
              <el-icon>
                <Calendar />
              </el-icon>
              <span>注册时间</span>
            </div>
          </template>
          {{ regTime }}
        </el-descriptions-item>

      </el-descriptions>
    </el-card>

    <!-- 个人信息修改卡片 -->
    <el-card v-if="displayInfoCard" class="change-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="header-title">修改个人信息</span>
          <el-button type="primary" @click="updateInfo">更新</el-button>
        </div>
      </template>

      <el-form class="info-form">
        <el-form-item>
          <label class="custom-label">
            <el-icon>
              <User />
            </el-icon>
            <span>新用户名</span>
          </label>
          <el-input v-model="newName" placeholder="请输入新用户名" />
        </el-form-item>

        <el-form-item>
          <label class="custom-label">
            <el-icon>
              <Phone />
            </el-icon>
            <span>手机号</span>
          </label>
          <el-input v-model="phone" disabled />
        </el-form-item>

        <el-form-item>
          <label class="custom-label">
            <el-icon>
              <LocationInformation />
            </el-icon>
            <span>地址</span>
          </label>
          <el-input v-model="address" placeholder="请输入地址" />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 密码修改卡片 -->
    <el-card v-else class="change-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="header-title">修改密码</span>
          <el-button type="primary" @click="updatePassword" :disabled="changeDisabled">
            修改
          </el-button>
        </div>
      </template>

      <el-form class="password-form">
        <el-form-item>
          <label class="custom-label" :class="{ 'error': hasPasswordInput && !isPasswordValid }">
            <el-icon>
              <Lock />
            </el-icon>
            <span>{{ !hasPasswordInput || isPasswordValid ? '密码' : '密码长度至少为6位' }}</span>
          </label>
          <el-input type="password" v-model="password" :class="{ 'error-input': hasPasswordInput && !isPasswordValid }"
            placeholder="请输入新密码" show-password />
        </el-form-item>

        <el-form-item>
          <label class="custom-label" :class="{ 'error': hasConfirmPasswordInput && !isPasswordMatching }">
            <el-icon>
              <Lock />
            </el-icon>
            <span>{{ !hasConfirmPasswordInput || isPasswordMatching ? '确认密码' : '密码不匹配' }}</span>
          </label>
          <el-input type="password" v-model="confirmPassword"
            :class="{ 'error-input': hasConfirmPasswordInput && !isPasswordMatching }" placeholder="请再次输入新密码"
            show-password />
        </el-form-item>
      </el-form>
    </el-card>
  </el-main>
</template>


<style scoped>
/* 主布局样式 */
.main-container {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-5) 100%);
  display: flex;
  flex-direction: row;
  padding: 24px;
  gap: 24px;
  justify-content: center;
  align-items: flex-start;
}

/* 侧边卡片样式 */
.aside-card {
  width: 360px;
  height: fit-content;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  background-color: var(--el-bg-color);
}

.aside-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 头像区域样式 */
.avatar-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 24px 0;
}

.user-avatar {
  border: 4px solid var(--el-color-primary-light-8);
  transition: all 0.3s ease;
  font-size: 32px;
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.user-avatar:hover {
  transform: scale(1.05);
  border-color: var(--el-color-primary);
}

.avatar-text {
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

/* 修改卡片样式 */
.change-card {
  flex: 1;
  max-width: 600px;
  height: fit-content;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  background-color: var(--el-bg-color);
}

.change-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 4px 0;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-color-primary);
}

/* 表单样式 */
.info-form,
.password-form {
  padding: 20px 0;
}

.custom-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  color: var(--el-text-color-primary);
  font-size: 14px;
}

.custom-label.error {
  color: var(--el-color-danger);
}

.error-input {
  --el-input-border-color: var(--el-color-danger);
  --el-input-focus-border-color: var(--el-color-danger);
}

/* Element Plus 组件深度样式 */
:deep(.el-input__wrapper) {
  height: 40px;
  line-height: 40px;
  border-radius: 6px;
}

:deep(.el-descriptions) {
  --el-descriptions-item-bordered-label-background: var(--el-color-primary-light-9);
}

:deep(.el-descriptions__header) {
  margin-bottom: 16px;
}

:deep(.el-descriptions__title) {
  font-weight: 600;
  color: var(--el-color-primary);
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-divider) {
  margin: 16px 0;
}

:deep(.el-button) {
  border-radius: 6px;
  transition: all 0.3s ease;
}

:deep(.el-tag) {
  border-radius: 4px;
}

/* 响应式设计 */
@media screen and (max-width: 1024px) {
  .main-container {
    flex-direction: column;
    align-items: center;
    padding: 20px;
  }

  .aside-card,
  .change-card {
    width: 100%;
    max-width: 600px;
  }
}

@media screen and (max-width: 640px) {
  .main-container {
    padding: 12px;
  }

  .avatar-text {
    font-size: 18px;
  }

  .avatar-area {
    padding: 16px 0;
  }

  .header-title {
    font-size: 16px;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .main-container {
    background: linear-gradient(135deg,
        var(--el-color-primary-dark-9) 0%,
        var(--el-color-primary-dark-5) 100%);
  }

  .aside-card,
  .change-card {
    background-color: var(--el-bg-color-overlay);
  }

  .user-avatar {
    background-color: var(--el-color-primary-dark-9);
    border-color: var(--el-border-color-darker);
  }
}
</style>
