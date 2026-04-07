<template>
  <div class="dashboard-container">
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon employee-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ employeeCount?.total || 0 }}</div>
              <div class="stat-label">{{ t('statistics.totalEmployees') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active-icon">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ employeeCount?.active || 0 }}</div>
              <div class="stat-label">{{ t('statistics.activeEmployees') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon inactive-icon">
              <el-icon><CircleClose /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ employeeCount?.inactive || 0 }}</div>
              <div class="stat-label">{{ t('statistics.inactiveEmployees') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon gender-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ genderRatio.length }}</div>
              <div class="stat-label">{{ t('statistics.genderRatio') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ t('statistics.genderRatio') }}</span>
            </div>
          </template>
          <div ref="genderChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ t('statistics.departmentCount') }}</span>
            </div>
          </template>
          <div ref="departmentChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ t('statistics.ageDistribution') }}</span>
            </div>
          </template>
          <div ref="ageChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, CircleCheck, CircleClose, UserFilled } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { useI18n } from '@/utils/i18n'
import {
  getEmployeeCount,
  getGenderRatio,
  getDepartmentCount,
  getAgeDistribution,
  type EmployeeCountDTO,
  type GenderRatioDTO,
  type DepartmentCountDTO,
  type AgeDistributionDTO
} from '@/api/statistics'

const { t } = useI18n()

const employeeCount = ref<EmployeeCountDTO | null>(null)
const genderRatio = ref<GenderRatioDTO[]>([])
const departmentCount = ref<DepartmentCountDTO[]>([])
const ageDistribution = ref<AgeDistributionDTO[]>([])

const genderChartRef = ref<HTMLElement>()
const departmentChartRef = ref<HTMLElement>()
const ageChartRef = ref<HTMLElement>()

let genderChart: ECharts | null = null
let departmentChart: ECharts | null = null
let ageChart: ECharts | null = null

const loadData = async () => {
  try {
    const [countRes, genderRes, deptRes, ageRes] = await Promise.all([
      getEmployeeCount(),
      getGenderRatio(),
      getDepartmentCount(),
      getAgeDistribution()
    ])

    if (countRes.code === 200) {
      employeeCount.value = countRes.data
    }
    if (genderRes.code === 200) {
      genderRatio.value = genderRes.data
    }
    if (deptRes.code === 200) {
      departmentCount.value = deptRes.data
    }
    if (ageRes.code === 200) {
      ageDistribution.value = ageRes.data
    }

    renderCharts()
  } catch (error) {
    ElMessage.error(t('statistics.loadStatsFailed'))
  }
}

const renderCharts = () => {
  renderGenderChart()
  renderDepartmentChart()
  renderAgeChart()
}

const renderGenderChart = () => {
  if (!genderChartRef.value) return

  if (!genderChart) {
    genderChart = echarts.init(genderChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: t('statistics.genderRatio'),
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: genderRatio.value.map(item => ({
          value: item.count,
          name: item.gender
        }))
      }
    ]
  }

  genderChart.setOption(option)
}

const renderDepartmentChart = () => {
  if (!departmentChartRef.value) return

  if (!departmentChart) {
    departmentChart = echarts.init(departmentChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: departmentCount.value.map(item => item.departmentName)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: t('statistics.departmentCount'),
        type: 'bar',
        data: departmentCount.value.map(item => item.count),
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        }
      }
    ]
  }

  departmentChart.setOption(option)
}

const renderAgeChart = () => {
  if (!ageChartRef.value) return

  if (!ageChart) {
    ageChart = echarts.init(ageChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'item'
    },
    xAxis: {
      type: 'category',
      data: ageDistribution.value.map(item => item.range)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: t('statistics.ageDistribution'),
        type: 'line',
        data: ageDistribution.value.map(item => item.count),
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.5)' },
            { offset: 1, color: 'rgba(118, 75, 162, 0.1)' }
          ])
        },
        lineStyle: {
          width: 3,
          color: '#667eea'
        },
        itemStyle: {
          color: '#667eea'
        }
      }
    ]
  }

  ageChart.setOption(option)
}

const handleResize = () => {
  genderChart?.resize()
  departmentChart?.resize()
  ageChart?.resize()
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  genderChart?.dispose()
  departmentChart?.dispose()
  ageChart?.dispose()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  .stat-content {
    display: flex;
    align-items: center;
    gap: 15px;
  }

  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28px;
    color: white;
  }

  .employee-icon {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  .active-icon {
    background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  }

  .inactive-icon {
    background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);
  }

  .gender-icon {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }

  .stat-info {
    flex: 1;
  }

  .stat-value {
    font-size: 28px;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 4px;
  }

  .stat-label {
    font-size: 14px;
    color: var(--text-secondary);
  }
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  .card-header {
    font-weight: 600;
    color: var(--text-primary);
  }
}

.chart-container {
  width: 100%;
  height: 350px;
}
</style>
