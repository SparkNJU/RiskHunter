<script setup lang="ts">
import { ref } from 'vue'
import { searchRiskSignals, type RiskSignal, type RiskSignalQueryDTO } from '../api/risk_signal'
import { parseTime } from '../utils'

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
  quoteCurrency: '',
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
    quoteCurrency: '',
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

// 初始加载
loadData()
</script>

<template>
  <el-main class="main-container">
    <!-- 筛选栏 -->
    <el-card class="filter-card">
      <template #header>
        <div class="card-header">
          <span>筛选条件</span>
          <div class="button-group">
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </div>
        </div>
      </template>

      <el-form :model="queryForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="基准货币">
              <el-input v-model="queryForm.baseCurrency" placeholder="请输入基准货币" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报价货币">
              <el-input v-model="queryForm.quoteCurrency" placeholder="请输入报价货币" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="起始时间">
              <el-date-picker
                v-model="queryForm.startTime"
                type="datetime"
                placeholder="选择起始时间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-date-picker
                v-model="queryForm.endTime"
                type="datetime"
                placeholder="选择结束时间"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 交易需求栏 -->
    <el-card class="trading-card">
      <template #header>
        <div class="card-header">
          <span>交易需求</span>
        </div>
      </template>

      <el-form :model="tradingForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大回撤比例">
              <el-input
                v-model="tradingForm.maximumDrawdownRatio"
                type="number"
                placeholder="请输入最大回撤比例"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易期限">
              <el-input
                v-model="tradingForm.transactionTerm"
                type="number"
                placeholder="请输入交易期限"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 信号列表 -->
    <el-card class="list-card">
      <template #header>
        <div class="card-header">
          <span>风险信号列表</span>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="signals"
        style="width: 100%"
      >
        <el-table-column prop="baseCurrency" label="基准货币" width="100" />
        <el-table-column prop="quoteCurrency" label="报价货币" width="100" />
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
            <el-descriptions :column="2" border>
              <el-descriptions-item label="分析文本" :span="2">
                {{ row.analysis }}
              </el-descriptions-item>
              <el-descriptions-item label="建议文本" :span="2">
                {{ row.advice }}
              </el-descriptions-item>
            </el-descriptions>
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
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>
  </el-main>
</template>

<style scoped>
.main-container {
  padding: 15px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-card,
.trading-card,
.list-card {
  margin-bottom: 15px;
}

.button-group {
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-date-editor) {
  width: 100%;
}
</style>