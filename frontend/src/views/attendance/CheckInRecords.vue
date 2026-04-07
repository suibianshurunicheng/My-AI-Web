<template>
  <div class="check-in-records">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('attendance.checkInRecords') }}</h2>
        <p class="page-description">{{ t('attendance.checkInRecordsDesc') }}</p>
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
          <el-table-column prop="attendanceDate" :label="t('attendance.attendanceDate')" />
          <el-table-column prop="checkInTime" :label="t('attendance.checkInTime')">
            <template #default="{ row }">
              {{ formatDateTime(row.checkInTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="checkOutTime" :label="t('attendance.checkOutTime')">
            <template #default="{ row }">
              {{ formatDateTime(row.checkOutTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" :label="t('attendance.status')">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
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
import { ElMessage } from 'element-plus'
import { attendanceApi, type AttendanceRecord } from '@/api/attendance'
import { useI18n } from '@/utils/i18n'

const { t } = useI18n()

const records = ref<AttendanceRecord[]>([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})
const loading = ref(false)

const fetchRecords = async () => {
  loading.value = true
  try {
    const res = await attendanceApi.getAttendanceRecords(pagination.value.page, pagination.value.size)
    records.value = res.data.list
    pagination.value.total = res.data.total
  } catch (error) {
    ElMessage.error(t('attendance.loadRecordsFailed'))
  } finally {
    loading.value = false
  }
}

const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString()
}

const getStatusType = (status: string) => {
  const map: Record<string, any> = {
    NORMAL: 'success',
    LATE: 'warning',
    EARLY_LEAVE: 'warning',
    ABSENT: 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    NORMAL: t('attendance.normal'),
    LATE: t('attendance.late'),
    EARLY_LEAVE: t('attendance.earlyLeave'),
    ABSENT: t('attendance.absent')
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

.check-in-records {
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
