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
const captchaCode = ref('')

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
  const { image, code } = captchaGenerator.generate()
  captchaImage.value = image
  captchaCode.value = code
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
        <p class="brand-subtitle">您身边的<br/>外汇风险管理助手</p>
      </div>
      
      <!-- 右侧登录卡片 -->
      <div class="auth-form-wrapper">
        <el-card class="auth-card" shadow="hover">
          <div class="auth-header">
            <h1 class="auth-title">登录</h1>
          </div>

          <el-form @keydown.enter="!loginDisabled && handleLogin()">
            <!-- 表单内容保持不变 -->
            <el-form-item>
              <label class="custom-label" :class="{ 'error': hasPhoneInput && !isPhoneValid }">
                <el-icon><User /></el-icon>
                <span>{{ !hasPhoneInput || isPhoneValid ? '手机号' : '手机号格式不正确' }}</span>
              </label>
              <el-input v-model="phone" :class="{ 'error-input': hasPhoneInput && !isPhoneValid }" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon><Lock /></el-icon>
                <span>密码</span>
              </label>
              <el-input type="password" v-model="password" show-password placeholder="请输入密码" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon><PictureRounded /></el-icon>
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
/* 整体背景和容器设置 */
.auth-container {
  min-height: 100vh;
  height: 100vh; /* 确保容器铺满整个视口高度 */
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e0f7fa 0%, #cfb0d4 100%); /* 与主页保持一致的背景色 */
  position: relative;
  overflow: hidden;
}

/* 添加背景图案效果 */
.auth-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url('../../assets/background-pattern.svg');
  background-size: cover;
  opacity: 0.05;
  z-index: 0;
}

/* 内容区域布局 */
.auth-content {
  display: flex;
  width: 90%;
  height: 90vh; /* 使用视口高度的90%，留出一点边距 */
  max-width: 1200px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  border-radius: 10px;
  overflow: hidden;
  position: relative;
  z-index: 1;
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
}

/* 左侧品牌区域 */
.auth-branding {
  flex: 1;
  height: 100%; /* 铺满父容器高度 */
  background: linear-gradient(135deg, #626aef 0%, #512da8 100%);
  color: white;
  padding: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
}


.brand-title {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  line-height: 1.2;
}

.brand-subtitle {
  font-size: 1.8rem;
  font-weight: 300;
  line-height: 1.5;
}

/* 右侧表单区域 */
.auth-form-wrapper {
  flex: 1;
  height: 100%; /* 铺满父容器高度 */
  padding: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.auth-card {
  width: 100%;
  max-width: 420px;
  border: none;
  box-shadow: none;
  background: transparent;
}

.auth-header {
  text-align: center;
  margin-bottom: 2rem;
}

.auth-title {
  color: #303030;
  font-size: 2rem;
  font-weight: 600;
}

/* 验证码区域样式 */
.auth-verify-group {
  display: flex;
  gap: 10px;
}

.captcha-image {
  width: 120px;
  height: 2.5rem;
  cursor: pointer;
  border: 1px solid var(--el-border-color-base);
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.captcha-image img {
  max-width: 100%;
  max-height: 100%;
}

/* 按钮组样式 */
.auth-button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.auth-button-group .el-button {
  width: 48%;
}

/* 自定义标签样式 */
.custom-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
}

.custom-label.error {
  color: var(--el-color-danger);
}

.error-input :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--el-color-danger) inset;
}

:deep(.el-input__wrapper) {
  height: 2.5rem;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .auth-content {
    flex-direction: column;
    width: 95%;
    height: auto; /* 在小屏幕上允许高度自适应 */
    min-height: 95vh; /* 但仍然至少占据95%视口高度 */
  }
  
  .auth-branding {
    padding: 2rem;
    align-items: center;
    text-align: center;
  }
  
  .brand-title {
    font-size: 2.5rem;
  }
  
  .brand-subtitle {
    font-size: 1.4rem;
  }
}

@media (max-width: 576px) {
  .auth-content {
    width: 100%;
    height: 100vh; /* 在最小屏幕上完全铺满 */
    min-height: 100vh;
    border-radius: 0;
  }
  
  .auth-form-wrapper {
    padding: 1.5rem;
  }
  
  .auth-branding {
    padding: 1.5rem;
  }
  
  .brand-title {
    font-size: 2rem;
  }
  
  .brand-subtitle {
    font-size: 1.2rem;
  }
}
</style>