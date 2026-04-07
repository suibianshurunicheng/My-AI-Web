<template>
  <div class="kpi-indicator-list">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('performance.kpiIndicators') }}</h2>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus" @click="showAddDialog">
          {{ t('common.add') }}
        </el-button>
      </div>
    </div>

    <el-card class="table-card">
      <el-table :data="indicators" v-loading="loading" stripe>
        <el-table-column prop="name" :label="t('performance.name')" min-width="150" />
        <el-table-column prop="description" :label="t('performance.description')" min-width="200" />
        <el-table-column prop="position" :label="t('performance.position')" min-width="120" />
        <el-table-column prop="weight" :label="t('performance.weight')" width="100">
          <template #default="{ row }">
            {{ (row.weight * 100).toFixed(0) }}%
          </template>
        </el-table-column>
        <el-table-column prop="isActive" :label="t('performance.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'info'">
              {{ row.isActive ? t('common.active') : t('common.inactive') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.actions')" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="showEditDialog(row)">
              {{ t('common.edit') }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? t('common.edit') : t('common.add')"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item :label="t('performance.name')" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item :label="t('performance.description')" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item :label="t('performance.position')" prop="position">
          <el-input v-model="form.position" :placeholder="t('performance.positionPlaceholder')" />
        </el-form-item>
        <el-form-item :label="t('performance.weight')" prop="weight">
          <el-input-number v-model="form.weight" :min="0" :max="1" :step="0.01" :precision="2" style="width: 100%" />
          <span style="margin-left: 10px">{{ (form.weight * 100).toFixed(0) }}%</span>
        </el-form-item>
        <el-form-item :label="t('performance.status')" prop="isActive">
          <el-switch v-model="form.isActive" />
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
import { kpiApi, type KpiIndicator } from '@/api/performance'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()

const indicators = ref<KpiIndicator[]>([])

const form = ref<KpiIndicator>({
  name: '',
  description: '',
  position: '',
  weight: 0.1,
  isActive: true
})

const rules = {
  name: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  weight: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }]
}

const loadIndicators = async () => {
  loading.value = true
  try {
    const response = await kpiApi.getIndicators()
    if (response.code === 200) {
      indicators.value = response.data
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
    description: '',
    position: '',
    weight: 0.1,
    isActive: true
  }
  dialogVisible.value = true
}

const showEditDialog = (indicator: KpiIndicator) => {
  isEdit.value = true
  form.value = { ...indicator }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    let response
    if (isEdit.value && form.value.id) {
      response = await kpiApi.updateIndicator(form.value.id, form.value)
    } else {
      response = await kpiApi.createIndicator(form.value)
    }
    
    if (response.code === 200) {
      ElMessage.success(t('common.success'))
      dialogVisible.value = false
      loadIndicators()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (indicator: KpiIndicator) => {
  try {
    await ElMessageBox.confirm(t('common.deleteConfirm'), t('common.confirm'), {
      type: 'warning'
    })
    
    if (indicator.id) {
      const response = await kpiApi.deleteIndicator(indicator.id)
      if (response.code === 200) {
        ElMessage.success(t('common.success'))
        loadIndicators()
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
  loadIndicators()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.kpi-indicator-list {
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
