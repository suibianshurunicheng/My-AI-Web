<template>
  <div class="performance-cycle-list">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('performance.cycles') }}</h2>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus" @click="showAddDialog">
          {{ t('common.add') }}
        </el-button>
      </div>
    </div>

    <el-card class="table-card">
      <el-table :data="cycles" v-loading="loading" stripe>
        <el-table-column prop="name" :label="t('performance.name')" min-width="200" />
        <el-table-column prop="cycleType" :label="t('performance.cycleType')" width="120">
          <template #default="{ row }">
            <el-tag>
              {{ row.cycleType === 'MONTHLY' ? t('performance.cycleTypeMonthly') : 
                 row.cycleType === 'QUARTERLY' ? t('performance.cycleTypeQuarterly') : 
                 t('performance.cycleTypeYearly') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" :label="t('performance.startDate')" width="180" />
        <el-table-column prop="endDate" :label="t('performance.endDate')" width="180" />
        <el-table-column prop="status" :label="t('performance.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : row.status === 'COMPLETED' ? '' : 'info'">
              {{ row.status === 'DRAFT' ? t('performance.statusDraft') : 
                 row.status === 'ACTIVE' ? t('performance.statusActive') : 
                 t('performance.statusCompleted') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.actions')" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="showEditDialog(row)">
              {{ t('common.edit') }}
            </el-button>
            <el-button size="small" type="success" @click="handleCalculate(row)">
              {{ t('performance.calculate') }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">
              {{ t('common.delete') }}
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
        @size-change="loadCycles"
        @current-change="loadCycles"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? t('common.edit') : t('common.add')"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item :label="t('performance.name')" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item :label="t('performance.cycleType')" prop="cycleType">
          <el-select v-model="form.cycleType" style="width: 100%">
            <el-option :label="t('performance.cycleTypeMonthly')" value="MONTHLY" />
            <el-option :label="t('performance.cycleTypeQuarterly')" value="QUARTERLY" />
            <el-option :label="t('performance.cycleTypeYearly')" value="YEARLY" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('performance.startDate')" prop="startDate">
          <el-date-picker
            v-model="form.startDate"
            type="datetime"
            placeholder="选择开始日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="t('performance.endDate')" prop="endDate">
          <el-date-picker
            v-model="form.endDate"
            type="datetime"
            placeholder="选择结束日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="t('performance.status')" prop="status">
          <el-select v-model="form.status" style="width: 100%">
            <el-option :label="t('performance.statusDraft')" value="DRAFT" />
            <el-option :label="t('performance.statusActive')" value="ACTIVE" />
            <el-option :label="t('performance.statusCompleted')" value="COMPLETED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ t('common.confirm') }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { cycleApi, type PerformanceCycle } from '@/api/performance'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const calculateLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()

const cycles = ref<PerformanceCycle[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const form = ref<PerformanceCycle>({
  name: '',
  cycleType: 'MONTHLY',
  startDate: '',
  endDate: '',
  status: 'DRAFT'
})

const rules = {
  name: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  cycleType: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'change' }],
  startDate: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'change' }],
  endDate: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'change' }],
  status: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'change' }]
}

const loadCycles = async () => {
  loading.value = true
  try {
    const response = await cycleApi.getCycles({
      page: currentPage.value,
      size: pageSize.value
    })
    if (response.code === 200) {
      cycles.value = response.data.list
      total.value = response.data.total
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    name: '',
    cycleType: 'MONTHLY',
    startDate: '',
    endDate: '',
    status: 'DRAFT'
  }
  dialogVisible.value = true
}

const showEditDialog = (cycle: PerformanceCycle) => {
  isEdit.value = true
  form.value = { ...cycle }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    let response
    if (isEdit.value && form.value.id) {
      response = await cycleApi.updateCycle(form.value.id, form.value)
    } else {
      response = await cycleApi.createCycle(form.value)
    }
    
    if (response.code === 200) {
      ElMessage.success(t('common.success'))
      dialogVisible.value = false
      loadCycles()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    submitLoading.value = false
  }
}

const handleCalculate = async (cycle: PerformanceCycle) => {
  if (!cycle.id) return
  
  try {
    await ElMessageBox.confirm(t('performance.calculateConfirm'), t('common.confirm'), {
      type: 'warning'
    })
    
    calculateLoading.value = true
    const response = await cycleApi.calculateResults(cycle.id)
    if (response.code === 200) {
      ElMessage.success(t('common.success'))
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.error'))
    }
  } finally {
    calculateLoading.value = false
  }
}

const handleDelete = async (cycle: PerformanceCycle) => {
  try {
    await ElMessageBox.confirm(t('common.deleteConfirm'), t('common.confirm'), {
      type: 'warning'
    })
    
    if (cycle.id) {
      const response = await cycleApi.deleteCycle(cycle.id)
      if (response.code === 200) {
        ElMessage.success(t('common.success'))
        loadCycles()
      } else {
        ElMessage.error(response.message)
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.error'))
    }
  }
}

onMounted(() => {
  loadCycles()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.performance-cycle-list {
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

.table-card {
  flex: 1;
  overflow: auto;
}
</style>
