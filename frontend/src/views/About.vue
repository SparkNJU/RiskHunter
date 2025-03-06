<script setup lang="ts">
import { InfoFilled, Phone, Location, Message, Warning, Memo, DataLine } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path: string) => {
  router.push({ path })
}
</script>

<template>
  <el-main class="main-container">
    <div
      class="absolute top-0 inset-x-0 h-[37.5rem] bg-grid-slate-900/[0.04] bg-top [mask-image:linear-gradient(0deg,transparent,black)] dark:bg-grid-slate-100/[0.03] dark:bg-[center_top_-1px] dark:border-t dark:border-slate-100/5 top-0 xl:top-14">
    </div>
    <div class="relative z-10 w-full max-w-7xl mx-auto p-8">
      <div class="text-center mb-16">
        <h1 class="text-4xl font-bold text-slate-900 dark:text-white">Risk Hunter</h1>
        <p class="text-lg text-slate-600 dark:text-slate-900">您身边的外汇风险管理助手</p>
      </div>

      <!-- 简介 -->
      <div class="mb-16">
        <div class="flex items-center justify-center mb-4">
          <el-icon class="text-slate-900 dark:text-white">
            <InfoFilled />
          </el-icon>
          <span class="ml-2 text-2xl font-bold text-slate-900 dark:text-white">关于我们</span>
        </div>
        <p class="text-center text-slate-700 dark:text-slate-900">
          Risk Hunter 是一个前沿的统计与AI工具，能够整合分析过往趋势，通过多维度的数据模型精准捕捉汇率风险信号，为企业提供更具前瞻性、科学性的风险管理支持。
        </p>
      </div>

      <!-- 四大板块 -->
      <div class="flex flex-col space-y-8 w-full">
        <div
          class="flex flex-col items-center justify-center h-[50vh] w-full bg-gradient-to-r from-blue-100 to-purple-100 rounded-lg shadow-md">
          <el-icon class="text-slate-900 dark:text-white">
            <Warning />
          </el-icon>
          <h2 class="mt-4 text-xl font-bold text-slate-900 dark:text-white">风险信号</h2>
          <el-button type="primary" class="mt-4" @click="navigateTo('/risk-signal')">查看详情</el-button>
        </div>
        <div
          class="flex flex-col items-center justify-center h-[50vh] bg-gradient-to-r from-blue-100 to-purple-100 rounded-lg shadow-md">
          <el-icon class="text-slate-900 dark:text-white">
            <Memo />
          </el-icon>
          <h2 class="mt-4 text-xl font-bold text-slate-900 dark:text-white">智能体</h2>
          <el-button type="primary" class="mt-4" @click="navigateTo('/chat')">查看详情</el-button>
        </div>
        <div
          class="flex flex-col items-center justify-center h-[50vh] bg-gradient-to-r from-blue-100 to-purple-100 rounded-lg shadow-md">
          <el-icon class="text-slate-900 dark:text-white">
            <DataLine />
          </el-icon>
          <h2 class="mt-4 text-xl font-bold text-slate-900 dark:text-white">外汇数据</h2>
          <el-button type="primary" class="mt-4" @click="navigateTo('/fx-data')">查看详情</el-button>
        </div>
        <div
          class="flex flex-col items-center justify-center h-[50vh] bg-gradient-to-r from-blue-100 to-purple-100 rounded-lg shadow-md">
          <el-icon class="text-slate-900 dark:text-white">
            <Memo />
          </el-icon>
          <h2 class="mt-4 text-xl font-bold text-slate-900 dark:text-white">新闻展示</h2>
          <el-button type="primary" class="mt-4" @click="navigateTo('/news')">查看详情</el-button>
        </div>
      </div>

      <!-- 联系方式 -->
      <div class="mb-16">
        <div class="flex items-center justify-center mb-4">
          <el-icon class="text-slate-900 dark:text-white">
            <Phone />
          </el-icon>
          <span class="ml-2 text-2xl font-bold text-slate-900 dark:text-white">联系我们</span>
        </div>
        <div class="flex flex-col items-center space-y-4">
          <div class="flex items-center space-x-2">
            <el-icon class="text-slate-900 dark:text-white">
              <Message />
            </el-icon>
            <span class="text-slate-700 dark:text-slate-300">邮箱:</span>
            <a href="mailto:riskhunter@smail.nju.edu.cn"
              class="text-slate-900 dark:text-white">riskhunter@smail.nju.edu.cn</a>
          </div>
          <div class="flex items-center space-x-2">
            <el-icon class="text-slate-900 dark:text-white">
              <Location />
            </el-icon>
            <span class="text-slate-700 dark:text-slate-300">地址:</span>
            <span class="text-slate-900 dark:text-white">中国江苏省南京市栖霞区南京大学</span>
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
.main-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e0f7fa 0%, #e1bee7 100%);
}

.text-slate-900 {
  color: #1e293b;
}

.text-slate-600 {
  color: #475569;
}

.text-slate-700 {
  color: #334155;
}

.text-slate-300 {
  color: #cbd5e1;
}

.bg-slate-800 {
  background-color: #1e293b;
}

.bg-slate-900 {
  background-color: #0f172a;
}

.bg-white {
  background-color: #ffffff;
}

.dark\:bg-slate-800 {
  background-color: #1e293b;
}

.dark\:bg-slate-400 {
  background-color: #94a3b8;
}

.dark\:text-white {
  color: #ffffff;
}

.dark\:text-slate-300 {
  color: #cbd5e1;
}

.dark\:border-slate-100\/5 {
  border-color: rgba(241, 245, 249, 0.05);
}

.dark\:bg-grid-slate-400\/\[0.05\] {
  background-color: rgba(148, 163, 184, 0.05);
}
</style>