<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from "../../api/user.ts"
import { ElMessage } from 'element-plus'
import { User, Lock, Message, Phone, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()

// 注册表项
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')
const username = ref('')
const role = ref()
const verificationCode = ref('')

// 前端表单校验
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
const handleRegister = async () => {
  console.log('rrrr')
  userRegister({
    phone: phone.value,
    password: password.value,
    username: username.value,
    role: role.value,
    // verificationCode: verificationCode.value
  }).then(res => {
    if (res.data.code === '000') {
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
  ElMessage.error('暂未实现')
}

</script>

<template>
  <el-main class="auth-container">
    <el-card class="auth-card" shadow="hover">
      <div class="auth-header">
        <h1 class="auth-title">注册</h1>
      </div>

      <el-form @keydown.enter="!registerDisabled && handleRegister()">
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
          <el-select v-model="role" placeholder="请选择身份">
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
          <div class="auth-verify-group">
            <el-input v-model="verificationCode" placeholder="请输入验证码" />
            <el-button type="primary" @click="sendVerificationCode" :disabled="!isPhoneValid" class="auth-verify-btn">
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
          <el-input type="password" v-model="password" :class="{ 'error-input': hasPasswordInput && !isPasswordValid }"
            placeholder="请输入密码" show-password />
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

        <div class="auth-button-group">
          <el-button type="primary" @click.prevent="handleRegister" :disabled="registerDisabled" class="register-btn">
            注册
          </el-button>

          <router-link to="/login" custom v-slot="{ navigate }">
            <el-button @click="navigate">
              返回登录
            </el-button>
          </router-link>
        </div>
      </el-form>
    </el-card>
  </el-main>
</template>

<style scoped>
/* 统一 height */
.auth-verify-btn {
  height: 2.5rem;
}

:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-select .el-input__wrapper) {
  height: 2.5rem;
  width: 100%;
}
</style>