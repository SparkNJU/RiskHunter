<template>
  <div class="layout">
    <header>
      <nav>
        <router-link to="/about">关于我们</router-link>
        <template v-if="!isLoggedIn">
          <router-link to="/login">登录</router-link>
          <router-link to="/register">注册</router-link>
        </template>
        <template v-else>
          <router-link to="/dashboard">个人信息</router-link>
          <a @click="handleLogout" href="javascript:void(0)">退出</a>
        </template>
      </nav>
    </header>
    
    <main>
      <router-view></router-view>
    </main>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'Layout',
  setup() {
    const router = useRouter()
    
    const isLoggedIn = computed(() => {
      return !!sessionStorage.getItem('token')
    })
    
    const handleLogout = () => {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('role')
      router.push('/about')
    }
    
    return {
      isLoggedIn,
      handleLogout
    }
  }
})
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  padding: 1rem;
  background-color: #f8f9fa;
}

nav a {
  margin-right: 1rem;
}

main {
  flex: 1;
  padding: 1rem;
}
</style>