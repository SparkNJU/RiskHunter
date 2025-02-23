<script setup lang="ts">
import { ElForm, ElFormItem } from "element-plus"
import { ref, computed, onMounted } from 'vue'
import { router } from '../../router'
import { userInfo, userLogin } from "../../api/user.ts"
import { captchaGenerator } from '../../utils/captcha'

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

const getCaptcha = async () => {
  const { image, code } = captchaGenerator.generate()
  captchaImage.value = image
  captchaCode.value = code
}

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
  <el-main class="main-frame bgimage">
    <el-card class="login-card">
      <div>
        <h1>登入您的账户</h1>
        <el-form>
          <el-form-item>
            <label v-if="!hasPhoneInput || isPhoneValid">手机号</label>
            <label v-else class="error-warn">手机号格式不正确</label>
            <el-input v-model="phone" :class="{ 'error-warn-input': hasPhoneInput && !isPhoneValid }"
              placeholder="请输入手机号" />
          </el-form-item>

          <el-form-item>
            <label for="password">密码</label>
            <el-input id="password" type="password" v-model="password" required placeholder="请输入您的密码" 
            show-password/>
          </el-form-item>

          <el-form-item>
            <div class="captcha-container">
              <el-input id="captcha" v-model="captcha" placeholder="请输入验证码" />
              <div class="captcha-image" @click="getCaptcha">
                <img :src="captchaImage" alt="验证码" title="点击刷新" />
              </div>
            </div>
          </el-form-item>

          <span class="button-group">
            <el-button @click.prevent="handleLogin" :disabled="loginDisabled" type="primary">登入</el-button>
            <router-link to="/register" v-slot="{ navigate }">
              <el-button @click="navigate">前往注册</el-button>
            </router-link>
          </span>
        </el-form>
      </div>
    </el-card>
  </el-main>
</template>


<style scoped>
.main-frame {
  width: 100%;
  height: 100%;

  display: flex;
  align-items: center;
  justify-content: center;
}

.bgimage {
  background-image: url("../../assets/shopping-1s-1084px.svg");
}

.login-card {
  width: 60%;
  padding: 10px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.captcha-container {
  display: flex;
  gap: 10px;
  align-items: center;
}

.captcha-container .el-input {
  flex: 1;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}
</style>

