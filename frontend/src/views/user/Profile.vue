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
const formatCurrency = (value:number) => {
  return new Intl.NumberFormat('zh-CN').format(value)
}

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
                  <el-icon><User /></el-icon>
                  <span>用户身份</span>
                </div>
              </template>
              <el-tag :type="role == 1 ? 'primary' : 'success'">{{ parseRole(role) }}</el-tag>
            </el-descriptions-item>

            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><Phone /></el-icon>
                  <span>联系电话</span>
                </div>
              </template>
              {{ phone }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
        
        <!-- 投资信息卡片 -->
        <el-card shadow="hover" class="investment-card">
          <template #header>
            <div class="card-header">
              <span class="profile-title">投资偏好</span>
            </div>
          </template>
          
          <el-descriptions :column="1" border>
            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><TrendCharts /></el-icon>
                  <span>最大回撤</span>
                </div>
              </template>
              {{ formatPercent(maxDrawdown) }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><CreditCard /></el-icon>
                  <span>交易周期</span>
                </div>
              </template>
              {{ tradingPeriod }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><Money /></el-icon>
                  <span>总资金</span>
                </div>
              </template>
              {{ formatCurrency(totalFunds) }} 元
            </el-descriptions-item>
            
            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><Wallet /></el-icon>
                  <span>风险偏好</span>
                </div>
              </template>
              {{ riskPreference }}
            </el-descriptions-item>
            
            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><Wallet /></el-icon>
                  <span>资本充足率</span>
                </div>
              </template>
              {{ formatPercent(capitalAdequacy) }}
            </el-descriptions-item>
            
            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><Wallet /></el-icon>
                  <span>流动性</span>
                </div>
              </template>
              {{ liquidity }}
            </el-descriptions-item>
            
            <el-descriptions-item>
              <template #label>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-icon><Money /></el-icon>
                  <span>货币曝险</span>
                </div>
              </template>
              <div class="currency-exposure">
                <el-tag v-for="(value, key) in currencyExposure" :key="key" class="currency-tag">
                  {{ key }}: {{ value }}%
                </el-tag>
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <!-- 修改页 -->
      <el-col :xs="24" :sm="24" :md="14" class="change-col">
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
                <el-icon><User /></el-icon>
                <span>{{ hasNameInput ? '新用户名' : '新用户名不能为空' }}</span>
              </label>
              <el-input v-model="newName" :class="{ 'error-input': !hasNameInput }" placeholder="请输入新用户名" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon><Phone /></el-icon>
                <span>手机号</span>
              </label>
              <el-input v-model="phone" disabled />
            </el-form-item>

            <el-form-item>
              <label class="custom-label" :class="{ 'error': hasPasswordInput && !isPasswordValid }">
                <el-icon><Lock /></el-icon>
                <span>{{ !hasPasswordInput || isPasswordValid ? '新密码 (无需修改则留白)' : '密码长度至少为6位' }}</span>
              </label>
              <el-input type="password" v-model="password"
                :class="{ 'error-input': hasPasswordInput && !isPasswordValid }" placeholder="请输入新密码" show-password />
            </el-form-item>

            <el-form-item>
              <label class="custom-label" :class="{ 'error': hasConfirmPasswordInput && !isPasswordMatching }">
                <el-icon><Lock /></el-icon>
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
              <el-button type="primary" @click="updateInfo">更新</el-button>
            </div>
          </template>

          <el-form>
            <el-form-item>
              <label class="custom-label">
                <el-icon><TrendCharts /></el-icon>
                <span>最大回撤</span>
              </label>
              <el-slider v-model="maxDrawdown" :step="0.01" :min="0" :max="1" :format-tooltip="formatPercent" />
            </el-form-item>

            <el-form-item>
              <label class="custom-label">
                <el-icon><CreditCard /></el-icon>
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
                <el-icon><Money /></el-icon>
                <span>总资金 (元)</span>
              </label>
              <el-input-number v-model="totalFunds" :min="0" :step="100000" :controls="false" style="width: 100%" />
            </el-form-item>
            
            <el-form-item>
              <label class="custom-label">
                <el-icon><Wallet /></el-icon>
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
                <el-icon><Wallet /></el-icon>
                <span>资本充足率</span>
              </label>
              <el-slider v-model="capitalAdequacy" :step="0.01" :min="0" :max="1" :format-tooltip="formatPercent" />
            </el-form-item>
            
            <el-form-item>
              <label class="custom-label">
                <el-icon><Wallet /></el-icon>
                <span>流动性</span>
              </label>
              <el-select v-model="liquidity" placeholder="请选择流动性" style="width: 100%">
                <el-option label="低" value="低" />
                <el-option label="中" value="中" />
                <el-option label="高" value="高" />
              </el-select>
            </el-form-item>
            
            <el-form-item>
              <label class="custom-label">
                <el-icon><Money /></el-icon>
                <span>货币曝险 (%)</span>
              </label>
              <div class="currency-inputs">
                <el-row :gutter="10" v-for="key in Object.keys(currencyExposure)" :key="key" class="currency-row">
                  <el-col :span="8">
                    <el-tag>{{ key }}</el-tag>
                  </el-col>
                  <el-col :span="16">
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

.investment-card {
  margin-top: 20px;
}

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

.card-header {
  display: flex;
  align-items: center;
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
  
  .currency-tag {
    font-size: 0.7rem;
  }
  
  .currency-row {
    margin-bottom: 15px;
  }
}
</style>