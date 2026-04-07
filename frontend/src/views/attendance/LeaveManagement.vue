<template>
  <div class="leave-management">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('attendance.leaveManagement') }}</h2>
        <p class="page-description">{{ t('attendance.leaveManagementDesc') }}</p>
      </div>
    </div>
    
    <el-card>
      <div class="table-container">
        <el-table
          :data="applications"
          v-loading="loading"
          style="width: 100%"
          stripe
          border
        >
          <el-table-column prop="employeeName" :label="t('attendance.employeeName')" />
          <el-table-column prop="leaveType" :label="t('attendance.leaveType')">
            <template #default="{ row }">
              {{ getLeaveTypeText(row.leaveType) }}
            </template>
          </el-table-column>
          <el-table-column prop="startDate" :label="t('attendance.startDate')" />
          <el-table-column prop="endDate" :label="t('attendance.endDate')" />
          <el-table-column prop="days" :label="t('attendance.days')" />
          <el-table-column prop="reason" :label="t('attendance.reason')" />
          <el-table-column prop="status" :label="t('attendance.status')">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="t('common.operation')" width="200" fixed="right">
            <template #default="{ row }">
              <template v-if="row.status === 'PENDING'">
                <el-button 
                  type="success" 
                  size="small" 
                  link
                  @click="handleApprove(row, true)"
                >
                  {{ t('common.approve') }}
                </el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  link
                  @click="handleApprove(row, false)"
                >
                  {{ t('common.reject') }}
                </el-button>
              </template>
              <template v-else>
                <span style="color: var(--el-text-color-secondary)">{{ t('attendance.processed') }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-wrapper" v-if="pagination.total > 0">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { leaveApi, type LeaveApplication } from '@/api/attendance'
import { useAuthStore } from '@/stores/auth'
import { useI18n } from '@/utils/i18n'

const { t } = useI18n()
const authStore = useAuthStore()
const currentUser = { id: 1, username: authStore.getUsername() || 'admin' }

const applications = ref<LeaveApplication[]>([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})
const loading = ref(false)

const fetchApplications = async () => {
  loading.value = true
  try {
    const res = await leaveApi.getLeaveApplications(pagination.value.page, pagination.value.size)
    applications.value = res.data.list
    pagination.value.total = res.data.total
  } catch (error) {
    ElMessage.error(t('attendance.loadApplicationsFailed'))
  } finally {
    loading.value = false
  }
}

const handleApprove = async (row: LeaveApplication, approved: boolean) => {
  try {
    const message = approved ? t('common.approve') : t('common.reject')
    await ElMessageBox.confirm(
      `${message} ${row.employeeName} 的请假申请？`,
      t('common.confirm'),
      {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        type: 'warning'
      }
    )
    await leaveApi.approveLeave(row.id!, {
      approverId: currentUser.id,
      approverName: currentUser.username,
      approved,
      comment: approved ? '' : t('common.rejected')
    })
    ElMessage.success(approved ? t('common.approveSuccess') : t('common.rejectSuccess'))
    fetchApplications()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.operationFailed'))
    }
  }
}

const getLeaveTypeText = (type: string) => {
  const map: Record<string, string> = {
    ANNUAL: t('attendance.annualLeave'),
    SICK: t('attendance.sickLeave'),
    PERSONAL: t('attendance.personalLeave'),
    OTHER: t('attendance.otherLeave')
  }
  return map[type] || type
}

const getStatusType = (status: string) => {
  const map: Record<string, any> = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    PENDING: t('attendance.pending'),
    APPROVED: t('attendance.approved'),
    REJECTED: t('attendance.rejected')
  }
  return map[status] || status
}

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  fetchApplications()
}

const handleCurrentChange = (page: number) => {
  pagination.value.page = page
  fetchApplications()
}

onMounted(() => {
  fetchApplications()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.leave-management {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  
  .header-left {
    .page-title {
      margin: 0 0 4px 0;
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
}

.table-container {
  margin-top: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
