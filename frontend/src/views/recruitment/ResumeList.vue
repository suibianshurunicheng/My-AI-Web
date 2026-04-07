<template>
  <div class="resume-list">
    <div class="header">
      <el-button type="primary" :icon="Plus" @click="showUploadDialog">
        {{ t('recruitment.uploadResume') }}
      </el-button>
    </div>

    <el-card class="filter-card">
      <el-form :model="searchForm" inline>
        <el-form-item :label="t('recruitment.job')">
          <el-select v-model="searchForm.jobId" placeholder="选择职位" @change="loadResumes">
            <el-option
              v-for="job in jobs"
              :key="job.id"
              :label="job.title"
              :value="job.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('recruitment.status')">
          <el-select v-model="searchForm.status" :placeholder="t('common.pleaseSelect')" @change="loadResumes">
            <el-option :label="t('common.all')" value="" />
            <el-option :label="t('recruitment.statusValues.PENDING')" value="PENDING" />
            <el-option :label="t('recruitment.statusValues.PENDING_INTERVIEW')" value="INTERVIEW" />
            <el-option :label="t('recruitment.statusValues.APPROVED')" value="PASSED" />
            <el-option :label="t('recruitment.statusValues.REJECTED')" value="REJECTED" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="resumes" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="name" :label="t('recruitment.candidateName')" width="120" />
        <el-table-column prop="phone" :label="t('recruitment.phone')" width="150" />
        <el-table-column prop="email" :label="t('recruitment.email')" width="200" />
        <el-table-column prop="jobTitle" :label="t('recruitment.job')" width="180" />
        <el-table-column prop="status" :label="t('recruitment.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('recruitment.resumeFile')" width="150">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="downloadResume(row.resumeFile)">
              {{ t('recruitment.download') }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.actions')" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="updateStatus(row, 'INTERVIEW')" v-if="row.status === 'PENDING'">
              {{ t('recruitment.toInterview') }}
            </el-button>
            <el-button size="small" type="success" @click="updateStatus(row, 'PASSED')" v-if="row.status === 'INTERVIEW'">
              {{ t('recruitment.pass') }}
            </el-button>
            <el-button size="small" type="danger" @click="updateStatus(row, 'REJECTED')">
              {{ t('recruitment.reject') }}
            </el-button>
            <el-button size="small" @click="scheduleInterview(row)" v-if="row.status === 'INTERVIEW'">
              {{ t('recruitment.scheduleInterview') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 上传简历对话框 -->
    <el-dialog
      v-model="uploadDialogVisible"
      :title="t('recruitment.uploadResume')"
      width="600px"
    >
      <el-form :model="uploadForm" :rules="uploadRules" ref="uploadFormRef" label-width="120px">
        <el-form-item :label="t('recruitment.candidateName')" prop="name">
          <el-input v-model="uploadForm.name" />
        </el-form-item>
        <el-form-item :label="t('recruitment.phone')" prop="phone">
          <el-input v-model="uploadForm.phone" />
        </el-form-item>
        <el-form-item :label="t('recruitment.email')" prop="email">
          <el-input v-model="uploadForm.email" />
        </el-form-item>
        <el-form-item :label="t('recruitment.job')" prop="jobId">
          <el-select v-model="uploadForm.jobId" style="width: 100%">
            <el-option
              v-for="job in jobs"
              :key="job.id"
              :label="job.title"
              :value="job.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('recruitment.resumeFile')" prop="resumeFile">
          <el-upload
            class="upload-demo"
            drag
            action=""
            :auto-upload="false"
            :on-change="handleFileChange"
            :limit="1"
            :file-list="fileList"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              {{ t('recruitment.dragUpload') }}
            </div>
            <template #tip>
              <div class="el-upload__tip">
                {{ t('recruitment.uploadTip') }}
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="uploadDialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleUpload" :loading="uploadLoading">
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
import { Plus, UploadFilled } from '@element-plus/icons-vue'
import { resumeApi, jobApi, type Resume, type Job } from '@/api/recruitment'

const { t } = useI18n()

const loading = ref(false)
const uploadLoading = ref(false)
const uploadDialogVisible = ref(false)
const uploadFormRef = ref()

const resumes = ref<Resume[]>([])
const jobs = ref<Job[]>([])
const fileList = ref<any[]>([])

const searchForm = ref({
  jobId: undefined,
  status: undefined
})

const uploadForm = ref({
  name: '',
  phone: '',
  email: '',
  jobId: undefined,
  resumeFile: ''
})

const uploadRules = {
  name: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  phone: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  email: [{ required: true, message: t('common.pleaseFillComplete'), trigger: 'blur' }],
  jobId: [{ required: true, message: t('common.pleaseSelect'), trigger: 'change' }],
  resumeFile: [{ required: true, message: t('recruitment.pleaseUploadResume'), trigger: 'change' }]
}

const loadJobs = async () => {
  try {
    const response = await jobApi.getJobs({ page: 1, size: 100 })
    if (response.code === 200) {
      jobs.value = response.data?.list || []
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
    jobs.value = []
  }
}

const loadResumes = async () => {
  loading.value = true
  try {
    const response = await resumeApi.getResumes({
      page: 1,
      size: 100,
      jobId: searchForm.value.jobId,
      status: searchForm.value.status
    })
    if (response.code === 200) {
      resumes.value = response.data?.list || []
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
    resumes.value = []
  } finally {
    loading.value = false
  }
}

const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    PENDING: 'info',
    INTERVIEW: 'warning',
    PASSED: 'success',
    REJECTED: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    PENDING: t('recruitment.statusPending'),
    INTERVIEW: t('recruitment.statusInterview'),
    PASSED: t('recruitment.statusPassed'),
    REJECTED: t('recruitment.statusRejected')
  }
  return texts[status] || status
}

const handleFileChange = (file: any) => {
  fileList.value = [file]
  uploadForm.value.resumeFile = file.raw
}

const handleUpload = async () => {
  await uploadFormRef.value.validate()
  uploadLoading.value = true
  try {
    const formData = new FormData()
    formData.append('name', uploadForm.value.name)
    formData.append('phone', uploadForm.value.phone)
    formData.append('email', uploadForm.value.email)
    formData.append('jobId', uploadForm.value.jobId)
    formData.append('resumeFile', uploadForm.value.resumeFile)
    
    const response = await resumeApi.uploadResume(formData)
    if (response.code === 200) {
      ElMessage.success(t('common.success'))
      uploadDialogVisible.value = false
      loadResumes()
      resetUploadForm()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    uploadLoading.value = false
  }
}

const resetUploadForm = () => {
  uploadForm.value = {
    name: '',
    phone: '',
    email: '',
    jobId: undefined,
    resumeFile: ''
  }
  fileList.value = []
}

const updateStatus = async (resume: Resume, status: string) => {
  try {
    const response = await resumeApi.updateStatus(resume.id!, status)
    if (response.code === 200) {
      ElMessage.success(t('common.success'))
      loadResumes()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  }
}

const scheduleInterview = (resume: Resume) => {
  // 跳转到面试安排页面或打开面试安排对话框
  ElMessage.info(t('recruitment.scheduleInterviewHint'))
}

const downloadResume = (resumeFile: string) => {
  window.open(resumeFile)
}

const showUploadDialog = () => {
  resetUploadForm()
  uploadDialogVisible.value = true
}

onMounted(() => {
  loadJobs()
  loadResumes()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.resume-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.table-card {
  flex: 1;
  overflow: auto;
}
</style>