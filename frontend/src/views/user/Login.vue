<script setup lang="ts">
import { ElForm, ElFormItem } from "element-plus"
import { ref, computed, onMounted } from 'vue'
import { router } from '../../router'
import { userInfo, userLogin } from "../../api/user.ts"
import { captchaGenerator } from '../../utils/captcha'
import { User, Lock, PictureRounded } from '@element-plus/icons-vue'

const phone = ref('')
const password = ref('')
const captcha = ref('')
const captchaImage = ref('')
const captchaCode = ref('')

const hasPhoneInput = computed(() => phone.value !== '')
const hasPasswordInput = computed(() => password.value !== '')
const hasCaptchaInput = computed(() => captcha.value !== '')

const phoneRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const isPhoneValid = computed(() => phoneRegex.test(phone.value))

const loginDisabled = computed(() => {
  return !(hasPhoneInput.value && isPhoneValid.value &&
    hasPasswordInput.value && hasCaptchaInput.value)
})

// 从前端获取验证码
const getCaptcha = async () => {
  const { image, code } = captchaGenerator.generate()
  captchaImage.value = image
  captchaCode.value = code
}

// 登录处理
function handleLogin() {
  if (!captchaGenerator.validate(captcha.value)) {
    ElMessage({
      message: "验证码错误",
      type: 'error',
      center: true,
    })
    getCaptcha()
    captcha.value = ''
    return
  }

  userLogin({
    phone: phone.value,
    password: password.value
  }).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: "登录成功！",
        type: 'success',
        center: true,
      })
      const token = res.data.result
      sessionStorage.setItem('token', token)

      userInfo().then(res => {
        sessionStorage.setItem('username', res.data.result.username)
        sessionStorage.setItem('role', res.data.result.role)
        router.push({ path: "/dashboard" })
      })
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
      getCaptcha()
      captcha.value = ''
      password.value = ''
    }
  })
}

// 组件挂载时获取验证码
onMounted(() => {
  getCaptcha()
})
</script>

<template>
  <el-main class="main-frame">
    <div class="login-container">
      <el-card class="login-card" shadow="hover">
        <div class="login-header">
          <h1 class="login-title">登录</h1>
        </div>

        <el-form class="login-form">
          <el-form-item>
            <label class="custom-label" :class="{ 'error': hasPhoneInput && !isPhoneValid }">
              <el-icon><User /></el-icon>
              <span>{{ !hasPhoneInput || isPhoneValid ? '手机号' : '手机号格式不正确' }}</span>
            </label>
            <el-input 
              v-model="phone" 
              :class="{ 'error-input': hasPhoneInput && !isPhoneValid }"
              placeholder="请输入手机号"
            />
          </el-form-item>

          <el-form-item>
            <label class="custom-label">
              <el-icon><Lock /></el-icon>
              <span>密码</span>
            </label>
            <el-input 
              type="password" 
              v-model="password" 
              show-password
              placeholder="请输入密码"
            />
          </el-form-item>

          <el-form-item>
            <label class="custom-label">
              <el-icon><PictureRounded /></el-icon>
              <span>验证码</span>
            </label>
            <div class="captcha-group">
              <el-input 
                v-model="captcha" 
                placeholder="请输入验证码"
              />
              <div class="captcha-image" @click="getCaptcha">
                <img :src="captchaImage" alt="验证码" title="点击刷新" />
              </div>
            </div>
          </el-form-item>

          <div class="button-group">
            <el-button 
              type="primary" 
              @click.prevent="handleLogin" 
              :disabled="loginDisabled"
              class="login-btn"
            >
              登录
            </el-button>

            <router-link to="/register" custom v-slot="{ navigate }">
              <el-button @click="navigate" class="register-btn">
                注册
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

.login-container {
  width: 100%;
  max-width: 440px;
}

/* 卡片样式 */
.login-card {
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

/* 悬停效果 */
.login-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 标题样式 */
.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-title {
  font-size: 24px;
  color: var(--el-color-primary);
  margin-bottom: 8px;
}

/* 表单样式 */
.login-form {
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
.captcha-group {
  display: flex;
  gap: 10px;
  width: 100%;
  align-items: center;
}

.captcha-group .el-input {
  flex: 1;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.captcha-image:hover {
  opacity: 0.8;
}

.captcha-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 统一输入框 */
:deep(.el-input__wrapper) {
  height: 40px;
  line-height: 40px;
  border-radius: 6px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
  width: 100%;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
  gap: 16px;
}

.login-btn, .register-btn {
  flex: 1;
  padding: 12px 0;
  font-size: 16px;
  border-radius: 6px;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .login-container {
    max-width: 100%;
  }

  .login-form {
    padding: 0 10px;
  }

  .button-group {
    flex-direction: column;
  }

  .login-btn, .register-btn {
    width: 100%;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .main-frame {
    background: linear-gradient(135deg, 
      var(--el-color-primary-dark-9) 0%, 
      var(--el-color-primary-dark-5) 100%
    );
  }
}
</style>

