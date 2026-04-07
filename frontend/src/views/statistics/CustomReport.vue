<template>
  <div class="custom-report-container">
    <el-card class="report-card">
      <template #header>
        <div class="card-header">
          <span>{{ t('statistics.customReport') }}</span>
        </div>
      </template>

      <el-form :model="formData" label-width="120px">
        <el-form-item :label="t('statistics.exportFormat')">
          <el-radio-group v-model="formData.format">
            <el-radio value="excel">{{ t('employees.excel') }}</el-radio>
            <el-radio value="csv">{{ t('employees.csv') }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="t('statistics.selectFields')">
          <el-checkbox-group v-model="formData.fields">
            <el-row :gutter="10">
              <el-col :span="8" v-for="field in availableFields" :key="field.value">
                <el-checkbox :label="field.value">{{ field.label }}</el-checkbox>
              </el-col>
            </el-row>
          </el-checkbox-group>
        </el-form-item>

        <el-divider>{{ t('statistics.selectFilters') }}</el-divider>

        <el-form-item :label="t('statistics.department')">
          <el-select v-model="formData.filters.departmentId" placeholder="请选择" clearable style="width: 300px">
            <el-option label="技术部" :value="1" />
            <el-option label="产品部" :value="2" />
            <el-option label="市场部" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item :label="t('statistics.status')">
          <el-select v-model="formData.filters.status" placeholder="请选择" clearable style="width: 300px">
            <el-option label="在职" value="ACTIVE" />
            <el-option label="离职" value="INACTIVE" />
          </el-select>
        </el-form-item>

        <el-form-item :label="t('statistics.minAge')">
          <el-input-number v-model="formData.filters.minAge" :min="0" :max="100" clearable style="width: 300px" />
        </el-form-item>

        <el-form-item :label="t('statistics.maxAge')">
          <el-input-number v-model="formData.filters.maxAge" :min="0" :max="100" clearable style="width: 300px" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleExport">
            <el-icon><Download /></el-icon>
            {{ t('statistics.exportReport') }}
          </el-button>
          <el-button @click="handleReset">
            {{ t('common.reset') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import { useI18n } from '@/utils/i18n'
import { exportCustomReport, type ExportRequest } from '@/api/statistics'

const { t } = useI18n()
const loading = ref(false)

const availableFields = [
  { label: '姓名', value: 'name' },
  { label: '性别', value: 'gender' },
  { label: '年龄', value: 'age' },
  { label: '部门', value: 'departmentName' },
  { label: '职位', value: 'positionName' },
  { label: '邮箱', value: 'email' },
  { label: '电话', value: 'phone' },
  { label: '员工编码', value: 'employeeCode' },
  { label: '入职日期', value: 'entryDate' },
  { label: '状态', value: 'status' }
]

const formData = ref<ExportRequest & { filters: any }>({
  format: 'excel',
  fields: ['name', 'gender', 'age', 'departmentName', 'email', 'phone', 'employeeCode'],
  filters: {}
})

const handleExport = async () => {
  if (formData.value.fields.length === 0) {
    ElMessage.warning(t('employees.pleaseSelectFields'))
    return
  }

  loading.value = true
  try {
    const response = await exportCustomReport({
      format: formData.value.format,
      fields: formData.value.fields,
      filters: formData.value.filters
    }) as any

    const blob = response.data || response

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `custom-report.${formData.value.format === 'excel' ? 'xlsx' : 'csv'}`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success(t('common.exportSuccess'))
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error(t('statistics.exportFailed'))
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  formData.value = {
    format: 'excel',
    fields: ['name', 'gender', 'age', 'departmentName', 'email', 'phone', 'employeeCode'],
    filters: {}
  }
}
</script>

<style scoped lang="scss">
.custom-report-container {
  padding: 20px;
}

.report-card {
  .card-header {
    font-weight: 600;
    color: var(--text-primary);
  }
}
</style>
