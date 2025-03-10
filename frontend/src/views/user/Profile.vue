<script setup lang="ts">
import { ref, computed } from 'vue'
import { userInfo, userInfoUpdate } from '../../api/user.ts'
import { parseRole } from "../../utils"
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from "element-plus"
import { User, Phone, LocationInformation, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const role = ref(1);
const username = ref('')
const phone = ref('')
const address = ref('')

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


const getUserInfo = async () => {
  userInfo().then(res => {
    role.value = res.data.result.role
    username.value = res.data.result.username
    phone.value = res.data.result.phone
    newName.value = username.value
  })
}
getUserInfo()

const updateInfo = async() => {
  userInfoUpdate({
    username: newName.value,
    password: undefined,
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

const updatePassword = async() => {
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
  <el-main class="profile-container">
    <el-row :gutter="20">
      <!-- 个人信息页 -->
      <el-col :xs="24" :sm="24" :md="10" class="profile-col">
        <el-card shadow="hover">
          <div class="avatar-area">
            <span class="profile-title">欢迎您，{{ username }}</span>
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

          </el-descriptions>
        </el-card>
      </el-col>


      <!-- 修改页 -->
      <el-col :xs="24" :sm="24" :md="14" class="profile-col">
        <!-- 修改个人信息 -->
        <el-card v-if="displayInfoCard" shadow="hover">
          <template #header>
            <div class="profile-header">
              <span class="profile-title">修改个人信息</span>
              <el-button type="primary" @click="updateInfo">更新</el-button>
            </div>
          </template>

          <el-form>
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

        <!-- 修改密码 -->
        <el-card v-else shadow="hover">
          <template #header>
            <div class="profile-header">
              <span class="profile-title">修改密码</span>
              <el-button type="primary" @click="updatePassword" :disabled="changeDisabled">
                更新
              </el-button>
            </div>
          </template>

          <el-form>
            <el-form-item>
              <label class="custom-label" :class="{ 'error': hasPasswordInput && !isPasswordValid }">
                <el-icon>
                  <Lock />
                </el-icon>
                <span>{{ !hasPasswordInput || isPasswordValid ? '密码' : '密码长度至少为6位' }}</span>
              </label>
              <el-input type="password" v-model="password"
                :class="{ 'error-input': hasPasswordInput && !isPasswordValid }" placeholder="请输入新密码" show-password />
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
      </el-col>
    </el-row>
  </el-main>
</template>


<style scoped>
.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-title {
  color: var(--el-text-color-primary);
  font-size: 1.2rem;
  font-weight: 600;
}

:deep(.el-row) {
  width: 100vw;
  max-width: 1200px;
}

:deep(.el-card) {
  border-radius: 12px;
  margin-bottom: 20px;
}

:deep(.el-input__wrapper) {
  height: 2.5rem;
  width: 100%;
}

/* 移动端适配: 调整字体大小和间距 */
@media (max-width: 768px) {
  .profile-title {
    font-size: 1rem;
  }

  :deep(.el-descriptions-item__label),
  :deep(.el-descriptions-item__content) {
    font-size: 0.8rem;
  }

  .el-form-item {
    margin-bottom: 1rem;
  }
}
</style>