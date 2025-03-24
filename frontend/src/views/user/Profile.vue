<script setup lang="ts">
import { ref, computed } from 'vue'
import { userInfo, userInfoUpdate } from '../../api/user.ts'
import { parseRole } from "../../utils"
import { ElMessage } from "element-plus"
import { User, Phone, Lock, Money, Wallet, TrendCharts, CreditCard } from '@element-plus/icons-vue'

// 基本用户信息
const role = ref(1);
const username = ref('')
const newName = ref('')
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')

// 投资相关信息
const maxDrawdown = ref(0.15)
const tradingPeriod = ref('3年')
const currencyExposure = ref<Record<string, number>>({
  USD: 25,
  CNY: 30,
  EUR: 15,
  JPY: 10,
  GBP: 5,
  AUD: 5,
  HKD: 5,
  CHF: 5
})
const riskPreference = ref('中立')
const capitalAdequacy = ref(0.18)
const liquidity = ref('高')
const totalFunds = ref(5000000)

// 前端表单验证
const hasNameInput = computed(() => newName.value.trim() !== '')
const hasPasswordInput = computed(() => password.value !== '')
const isPasswordValid = computed(() => password.value.length >= 6)
const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordMatching = computed(() => password.value == confirmPassword.value)
const changeEnable = computed(() => {
  return (hasPasswordInput.value && isPasswordValid.value && hasConfirmPasswordInput.value && isPasswordMatching.value) ||
    (!hasPasswordInput.value && !hasConfirmPasswordInput.value && hasNameInput.value && newName.value !== username.value)
})

// 辅助函数：格式化百分比
const formatPercent = (value: number): string => {
  return (value * 100).toFixed(2) + '%'
}

// 辅助函数：格式化货币
const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('zh-CN').format(value)
}
// 投资偏好数据（用于表格展示）
const investmentData = computed(() => [
  {
    label: "最大回撤",
    value: formatPercent(maxDrawdown.value),
    icon: "TrendCharts"
  },
  {
    label: "交易周期",
    value: tradingPeriod.value,
    icon: "CreditCard"
  },
  {
    label: "总资金",
    value: formatCurrency(totalFunds.value) + " 元",
    icon: "Money"
  },
  {
    label: "风险偏好",
    value: riskPreference.value,
    icon: "Wallet"
  },
  {
    label: "资本充足率",
    value: formatPercent(capitalAdequacy.value),
    icon: "Wallet"
  },
  {
    label: "流动性",
    value: liquidity.value,
    icon: "Wallet"
  },
  {
    label: "货币曝险",
    value: "",
    icon: "Money",
    special: "currency"
  }
]);

const getUserInfo = async () => {
  userInfo().then(res => {
    role.value = res.data.result.role
    username.value = res.data.result.username
    newName.value = username.value
    phone.value = res.data.result.phone

    // 解析description JSON
    if (res.data.result.description) {
      try {
        const description = JSON.parse(res.data.result.description)
        maxDrawdown.value = description.maxDrawdown || 0.15
        tradingPeriod.value = description.tradingPeriod || '3年'
        currencyExposure.value = description.currencyExposure || {
          USD: 25,
          CNY: 30,
          EUR: 15,
          JPY: 10,
          GBP: 5,
          AUD: 5,
          HKD: 5,
          CHF: 5
        }
        riskPreference.value = description.riskPreference || '中立'
        capitalAdequacy.value = description.capitalAdequacy || 0.18
        liquidity.value = description.liquidity || '高'
        totalFunds.value = description.totalFunds || 5000000
      } catch (e) {
        console.error('解析用户投资信息失败', e)
      }
    }
  })
}

