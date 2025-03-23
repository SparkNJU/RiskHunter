<script setup lang="ts">
import { ref, computed } from 'vue'
import { userInfo, userInfoUpdate } from '../../api/user.ts'
import { parseRole } from "../../utils"
import { ElMessage } from "element-plus"
import { User, Phone, LocationInformation, Lock } from '@element-plus/icons-vue'

const role = ref(1);
const username = ref('')
const newName = ref('')
const phone = ref('')
const address = ref('')
const newAddress = ref('')
const password = ref('')
const confirmPassword = ref('')

// 前端表单验证
const hasNameInput = computed(() => newName.value.trim() !== '')
const hasPasswordInput = computed(() => password.value !== '')
const isPasswordValid = computed(() => password.value.length >= 6)
const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordMatching = computed(() => password.value == confirmPassword.value)
const changeEnable = computed(() => {
  return (hasPasswordInput.value && isPasswordValid.value && hasConfirmPasswordInput.value && isPasswordMatching.value) ||
    (!hasPasswordInput.value && !hasConfirmPasswordInput.value && ((hasNameInput.value && newName.value !== username.value) || newAddress.value !== address.value))
})

const getUserInfo = async () => {
  userInfo().then(res => {
    role.value = res.data.result.role
    username.value = res.data.result.username
    newName.value = username.value
    phone.value = res.data.result.phone
    address.value = res.data.result.address
    newAddress.value = address.value
  })
}

const updateInfo = async () => {
  userInfoUpdate({
    username: newName.value,
    address: newAddress.value.trim() ? newAddress.value.trim() : undefined,
    password: password.value ? password.value : undefined,
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

getUserInfo()
</script>

<template>
  <el-main class="profile-container">
    <el-row :gutter="20">
      <!-- 个人信息页 -->
      <el-col :xs="24" :sm="24" :md="10" class="profile-col">
        <el-card shadow="hover">
          <div class="avatar-gradient">
            <div class="avatar-area">
              <span class="avatar-title">{{ username }}</span>
            </div>
          </div>

          <el-divider class="custom-divider" />

          <el-descriptions :column="1" border>
            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon>
                    <User />
                  </el-icon>
                  <span>用户身份</span>
                </div>
              </template>
              <el-tag :type="role == 1 ? 'primary' : 'success'">{{ parseRole(role) }}</el-tag>
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
      <el-col :xs="24" :sm="24" :md="14" class="change-col">
        <!-- 修改个人信息 -->
        <el-card shadow="hover">
          <template #header>
            <div class="change-header">
              <span class="profile-title">修改个人信息</span>
              <el-button type="primary" @click="updateInfo" :disabled="!changeEnable">更新</el-button>
            </div>
          </template>

          <el-form>
            <el-form-item>
              <label class="custom-label" :class="{ 'error': !hasNameInput }">
                <el-icon>
                  <User />
                </el-icon>
                <span>{{ hasNameInput ? '新用户名' : '新用户名不能为空' }}</span>
              </label>
              <el-input v-model="newName" :class="{ 'error-input': !hasNameInput }" placeholder="请输入新用户名" />
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
              <el-input v-model="newAddress" placeholder="请输入地址" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label" :class="{ 'error': hasPasswordInput && !isPasswordValid }">
                <el-icon>
                  <Lock />
                </el-icon>
                <span>{{ !hasPasswordInput || isPasswordValid ? '新密码 (无需修改则留白)' : '密码长度至少为6位' }}</span>
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
.profile-col {
  .avatar-gradient {
    background: linear-gradient(135deg, var(--el-color-primary) 0%, #512da8 100%);
    border-radius: 12px 12px 0 0;
    padding: 1.5rem;
  }

  .avatar-area {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
  }

  .avatar-title {
    font-size: 1.8rem;
    color: white;
    font-weight: 600;
    background: linear-gradient(90deg, #fff 20%, #e0f7fa 80%);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}

.custom-divider {
  margin: 1.5rem 0;
}

.change-col {
  .change-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .profile-title {
      font-size: 1.4rem;
      font-weight: 600;
      color: var(--el-color-primary);
    }
  }

  :deep(.el-input__wrapper) {
    height: 2.5rem;
    width: 100%;
  }
}

:deep(.el-row) {
  width: 100vw;
  max-width: 1200px;
}

:deep(.el-card) {
  border-radius: 12px;
  margin-bottom: 20px;
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