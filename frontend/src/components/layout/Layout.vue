<script setup lang="ts">
import Header from './Header.vue'
import IndexList from './IndexList.vue'
import { useViewport } from '../../composables/useViewport'
import { provide } from 'vue'

const viewport = useViewport()
provide('viewport', viewport)
</script>

<template>
  <div class="layout-container">
    <Header />
    <IndexList />
    <div class="content-container">
      <router-view />
    </div>
  </div>
</template>

<style>
.layout-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-container {
  flex: 1;
  padding-top: 0px; /* 为固定的汇率条留出空间 */
}
</style>