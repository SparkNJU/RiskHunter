<script setup lang="ts">
import { inject, ref } from 'vue'
import { searchRiskSignals, type RiskSignal, type RiskSignalQueryDTO } from '../../api/risk_signal'
import { parseTime, parseCurrencyName, CurrencyList } from '../../utils'
import { Search, Refresh, ArrowDown, Warning } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 表格数据和分页
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const signals = ref<RiskSignal[]>([])

// 窗口监听
const viewport = inject('viewport', {
  isMobile: ref(false),
  viewportWidth: ref(0),
  breakpoints: { md: 768 }
})

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
  <el-card>
    <!-- 筛选栏 -->
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <Warning />
          </el-icon>
         信号列表 
        </h3>
      </div>
    </template>

    <el-card class="signal-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="signal-header">
          <div class="signal-header-left">
            <el-icon class="signal-header-icon">
              <Search />
            </el-icon>
            <span class="signal-title">筛选条件</span>
          </div>
          <div class="signal-button-group">
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

      <el-form :model="queryForm" label-position="top">
        <el-row :gutter="20" :class="{ 'mobile-row': viewport.isMobile.value }">
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="基准货币">
              <el-select v-model="queryForm.baseCurrency" placeholder="请选择基准货币" clearable>
                <el-option v-for="currency in CurrencyList" :key="currency.code"
                  :label="`${currency.code} - ${currency.name}`" :value="currency.number" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="报价货币">
              <el-select v-model="queryForm.targetCurrency" placeholder="请选择报价货币" clearable>
                <el-option v-for="currency in CurrencyList" :key="currency.code"
                  :label="`${currency.code} - ${currency.name}`" :value="currency.number" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" :class="{ 'mobile-row': viewport.isMobile.value }">
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="起始时间">
              <el-date-picker v-model="queryForm.startTime" type="datetime" placeholder="选择起始时间" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="结束时间">
              <el-date-picker v-model="queryForm.endTime" type="datetime" placeholder="选择结束时间" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 需求栏 -->
    <el-card class="signal-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="signal-header">
          <div class="signal-header-left">
            <el-icon class="signal-header-icon">
              <Warning />
            </el-icon>
            <span class="signal-title">交易需求</span>
          </div>
        </div>
      </template>

      <el-form :model="tradingForm" label-position="top">
        <el-row :gutter="20" :class="{ 'mobile-row': viewport.isMobile.value }">
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="最大回撤比例">
              <el-input v-model="tradingForm.maximumDrawdownRatio" type="number" placeholder="请输入最大回撤比例" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="交易期限">
              <el-input v-model="tradingForm.transactionTerm" type="number" placeholder="请输入交易期限" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 信号列表 -->
    <el-card class="signal-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="signal-header">
          <div class="signal-header-left">
            <el-icon class="signal-header-icon">
              <ArrowDown />
            </el-icon>
            <span class="signal-title">风险信号列表</span>
          </div>
          <div class="signal-summary" v-if="total > 0">
            共 {{ total }} 条记录
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="signals.slice((currentPage - 1) * pageSize, currentPage * pageSize)"
        style="width: 100%" :size="viewport.isMobile.value ? 'small' : 'default'">
        <!-- 移除固定宽度约束，让表格自适应 -->
        <el-table-column prop="baseCurrency" label="基准货币">
          <template #default="{ row }">
            {{ parseCurrencyName(row.baseCurrency) }}
          </template>
        </el-table-column>
        <el-table-column prop="targetCurrency" label="报价货币">
          <template #default="{ row }">
            {{ parseCurrencyName(row.targetCurrency) }}
          </template>
        </el-table-column>
        <el-table-column label="时间">
          <template #default="{ row }">
            {{ parseTime(row.time) }}
          </template>
        </el-table-column>
        <el-table-column prop="emp" label="EMP">
          <template #default="{ row }">
            <el-tag :type="row.emp > 1 ? 'danger' : 'success'">
              {{ row.emp.toFixed(4) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="exchangeRate" label="汇率">
          <template #default="{ row }">
            {{ row.exchangeRate.toFixed(4) }}
          </template>
        </el-table-column>
        <el-table-column prop="interestRate" label="利率">
          <template #default="{ row }">
            {{ row.interestRate.toFixed(4) }}
          </template>
        </el-table-column>
        <el-table-column prop="fxReserves" label="外汇储备">
          <template #default="{ row }">
            {{ row.fxReserves.toFixed(2) }}
          </template>
        </el-table-column>


        <!-- 展开行 -->
        <el-table-column type="expand">
          <template #default="{ row }">
            <div class="signal-expand">
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
      <div class="signal-pagination">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total"
          :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>
  </el-card>
</template>

<style scoped>
.signal-card {
  margin-bottom: 1rem;
}

@media screen and (max-width: 768px) {
  .signal-card {
    width: 90vw;
  }

  .mobile-row {
    margin-left: 0 !important;
    margin-right: 0 !important;
  }

  :deep(.el-form-item__label) {
    font-size: 0.75rem;
  }
}

/* 每个卡片的顶栏 */
.signal-header {
  height: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.signal-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.signal-title {
  font-size: 1.1em;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.signal-header-icon {
  font-size: 1.1em;
  color: var(--el-color-primary);
}

.signal-button-group {
  display: flex;
  gap: 12px;
  margin-top: 1.5rem;
}

.signal-pagination {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
}

.signal-expand {
  padding: 1.2rem;
}

/* 表格样式优化，占满卡片宽度 */
:deep(.el-table) {
  width: 100%;
  font-size: 1rem;
}

:deep(.el-table th) {
  font-size: 1.05rem;
  font-weight: 600;
  background-color: var(--el-fill-color-light);
}

/* 移除固定宽度约束，让表格自适应 */
:deep(.el-table-column--default > .cell) {
  width: auto !important;
  white-space: nowrap;
}

/* 优化表格行高 */
:deep(.el-table__row) {
  height: 3rem;
}

/* 标签样式优化 */
:deep(.el-tag) {
  font-size: 0.95rem;
  padding: 0 10px;
}

:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-select .el-input__wrapper),
:deep(.el-date-editor.el-input__wrapper),
:deep(.el-date-editor--datetime),
:deep(.el-date-editor--date) {
  height: 2rem;
  width: 100%;
}
</style>