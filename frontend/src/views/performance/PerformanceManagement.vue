<template>
  <div class="performance-management">
    <el-tabs v-model="activeTab" class="performance-tabs">
      <el-tab-pane :label="t('performance.kpiIndicators')" name="kpi">
        <KpiIndicatorList />
      </el-tab-pane>
      <el-tab-pane :label="t('performance.cycles')" name="cycles">
        <PerformanceCycleList />
      </el-tab-pane>
      <el-tab-pane :label="t('performance.results')" name="results">
        <PerformanceResultList />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import KpiIndicatorList from './KpiIndicatorList.vue'
import PerformanceCycleList from './PerformanceCycleList.vue'
import PerformanceResultList from './PerformanceResultList.vue'
import { useI18n } from '@/utils/i18n'

const { t } = useI18n()
const activeTab = ref('kpi')
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.performance-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.performance-tabs {
  flex: 1;
  display: flex;
  flex-direction: column;
  
  :deep(.el-tabs__content) {
    flex: 1;
    overflow: auto;
  }
}
</style>
