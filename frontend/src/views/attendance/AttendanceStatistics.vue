<template>
  <div class="attendance-statistics">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('attendance.attendanceStatistics') }}</h2>
        <p class="page-description">{{ t('attendance.attendanceStatisticsDesc') }}</p>
      </div>
    </div>
    
    <div class="filter-bar">
      <el-date-picker
        v-model="selectedDate"
        type="month"
        :placeholder="t('attendance.selectMonth')"
        value-format="YYYY-MM"
        @change="fetchStatistics"
      />
    </div>

    <el-empty v-if="loading" :description="t('common.loading')" />

    <template v-else>
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value">{{ (statistics || {}).totalDays || 0 }}</div>
              <div class="stat-label">{{ t('attendance.totalDays') }}</div>
            </div>
            <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
              <el-icon :size="32"><Calendar /></el-icon>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value" style="color: var(--el-color-success)">{{ (statistics || {}).normalDays || 0 }}</div>
              <div class="stat-label">{{ t('attendance.normalDays') }}</div>
            </div>
            <div class="stat-icon" style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%)">
              <el-icon :size="32"><CircleCheck /></el-icon>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value" style="color: var(--el-color-warning)">{{ (statistics || {}).lateDays || 0 }}</div>
              <div class="stat-label">{{ t('attendance.lateDays') }}</div>
            </div>
            <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
              <el-icon :size="32"><Clock /></el-icon>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value" style="color: var(--el-color-danger)">{{ (statistics || {}).absentDays || 0 }}</div>
              <div class="stat-label">{{ t('attendance.absentDays') }}</div>
            </div>
            <div class="stat-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%)">
              <el-icon :size="32"><Warning /></el-icon>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="detail-card">
        <template #header>
          <div class="card-header">
            <span>{{ t('attendance.detailedStats') }}</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item :label="t('attendance.year')">
            {{ (statistics || {}).year || '-' }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('attendance.month')">
            {{ (statistics || {}).month || '-' }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('attendance.normalDays')">
            <span style="color: var(--el-color-success)">{{ (statistics || {}).normalDays || 0 }} {{ t('attendance.days') }}</span>
          </el-descriptions-item>
          <el-descriptions-item :label="t('attendance.lateDays')">
            <span style="color: var(--el-color-warning)">{{ (statistics || {}).lateDays || 0 }} 次</span>
          </el-descriptions-item>
          <el-descriptions-item :label="t('attendance.earlyLeaveDays')">
            <span style="color: var(--el-color-warning)">{{ (statistics || {}).earlyLeaveDays || 0 }} 次</span>
          </el-descriptions-item>
          <el-descriptions-item :label="t('attendance.absentDays')">
            <span style="color: var(--el-color-danger)">{{ (statistics || {}).absentDays || 0 }} {{ t('attendance.days') }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Calendar, CircleCheck, Clock, Warning } from '@element-plus/icons-vue'
import { statisticsApi } from '@/api/attendance'
import { useI18n } from '@/utils/i18n'

const { t } = useI18n()
const selectedDate = ref('')
const statistics = ref<Record<string, any>>({
  totalDays: 0,
  normalDays: 0,
  lateDays: 0,
  earlyLeaveDays: 0,
  absentDays: 0
})
const loading = ref(true)

const fetchStatistics = async () => {
  if (!selectedDate.value) {
    return
  }
  const [year, month] = selectedDate.value.split('-').map(Number)
  loading.value = true
  try {
    const res = await statisticsApi.getMonthlyStatistics(year, month)
    statistics.value = res.data || {
      totalDays: 0,
      normalDays: 0,
      lateDays: 0,
      earlyLeaveDays: 0,
      absentDays: 0,
      year: year,
      month: month
    }
  } catch (error) {
    ElMessage.error(t('attendance.loadStatsFailed'))
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const now = new Date()
  selectedDate.value = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  fetchStatistics()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.attendance-statistics {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  
  .header-left {
    .page-title {
      margin: 0 0 4px 0;
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
}

.filter-bar {
  margin-bottom: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  :deep(.el-card__body) {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.detail-card {
  .card-header {
    font-weight: 600;
  }
}
</style>
