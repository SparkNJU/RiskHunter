<script setup lang="ts">
import Header from './Header.vue'
</script>

<template>
  <div class="layout">
    <Header />
    <router-view />
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: var(--el-bg-color-page);
}

:deep(.dark) {
  .layout {
    background-color: var(--el-bg-color-overlay);
  }
}
</style>