const updateInfo = async () => {
  // 构建投资信息JSON
  const investmentInfo = {
    maxDrawdown: maxDrawdown.value,
    tradingPeriod: tradingPeriod.value,
    currencyExposure: currencyExposure.value,
    riskPreference: riskPreference.value,
    capitalAdequacy: capitalAdequacy.value,
    liquidity: liquidity.value,
    totalFunds: totalFunds.value
  }

  userInfoUpdate({
    username: newName.value,
    password: password.value ? password.value : undefined,
    description: JSON.stringify(investmentInfo)
  }).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        customClass: 'customMessage',
        type: 'success',
        message: '更新成功！',
      })
      getUserInfo()
      // 清空密码字段
      password.value = ''
      confirmPassword.value = ''
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
      <el-col :xs="24" :sm="24" :md="12" class="profile-col">
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

        <!-- 投资信息卡片 - 使用el-table替代el-descriptions -->
        <el-card shadow="hover" class="investment-card">
          <template #header>
            <div class="card-header">
              <span class="profile-title">投资偏好</span>
            </div>
          </template>

          <el-table :data="investmentData" :show-header="false" style="width: 100%" border>
            <el-table-column prop="label" width="300">
              <template #default="scope">
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon>
                    <component :is="scope.row.icon"></component>
                  </el-icon>
                  <span class="label-text">{{ scope.row.label }}</span>
                </div>
              </template>
            </el-table-column><el-table-column prop="value">
              <template #default="scope">
                <div v-if="scope.row.special === 'currency'" class="currency-exposure">
                  <el-tag v-for="(value, key) in currencyExposure" :key="key" class="currency-tag">
                    {{ key }}: {{ value }}%
                  </el-tag>
                </div>
                <div v-else>{{ scope.row.value }}</div>
              </template>
            </el-table-column>
          </el-table>
        </el-card> </el-col>

      <!-- 修改页 -->
      <el-col :xs="24" :sm="24" :md="12" class="change-col">
        <!-- 修改个人基本信息 -->
        <el-card shadow="hover">
          <template #header>
            <div class="change-header">
              <span class="profile-title">修改基本信息</span>
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

        <!-- 修改投资信息 -->
        <el-card shadow="hover">
          <template #header>
            <div class="change-header">
              <span class="profile-title">修改投资偏好</span>
              <el-button type="primary" @click="updateInfo">更新</el-button>            </div>
          </template>

          <el-form class="form-container">
            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <TrendCharts />
                </el-icon>
                <span>最大回撤</span>
              </label>
              <el-slider v-model="maxDrawdown" :step="0.01" :min="0" :max="1" :format-tooltip="formatPercent" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <CreditCard />
                </el-icon>
                <span>交易周期</span>
              </label>
              <el-select v-model="tradingPeriod" placeholder="请选择交易周期" style="width: 100%">
                <el-option label="1年" value="1年" />
                <el-option label="2年" value="2年" />
                <el-option label="3年" value="3年" />
                <el-option label="5年" value="5年" />
                <el-option label="10年" value="10年" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <Money />
                </el-icon>
                <span>总资金 (元)</span>
              </label>
              <el-input-number v-model="totalFunds" :min="0" :step="100000" :controls="false" style="width: 100%" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <Wallet />
                </el-icon>
                <span>风险偏好</span>
              </label>
              <el-select v-model="riskPreference" placeholder="请选择风险偏好" style="width: 100%">
                <el-option label="保守" value="保守" />
                <el-option label="中立" value="中立" />
                <el-option label="激进" value="激进" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <Wallet />
                </el-icon>
                <span>资本充足率</span>
              </label>
              <el-slider v-model="capitalAdequacy" :step="0.01" :min="0" :max="1" :format-tooltip="formatPercent" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon>
                  <Wallet />
                </el-icon>
                <span>流动性</span>
              </label>
              <el-select v-model="liquidity" placeholder="请选择流动性" style="width: 100%">
                <el-option label="低" value="低" />
                <el-option label="中" value="中" />
                <el-option label="高" value="高" />
              </el-select>
            </el-form-item>

            <el-form-item class="currency-form-item">
              <label class="custom-label">
                <el-icon>
                  <Money />
                </el-icon>
                <span>货币曝险 (%)</span>
              </label>
              <div class="currency-inputs">
                <el-row :gutter="10" v-for="key in Object.keys(currencyExposure)" :key="key" class="currency-row">
                  <el-col :span="5">
                    <el-tag>{{ key }}</el-tag>
                  </el-col>
                  <el-col :span="19">
                    <el-slider v-model="currencyExposure[key]" :min="0" :max="100" />
                  </el-col>
                </el-row>
              </div>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </el-main>
</template>

