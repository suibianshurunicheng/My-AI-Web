<template>
  <div class="performance-result-list">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('performance.results') }}</h2>
      </div>
    </div>

    <el-card class="filter-card">
      <el-form :model="searchForm" inline>
        <el-form-item :label="t('performance.cycle')">
          <el-select v-model="searchForm.cycleId" placeholder="选择考核周期" style="width: 200px" @change="loadResults">
            <el-option
              v-for="cycle in cycles"
              :key="cycle.id"
              :label="cycle.name"
              :value="cycle.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ gradeCounts.A || 0 }}</div>
            <div class="stat-label">A 等级</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ gradeCounts.B || 0 }}</div>
            <div class="stat-label">B 等级</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ gradeCounts.C || 0 }}</div>
            <div class="stat-label">C 等级</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ gradeCounts.D || 0 }}/{{ gradeCounts.E || 0 }}</div>
            <div class="stat-label">D/E 等级</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card">
      <el-table :data="results" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="employeeName" :label="t('performance.employeeName')" width="120" />
        <el-table-column prop="employeeCode" :label="t('performance.employeeCode')" width="120" />
        <el-table-column prop="cycleName" :label="t('performance.cycle')" min-width="200" />
        <el-table-column prop="totalScore" :label="t('performance.totalScore')" width="100">
          <template #default="{ row }">
            <span class="score-value">{{ row.totalScore.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="grade" :label="t('performance.grade')" width="80">
          <template #default="{ row }">
            <el-tag :type="getGradeType(row.grade)">{{ row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bonusAmount" :label="t('performance.bonus')" width="120">
          <template #default="{ row }">
            ¥{{ (row.bonusAmount || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="t('performance.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'COMPLETED' ? 'success' : 'info'">
              {{ row.status === 'COMPLETED' ? t('performance.statusCompleted') : row.status === 'ACTIVE' ? t('performance.statusActive') : t('performance.statusDraft') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.actions')" width="100" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="showDetail(row)">
              {{ t('common.view') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadResults"
        @current-change="loadResults"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="detailVisible" :title="t('performance.resultDetail')" width="600px">
      <div v-if="selectedResult" class="result-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item :label="t('performance.employeeName')">{{ selectedResult.employeeName }}</el-descriptions-item>
          <el-descriptions-item :label="t('performance.employeeCode')">{{ selectedResult.employeeCode }}</el-descriptions-item>
          <el-descriptions-item :label="t('performance.cycle')">{{ selectedResult.cycleName }}</el-descriptions-item>
          <el-descriptions-item :label="t('performance.totalScore')">
            <span class="score-value">{{ selectedResult.totalScore.toFixed(2) }}</span>
          </el-descriptions-item>
          <el-descriptions-item :label="t('performance.grade')">
            <el-tag :type="getGradeType(selectedResult.grade)">{{ selectedResult.grade }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item :label="t('performance.bonus')">
            ¥{{ (selectedResult.bonusAmount || 0).toFixed(2) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('performance.finalComment')" :span="2">{{ selectedResult.finalComment }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { ElMessage } from 'element-plus'
import { resultApi, cycleApi, type PerformanceResult, type PerformanceCycle } from '@/api/performance'

const { t } = useI18n()

const loading = ref(false)
const detailVisible = ref(false)
const selectedResult = ref<PerformanceResult | null>(null)

const results = ref<PerformanceResult[]>([])
const allResults = ref<PerformanceResult[]>([])
const cycles = ref<PerformanceCycle[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = ref({
  cycleId: undefined as number | undefined
})

const gradeCounts = computed(() => {
  const counts: Record<string, number> = { A: 0, B: 0, C: 0, D: 0, E: 0 }
  allResults.value.forEach(result => {
    if (result.grade) {
      counts[result.grade]++
    }
  })
  return counts
})

const getGradeType = (grade: string) => {
  const types: Record<string, any> = {
    A: 'success',
    B: 'primary',
    C: 'warning',
    D: 'info',
    E: 'danger'
  }
  return types[grade] || 'info'
}

const loadCycles = async () => {
  try {
    const response = await cycleApi.getCycles({ page: 1, size: 100 })
    if (response.code === 200) {
      cycles.value = response.data.list
      if (cycles.value.length > 0) {
        searchForm.value.cycleId = cycles.value[0].id
      }
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  }
}

const loadResults = async () => {
  if (!searchForm.value.cycleId) return
  
  loading.value = true
  try {
    // 获取所有结果用于统计
    const allResponse = await resultApi.getResultsByCycle(searchForm.value.cycleId, {
      page: 1,
      size: 1000 // 设置一个足够大的值来获取所有数据
    })
    if (allResponse.code === 200) {
      allResults.value = allResponse.data.list
    }
    
    // 获取当前页数据
    const pageResponse = await resultApi.getResultsByCycle(searchForm.value.cycleId, {
      page: currentPage.value,
      size: pageSize.value
    })
    if (pageResponse.code === 200) {
      results.value = pageResponse.data.list
      total.value = pageResponse.data.total
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    loading.value = false
  }
}

const showDetail = (result: PerformanceResult) => {
  selectedResult.value = result
  detailVisible.value = true
}

onMounted(() => {
  loadCycles().then(() => {
    loadResults()
  })
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.performance-result-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .page-title {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary);
    transition: color 0.3s ease;
  }
}

.filter-card {
  margin-bottom: 20px;
}

.stats-row {
  margin-bottom: 20px;
  
  .stat-card {
    .stat-content {
      text-align: center;
      
      .stat-number {
        font-size: 32px;
        font-weight: 700;
        color: var(--primary-color);
        margin-bottom: 8px;
      }
      
      .stat-label {
        font-size: 14px;
        color: var(--text-secondary);
      }
    }
  }
}

.table-card {
  flex: 1;
  overflow: auto;
}

.score-value {
  font-size: 16px;
  font-weight: 600;
  color: var(--primary-color);
}

.result-detail {
  padding: 10px;
}
</style>
