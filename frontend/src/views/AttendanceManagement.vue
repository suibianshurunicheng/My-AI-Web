<template>
  <div class="attendance-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">{{ t('attendance.title') }}</h1>
        <p class="page-description">{{ t('attendance.description') }}</p>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="attendance-tabs">
      <el-tab-pane :label="t('attendance.checkInTab')" name="check-in">
        <CheckInRecords />
      </el-tab-pane>
      <el-tab-pane :label="t('attendance.leaveTab')" name="leave">
        <LeaveManagement />
      </el-tab-pane>
      <el-tab-pane :label="t('attendance.overtimeTab')" name="overtime">
        <OvertimeManagement />
      </el-tab-pane>
      <el-tab-pane :label="t('attendance.statisticsTab')" name="statistics">
        <AttendanceStatistics />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CheckInRecords from './attendance/CheckInRecords.vue'
import LeaveManagement from './attendance/LeaveManagement.vue'
import OvertimeManagement from './attendance/OvertimeManagement.vue'
import AttendanceStatistics from './attendance/AttendanceStatistics.vue'
import { useI18n } from '@/utils/i18n'

const { t } = useI18n()
const activeTab = ref('check-in')
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.attendance-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  margin-bottom: 20px;

  .page-title {
    margin: 0 0 8px 0;
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary);
    transition: color 0.3s ease;
  }

  .page-description {
    margin: 0;
    color: var(--text-secondary);
    font-size: 14px;
    transition: color 0.3s ease;
  }
}

.attendance-tabs {
  flex: 1;
  display: flex;
  flex-direction: column;

  :deep(.el-tabs__content) {
    flex: 1;
    overflow: auto;
  }
}
</style>
