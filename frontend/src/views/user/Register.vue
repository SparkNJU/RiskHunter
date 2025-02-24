<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from "../../api/user.ts"
import { User, Lock, Message, Phone, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')
const username = ref('')
const role = ref()
const verificationCode = ref('')

const hasPhoneInput = computed(() => phone.value !== '')
const hasPasswordInput = computed(() => password.value !== '')
const hasConfirmPasswordInput = computed(() => confirmPassword.value !== '')
const hasUsernameInput = computed(() => username.value !== '')
const hasRoleSelected = computed(() => role.value !== '')
const hasVerificationCode = computed(() => verificationCode.value !== '')

const phoneRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const isPhoneValid = computed(() => phoneRegex.test(phone.value))
const isPasswordMatching = computed(() => password.value === confirmPassword.value)
const isPasswordValid = computed(() => password.value.length >= 6)

const registerDisabled = computed(() => {
  return !(hasPhoneInput.value && hasPasswordInput.value &&
    hasConfirmPasswordInput.value && hasUsernameInput.value &&
    hasRoleSelected.value && isPhoneValid.value && isPasswordMatching.value &&
    hasVerificationCode.value && isPasswordValid.value)
})

// 注册处理
function handleRegister() {
  userRegister({
    phone: phone.value,
    password: password.value,
    username: username.value,
    role: role.value,
    // verificationCode: verificationCode.value
  }).then(res => {
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "注册成功！请登录账号",
        type: 'success',
        center: true,
      })
      router.push({ path: "/login" })
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

// 发送短信验证码
const sendVerificationCode = () => {
  // TODO
}

function navigateToLogin() {
  router.push({ path: '/login' })
}
</script>

<template>
  <el-main class="main-frame">
    <div class="register-container">
      <el-card class="register-card" shadow="hover">
        <div class="register-header">
          <h1 class="register-title">注册</h1>
        </div>

        <el-form class="register-form">
          <el-form-item>
            <label class="custom-label">
              <el-icon>
                <User />
              </el-icon>
              <span>用户名</span>
            </label>
            <el-input v-model="username" placeholder="请输入用户名" />
          </el-form-item>

          <el-form-item>
            <label class="custom-label">
              <el-icon>
                <UserFilled />
              </el-icon>
              <span>身份</span>
            </label>
            <el-select v-model="role" placeholder="请选择身份" class="full-width">
              <el-option label="企业" value="1" />
              <el-option label="金融机构" value="2" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <label class="custom-label" :class="{ 'error': hasPhoneInput && !isPhoneValid }">
              <el-icon>
                <Phone />
              </el-icon>
              <span>{{ !hasPhoneInput || isPhoneValid ? '手机号' : '手机号格式不正确' }}</span>
            </label>
            <el-input v-model="phone" :class="{ 'error-input': hasPhoneInput && !isPhoneValid }" placeholder="请输入手机号" />
          </el-form-item>

          <el-form-item>
            <label class="custom-label">
              <el-icon>
                <Message />
              </el-icon>
              <span>验证码</span>
            </label>
            <div class="verification-group">
              <el-input v-model="verificationCode" placeholder="请输入验证码" />
              <el-button type="primary" @click="sendVerificationCode" :disabled="!isPhoneValid" class="verify-btn">
                发送验证码
              </el-button>
            </div>
          </el-form-item>

          <el-form-item>
            <label class="custom-label" :class="{ 'error': hasPasswordInput && !isPasswordValid }">
              <el-icon>
                <Lock />
              </el-icon>
              <span>{{ !hasPasswordInput || isPasswordValid ? '密码' : '密码长度至少为6位' }}</span>
            </label>
            <el-input type="password" v-model="password"
              :class="{ 'error-input': hasPasswordInput && !isPasswordValid }" placeholder="请输入密码" show-password />
          </el-form-item>

          <el-form-item>
            <label class="custom-label" :class="{ 'error': hasConfirmPasswordInput && !isPasswordMatching }">
              <el-icon>
                <Lock />
              </el-icon>
              <span>{{ !hasConfirmPasswordInput || isPasswordMatching ? '确认密码' : '密码不匹配' }}</span>
            </label>
            <el-input type="password" v-model="confirmPassword"
              :class="{ 'error-input': hasConfirmPasswordInput && !isPasswordMatching }" placeholder="请再次输入密码"
              show-password />
          </el-form-item>

          <div class="button-group">
            <el-button type="primary" @click.prevent="handleRegister" :disabled="registerDisabled" class="register-btn">
              注册
            </el-button>

            <router-link to="/login" custom v-slot="{ navigate }">
              <el-button @click="navigate" class="back-btn">
                返回登录
              </el-button>
            </router-link>
          </div>
        </el-form>
      </el-card>
    </div>
  </el-main>
</template>

<style scoped>
/* 主布局样式 */
.main-frame {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-5) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-container {
  width: 100%;
  max-width: 500px;
}

/* 卡片样式 */
.register-card {
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

/* 悬停效果 */
.register-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 标题样式 */
.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-title {
  font-size: 24px;
  color: var(--el-color-primary);
  margin-bottom: 8px;
}

/* 表单样式 */
.register-form {
  padding: 0 20px;
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

/* 验证码样式 */
.verification-group {
  display: flex;
  gap: 10px;
  width: 100%;
  align-items: center;
}

.verification-group .el-input {
  flex: 1;
}

/* 统一输入框 */
:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-select .el-input__wrapper) {
  height: 40px;
  line-height: 40px;
  border-radius: 6px;
}

:deep(.el-input__inner) {
  height: 40px;
  line-height: 40px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
  width: 100%;
}

:deep(.el-form-item__content) {
  width: 100%;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
  gap: 16px;
}

.register-btn,
.back-btn {
  flex: 1;
  padding: 12px 0;
  font-size: 16px;
  border-radius: 6px;
}

.verify-btn {
  width: 120px;
  height: 40px;
  padding: 0;
  margin: 0;
  white-space: nowrap;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .register-container {
    max-width: 100%;
  }

  .register-form {
    padding: 0 10px;
  }

  .button-group {
    flex-direction: column;
  }

  .register-btn,
  .back-btn {
    width: 100%;
  }

  .verification-group {
    flex-direction: column;
    gap: 8px;
  }

  .verify-btn {
    width: 100%;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .main-frame {
    background: linear-gradient(135deg,
        var(--el-color-primary-dark-9) 0%,
        var(--el-color-primary-dark-5) 100%);
  }
}
</style>