<style scoped>
/* 主容器样式 */
.profile-container {
  background: linear-gradient(135deg, #e0f7fa 0%, #cfb0d4 100%);
  padding: 0;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  overflow-x: hidden;
  width: 100vw !important;
  /* 使用视口宽度单位确保全屏 */
  margin: 0 !important;
  /* 移除可能的外边距 */
  box-sizing: border-box;
}

/* 内容包装器 */
.profile-container .el-row {
  width: 100%;
  max-width: 2000px;
  padding: 30px 20px;
  box-sizing: border-box;
  margin: 0 !important;
}

/* 卡片基础样式 */
:deep(.el-card) {
  background-color: rgba(255, 255, 255, 0.95);
  border: none;
  border-radius: 12px;
  margin-bottom: 20px;
  width: 100%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
}

:deep(.el-card:hover) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
}

.investment-card {
  margin-top: 20px;
}

/* 标题栏样式 */
.card-header {
  display: flex;
  align-items: center;
}

.card-header .profile-title {
  color: #6a56c6;
  font-weight: 600;
}

/* 放大投资偏好标题 */
.investment-card .card-header .profile-title {
  font-size: 1.5rem;
  letter-spacing: 0.5px;
}

/* 自定义紫色按钮 */
:deep(.el-button--primary) {
  background-color: #6a56c6 !important;
  border-color: #6a56c6 !important;
  transition: all 0.3s;
}

:deep(.el-button--primary:hover) {
  background-color: #7b68d7 !important;
  border-color: #7b68d7 !important;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(106, 86, 198, 0.3);
}

:deep(.el-button--primary:disabled) {
  background-color: rgba(106, 86, 198, 0.6) !important;
  border-color: rgba(106, 86, 198, 0.6) !important;
}

.custom-divider {
  margin: 1.5rem 0;
}

/* 描述列表样式 */
:deep(.el-descriptions) {
  background-color: transparent;
}

:deep(.el-descriptions__body) {
  background-color: transparent;
}

/* 基础描述列表标签样式 */
:deep(.el-descriptions-item__label) {
  width: 200px;
  min-width: 180px;
  white-space: normal;
  line-height: 1.5;
  padding: 16px 20px !important;
}

/* 表格样式增强 */
.investment-card :deep(.el-table) {
  background-color: transparent;
  --el-table-border-color: rgba(106, 86, 198, 0.2);
}

.investment-card :deep(.el-table__row) {
  background-color: transparent;
}

.investment-card :deep(.el-table__cell) {
  padding: 16px 24px;
}

.investment-card .label-text {
  font-size: 1.05rem;
  color: #333;
  font-weight: 500;
}

:deep(.el-descriptions-item__cell) {
  padding: 12px 16px;
}

/* 个人资料列样式 */
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

/* 修改列样式 */
.change-col {
  .change-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .profile-title {
      font-size: 1.4rem;
      font-weight: 600;
      color: #6a56c6;
    }
  }

  :deep(.el-input__wrapper) {
    height: 2.5rem;
    width: 100%;
  }
}

/* 自定义标签样式 */
.custom-label {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #333;
  font-weight: 500;
  margin-bottom: 8px;
}

.custom-label.error {
  color: var(--el-color-danger);
}

.error-input {
  border-color: var(--el-color-danger);
}

/* 货币曝险样式 */
.currency-exposure {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.currency-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}

.currency-inputs {
  width: 100%;
}

.currency-row {
  margin-bottom: 12px;
  align-items: center;
}

.currency-form-item {
  margin-bottom: 0;
}

/* 表单样式 */
.form-container {
  padding: 0 10px;
}

:deep(.el-slider) {
  width: 100%;
  margin: 0;
  padding-right: 0;
}

:deep(.el-row) {
  width: 100%;
  margin: 0 !important;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .profile-container .el-row {
    padding: 15px;
  }

  .profile-title {
    font-size: 1rem;
  }
  
  .investment-card .card-header .profile-title {
    font-size: 1.2rem;
  }

  :deep(.el-descriptions-item__label),
  :deep(.el-descriptions-item__content) {
    font-size: 0.8rem;
  }

  .el-form-item {
    margin-bottom: 1rem;
  }

  .currency-tag {
    font-size: 0.7rem;
  }

  .currency-row {
    margin-bottom: 15px;
  }

  .change-col {
    .change-header {
      .profile-title {
        font-size: 1.1rem;
      }
    }
  }
}

@media (min-width: 769px) and (max-width: 1200px) {
  .profile-container .el-row {
    padding: 15px;
  }
  
  .investment-card .card-header .profile-title {
    font-size: 1.3rem;
  }
}

@media (min-width: 1201px) {
  .profile-container .el-row {
    padding: 25px;
  }
}
</style>