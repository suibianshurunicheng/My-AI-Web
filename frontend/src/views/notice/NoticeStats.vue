<template>
  <div class="notice-stats">
    <div class="page-header">
      <h2>{{ t('notice.stats') }}</h2>
    </div>
    
    <el-table :data="notices" v-loading="loading">
      <el-table-column :label="t('notice.title')" prop="title" width="300" />
      <el-table-column :label="t('notice.scope')" prop="scope" width="120">
        <template #default="{ row }">
          <span>{{ t(`notice.scopeValues.${row.scope}`) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('notice.viewCount')" prop="viewCount" width="100" />
      <el-table-column :label="t('notice.publishTime')" prop="publishTime" width="180">
        <template #default="{ row }">
          <span>{{ formatDate(row.publishTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.actions')" fixed="right" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="viewStats(row)">{{ t('notice.viewStats') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="getNotices"
      @size-change="getNotices"
      layout="total, prev, pager, next, jumper"
    />
    
    <!-- 查看统计详情 -->
    <el-dialog v-model="statsVisible" :title="currentNotice?.title" width="70%">
      <div v-if="currentStats" class="stats-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <div class="stat-item">
                <div class="stat-value">{{ currentStats.totalEmployees }}</div>
                <div class="stat-label">{{ t('notice.totalEmployees') }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <div class="stat-item">
                <div class="stat-value read">{{ currentStats.readCount }}</div>
                <div class="stat-label">{{ t('notice.readCount') }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <div class="stat-item">
                <div class="stat-value unread">{{ currentStats.unreadCount }}</div>
                <div class="stat-label">{{ t('notice.unreadCount') }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <div class="stat-item">
                <div class="stat-value">{{ (currentStats.readRate * 100).toFixed(1) }}%</div>
                <div class="stat-label">{{ t('notice.readRate') }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <div class="read-details">
          <h3>{{ t('notice.readDetails') }}</h3>
          <el-table :data="readRecords" style="margin-top: 20px">
            <el-table-column :label="t('notice.employeeName')" prop="employeeName" />
            <el-table-column :label="t('notice.departmentName')" prop="departmentName" />
            <el-table-column :label="t('notice.isRead')" prop="isRead">
              <template #default="{ row }">
                <el-tag v-if="row.isRead" type="success">{{ t('notice.hasRead') }}</el-tag>
                <el-tag v-else type="warning">{{ t('notice.hasNotRead') }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="t('notice.readTime')" prop="readTime">
              <template #default="{ row }">
                <span v-if="row.readTime">{{ formatDate(row.readTime) }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { noticeApi, type Notice, type NoticeStats, type NoticeReadRecord } from '@/api/notice'
import { ElMessage } from 'element-plus'

const { t } = useI18n()

const notices = ref<Notice[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const statsVisible = ref(false)
const currentNotice = ref<Notice | null>(null)
const currentStats = ref<NoticeStats | null>(null)
const readRecords = ref<NoticeReadRecord[]>([])

const formatDate = (date: string | undefined) => {
  if (!date) return ''
  return new Date(date).toLocaleString()
}

const getNotices = async () => {
  try {
    loading.value = true
    const response = await noticeApi.getAdminNotices({
      page: currentPage.value,
      size: pageSize.value
    })
    notices.value = response.data?.list || []
    total.value = response.data?.total || 0
  } catch (error) {
    console.error('Failed to get notices:', error)
    ElMessage.error(t('notice.getNoticesError'))
  } finally {
    loading.value = false
  }
}

const viewStats = async (notice: Notice) => {
  currentNotice.value = notice
  statsVisible.value = true
  
  try {
    const [statsRes, detailsRes] = await Promise.all([
      noticeApi.getNoticeStats(notice.id!),
      noticeApi.getReadDetails(notice.id!, { page: 1, size: 100 })
    ])
    currentStats.value = statsRes.data
    readRecords.value = detailsRes.data?.list || []
  } catch (error) {
    console.error('Failed to get stats:', error)
    ElMessage.error(t('notice.getStatsError'))
  }
}

onMounted(() => {
  getNotices()
})
</script>

<style scoped lang="scss">
.notice-stats {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
  }
  
  .stats-overview {
    .stat-item {
      text-align: center;
      
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 8px;
        
        &.read {
          color: #67C23A;
        }
        
        &.unread {
          color: #E6A23C;
        }
      }
      
      .stat-label {
        color: #909399;
        font-size: 14px;
      }
    }
  }
  
  .read-details {
    margin-top: 30px;
    
    h3 {
      margin-bottom: 15px;
    }
  }
}
</style>
