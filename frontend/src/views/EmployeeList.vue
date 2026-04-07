<template>
  <div class="employee-management">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('employees.title') }}</h2>
        <p class="page-description">{{ t('employees.subtitle') }}</p>
      </div>
      <div class="header-right">
        <el-button @click="handleExport" class="export-btn">
          <el-icon><FolderOpened /></el-icon>
          {{ t('employees.export') }}
        </el-button>
        <el-button 
          type="primary" 
          @click="handleCreate"
          class="create-btn"
        >
          <el-icon><Plus /></el-icon>
          {{ t('employees.create') }}
        </el-button>
      </div>
    </div>
    
    <div class="search-bar">
      <div class="search-container">
        <el-input
          v-model="searchParams.name"
          :placeholder="t('employees.searchName')"
          clearable
          @keyup.enter="handleSearch"
          @input="handleSearch"
          class="search-input name-input"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
        
        <el-input
          v-model="searchParams.code"
          :placeholder="t('employees.searchCode')"
          clearable
          @keyup.enter="handleSearch"
          @input="handleSearch"
          class="search-input code-input"
        >
          <template #prefix>
            <el-icon><Document /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    
    <div class="table-container">
      <el-table
        :data="employeeStore.state.employees"
        v-loading="employeeStore.state.loading"
        style="width: 100%"
        stripe
        border
        @row-click="handleRowClick"
      >
        <el-table-column :prop="field.prop" :label="t(field.labelKey)" v-for="field in visibleFields" :key="field.prop" />
        <el-table-column :label="t('common.edit')" width="180" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              size="small" 
              link
              @click="handleView(row)"
            >
              {{ t('common.view') }}
            </el-button>
            <el-button 
              type="warning" 
              size="small" 
              link
              @click="handleEdit(row)"
            >
              {{ t('common.edit') }}
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              link
              @click="handleDelete(row)"
            >
              {{ t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper" v-if="employeeStore.state.pagination.total > 0">
        <el-pagination
          v-model:current-page="employeeStore.state.pagination.page"
          v-model:page-size="employeeStore.state.pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="employeeStore.state.pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <el-dialog 
      v-model="exportDialogVisible" 
      :title="t('employees.exportData')" 
      width="600px"
    >
      <el-form :model="exportForm" label-width="120px">
        <el-form-item :label="t('employees.exportFormat')">
          <el-radio-group v-model="exportForm.format">
            <el-radio-button value="excel">{{ t('employees.excel') }}</el-radio-button>
            <el-radio-button value="csv">{{ t('employees.csv') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item :label="t('employees.exportFields')">
          <div class="field-selection">
            <el-checkbox 
              v-model="selectAll" 
              @change="handleSelectAll"
            >
              {{ t('employees.selectAll') }}
            </el-checkbox>
            <div class="field-checkboxes">
              <el-checkbox 
                v-for="field in exportableFields" 
                :key="field.prop"
                :label="field.prop"
                v-model="exportForm.fields"
              >
                {{ t(field.labelKey) }}
              </el-checkbox>
            </div>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="exportDialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exporting">
          {{ t('common.confirm') }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useEmployeeStore } from '@/stores/employee'
import { Plus, FolderOpened, Search, User, Document } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { exportApi, type ExportRequest } from '@/api/system'
import { useI18n } from '@/utils/i18n'

const router = useRouter()
const employeeStore = useEmployeeStore()
const { t } = useI18n()

const searchParams = ref({
  name: '',
  code: ''
})

const exportDialogVisible = ref(false)
const exporting = ref(false)
const deleting = ref(false)
const exportForm = ref<ExportRequest>({
  format: 'excel',
  fields: ['name', 'gender', 'age', 'position', 'email', 'phone', 'employeeCode']
})

const exportableFields = [
  { prop: 'name', labelKey: 'employees.name' },
  { prop: 'gender', labelKey: 'employees.gender' },
  { prop: 'age', labelKey: 'employees.age' },
  { prop: 'birthDate', labelKey: 'employees.birthDate' },
  { prop: 'height', labelKey: 'employees.height' },
  { prop: 'weight', labelKey: 'employees.weight' },
  { prop: 'position', labelKey: 'employees.position' },
  { prop: 'email', labelKey: 'employees.email' },
  { prop: 'phone', labelKey: 'employees.phone' },
  { prop: 'employeeCode', labelKey: 'employees.employeeCode' },
  { prop: 'createdAt', labelKey: 'employees.createdAt' },
  { prop: 'updatedAt', labelKey: 'employees.updatedAt' }
]

const visibleFields = [
  { prop: 'name', labelKey: 'employees.name' },
  { prop: 'gender', labelKey: 'employees.gender' },
  { prop: 'age', labelKey: 'employees.age' },
  { prop: 'position', labelKey: 'employees.position' },
  { prop: 'email', labelKey: 'employees.email' },
  { prop: 'phone', labelKey: 'employees.phone' },
  { prop: 'employeeCode', labelKey: 'employees.employeeCode' }
]

const selectAll = computed({
  get: () => exportForm.value.fields.length === exportableFields.length,
  set: (value) => {
    if (value) {
      exportForm.value.fields = exportableFields.map(f => f.prop)
    } else {
      exportForm.value.fields = []
    }
  }
})

const handleSelectAll = () => {
}

onMounted(async () => {
  try {
    await employeeStore.fetchEmployees()
  } catch (error) {
    ElMessage.error(t('common.error'))
  }
})

const handleSearch = () => {
  employeeStore.setSearchParams(searchParams.value)
  employeeStore.fetchEmployees(1, employeeStore.state.pagination.size)
}

const handleCreate = () => {
  router.push('/employees/create')
}

const handleView = (employee: any) => {
  router.push(`/employees/detail/${employee.id}`)
}

const handleEdit = (employee: any) => {
  router.push(`/employees/detail/${employee.id}`)
}

const handleDelete = async (employee: any) => {
  if (deleting.value) return
  try {
    deleting.value = true
    await ElMessageBox.confirm(
      `${t('common.confirm')}要删除员工 "${employee.name}" 吗？`,
      t('common.delete'),
      {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        type: 'warning',
      }
    )
    
    await employeeStore.deleteEmployee(employee.id!)
    ElMessage.success(t('common.success'))
    
    router.push('/employees/list')
    
    await employeeStore.fetchEmployees(
      employeeStore.state.pagination.page,
      employeeStore.state.pagination.size
    )
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.error'))
    }
  } finally {
    deleting.value = false
  }
}

const handleRowClick = (row: any) => {
  handleView(row)
}

const handleSizeChange = (size: number) => {
  employeeStore.fetchEmployees(1, size)
}

const handleCurrentChange = (page: number) => {
  employeeStore.fetchEmployees(page, employeeStore.state.pagination.size)
}

const handleExport = () => {
  exportDialogVisible.value = true
}

const confirmExport = async () => {
  if (exportForm.value.fields.length === 0) {
    ElMessage.warning(t('employees.pleaseSelectFields'))
    return
  }
  
  exporting.value = true
  try {
    const response = await exportApi.exportEmployees(exportForm.value)
    
    // 获取文件扩展名
    const extension = exportForm.value.format === 'csv' ? 'csv' : 'xlsx'
    const contentType = exportForm.value.format === 'csv' ? 'text/csv' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    
    // 从响应头获取文件名，如果没有则使用默认文件名
    let filename = `employees.${extension}`
    const contentDisposition = response.headers['content-disposition']
    if (contentDisposition) {
      const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
      if (filenameMatch && filenameMatch[1]) {
        filename = filenameMatch[1].replace(/['"]/g, '')
      }
    }
    
    // 创建下载链接
    const blob = new Blob([response.data], { type: contentType })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success(t('common.success'))
    exportDialogVisible.value = false
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    exporting.value = false
  }
}
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.employee-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  
  .header-left {
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
  
  .header-right {
    display: flex;
    gap: 10px;
    
    .create-btn, .export-btn {
      padding: 10px 20px;
      font-size: 14px;
      transition: all 0.3s ease;
      box-shadow: none;
      
      &:hover {
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
        transform: translateY(-1px);
      }
    }
  }
}

.search-bar {
  margin-bottom: 20px;
  
  .search-container {
    display: flex;
    gap: 12px;
    align-items: center;
    max-width: 800px;
    
    .search-input {
      flex: 1;
      transition: box-shadow 0.3s ease;
      
      :deep(.el-input__wrapper) {
        border-radius: 4px;
        transition: box-shadow 0.3s ease;
      }
      
      &:hover {
        :deep(.el-input__wrapper) {
          box-shadow: 0 0 0 2px rgba(96, 98, 102, 0.2);
        }
      }
    }
    
    .name-input {
      max-width: 200px;
    }
    
    .code-input {
      max-width: 200px;
    }
  }
}

.table-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-color-lighter);
  border-radius: $border-radius;
  box-shadow: $box-shadow-light;
  overflow: hidden;
  transition: background-color 0.3s ease;
  
  :deep(.el-table) {
    .el-table__header-wrapper {
      background-color: var(--table-header-bg);
      transition: background-color 0.3s ease;
    }
    
    .el-table__row {
      cursor: pointer;
      
      &:hover {
        background-color: var(--table-row-hover);
        transition: background-color 0.3s ease;
      }
    }
  }
}

.field-selection {
  .field-checkboxes {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
    margin-top: 10px;
  }
}

.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: center;
  border-top: 1px solid var(--border-light);
  background: var(--bg-color-lighter);
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 15px;
    
    .header-right {
      width: 100%;
      flex-direction: column;
      
      .create-btn, .export-btn {
        width: 100%;
      }
    }
  }
  
  .search-bar {
    .search-input {
      max-width: 100%;
    }
  }
  
  .field-selection {
    .field-checkboxes {
      grid-template-columns: repeat(2, 1fr);
    }
  }
}
</style>
