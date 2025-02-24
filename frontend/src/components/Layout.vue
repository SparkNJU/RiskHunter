<script lang="ts">
import { defineComponent, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  InfoFilled,
  Warning,
  DataLine,
  Memo,
  User,
  UserFilled,
  Plus,
  SwitchButton
} from '@element-plus/icons-vue'

export default defineComponent({
  name: 'Layout',
  components: {
    InfoFilled,
    Warning,
    DataLine,
    Memo,
    User,
    UserFilled,
    Plus,
    SwitchButton
  },
  setup() {
    const router = useRouter()
    const route = useRoute()

    const isLoggedIn = computed(() => {
      return !!sessionStorage.getItem('token')
    })

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
        router.push({ path: "/login" })
      })
    }

    // 监听路由变化
    watch(route, () => {
      // 刷新组件
      router.go(0)
    })

    return {
      isLoggedIn,
      handleLogout
    }
  }
})
</script>

<template>
  <div class="layout">
    <el-affix :offset="0">
      <el-menu class="el-menu" mode="horizontal" :ellipsis="false" router>
        <!-- LOGO -->
        <!-- <div class="brand">
          <img src="../assets/logo.png" alt="Logo" class="brand-logo" />
          <span class="brand-text">RiskHunter</span>
        </div> -->

        <!-- 主导航 -->
        <div class="nav-group">
          <el-menu-item index="/about">
            <el-icon>
              <InfoFilled />
            </el-icon>
            <span>关于我们</span>
          </el-menu-item>
          <el-menu-item index="/risk-signal">
            <el-icon>
              <Warning />
            </el-icon>
            <span>风险信号</span>
          </el-menu-item>
          <el-menu-item index="/about">
            <el-icon>
              <DataLine />
            </el-icon>
            <span>外汇数据</span>
          </el-menu-item>
          <el-menu-item index="/about">
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

            <el-menu-item index="/login">
              <el-button type="primary" text>
                <el-icon>
                  <User />
                </el-icon>
                登录
              </el-button>
            </el-menu-item>

            <el-menu-item index="/register">
              <el-button type="primary" class="register-btn">
                <el-icon>
                  <Plus />
                </el-icon>
                注册
              </el-button>
            </el-menu-item>
          </template>

          <template v-else>

            <el-menu-item index="/dashboard">
              <el-icon>
                <UserFilled />
              </el-icon>
              <span>个人信息</span>

            </el-menu-item>
            <el-menu-item @click="handleLogout">
              <el-button type="danger" text>
                <el-icon>
                  <SwitchButton />
                </el-icon>
                退出
              </el-button>
            </el-menu-item>

          </template>
        </div>
      </el-menu>
    </el-affix>

    <!-- 主视图 -->
    <router-view />
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.el-menu {
  padding: 0 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background-color: #ffffff00; /* 修改为偏灰黑色 */
  display: flex;
  align-items: center;
  height: 60px;
}

/* LOGO相关 */
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
}


.brand-text {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--el-color-primary);
}

/* 导航栏 */
.nav-group {
  display: flex;
  gap: 10px;
}

.flex-grow {
  flex-grow: 1;
}

.user-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* ELEMEMT PLUS */
:deep(.el-menu-item) {
  display: flex;
  align-items: center;
  gap: 5px;
  height: 60px;
  padding: 0 20px;
  transition: all 0.3s ease;
}

:deep(.el-menu-item:hover) {
  color: #3a3a3a; /* 修改为稍浅的灰黑色 */
  --el-menu-text-color: #00ff00; /* 修改为绿色 */
}

:deep(.el-menu-item.is-active) {
  color: #ffd700; /* 修改为金色 */
  border-bottom: 2px solid #ffd700; /* 修改为金色 */
}

:deep(.el-button) {
  display: flex;
  align-items: center;
  gap: 5px;
}

.register-btn {
  border-radius: 20px;
  padding: 8px 24px;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-icon) {
  margin-right: 4px;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .brand-text {
    display: none;
  }

  .nav-group {
    gap: 5px;
  }

  :deep(.el-menu-item) {
    padding: 0 12px;
  }
}
</style>