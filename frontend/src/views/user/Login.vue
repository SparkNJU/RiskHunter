<script setup lang="ts">
import { ElForm, ElFormItem, ElMessage } from "element-plus"
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { userInfo, userLogin } from "../../api/user.ts"
import { captchaGenerator } from '../../utils/captcha'
import { User, Lock, PictureRounded } from '@element-plus/icons-vue'

const router = useRouter()

// 登录表项
const phone = ref('')
const password = ref('')
const captcha = ref('')
const captchaImage = ref('')

// 前端表单校验
const hasPhoneInput = computed(() => phone.value !== '')
const hasPasswordInput = computed(() => password.value !== '')
const hasCaptchaInput = computed(() => captcha.value !== '')
const phoneRegex = /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[01256789]))\d{8}$/
const isPhoneValid = computed(() => phoneRegex.test(phone.value))

const loginDisabled = computed(() => {
  return !(hasPhoneInput.value && isPhoneValid.value &&
    hasPasswordInput.value && hasCaptchaInput.value)
})

// 从前端获取验证码
const getCaptcha = async () => {
  const { image } = captchaGenerator.generate()
  captchaImage.value = image
}
getCaptcha()

// 登录处理
const handleLogin = async () => {
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
        sessionStorage.setItem('userId', res.data.result.id)
        router.push({ path: "/profile" })
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
</script>

<template>
  <el-main class="auth-container">
    <div class="auth-content">
      <!-- 左侧标题区域 -->
      <div class="auth-branding">
        <h1 class="brand-title">RiskHunter</h1>
        <p class="brand-subtitle">您身边的<br />外汇风险管理助手</p>
      </div>

      <!-- 右侧登录卡片 -->
      <div class="auth-form-wrapper">
        <el-card class="auth-card">
          <div class="auth-header">
            <h1 class="auth-title">登录</h1>
          </div>

          <el-form @keydown.enter="!loginDisabled && handleLogin()">
            <el-form-item>
              <label class="custom-label" :class="{ 'error': hasPhoneInput && !isPhoneValid }">
                <el-icon>
                  <User />
                </el-icon>
                <span>{{ !hasPhoneInput || isPhoneValid ? '手机号' : '手机号格式不正确' }}</span>
              </label>
              <el-input v-model="phone" :class="{ 'error-input': hasPhoneInput && !isPhoneValid }"
                placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <Lock />
                </el-icon>
                <span>密码</span>
              </label>
              <el-input type="password" v-model="password" show-password placeholder="请输入密码" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <PictureRounded />
                </el-icon>
                <span>验证码</span>
              </label>
              <div class="auth-verify-group">
                <el-input v-model="captcha" placeholder="请输入验证码" />
                <div class="captcha-image" @click="getCaptcha">
                  <img :src="captchaImage" alt="验证码" title="点击刷新" />
                </div>
              </div>
            </el-form-item>

            <div class="auth-button-group">
              <el-button type="primary" @click.prevent="handleLogin" :disabled="loginDisabled">
                登录
              </el-button>

              <router-link to="/register" custom v-slot="{ navigate }">
                <el-button @click="navigate">
                  注册
                </el-button>
              </router-link>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>
  </el-main>
</template>
<style scoped>
/* 保留现有样式 */
:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-select .el-input__wrapper) {
  height: 2.5rem;
  width: 100%;
}

/* 添加以下样式 */
.auth-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 1rem;
}

.auth-content {
  display: flex;
  width: 100%;
  max-width: 1100px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border-radius: 8px;
  overflow: hidden;
  background-color: white;
}

.auth-branding {
  flex: 1;
  background: linear-gradient(135deg, #6a56c6 0%, #9370DB 100%);
  padding: 3rem;
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.brand-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.brand-subtitle {
  font-size: 1.2rem;
  line-height: 1.6;
  opacity: 0.9;
}

.auth-form-wrapper {
  flex: 1;
  padding: 2rem;
  max-height: 80vh; /* 限制最大高度 */
  overflow: hidden; /* 防止整体溢出 */
}

.auth-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  border: none;
  box-shadow: none;
}

:deep(.el-card__body) {
  flex: 1;
  padding: 0;
  overflow-y: auto; /* 添加垂直滚动条 */
}

.auth-header {
  padding: 0 0 1.5rem 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 1.5rem;
}

.auth-title {
  font-size: 1.8rem;
  color: #333;
  margin: 0;
}

.custom-label {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
  color: #606266;
}

.custom-label .el-icon {
  margin-right: 8px;
  color: #6a56c6;
}

.auth-verify-group {
  display: flex;
  gap: 1rem;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

.captcha-image img {
  width: 100%;
  height: auto;
}

.auth-button-group {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

/* 为错误状态添加样式 */
.error {
  color: #f56c6c;
}

.error-input :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #f56c6c inset !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .auth-content {
    flex-direction: column;
  }
  
  .auth-branding {
    padding: 2rem;
  }
  
  .auth-form-wrapper {
    max-height: none; /* 在移动设备上不限制高度 */
  }
}
</style>