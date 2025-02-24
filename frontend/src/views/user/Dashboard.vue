<script setup lang="ts">
import { ref, computed } from 'vue'
import { userInfo, userInfoUpdate } from '../../api/user.ts'
import { parseRole, parseTime } from "../../utils"
import { router } from '../../router'
import { UserFilled } from "@element-plus/icons-vue";

const role = ref(Number(sessionStorage.getItem("role")))
const username = ref('')
const phone = ref('')
const address = ref('')
const regTime = ref()

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
    <el-card class="aside-card">
      <div class="avatar-area">
        <el-avatar :icon="UserFilled" :size="80">
        </el-avatar>
        <span class="avatar-text"> 欢迎您，{{ username }}</span>
      </div>

      <el-divider></el-divider>

      <el-descriptions :column="1" border title="个人信息">
        <template #extra>
          <el-button type="primary" @click="displayInfoCard = displayInfoCard === false;">
            <span v-if="displayInfoCard">修改密码</span>
            <span v-else>修改个人信息</span>
          </el-button>
        </template>

        <el-descriptions-item label="身份">
          <el-tag>{{ parseRole(role) }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="联系电话">
          {{ phone }}
        </el-descriptions-item>

        <el-descriptions-item label="注册时间">
          {{ regTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="displayInfoCard" class="change-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button @click="updateInfo">更新</el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label for="name">用户名</label>
          <el-input type="text" id="name" v-model="newName" />
        </el-form-item>

        <el-form-item>
          <label for="phone">手机号</label>
          <el-input id="phone" v-model="phone" disabled />
        </el-form-item>

        <el-form-item>
          <label for="address">地址</label>
          <el-input id="address" v-model="address" />
        </el-form-item>

      </el-form>
    </el-card>

    <el-card v-if="!displayInfoCard" class="change-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
          <el-button @click="updatePassword" :disabled="changeDisabled">
            修改
          </el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label v-if="!hasPasswordInput || isPasswordValid">密码</label>
          <label v-else class="error-warn">密码长度至少为6位</label>
          <el-input type="password" v-model="password"
            :class="{ 'error-warn-input': hasPasswordInput && !isPasswordValid }" placeholder="请输入新密码" show-password />
        </el-form-item>

        <el-form-item>
          <label v-if="!hasConfirmPasswordInput || isPasswordMatching">确认密码</label>
          <label v-else class="error-warn">密码不匹配</label>
          <el-input type="password" v-model="confirmPassword"
            :class="{ 'error-warn-input': hasConfirmPasswordInput && !isPasswordMatching }" placeholder="请再次输入新密码"
            show-password />
        </el-form-item>
      </el-form>

    </el-card>
  </el-main>

</template>


<style scoped>
.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.main-container {
  display: flex;
  flex-direction: row;
  padding: 15px;
  gap: 5px;
  justify-content: center;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.change-card {
  width: 66%;
}

.avatar-area {
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 30px;
}

.avatar-text {
  font-size: x-large;
  font-weight: bolder;
  padding-right: 40px;
}
</style>
