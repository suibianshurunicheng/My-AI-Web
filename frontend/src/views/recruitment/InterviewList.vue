<template>
  <div class="interview-list">
    <div class="list-header">
      <h3 class="list-title">{{ t('recruitment.interviews') }}</h3>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item :label="t('recruitment.candidateName')">
          <el-input v-model="searchForm.candidateName" :placeholder="t('recruitment.enterCandidateName')" />
        </el-form-item>
        <el-form-item :label="t('recruitment.jobTitle')">
          <el-input v-model="searchForm.jobTitle" :placeholder="t('recruitment.enterJobTitle')" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ t('common.search') }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="data-card">
      <el-table :data="interviews" style="width: 100%" border>
        <el-table-column prop="id" :label="t('recruitment.id')" width="80" />
        <el-table-column prop="candidateName" :label="t('recruitment.candidateName')" width="120" />
        <el-table-column prop="jobTitle" :label="t('recruitment.jobTitle')" width="180" />
        <el-table-column prop="interviewTime" :label="t('recruitment.interviewTime')" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.interviewTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="interviewers" :label="t('recruitment.interviewers')" width="150" />
        <el-table-column prop="status" :label="t('recruitment.status')" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ t(`recruitment.statusValues.${scope.row.status}`) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.actions')" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewInterview(scope.row)">{{ t('common.view') }}</el-button>
            <el-button size="small" type="primary" @click="editInterview(scope.row)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="success" v-if="scope.row.status === 'PENDING'" @click="completeInterview(scope.row)">{{ t('recruitment.completeInterview') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 面试详情对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="interviewForm" label-width="120px">
        <el-form-item :label="t('recruitment.candidateName')">
          <el-input v-model="interviewForm.candidateName" disabled />
        </el-form-item>
        <el-form-item :label="t('recruitment.jobTitle')">
          <el-input v-model="interviewForm.jobTitle" disabled />
        </el-form-item>
        <el-form-item :label="t('recruitment.interviewTime')">
          <el-date-picker
            v-model="interviewForm.interviewTime"
            type="datetime"
            placeholder="{{ t('recruitment.selectInterviewTime') }}"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="t('recruitment.interviewers')">
          <el-input v-model="interviewForm.interviewers" placeholder="{{ t('recruitment.enterInterviewers') }}" />
        </el-form-item>
        <el-form-item :label="t('recruitment.interviewNotes')">
          <el-input
            v-model="interviewForm.interviewNotes"
            type="textarea"
            rows="4"
            :placeholder="t('recruitment.enterInterviewNotes')"
          />
        </el-form-item>
        <el-form-item v-if="dialogType === 'complete'" :label="t('recruitment.evaluation')">
          <el-input
            v-model="interviewForm.evaluation"
            type="textarea"
            rows="4"
            :placeholder="t('recruitment.enterEvaluation')"
          />
        </el-form-item>
        <el-form-item v-if="dialogType === 'complete'" :label="t('recruitment.result')">
          <el-radio-group v-model="interviewForm.result">
            <el-radio label="PASSED">{{ t('recruitment.passed') }}</el-radio>
            <el-radio label="REJECTED">{{ t('recruitment.rejected') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
          <el-button type="primary" @click="saveInterview">{{ t('common.save') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { interviewApi } from '@/api/recruitment'

const { t } = useI18n()

const interviews = ref<any[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchForm = ref({
  candidateName: '',
  jobTitle: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogType = ref('')
const interviewForm = ref({
  id: '',
  candidateName: '',
  jobTitle: '',
  interviewTime: '',
  interviewers: '',
  interviewNotes: '',
  evaluation: '',
  result: ''
})

const getInterviews = async () => {
  try {
    const response = await interviewApi.getInterviews({
      page: currentPage.value,
      size: pageSize.value
    })
    interviews.value = response.data?.list || []
    total.value = response.data?.total || 0
  } catch (error: any) {
    console.error('Failed to get interviews:', error)
    interviews.value = []
    total.value = 0
  }
}

const handleSearch = () => {
  currentPage.value = 1
  getInterviews()
}

const resetSearch = () => {
  searchForm.value = {
    candidateName: '',
    jobTitle: ''
  }
  currentPage.value = 1
  getInterviews()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  getInterviews()
}

const handleCurrentChange = (current: number) => {
  currentPage.value = current
  getInterviews()
}

const viewInterview = (interview: any) => {
  dialogType.value = 'view'
  dialogTitle.value = t('recruitment.viewInterview')
  interviewForm.value = {
    id: interview.id,
    candidateName: interview.candidateName || '',
    jobTitle: interview.jobTitle || '',
    interviewTime: interview.interviewTime,
    interviewers: interview.interviewers,
    interviewNotes: interview.interviewNotes,
    evaluation: interview.evaluation,
    result: interview.result
  }
  dialogVisible.value = true
}

const editInterview = (interview: any) => {
  dialogType.value = 'edit'
  dialogTitle.value = t('recruitment.editInterview')
  interviewForm.value = {
    id: interview.id,
    candidateName: interview.candidateName || '',
    jobTitle: interview.jobTitle || '',
    interviewTime: interview.interviewTime,
    interviewers: interview.interviewers,
    interviewNotes: interview.interviewNotes,
    evaluation: interview.evaluation,
    result: interview.result
  }
  dialogVisible.value = true
}

const completeInterview = (interview: any) => {
  dialogType.value = 'complete'
  dialogTitle.value = t('recruitment.completeInterview')
  interviewForm.value = {
    id: interview.id,
    candidateName: interview.candidateName || '',
    jobTitle: interview.jobTitle || '',
    interviewTime: interview.interviewTime,
    interviewers: interview.interviewers,
    interviewNotes: interview.interviewNotes,
    evaluation: '',
    result: ''
  }
  dialogVisible.value = true
}

const saveInterview = async () => {
  try {
    if (dialogType.value === 'complete') {
      await interviewApi.evaluateInterview(interviewForm.value.id, {
        evaluation: interviewForm.value.evaluation,
        result: interviewForm.value.result
      })
    } else {
      await interviewApi.updateInterview(interviewForm.value.id, {
        interviewTime: interviewForm.value.interviewTime,
        interviewers: interviewForm.value.interviewers,
        location: interviewForm.value.location || ''
      })
    }
    dialogVisible.value = false
    getInterviews()
  } catch (error) {
    console.error('Failed to save interview:', error)
  }
}

const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    PENDING: 'info',
    COMPLETED: 'success'
  }
  return statusMap[status] || 'default'
}

onMounted(() => {
  getInterviews()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.interview-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.list-header {
  margin-bottom: 20px;
  
  .list-title {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  align-items: center;
  gap: 10px;
}

.data-card {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.el-table {
  flex: 1;
  overflow: auto;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>