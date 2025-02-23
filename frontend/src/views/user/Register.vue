<script setup lang="ts">
import { ref, computed } from 'vue'
import { router } from '../../router'
import { userRegister } from "../../api/user.ts"

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

const sendVerificationCode = () => {
  // TODO
}
</script>

<template>
  <el-main class="main-frame bgimage">
    <el-card class="login-card">
      <div>
        <h1>创建一个新的账户</h1>

        <el-form>
          <el-form-item>
            <label>用户名</label>
            <el-input v-model="username" placeholder="请输入用户名" />
          </el-form-item>

          <el-form-item>
            <label>身份</label>
            <el-select v-model="role" placeholder="请选择身份" style="width: 100%;">
              <el-option label="企业" value="1" />
              <el-option label="金融机构" value="2" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasPhoneInput || isPhoneValid">手机号</label>
            <label v-else class="error-warn">手机号格式不正确</label>
            <el-input v-model="phone" :class="{ 'error-warn-input': hasPhoneInput && !isPhoneValid }"
              placeholder="请输入手机号" />
          </el-form-item>

          <el-form-item>
            <el-row :gutter="10">
              <el-col :span="16">
                <el-input v-model="verificationCode" placeholder="请输入手机验证码" />
              </el-col>
              <el-col :span="8">
                <el-button @click="sendVerificationCode" :disabled="!isPhoneValid">
                  发送验证码
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasPasswordInput || isPasswordValid">密码</label>
            <label v-else class="error-warn">密码长度至少为6位</label>
            <el-input type="password" v-model="password" 
            :class="{ 'error-warn-input': hasPasswordInput && !isPasswordValid }" placeholder="请输入密码" 
            show-password/>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasConfirmPasswordInput || isPasswordMatching">确认密码</label>
            <label v-else class="error-warn">密码不匹配</label>
            <el-input type="password" v-model="confirmPassword"
              :class="{ 'error-warn-input': hasConfirmPasswordInput && !isPasswordMatching }" placeholder="请再次输入密码" 
              show-password/>
          </el-form-item>

          <span class="button-group">
            <el-button @click.prevent="handleRegister" :disabled="registerDisabled" type="primary">
              注册
            </el-button>

            <router-link to="/login" v-slot="{ navigate }">
              <el-button @click="navigate">
                返回登录
              </el-button>
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

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}
</style>