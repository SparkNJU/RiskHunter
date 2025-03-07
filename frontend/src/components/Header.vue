<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { House, Warning, DataLine, Memo, User, UserFilled, Plus, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 使用 ref 控制登录状态
const isLoggedIn = ref(false)

// 初始化登录状态
onMounted(() => {
  checkLoginStatus()
})

// 监听路由变化
watch(
  () => route.path,
  () => {
    checkLoginStatus()
  }
)

// 检查登录状态
const checkLoginStatus = () => {
  const token = sessionStorage.getItem('token')
  isLoggedIn.value = !!token
}

// 登出处理
const handleLogout = () => {
  ElMessageBox.confirm(
    '是否要退出登录？',
    '提示',
    {
      customClass: "customDialog",
      confirmButtonText: '是',
      cancelButtonText: '否',
      type: "warning",
      showClose: false,
      roundButton: true,
      center: true
    }
  ).then(() => {
    sessionStorage.setItem('token', '')
    sessionStorage.removeItem('role')
    isLoggedIn.value = false
    router.push({ path: "/login" })
  })
}
</script>

<template>
  <el-affix :offset="0">
    <el-menu class="el-menu" mode="horizontal" :ellipsis="false" router>
      <!-- LOGO -->
      <!-- <div class="brand">
        <img src="../assets/vue.svg" alt="Logo" class="brand-logo" />
        <span class="brand-text">RiskHunter</span>
      </div> -->

      <!-- 主导航 -->
      <div class="nav-group">
        <el-menu-item index="/about">
          <el-icon>
            <House />
          </el-icon>
          <span>关于我们</span>
        </el-menu-item>
        <el-menu-item index="/risk-signal">
          <el-icon>
            <Warning />
          </el-icon>
          <span>风险信号</span>
        </el-menu-item>
        <el-menu-item index="/chat">
          <el-icon>
            <Memo />
          </el-icon>
          <span>智能体</span>
        </el-menu-item>
        <el-menu-item index="/fx-data">
          <el-icon>
            <DataLine />
          </el-icon>
          <span>外汇数据</span>
        </el-menu-item>
        <el-menu-item index="/news">
          <el-icon>
            <Memo />
          </el-icon>
          <span>新闻展示</span>
        </el-menu-item>
      </div>

      <div class="flex-grow" />

      <!-- 用户区 -->
      <div class="user-group">
        <template v-if="!isLoggedIn">
          <el-menu-item index="/login" class="login-btn">
            <el-icon>
              <User />
            </el-icon>
            <span>登录</span>
          </el-menu-item>

          <el-menu-item index="/register">
            <el-icon>
              <Plus />
            </el-icon>
            <span>注册</span>
          </el-menu-item>
        </template>

        <template v-else>
          <el-menu-item index="/dashboard">
            <el-icon>
              <UserFilled />
            </el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item @click="handleLogout" class="logout-btn">
            <el-icon>
              <SwitchButton />
            </el-icon>
            <span>退出</span>
          </el-menu-item>
        </template>
      </div>
    </el-menu>
  </el-affix>
</template>

<style scoped>
/* 导航栏样式 */
.el-menu {
  padding: 0 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  background-color: var(--el-bg-color);
  display: flex;
  align-items: center;
  height: 60px;
  transition: all 0.3s ease;
}

/* LOGO样式 */
.brand {
  display: flex;
  align-items: center;
  margin-right: 40px;
  cursor: pointer;
  padding: 0 20px;
}

.brand-logo {
  height: 32px;
  margin-right: 10px;
  transition: transform 0.3s ease;
}

.brand-logo:hover {
  transform: scale(1.05);
}

.brand-text {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--el-color-primary);
}

/* 导航组样式 */
.nav-group {
  display: flex;
  gap: 10px;
}

.flex-grow {
  flex-grow: 1;
}

/* 用户区域样式 */
.user-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 自定义登录按钮样式 */
.login-btn {
  color: rgb(75, 173, 226);
}

/* 自定义退出按钮样式 */
.logout-btn {
  color: rgb(255, 77, 0);
}

/* Element Plus 组件深度样式 */
:deep(.el-menu-item) {
  display: flex;
  align-items: center;
  gap: 5px;
  height: 60px;
  padding: 0 20px;
  transition: all 0.3s ease;
}

:deep(.el-menu-item:hover) {
  color: #3a3a3a;
  --el-menu-text-color: #b789eb;
}

:deep(.el-menu-item.is-active) {
  color: #b789eb;
  border-bottom: 2px solid #b789eb;
}

:deep(.el-button) {
  display: flex;
  align-items: center;
  gap: 5px;
  transition: all 0.3s ease;
}

/* 菜单样式 */
:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-icon) {
  margin-right: 4px;
  transition: transform 0.3s ease;
}

:deep(.el-menu-item:hover .el-icon) {
  transform: scale(1.1);
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .el-menu {
    padding: 0 12px;
  }

  .brand-text {
    display: none;
  }

  .nav-group {
    gap: 0px;
  }

  .user-group {
    gap: 0px;
  }

  :deep(.el-menu-item) {
    padding: 0 12px;
  }

  /* 隐藏按钮文本，只显示图标 */
  .user-group span,
  .nav-group span {
    display: none;
  }
  :deep(.el-icon) {
    transform: scale(0.8);
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .el-menu {
    background-color: var(--el-bg-color-overlay);
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  }

  :deep(.el-menu-item:hover) {
    background-color: var(--el-color-primary-dark-9);
  }
}
</style>