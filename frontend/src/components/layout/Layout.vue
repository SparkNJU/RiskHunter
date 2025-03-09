<script setup lang="ts">
import Header from './Header.vue'
import { useViewport } from '../../composables/useViewport'
import { provide } from 'vue'

const viewport = useViewport()
provide('viewport', viewport)
</script>

<template>
  <Header />
  <router-view />
</template>

<style>
</style>