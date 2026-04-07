<template>
  <div class="offer-list">
    <div class="list-header">
      <h3 class="list-title">{{ t('recruitment.offers') }}</h3>
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
      <el-table :data="offers" style="width: 100%" border>
        <el-table-column prop="id" :label="t('recruitment.id')" width="80" />
        <el-table-column prop="candidateName" :label="t('recruitment.candidateName')" width="120" />
        <el-table-column prop="jobTitle" :label="t('recruitment.jobTitle')" width="180" />
        <el-table-column prop="salary" :label="t('recruitment.salary')" width="120" />
        <el-table-column prop="joinDate" :label="t('recruitment.joinDate')" width="150">
          <template #default="scope">
            {{ formatDate(scope.row.joinDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="t('recruitment.status')" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ t(`recruitment.statusValues.${scope.row.status}`) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('common.actions')" width="250" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewOffer(scope.row)">{{ t('common.view') }}</el-button>
            <el-button size="small" type="primary" @click="editOffer(scope.row)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="success" v-if="scope.row.status === 'PENDING'" @click="approveOffer(scope.row)">{{ t('recruitment.approveOffer') }}</el-button>
            <el-button size="small" type="warning" v-if="scope.row.status === 'APPROVED'" @click="createEmployee(scope.row)">{{ t('recruitment.createEmployee') }}</el-button>
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

    <!-- Offer详情对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="offerForm" label-width="120px">
        <el-form-item :label="t('recruitment.candidateName')">
          <el-input v-model="offerForm.candidateName" disabled />
        </el-form-item>
        <el-form-item :label="t('recruitment.jobTitle')">
          <el-input v-model="offerForm.jobTitle" disabled />
        </el-form-item>
        <el-form-item :label="t('recruitment.salary')">
          <el-input v-model="offerForm.salary" :placeholder="t('recruitment.enterSalary')" />
        </el-form-item>
        <el-form-item :label="t('recruitment.joinDate')">
          <el-date-picker
            v-model="offerForm.joinDate"
            type="date"
            :placeholder="t('recruitment.selectJoinDate')"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="t('recruitment.notes')">
          <el-input
            v-model="offerForm.notes"
            type="textarea"
            rows="4"
            :placeholder="t('recruitment.enterNotes')"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
          <el-button type="primary" @click="saveOffer">{{ t('common.save') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { offerApi } from '@/api/recruitment'

const { t } = useI18n()

const offers = ref<any[]>([])
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
const offerForm = ref({
  id: '',
  candidateName: '',
  jobTitle: '',
  salary: '',
  joinDate: '',
  notes: ''
})

const getOffers = async () => {
  try {
    const response = await offerApi.getOffers({
      page: currentPage.value,
      size: pageSize.value
    })
    offers.value = response.data?.list || []
    total.value = response.data?.total || 0
  } catch (error: any) {
    console.error('Failed to get offers:', error)
    offers.value = []
    total.value = 0
  }
}

const handleSearch = () => {
  currentPage.value = 1
  getOffers()
}

const resetSearch = () => {
  searchForm.value = {
    candidateName: '',
    jobTitle: ''
  }
  currentPage.value = 1
  getOffers()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  getOffers()
}

const handleCurrentChange = (current: number) => {
  currentPage.value = current
  getOffers()
}

const viewOffer = (offer: any) => {
  dialogType.value = 'view'
  dialogTitle.value = t('recruitment.viewOffer')
  offerForm.value = {
    id: offer.id,
    candidateName: offer.candidateName || '',
    jobTitle: offer.jobTitle || '',
    salary: offer.salary,
    joinDate: offer.joinDate,
    notes: offer.notes
  }
  dialogVisible.value = true
}

const editOffer = (offer: any) => {
  dialogType.value = 'edit'
  dialogTitle.value = t('recruitment.editOffer')
  offerForm.value = {
    id: offer.id,
    candidateName: offer.candidateName || '',
    jobTitle: offer.jobTitle || '',
    salary: offer.salary,
    joinDate: offer.joinDate,
    notes: offer.notes
  }
  dialogVisible.value = true
}

const saveOffer = async () => {
  try {
    await offerApi.updateOffer(offerForm.value.id, {
      salary: offerForm.value.salary,
      joinDate: offerForm.value.joinDate
    })
    dialogVisible.value = false
    getOffers()
  } catch (error) {
    console.error('Failed to save offer:', error)
  }
}

const approveOffer = async (offer: any) => {
  try {
    await offerApi.approveOffer(offer.id)
    getOffers()
  } catch (error) {
    console.error('Failed to approve offer:', error)
  }
}

const createEmployee = async (offer: any) => {
  try {
    await offerApi.createEmployeeFromOffer(offer.id, {
      employeeCode: 'EMP00' + Date.now(),
      departmentId: 1,
      positionId: 1,
      entryDate: new Date().toISOString().split('T')[0]
    })
    getOffers()
  } catch (error: any) {
    if (error.response) {
      console.error('后端返回错误：', error.response.data)
      console.error('错误状态码：', error.response.status)
      console.error('请求URL：', error.config.url)
      console.error('请求数据：', error.config.data)
    } else {
      console.error('请求失败：', error.message)
    }
    console.error('Failed to create employee:', error)
  }
}

const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    PENDING: 'info',
    APPROVED: 'success',
    REJECTED: 'danger'
  }
  return statusMap[status] || 'default'
}

onMounted(() => {
  getOffers()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.offer-list {
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