<template>
  <div class="turnover-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm">
        <el-form-item :label="t('statistics.year')">
          <el-select v-model="filterForm.year" style="width: 150px">
            <el-option v-for="y in availableYears" :key="y" :label="y" :value="y" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon><Refresh /></el-icon>
            {{ t('common.search') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ t('statistics.monthlyTurnover') }}</span>
            </div>
          </template>
          <div ref="monthlyChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>{{ t('statistics.departmentTurnover') }}</span>
            </div>
          </template>
          <div ref="departmentChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { useI18n } from '@/utils/i18n'
import {
  getTurnoverByMonth,
  getTurnoverByDepartment,
  type TurnoverMonthlyDTO,
  type TurnoverDepartmentDTO
} from '@/api/statistics'

const { t } = useI18n()

const currentYear = new Date().getFullYear()
const availableYears = computed(() => [currentYear, currentYear - 1, currentYear - 2])

const filterForm = ref({
  year: currentYear
})

const monthlyTurnover = ref<TurnoverMonthlyDTO[]>([])
const departmentTurnover = ref<TurnoverDepartmentDTO[]>([])

const monthlyChartRef = ref<HTMLElement>()
const departmentChartRef = ref<HTMLElement>()

let monthlyChart: ECharts | null = null
let departmentChart: ECharts | null = null

const loadData = async () => {
  try {
    const [monthlyRes, deptRes] = await Promise.all([
      getTurnoverByMonth(filterForm.value.year),
      getTurnoverByDepartment(filterForm.value.year)
    ])

    if (monthlyRes.code === 200) {
      monthlyTurnover.value = monthlyRes.data
    }
    if (deptRes.code === 200) {
      departmentTurnover.value = deptRes.data
    }

    renderCharts()
  } catch (error) {
    ElMessage.error(t('statistics.loadStatsFailed'))
  }
}

const renderCharts = () => {
  renderMonthlyChart()
  renderDepartmentChart()
}

const renderMonthlyChart = () => {
  if (!monthlyChartRef.value) return

  if (!monthlyChart) {
    monthlyChart = echarts.init(monthlyChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        let result = params[0].name + '月<br/>'
        params.forEach((param: any) => {
          result += `${param.seriesName}: ${param.value}<br/>`
        })
        return result
      }
    },
    legend: {
      data: [t('statistics.total'), t('statistics.departures'), t('statistics.rate')]
    },
    xAxis: {
      type: 'category',
      data: monthlyTurnover.value.map(item => item.month)
    },
    yAxis: [
      {
        type: 'value',
        name: t('statistics.total'),
        position: 'left'
      },
      {
        type: 'value',
        name: t('statistics.rate'),
        position: 'right',
        axisLabel: {
          formatter: '{value}%'
        }
      }
    ],
    series: [
      {
        name: t('statistics.total'),
        type: 'bar',
        data: monthlyTurnover.value.map(item => item.totalEmployees),
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: '#667eea'
        }
      },
      {
        name: t('statistics.departures'),
        type: 'bar',
        data: monthlyTurnover.value.map(item => item.departures),
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: '#f093fb'
        }
      },
      {
        name: t('statistics.rate'),
        type: 'line',
        yAxisIndex: 1,
        data: monthlyTurnover.value.map(item => (item.turnoverRate * 100).toFixed(1)),
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#f45c43'
        },
        itemStyle: {
          color: '#f45c43'
        }
      }
    ]
  }

  monthlyChart.setOption(option)
}

const renderDepartmentChart = () => {
  if (!departmentChartRef.value) return

  if (!departmentChart) {
    departmentChart = echarts.init(departmentChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        let result = params[0].name + '<br/>'
        params.forEach((param: any) => {
          if (param.seriesName === t('statistics.rate')) {
            result += `${param.seriesName}: ${(param.value * 100).toFixed(1)}%<br/>`
          } else {
            result += `${param.seriesName}: ${param.value}<br/>`
          }
        })
        return result
      }
    },
    legend: {
      data: [t('statistics.total'), t('statistics.departures'), t('statistics.rate')]
    },
    xAxis: {
      type: 'category',
      data: departmentTurnover.value.map(item => item.departmentName)
    },
    yAxis: [
      {
        type: 'value',
        name: t('statistics.total'),
        position: 'left'
      },
      {
        type: 'value',
        name: t('statistics.rate'),
        position: 'right',
        axisLabel: {
          formatter: '{value}%'
        }
      }
    ],
    series: [
      {
        name: t('statistics.total'),
        type: 'bar',
        data: departmentTurnover.value.map(item => item.totalEmployees),
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: '#667eea'
        }
      },
      {
        name: t('statistics.departures'),
        type: 'bar',
        data: departmentTurnover.value.map(item => item.departures),
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: '#f093fb'
        }
      },
      {
        name: t('statistics.rate'),
        type: 'line',
        yAxisIndex: 1,
        data: departmentTurnover.value.map(item => (item.turnoverRate * 100).toFixed(1)),
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#f45c43'
        },
        itemStyle: {
          color: '#f45c43'
        }
      }
    ]
  }

  departmentChart.setOption(option)
}

const handleResize = () => {
  monthlyChart?.resize()
  departmentChart?.resize()
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  monthlyChart?.dispose()
  departmentChart?.dispose()
})
</script>

<style scoped lang="scss">
.turnover-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
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
  height: 400px;
}
</style>
