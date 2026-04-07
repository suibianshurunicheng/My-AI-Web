<template>
  <div class="logs-view">
    <div class="page-header">
      <h2 class="page-title">{{ t('logs.title') }}</h2>
    </div>
    
    <el-tabs v-model="activeTab" class="logs-tabs">
      <el-tab-pane :label="t('logs.operationLogs')" name="operation">
        <div class="table-container">
          <el-table :data="operationLogs" v-loading="loading" stripe border style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" :label="t('logs.username')" width="120" />
            <el-table-column prop="operation" :label="t('logs.operation')" width="150" />
            <el-table-column prop="details" :label="t('logs.details')" show-overflow-tooltip />
            <el-table-column prop="ip" :label="t('logs.ip')" width="130" />
            <el-table-column prop="createdAt" :label="t('logs.time')" width="180">
              <template #default="{ row }">
                {{ formatDate(row.createdAt) }}
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-wrapper" v-if="operationPagination.total > 0">
            <el-pagination
              v-model:current-page="operationPagination.page"
              v-model:page-size="operationPagination.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="operationPagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleOperationSizeChange"
              @current-change="handleOperationPageChange"
            />
          </div>
        </div>
      </el-tab-pane>
      
      <el-tab-pane :label="t('logs.loginLogs')" name="login">
        <div class="table-container">
          <el-table :data="loginLogs" v-loading="loading" stripe border style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" :label="t('logs.username')" width="120" />
            <el-table-column prop="status" :label="t('logs.status')" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
                  {{ row.status === 'success' ? t('logs.success') : t('logs.failed') }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="ip" :label="t('logs.ip')" width="130" />
            <el-table-column prop="createdAt" :label="t('logs.time')" width="180">
              <template #default="{ row }">
                {{ formatDate(row.createdAt) }}
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-wrapper" v-if="loginPagination.total > 0">
            <el-pagination
              v-model:current-page="loginPagination.page"
              v-model:page-size="loginPagination.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="loginPagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleLoginSizeChange"
              @current-change="handleLoginPageChange"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { logApi, type OperationLog, type LoginLog, type PageResponse } from '@/api/system'
import { useI18n } from '@/utils/i18n'
import { ElMessage } from 'element-plus'

const { t } = useI18n()
const activeTab = ref('operation')
const loading = ref(false)

const operationLogs = ref<OperationLog[]>([])
const operationPagination = ref({
  total: 0,
  page: 1,
  size: 10
})

const loginLogs = ref<LoginLog[]>([])
const loginPagination = ref({
  total: 0,
  page: 1,
  size: 10
})

const formatDate = (dateString: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

const loadOperationLogs = async (page: number = 1, size: number = 10) => {
  loading.value = true
  try {
    const response = await logApi.getOperationLogs(page, size)
    const data = response.data as PageResponse<OperationLog>
    operationLogs.value = data.list
    operationPagination.value = {
      total: data.total,
      page: data.page,
      size: data.size
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    loading.value = false
  }
}

const loadLoginLogs = async (page: number = 1, size: number = 10) => {
  loading.value = true
  try {
    const response = await logApi.getLoginLogs(page, size)
    const data = response.data as PageResponse<LoginLog>
    loginLogs.value = data.list
    loginPagination.value = {
      total: data.total,
      page: data.page,
      size: data.size
    }
  } catch (error) {
    ElMessage.error(t('common.error'))
  } finally {
    loading.value = false
  }
}

const handleOperationSizeChange = (size: number) => {
  loadOperationLogs(1, size)
}

const handleOperationPageChange = (page: number) => {
  loadOperationLogs(page, operationPagination.value.size)
}

const handleLoginSizeChange = (size: number) => {
  loadLoginLogs(1, size)
}

const handleLoginPageChange = (page: number) => {
  loadLoginLogs(page, loginPagination.value.size)
}

onMounted(() => {
  loadOperationLogs()
  loadLoginLogs()
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.logs-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  margin-bottom: 20px;
  
  .page-title {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary);
    transition: color 0.3s ease;
  }
}

.logs-tabs {
  flex: 1;
  display: flex;
  flex-direction: column;
  
  :deep(.el-tabs__content) {
    flex: 1;
    overflow: hidden;
  }
  
  :deep(.el-tab-pane) {
    height: 100%;
  }
}

.table-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--bg-color-lighter);
  border-radius: $border-radius;
  box-shadow: $box-shadow-light;
  overflow: hidden;
  transition: background-color 0.3s ease;
}

.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: center;
  border-top: 1px solid var(--border-light);
  background: var(--bg-color-lighter);
  transition: background-color 0.3s ease, border-color 0.3s ease;
}
</style>
