<template>
  <div class="job-list">
    <div class="header">
      <el-button type="primary" :icon="Plus" @click="showAddDialog">
        {{ t('common.add') }}
      </el-button>
    </div>

    <el-card class="table-card">
      <el-table :data="jobs" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="title" :label="t('recruitment.jobTitle')" min-width="180" />
        <el-table-column prop="departmentName" :label="t('recruitment.department')" width="120" />
        <el-table-column prop="positionName" :label="t('recruitment.position')" width="120" />
        <el-table-column prop="salaryRange" :label="t('recruitment.salaryRange')" width="120" />
        <el-table-column prop="recruitCount" :label="t('recruitment.recruitCount')" width="100" />
        <el-table-column prop="status" :label="t('recruitment.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
              {{ row.status === 'ACTIVE' ? t('common.active') : t('common.inactive') }}
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
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item :label="t('recruitment.jobTitle')" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item :label="t('recruitment.department')" prop="departmentId">
          <el-select v-model="form.departmentId" style="width: 100%">
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('recruitment.position')" prop="positionId">
          <el-select v-model="form.positionId" style="width: 100%">
            <el-option
              v-for="pos in positions"
              :key="pos.id"
              :label="pos.name"
              :value="pos.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('recruitment.requirement')" prop="requirement">
          <el-input v-model="form.requirement" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item :label="t('recruitment.salaryRange')" prop="salaryRange">
          <el-input v-model="form.salaryRange" />
        </el-form-item>
        <el-form-item :label="t('recruitment.recruitCount')" prop="recruitCount">
          <el-input-number v-model="form.recruitCount" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="t('recruitment.status')" prop="status">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
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
import { jobApi, type Job } from '@/api/recruitment'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()

const jobs = ref<Job[]>([])
const departments = ref<any[]>([
  { id: 1, name: '技术部' },
  { id: 2, name: '人力资源部' },
  { id: 3, name: '财务部' },
  { id: 4, name: '销售部' }
])
const positions = ref<any[]>([
  { id: 1, name: '前端开发' },
  { id: 2, name: '后端开发' },
  { id: 3, name: '人事专员' },
  { id: 4, name: '财务专员' },
  { id: 5, name: '销售代表' }
])

const form = ref<Job>({
  title: '',
  departmentId: 1,
  positionId: 1,
  requirement: '',
  salaryRange: '',
  recruitCount: 1,
  status: 'ACTIVE'
})

const rules = {
  title: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  departmentId: [{ required: true, message: t('common.pleaseSelect'), trigger: 'change' }],
  positionId: [{ required: true, message: t('common.pleaseSelect'), trigger: 'change' }],
  requirement: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  salaryRange: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  recruitCount: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  status: [{ required: true, message: t('common.pleaseSelect'), trigger: 'change' }]
}

const loadJobs = async () => {
  loading.value = true
  try {
    const response = await jobApi.getJobs({ page: 1, size: 100 })
    if (response.code === 200) {
      jobs.value = response.data?.list || []
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
    jobs.value = []
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    title: '',
    departmentId: 1,
    positionId: 1,
    requirement: '',
    salaryRange: '',
    recruitCount: 1,
    status: 'ACTIVE'
  }
  dialogVisible.value = true
}

const showEditDialog = (job: Job) => {
  isEdit.value = true
  form.value = { ...job }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    let response
    if (isEdit.value && form.value.id) {
      response = await jobApi.updateJob(form.value.id, form.value)
    } else {
      response = await jobApi.createJob(form.value)
    }
    
    if (response.code === 200) {
      ElMessage.success(t('common.success'))
      dialogVisible.value = false
      loadJobs()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (job: Job) => {
  try {
    await ElMessageBox.confirm(t('common.deleteConfirm'), t('common.confirm'), {
      type: 'warning'
    })
    
    if (job.id) {
      const response = await jobApi.deleteJob(job.id)
      if (response.code === 200) {
        ElMessage.success(t('common.success'))
        loadJobs()
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
  loadJobs()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.job-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.table-card {
  flex: 1;
  overflow: auto;
}
</style>