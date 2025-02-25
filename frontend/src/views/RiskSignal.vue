<script setup lang="ts">
import { ref } from 'vue'
import { searchRiskSignals, type RiskSignal, type RiskSignalQueryDTO } from '../api/risk_signal'
import { parseTime } from '../utils'
import { Search, Refresh, ArrowDown, Warning } from '@element-plus/icons-vue'

// 表格数据和分页
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const signals = ref<RiskSignal[]>([])
const CurrencyList = [
  { number: 1, code: 'CNY', name: '人民币' },
  { number: 2, code: 'USD', name: '美元' },
  { number: 3, code: 'EUR', name: '欧元' },
  { number: 4, code: 'JPY', name: '日元' },
  { number: 5, code: 'GBP', name: '英镑' }
];

// 查询条件
const queryForm = ref<RiskSignalQueryDTO>({
  baseCurrency: '',
  targetCurrency: '',
  startTime: '',
  endTime: '',
  page: 1,
  size: 10
})

// 交易需求
const tradingForm = ref({
  maximumDrawdownRatio: '',
  transactionTerm: ''
})

function parseCurrencyName(index: number): string {
  const currency = CurrencyList.find(c => c.number === index)
  return currency ? `${currency.code}` : 'null'
}

// 加载数据
function loadData() {
  loading.value = true
  const query = {
    ...queryForm.value,
    ...tradingForm.value,
    page: currentPage.value,
    size: pageSize.value
  }

  searchRiskSignals(query)
    .then(res => {
      if (res.data.code === '000') {
        signals.value = res.data.result.records
        total.value = res.data.result.total
      } else {
        ElMessage({
          customClass: 'customMessage',
          type: 'error',
          message: res.data.msg
        })
      }
    })
    .catch(() => {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: '加载数据失败'
      })
    })
    .finally(() => {
      loading.value = false
    })
}

// 搜索
function handleSearch() {
  currentPage.value = 1
  loadData()
}

// 重置
function handleReset() {
  queryForm.value = {
    baseCurrency: '',
    targetCurrency: '',
    startTime: '',
    endTime: '',
    page: 1,
    size: 10
  }
  tradingForm.value = {
    maximumDrawdownRatio: '',
    transactionTerm: ''
  }
  currentPage.value = 1
  loadData()
}

// 每页条数改变时触发
function handleSizeChange(val: number) {
  pageSize.value = val
  currentPage.value = 1
  loadData()
}

// 当前页改变时触发
function handleCurrentChange(val: number) {
  currentPage.value = val
  loadData()
}

// 初始加载
loadData()
</script>

