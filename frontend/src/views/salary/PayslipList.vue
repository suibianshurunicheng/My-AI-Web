<template>  
  <div class="payslip-list">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('salary.payslips') }}</h2>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Download" @click="showExportDialog" :loading="exportLoading">
          {{ t('common.export') }}
        </el-button>
        <el-button type="success" :icon="MagicStick" @click="showGenerateDialog" :loading="generateLoading">
          {{ t('salary.generatePayslips') }}
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item :label="t('salary.year')">
          <el-date-picker
            v-model="searchForm.month"
            type="month"
            :placeholder="t('salary.selectMonth')"
            value-format="YYYY-MM"
            @change="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" @click="handleSearch">{{ t('common.search') }}</el-button> 
          <el-button :icon="RefreshLeft" @click="handleReset">{{ t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="table-container">
      <el-table :data="payslips" v-loading="loading" stripe>
        <el-table-column prop="employeeCode" :label="t('employees.employeeCode')" width="120" />
        <el-table-column prop="employeeName" :label="t('employees.name')" width="120" />
        <el-table-column :label="t('salary.yearMonth')" width="120">
          <template #default="{ row }">
            {{ row.year }}-{{ String(row.monthNum).padStart(2, '0') }}
          </template>
        </el-table-column>
        <el-table-column prop="basicSalary" :label="t('salary.basicSalary')" width="120">
          <template #default="{ row }">¥{{ row.basicSalary }}</template>
        </el-table-column>
        <el-table-column prop="positionSalary" :label="t('salary.positionSalary')" width="120">
          <template #default="{ row }">¥{{ row.positionSalary }}</template>
        </el-table-column>
        <el-table-column prop="performanceBonus" :label="t('salary.performanceBonus')" width="120">
          <template #default="{ row }">¥{{ row.performanceBonus }}</template>
        </el-table-column>
        <el-table-column prop="grossSalary" :label="t('salary.grossSalary')" width="120">
          <template #default="{ row }">
            <span style="color: #67c23a; font-weight: 600;">¥{{ row.grossSalary }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="socialInsurance" :label="t('salary.socialInsurance')" width="120">
          <template #default="{ row }">¥{{ row.socialInsurance }}</template>
        </el-table-column>
        <el-table-column prop="housingFund" :label="t('salary.housingFund')" width="120">
          <template #default="{ row }">¥{{ row.housingFund }}</template>
        </el-table-column>
        <el-table-column prop="individualTax" :label="t('salary.individualTax')" width="120">
          <template #default="{ row }">¥{{ row.individualTax }}</template>
        </el-table-column>  
        <el-table-column prop="netSalary" :label="t('salary.netSalary')" width="140" fixed="right">
          <template #default="{ row }">
            <span style="color: #e6a23c; font-weight: 700; font-size: 16px;">¥{{ row.netSalary }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.operation')" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewPayslip(row)">
              {{ t('common.view') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />

    <el-dialog v-model="generateDialogVisible" :title="t('salary.generatePayslips')" width="500px">
      <el-form :model="generateForm" label-width="100px">
        <el-form-item :label="t('salary.yearMonth')">  
          <el-date-picker
            v-model="generateForm.month"
            type="month"
            :placeholder="t('salary.selectMonth')"
            value-format="YYYY-MM"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="generateDialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleGenerate" :loading="generateLoading">
          {{ t('common.confirm') }}
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="exportDialogVisible" :title="t('common.export')" width="500px">
      <el-form :model="exportForm" label-width="100px">
        <el-form-item :label="t('salary.yearMonth')">  
          <el-date-picker
            v-model="exportForm.month"
            type="month"
            :placeholder="t('salary.selectMonth')"
            value-format="YYYY-MM"
          />
        </el-form-item>
        <el-form-item :label="t('common.exportFormat')">
          <el-radio-group v-model="exportForm.format">
            <el-radio-button value="excel">Excel</el-radio-button>
            <el-radio-button value="csv">CSV</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="exportDialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleExport" :loading="exportLoading">
          {{ t('common.confirm') }}
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" :title="t('salary.payslipDetail')" width="700px">
      <div v-if="selectedPayslip" class="payslip-detail">
        <div class="detail-header">
          <h3>{{ selectedPayslip.employeeName }} - {{ selectedPayslip.year }}年{{ selectedPayslip.monthNum }}月工资条</h3>
          <p>员工编码: {{ selectedPayslip.employeeCode }}</p>
        </div>
        <el-divider />
        <div class="detail-section">
          <h4>收入明细</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item :label="t('salary.basicSalary')">¥{{ selectedPayslip.basicSalary }}</el-descriptions-item>  
            <el-descriptions-item :label="t('salary.positionSalary')">¥{{ selectedPayslip.positionSalary }}</el-descriptions-item>
            <el-descriptions-item :label="t('salary.performanceBonus')">¥{{ selectedPayslip.performanceBonus }}</el-descriptions-item>
            <el-descriptions-item :label="t('salary.allowances')">¥{{ selectedPayslip.allowances }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <el-divider />
        <div class="detail-section">
          <h4>扣款明细</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item :label="t('salary.socialInsurance')">¥{{ selectedPayslip.socialInsurance }}</el-descriptions-item>
            <el-descriptions-item :label="t('salary.housingFund')">¥{{ selectedPayslip.housingFund }}</el-descriptions-item>
            <el-descriptions-item :label="t('salary.individualTax')">¥{{ selectedPayslip.individualTax }}</el-descriptions-item>
            <el-descriptions-item :label="t('salary.deductions')">¥{{ selectedPayslip.deductions }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <el-divider />
        <div class="detail-summary">
          <div class="summary-item">
            <span class="label">{{ t('salary.grossSalary') }}:</span>
            <span class="value gross">¥{{ selectedPayslip.grossSalary }}</span>
          </div>
          <div class="summary-item">
            <span class="label">{{ t('salary.netSalary') }}:</span>
            <span class="value net">¥{{ selectedPayslip.netSalary }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>  
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, RefreshLeft, Download, MagicStick, View } from '@element-plus/icons-vue'
import { useI18n } from '@/utils/i18n'
import axios from 'axios'

const { t } = useI18n()

const loading = ref(false)
const generateLoading = ref(false)
const exportLoading = ref(false)
const payslips = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = ref({
  month: ''
})

const generateDialogVisible = ref(false)
const generateForm = ref({
  month: ''
})

const exportDialogVisible = ref(false)
const exportForm = ref({
  month: '',
  format: 'excel'
})

const detailDialogVisible = ref(false)
const selectedPayslip = ref<any>(null)

const loadPayslips = async () => {
  loading.value = true
  try {
    let year: number | undefined
    let monthNum: number | undefined
    
    if (searchForm.value.month) {
      const [y, m] = searchForm.value.month.split('-')
      year = parseInt(y)
      monthNum = parseInt(m)
    }
    
    const params: any = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (year) params.year = year
    if (monthNum) params.monthNum = monthNum
    
    const response = await axios.get('/api/salary/payslips', { params })
    if (response.data.code === 200) {
      payslips.value = response.data.data.list
      total.value = response.data.data.total
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadPayslips()
}

const handleReset = () => {
  searchForm.value.month = ''
  currentPage.value = 1
  loadPayslips()
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  loadPayslips()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadPayslips()
}

const showGenerateDialog = () => {    
  generateForm.value.month = ''
  generateDialogVisible.value = true
}

const handleGenerate = async () => {
  if (!generateForm.value.month) {
    ElMessage.warning(t('common.pleaseFillComplete'))
    return
  }
  
  generateLoading.value = true
  try {
    const [year, monthNum] = generateForm.value.month.split('-')
    const response = await axios.post('/api/salary/payslips/generate', {
      year: parseInt(year),
      monthNum: parseInt(monthNum)
    })
    
    if (response.data.code === 200) {
      ElMessage.success(t('salary.generateSuccess'))
      generateDialogVisible.value = false
      loadPayslips()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    generateLoading.value = false
  }
}

const showExportDialog = () => {
  exportForm.value.month = searchForm.value.month
  exportForm.value.format = 'excel'
  exportDialogVisible.value = true
}

const handleExport = async () => {
  exportLoading.value = true
  try {
    let year: number | undefined
    let monthNum: number | undefined
    
    if (exportForm.value.month) {
      const [y, m] = exportForm.value.month.split('-')
      year = parseInt(y)
      monthNum = parseInt(m)
    }
    
    const response = await axios.post('/api/salary/payslips/export', {
      format: exportForm.value.format,
      filters: { year, monthNum },
      fields: ['employeeCode', 'employeeName', 'basicSalary', 'positionSalary', 'performanceBonus', 'grossSalary', 'socialInsurance', 'housingFund', 'individualTax', 'netSalary']
    }, {
      responseType: 'blob'
    })
    
    const blob = new Blob([response.data], { 
      type: exportForm.value.format === 'excel' ? 
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' : 
        'text/csv' 
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `payslips.${exportForm.value.format === 'excel' ? 'xlsx' : 'csv'}`
    link.click()
    window.URL.revokeObjectURL(url)
    
    ElMessage.success(t('common.exportSuccess'))
    exportDialogVisible.value = false
  } catch (error) {
    ElMessage.error(t('common.exportFailed'))
  } finally {
    exportLoading.value = false
  }
}

const viewPayslip = (payslip: any) => {
  selectedPayslip.value = payslip
  detailDialogVisible.value = true
}

onMounted(() => {
  loadPayslips()
})
</script>

<style scoped lang="scss">  
@use '@/styles/variables.scss' as *;

.payslip-list {
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

.search-card {
  margin-bottom: 20px;
}

.table-container {
  flex: 1;
  overflow: auto;
  margin-bottom: 20px;
}

.payslip-detail {
  .detail-header {
    text-align: center;
    
    h3 {
      margin: 0 0 8px 0;
      font-size: 20px;
      color: var(--text-primary);
    }
    
    p {
      margin: 0;
      color: var(--text-secondary);
    }
  }
  
  .detail-section {
    h4 {
      margin: 0 0 12px 0;
      font-size: 16px;
      color: var(--text-primary);
    }
  }
  
  .detail-summary {
    display: flex;
    justify-content: space-around;
    padding: 20px;
    background: var(--bg-color-lighter);
    border-radius: 8px;
    
    .summary-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      
      .label {
        font-size: 14px;
        color: var(--text-secondary);
      }
      
      .value {
        font-size: 24px;
        font-weight: 700;
        
        &.gross {
          color: #67c23a;
        }
        
        &.net {
          color: #e6a23c;
          font-size: 28px;
        }
      }
    }
  }
}
</style>

