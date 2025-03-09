<script setup lang="ts">
import { ref, watch, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { House, Warning, DataLine, Memo, User, UserFilled, Plus, SwitchButton, Service, ArrowDown, ArrowRight } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 使用 ref 控制登录状态
const isLoggedIn = ref(false)

const isMenuOpen = ref(false)
// 窗口监听
const viewport = inject('viewport', {
  isMobile: ref(false),
  viewportWidth: ref(0),
  breakpoints: { md: 768 }
})

// 功能栏
const funcs = [
  { icon: House, title: '主页', path: '/' },
  { icon: Warning, title: '风险信号', path: '/risk-signal' },
  { icon: Service, title: '智能助手', path: '/chat' },
  { icon: DataLine, title: '外汇数据', path: '/forex-data' },
  { icon: Memo, title: '新闻展示', path: '/news' }
]

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
// 初始化登录状态
checkLoginStatus()

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
  <el-header>
    <el-affix>
      <el-menu class="custom-header" mode="horizontal" :ellipsis="false" router>

        <!-- 功能区 -->
        <div v-if="viewport.isMobile.value" class="nav-group">
          <el-menu-item index="">
            <el-dropdown trigger="click" @visible-change="(v: boolean) => isMenuOpen = v">
              <div class="header-item">
                <el-icon>
                  <component :is="isMenuOpen ? ArrowDown : ArrowRight " />
                </el-icon>
                <span>功能菜单</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu class="mobile-menu">
                  <el-dropdown-item v-for="(item, index) in funcs" :key="index" @click="router.push(item.path)">
                    <el-icon :size="16">
                      <component :is="item.icon" />
                    </el-icon>
                    <span style="margin-left: 8px">{{ item.title }}</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-menu-item>
        </div>

        <div v-else class="nav-group">
          <el-menu-item v-for="(item, index) in funcs" :key="index" :index="item.path">
            <el-icon>
              <component :is="item.icon" />
            </el-icon>
            <span>{{ item.title }}</span>
          </el-menu-item>
        </div>

        <!-- 用户区 -->
        <div class="nav-group">
          <template v-if="!isLoggedIn">
            <el-menu-item index="/login">
              <el-button type="default">
                <el-icon>
                  <User />
                </el-icon>
                登录
              </el-button>
            </el-menu-item>

            <el-menu-item index="/register">
              <el-button type="primary">
                <el-icon>
                  <Plus />
                </el-icon>
                注册
              </el-button>
            </el-menu-item>
          </template>

          <template v-else>
            <el-menu-item index="/profile">
              <el-icon>
                <UserFilled />
              </el-icon>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item @click="handleLogout" style="color: var(--el-color-danger);">
              <el-icon>
                <SwitchButton />
              </el-icon>
              <span>退出</span>
            </el-menu-item>
          </template>
        </div>
      </el-menu>
    </el-affix>
  </el-header>
</template>

<style scoped>
.el-header {
  --el-header-padding: 0;
  --el-header-height: 3.5rem;
}

.custom-header {
  height: var(--el-header-height);
  display: flex;
  justify-content: space-between;
  /* 取消导航栏底部分界线 */
  /* border-bottom: none !important; */
}

.nav-group {
  display: flex;
  align-items: center;
}

.el-menu-item {
  height: var(--el-header-height);
  padding: 0 16px;
  transition: all 0.2s;
}

.header-item {
  display: flex;
  align-items: center;
  color: var(--el-menu-text-color);
  transition: all 0.2s;
  padding: 0 4px;
  
  &:hover {
    color: var(--el-menu-hover-text-color);
    background-color: var(--el-menu-hover-bg-color);
  }
  
  .el-icon {
    margin-right: 4px;
    font-size: 18px;
  }
  
  span {
    font-size: 14px;
  }
}

:deep(.el-dropdown-menu__item) {
  color: var(--el-text-color);
  font-size: 14px;
  display: flex;
  align-items: center;
  padding: 12px 20px;

  &:hover {
    color: var(--el-color-primary);
    background: var(--el-color-primary-light-9);
  }
}
</style>