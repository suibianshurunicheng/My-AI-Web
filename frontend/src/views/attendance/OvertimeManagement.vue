<template>
  <div class="overtime-management">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('attendance.overtimeManagement') }}</h2>
        <p class="page-description">{{ t('attendance.overtimeManagementDesc') }}</p>
      </div>
    </div>
    
    <el-card>
      <div class="table-container">
        <el-table
          :data="records"
          v-loading="loading"
          style="width: 100%"
          stripe
          border
        >
          <el-table-column prop="employeeName" :label="t('attendance.employeeName')" />
          <el-table-column prop="startTime" :label="t('attendance.startTime')">
            <template #default="{ row }">
              {{ formatDateTime(row.startTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="endTime" :label="t('attendance.endTime')">
            <template #default="{ row }">
              {{ formatDateTime(row.endTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="hours" :label="t('attendance.hours')" />
          <el-table-column prop="reason" :label="t('attendance.reason')" />
          <el-table-column prop="status" :label="t('attendance.status')">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="t('common.operation')" width="250" fixed="right">
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
              <template v-else-if="row.status === 'APPROVED' && !row.convertedAt">
                <el-dropdown trigger="click" @command="(cmd) => handleConvert(row, cmd)">
                  <el-button type="primary" size="small" link>
                    {{ t('attendance.convertOvertime') }}
                    <el-icon><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="COMPENSATORY_LEAVE">
                        {{ t('attendance.compensatoryLeave') }}
                      </el-dropdown-item>
                      <el-dropdown-item command="OVERTIME_PAY">
                        {{ t('attendance.overtimePayType') }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template v-else>
                <span style="color: var(--el-text-color-secondary)">
                  {{ row.convertedAt ? t('attendance.converted') : t('attendance.processed') }}
                </span>
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
import { ArrowDown } from '@element-plus/icons-vue'
import { overtimeApi, type OvertimeRecord } from '@/api/attendance'
import { useAuthStore } from '@/stores/auth'
import { useI18n } from '@/utils/i18n'

const { t } = useI18n()
const authStore = useAuthStore()
const currentUser = { id: 1, username: authStore.getUsername() || 'admin' }

const records = ref<OvertimeRecord[]>([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})
const loading = ref(false)

const fetchRecords = async () => {
  loading.value = true
  try {
    const res = await overtimeApi.getOvertimeRecords(pagination.value.page, pagination.value.size)
    records.value = res.data.list
    pagination.value.total = res.data.total
  } catch (error) {
    ElMessage.error(t('attendance.loadOvertimeFailed'))
  } finally {
    loading.value = false
  }
}

const handleApprove = async (row: OvertimeRecord, approved: boolean) => {
  try {
    const message = approved ? t('common.approve') : t('common.reject')
    await ElMessageBox.confirm(
      `${message} ${row.employeeName} 的加班申请？`,
      t('common.confirm'),
      {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        type: 'warning'
      }
    )
    await overtimeApi.approveOvertime(row.id!, {
      approverId: currentUser.id,
      approverName: currentUser.username,
      approved,
      comment: approved ? '' : t('common.rejected')
    })
    ElMessage.success(approved ? t('common.approveSuccess') : t('common.rejectSuccess'))
    fetchRecords()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.operationFailed'))
    }
  }
}

const handleConvert = async (row: OvertimeRecord, conversionType: string) => {
  try {
    const typeText = conversionType === 'COMPENSATORY_LEAVE' 
      ? t('attendance.compensatoryLeave') 
      : t('attendance.overtimePayType')
    await ElMessageBox.confirm(
      t('attendance.confirmConvert', { type: typeText }),
      t('common.confirm'),
      {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        type: 'warning'
      }
    )
    await overtimeApi.convertOvertime({ id: row.id!, conversionType })
    ElMessage.success(t('attendance.convertSuccess'))
    fetchRecords()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(t('common.operationFailed'))
    }
  }
}

const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString()
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
  fetchRecords()
}

const handleCurrentChange = (page: number) => {
  pagination.value.page = page
  fetchRecords()
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.overtime-management {
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
