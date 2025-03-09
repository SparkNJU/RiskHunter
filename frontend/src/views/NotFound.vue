<script setup lang="ts">
import { Warning } from '@element-plus/icons-vue'
</script>


<template>
  <el-main class="main-frame">
    <el-card class="error-card" shadow="hover">
      <el-result
        icon="warning"
        title="404"
        sub-title="抱歉，您访问的页面不存在"
      >
        <template #icon>
          <el-icon class="error-icon"><Warning /></el-icon>
        </template>
        <template #extra>
          <router-link to="/about">
            <el-button type="primary" class="back-btn">返回主页</el-button>
          </router-link>
        </template>
      </el-result>
    </el-card>
  </el-main>
</template>


<style scoped>
</style>
