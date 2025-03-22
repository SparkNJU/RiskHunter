<script setup lang="ts">
import { ref } from 'vue';
import { Notification } from '@element-plus/icons-vue';
import { type AlertVO } from '../../types/signal';
import { getAlerts } from '../../api/signal';
import { parseTime } from '../../utils';

const alerts = ref<AlertVO[]>([
]);

const loading = ref(false);

const getAlertTypeTag = (level: string) => {
  switch (level) {
    case 'urgent': return 'danger';
    case 'warning': return 'warning';
    case 'normal': return 'success';
    default: return 'info';
  }
};

const getAlertIcon = (level: string) => {
  switch (level) {
    case 'urgent': return '🔴';
    case 'warning': return '🟠';
    case 'normal': return '🟢';
    default: return '⚪';
  }
};

const loadData = async () => {
  try {
    loading.value = true;
    getAlerts().then((res: any) => {
      alerts.value.push(res.data)
    })
  } catch (error) {
    console.error(error)
  }
  finally {
    loading.value = false;
  }
}
loadData()
</script>

<template>
  <el-card class="alert-list-card" v-loading="loading">
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <Notification />
          </el-icon>
          预警列表
        </h3>
      </div>
    </template>

    <div class="alert-content">
      <div v-for="(alert, index) in alerts" :key="index" class="alert-item" :class="`alert-${alert.level}`">
        <div class="alert-icon">{{ getAlertIcon(alert.level) }}</div>
        <div class="alert-details">
          <div class="alert-header">
            <div class="alert-title">{{ alert.title }}</div>
            <el-tag size="small" :type="getAlertTypeTag(alert.level)">
              {{ alert.level === 'urgent' ? '紧急' :
                alert.level === 'warning' ? '关注' : '正常' }}
            </el-tag>
          </div>
          <div class="alert-message">{{ alert.content }}</div>
          <div class="alert-time">{{ parseTime(alert.updateTime) }}</div>
        </div>
      </div>

      <div v-if="alerts.length === 0" class="no-alerts">
        暂无预警信息
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.alert-list-card {
  height: 100%;
}

.alert-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.alert-item {
  display: flex;
  padding: 12px;
  border-radius: 4px;
  background-color: var(--el-fill-color-light);
  transition: all 0.3s;
}

.alert-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.alert-urgent {
  border-left: 4px solid var(--el-color-danger);
}

.alert-warning {
  border-left: 4px solid var(--el-color-warning);
}

.alert-normal {
  border-left: 4px solid var(--el-color-success);
}

.alert-icon {
  margin-right: 12px;
  font-size: 20px;
  display: flex;
  align-items: flex-start;
}

.alert-details {
  flex-grow: 1;
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.alert-title {
  font-weight: 600;
  font-size: 14px;
  color: var(--el-text-color-primary);
}

.alert-message {
  font-size: 13px;
  color: var(--el-text-color-regular);
  margin-bottom: 4px;
}

.alert-time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.no-alerts {
  text-align: center;
  padding: 24px 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}
</style>