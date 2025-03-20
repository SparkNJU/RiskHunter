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
    <el-card class="auth-card" shadow="hover">
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
          <el-input v-model="phone" :class="{ 'error-input': hasPhoneInput && !isPhoneValid }" placeholder="请输入手机号" />
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
  </el-main>
</template>

<style scoped>
.captcha-image {
  width: 120px;
  height: 2.5rem;
  cursor: pointer;
  border: 1px solid var(--el-border-color-base);
  border-radius: 4px;
}

:deep(.el-input__wrapper) {
  height: 2.5rem;
  width: 100%;
}
</style>