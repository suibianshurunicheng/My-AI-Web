<template>
  <div class="salary-structure">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('salary.salaryStructure') }}</h2>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus" @click="showAddDialog">
          {{ t('common.add') }}
        </el-button>
      </div>
    </div>

    <div class="info-card">
      <el-alert
        title="社保公积金标准"
        type="info"
        :closable="false"
        show-icon
      >
        <template #default>
          <ul>
            <li>最低工资: 2360元/月</li>
            <li>养老保险: 个人8% / 单位14%</li>
            <li>医疗保险: 个人2% / 单位6%</li>
            <li>失业保险: 个人0.3% / 单位0.7%</li>
            <li>住房公积金: 个人5%-12% / 单位5%-12%</li>
          </ul>
        </template>
      </el-alert>
    </div>

    <el-card class="table-card">
      <el-table :data="structures" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="name" :label="t('salary.itemName')" min-width="150" />
        <el-table-column :label="t('salary.itemType')" width="120">
          <template #default="{ row }">
            <el-tag :type="row.type === 'allowance' ? 'success' : 'danger'">
              {{ row.type === 'allowance' ? t('salary.allowance') : t('salary.deduction') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('salary.calculationFormula')" width="120">
          <template #default="{ row }">
            {{ row.formula === 'fixed' ? t('salary.fixedAmount') : t('salary.percentage') }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="t('salary.amount')" width="150">
          <template #default="{ row }"> 
            {{ row.formula === 'fixed' ? '¥' + row.amount : row.amount + '%' }}
          </template>
        </el-table-column>
        <el-table-column :label="t('salary.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'info'">  
              {{ row.isActive ? t('common.enabled') : t('common.disabled') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.operation')" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="toggleStatus(row)">
              {{ row.isActive ? t('common.disable') : t('common.enable') }}
            </el-button>
            <el-button type="warning" link size="small" @click="showEditDialog(row)">   
              {{ t('common.edit') }}
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item :label="t('salary.itemName')" prop="name">
          <el-input v-model="form.name" :placeholder="t('salary.pleaseEnterItemName')" />
        </el-form-item>
        <el-form-item :label="t('salary.itemType')" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio value="allowance">{{ t('salary.allowance') }}</el-radio>
            <el-radio value="deduction">{{ t('salary.deduction') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="t('salary.calculationFormula')" prop="formula">
          <el-radio-group v-model="form.formula">
            <el-radio value="fixed">{{ t('salary.fixedAmount') }}</el-radio>
            <el-radio value="percentage">{{ t('salary.percentage') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="t('salary.amount')" prop="amount">
          <el-input-number 
            v-model="form.amount" 
            :min="0" 
            :precision="2"
            :step="form.formula === 'percentage' ? 1 : 100"
            style="width: 100%;"
          />
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useI18n } from '@/utils/i18n'
import axios from 'axios'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const structures = ref<any[]>([]) 
const dialogVisible = ref(false)
const dialogTitle = computed(() => isEdit.value ? t('common.edit') : t('common.add'))
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
  id: null as number | null,
  name: '',
  type: 'allowance',
  formula: 'fixed',
  amount: 0
})

const rules: FormRules = {
  name: [{ required: true, message: t('salary.pleaseEnterItemName'), trigger: 'blur' }],
  type: [{ required: true, message: t('common.pleaseSelect'), trigger: 'change' }],
  formula: [{ required: true, message: t('common.pleaseSelect'), trigger: 'change' }],
  amount: [{ required: true, message: t('common.pleaseEnter'), trigger: 'blur' }]
}

const loadStructures = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/salary/structures')
    if (response.data.code === 200) {
      structures.value = response.data.data
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    name: '',
    type: 'allowance',
    formula: 'fixed',
    amount: 0
  })
  dialogVisible.value = true
}

const showEditDialog = (row: any) => {
  isEdit.value = true
  Object.assign(form, {
    id: row.id,
    name: row.name,
    type: row.type,
    formula: row.formula,
    amount: row.amount
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {    
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const response = await axios.post('/api/salary/structures', form)
    if (response.data.code === 200) {
      ElMessage.success(isEdit.value ? t('common.editSuccess') : t('common.addSuccess'))
      dialogVisible.value = false
      loadStructures()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

const toggleStatus = async (row: any) => {
  try {
    const updated = { ...row, isActive: !row.isActive }
    const response = await axios.post('/api/salary/structures', updated)
    if (response.data.code === 200) {
      ElMessage.success(t('common.success'))
      loadStructures()
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  }
}

const handleDelete = async (row: any) => {  
  try {
    await ElMessageBox.confirm(t('common.confirmDelete'), t('common.confirm'), {
      confirmButtonText: t('common.confirm'),
      cancelButtonText: t('common.cancel'),
      type: 'warning'
    })
    
    const response = await axios.delete(`/api/salary/structures/${row.id}`)
    if (response.data.code === 200) {
      ElMessage.success(t('common.deleteSuccess'))
      loadStructures()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.error'))
    }
  }
}

onMounted(() => {
  loadStructures()
})
</script>

<style scoped lang="scss">  
@use '@/styles/variables.scss' as *;

.salary-structure {
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

.info-card {
  margin-bottom: 20px;
}

.table-card {
  flex: 1;
  overflow: auto;
}
</style>

