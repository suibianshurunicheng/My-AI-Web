<template>
  <div class="notice-list">
    <div class="page-header">
      <h2>{{ t('notice.list') }}</h2>
    </div>
    
    <el-table :data="notices" v-loading="loading">
      <el-table-column :label="t('notice.title')" prop="title" />
      <el-table-column :label="t('notice.scope')" prop="scope">
        <template #default="{ row }">
          <span>{{ t(`notice.scopeValues.${row.scope}`) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('notice.isImportant')" prop="isImportant">
        <template #default="{ row }">
          <el-tag v-if="row.isImportant" type="danger">{{ t('common.yes') }}</el-tag>
          <el-tag v-else type="info">{{ t('common.no') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('notice.viewCount')" prop="viewCount" />
      <el-table-column :label="t('notice.publishTime')" prop="publishTime">
        <template #default="{ row }">
          <span>{{ formatDate(row.publishTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.actions')" fixed="right" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="viewNotice(row)">{{ t('common.view') }}</el-button>
          <el-tag v-if="row.isRead" type="success">{{ t('notice.hasRead') }}</el-tag>
          <el-tag v-else type="warning">{{ t('notice.hasNotRead') }}</el-tag>
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
    
    <!-- 查看详情 -->
    <el-dialog v-model="detailVisible" :title="currentNotice?.title" width="60%">
      <div class="notice-detail">
        <div class="detail-info">
          <p><strong>{{ t('notice.scope') }}:</strong> {{ t(`notice.scopeValues.${currentNotice?.scope}`) }}</p>
          <p><strong>{{ t('notice.isImportant') }}:</strong> {{ currentNotice?.isImportant ? t('common.yes') : t('common.no') }}</p>
          <p><strong>{{ t('notice.viewCount') }}:</strong> {{ currentNotice?.viewCount }}</p>
          <p><strong>{{ t('notice.publishTime') }}:</strong> {{ formatDate(currentNotice?.publishTime) }}</p>
          <p><strong>{{ t('notice.publisher') }}:</strong> {{ currentNotice?.publisherName }}</p>
        </div>
        <div class="detail-content">{{ currentNotice?.content }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { noticeApi, type Notice } from '@/api/notice'
import { ElMessage } from 'element-plus'

const { t } = useI18n()

const notices = ref<Notice[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const currentNotice = ref<Notice | null>(null)

const formatDate = (date: string | undefined) => {
  if (!date) return ''
  return new Date(date).toLocaleString()
}

const getNotices = async () => {
  try {
    loading.value = true
    const response = await noticeApi.getEmployeeNotices({
      page: currentPage.value,
      size: pageSize.value,
      employeeId: 1
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

const viewNotice = async (notice: Notice) => {
  currentNotice.value = notice
  detailVisible.value = true
  
  if (!notice.isRead) {
    try {
      await noticeApi.markAsRead(notice.id!, 1)
      getNotices()
    } catch (error) {
      console.error('Failed to mark as read:', error)
    }
  }
}

onMounted(() => {
  getNotices()
})
</script>

<style scoped lang="scss">
.notice-list {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
  }
  
  .notice-detail {
    .detail-info {
      margin-bottom: 20px;
      
      p {
        margin: 8px 0;
      }
    }
    
    .detail-content {
      white-space: pre-wrap;
      line-height: 1.8;
    }
  }
}
</style>