<template>
  <el-main class="main-container">
    <!-- 筛选栏 -->
    <el-card class="filter-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon">
              <Search />
            </el-icon>
            <span class="header-title">筛选条件</span>
          </div>
          <div class="button-group">
            <el-button type="primary" @click="handleSearch">
              <el-icon>
                <Search />
              </el-icon>查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon>
                <Refresh />
              </el-icon>重置
            </el-button>
          </div>
        </div>
      </template>

      <el-form :model="queryForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="基准货币">
              <el-select v-model="queryForm.baseCurrency" placeholder="请选择基准货币" clearable>
                <el-option v-for="currency in CurrencyList" :key="currency.code"
                  :label="`${currency.code} - ${currency.name}`" :value="currency.number" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报价货币">
              <el-select v-model="queryForm.targetCurrency" placeholder="请选择报价货币" clearable>
                <el-option v-for="currency in CurrencyList" :key="currency.code"
                  :label="`${currency.code} - ${currency.name}`" :value="currency.number" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="起始时间">
              <el-date-picker v-model="queryForm.startTime" type="datetime" placeholder="选择起始时间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-date-picker v-model="queryForm.endTime" type="datetime" placeholder="选择结束时间" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 交易需求栏 -->
    <el-card class="trading-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon">
              <Warning />
            </el-icon>
            <span class="header-title">交易需求</span>
          </div>
        </div>
      </template>

      <el-form :model="tradingForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大回撤比例 (%)">
              <el-input v-model="tradingForm.maximumDrawdownRatio" type="number" placeholder="请输入最大回撤比例" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易期限 (天)">
              <el-input v-model="tradingForm.transactionTerm" type="number" placeholder="请输入交易期限" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 信号列表 -->
    <el-card class="list-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon">
              <ArrowDown />
            </el-icon>
            <span class="header-title">风险信号列表</span>
          </div>
          <div class="table-summary" v-if="total > 0">
            共 {{ total }} 条记录
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="signals.slice((currentPage-1)*pageSize, currentPage*pageSize)" style="width: 100%">
        <el-table-column prop="baseCurrency" label="基准货币" width="100">
          <template #default="{ row }">
            {{ parseCurrencyName(row.baseCurrency) }}
          </template>
        </el-table-column>
        <el-table-column prop="targetCurrency" label="报价货币" width="100">
          <template #default="{ row }">
            {{ parseCurrencyName(row.targetCurrency) }}
          </template>
        </el-table-column>
        <el-table-column label="时间" width="180">
          <template #default="{ row }">
            {{ parseTime(row.time) }}
          </template>
        </el-table-column>
        <el-table-column prop="emp" label="EMP" width="100">
          <template #default="{ row }">
            <el-tag :type="row.emp > 1 ? 'danger' : 'success'">
              {{ row.emp.toFixed(4) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="exchangeRate" label="汇率" width="100">
          <template #default="{ row }">
            {{ row.exchangeRate.toFixed(4) }}
          </template>
        </el-table-column>
        <el-table-column prop="interestRate" label="利率" width="100">
          <template #default="{ row }">
            {{ row.interestRate.toFixed(4) }}
          </template>
        </el-table-column>
        <el-table-column prop="fxReserves" label="外汇储备" width="120">
          <template #default="{ row }">
            {{ row.fxReserves.toFixed(2) }}
          </template>
        </el-table-column>

        <!-- 展开行 -->
        <el-table-column type="expand">
          <template #default="{ row }">
            <div class="expanded-content">
              <el-card class="analysis-card" shadow="never">
                <template #header>
                  <div class="analysis-header">分析详情</div>
                </template>
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="分析文本">
                    <div class="analysis-text">{{ row.analysis }}</div>
                  </el-descriptions-item>
                  <el-descriptions-item label="建议文本">
                    <div class="advice-text">{{ row.advice }}</div>
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </el-main>
</template>

<style scoped>
/* 主布局样式 */
.main-container {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-5) 100%);
  display: flex;
  flex-direction: column;
  padding: 24px;
  align-items: center;
}

/* 卡片通用样式 */
.filter-card,
.trading-card,
.list-card {
  width: 1000px;
  max-width: 100%;
  transition: all 0.3s ease;
  border-radius: 12px;
  margin-bottom: 20px;
  background-color: var(--el-bg-color);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.filter-card:hover,
.trading-card:hover,
.list-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 4px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-icon {
  font-size: 18px;
  color: var(--el-color-primary);
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

/* 按钮组样式 */
.button-group {
  display: flex;
  gap: 12px;
}

.button-group .el-button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  border-radius: 4px;
}

/* 表格样式 */
.table-summary {
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

/* 展开行样式 */
.expanded-content {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

.analysis-card {
  border: none;
  background-color: transparent;
}

.analysis-header {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.analysis-text,
.advice-text {
  line-height: 1.6;
  padding: 12px;
  background-color: var(--el-bg-color);
  border-radius: 4px;
}

/* Element Plus 组件深度样式 */
:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-input),
:deep(.el-date-editor) {
  width: 100%;
  --el-input-hover-border-color: var(--el-color-primary);
}

:deep(.el-table) {
  border-radius: 4px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: var(--el-bg-color-page);
  font-weight: 600;
}

/* 分页样式 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 响应式设计 */
@media screen and (max-width: 1024px) {

  .filter-card,
  .trading-card,
  .list-card {
    width: 100%;
  }
}

@media screen and (max-width: 768px) {
  .main-container {
    padding: 12px;
  }

  :deep(.el-form-item__label) {
    float: none;
    text-align: left;
    margin-bottom: 8px;
  }

  .button-group {
    flex-direction: column;
    gap: 8px;
  }

  .card-header {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .main-container {
    background: linear-gradient(135deg,
        var(--el-color-primary-dark-9) 0%,
        var(--el-color-primary-dark-5) 100%);
  }

  .filter-card,
  .trading-card,
  .list-card {
    background-color: var(--el-bg-color-overlay);
  }
}
</style>