<template>
  <div class="notice-publish">
    <div class="page-header">
      <h2>{{ t('notice.publish') }}</h2>
    </div>
    
    <el-card>
      <el-form :model="noticeForm" label-width="120px">
        <el-form-item :label="t('notice.title')" required>
          <el-input v-model="noticeForm.title" :placeholder="t('notice.titlePlaceholder')" />
        </el-form-item>
        
        <el-form-item :label="t('notice.content')" required>
          <el-input 
            v-model="noticeForm.content" 
            type="textarea" 
            :rows="10"
            :placeholder="t('notice.contentPlaceholder')" 
          />
        </el-form-item>
        
        <el-form-item :label="t('notice.scope')" required>
          <el-radio-group v-model="noticeForm.scope">
            <el-radio value="ALL">{{ t('notice.scopeValues.ALL') }}</el-radio>
            <el-radio value="DEPARTMENT">{{ t('notice.scopeValues.DEPARTMENT') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item v-if="noticeForm.scope === 'DEPARTMENT'" :label="t('notice.department')" required>
          <el-select v-model="noticeForm.departmentId" :placeholder="t('notice.departmentPlaceholder')">
            <el-option 
              v-for="dept in departments" 
              :key="dept.id" 
              :label="dept.name" 
              :value="dept.id" 
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('notice.isImportant')">
          <el-switch v-model="noticeForm.isImportant" />
        </el-form-item>
        
        <el-form-item :label="t('notice.sendEmail')">
          <el-switch v-model="noticeForm.sendEmail" />
        </el-form-item>
        
        <el-form-item :label="t('notice.sendInternalMessage')">
          <el-switch v-model="noticeForm.sendInternalMessage" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="publishNotice">{{ t('common.save') }}</el-button>
          <el-button @click="resetForm">{{ t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from '@/utils/i18n'
import { noticeApi, type Notice } from '@/api/notice'
import { ElMessage } from 'element-plus'

const { t } = useI18n()

const departments = ref([
  { id: 1, name: '技术部' },
  { id: 2, name: '人力资源部' },
  { id: 3, name: '市场部' }
])

const noticeForm = reactive<Notice>({
  title: '',
  content: '',
  scope: 'ALL',
  isImportant: false,
  sendEmail: false,
  sendInternalMessage: true,
  publisherId: 1,
  publisherName: '管理员'
})

const publishNotice = async () => {
  try {
    await noticeApi.publishNotice(noticeForm)
    ElMessage.success(t('notice.publishSuccess'))
    resetForm()
  } catch (error) {
    console.error('Failed to publish notice:', error)
    ElMessage.error(t('notice.publishError'))
  }
}

const resetForm = () => {
  noticeForm.title = ''
  noticeForm.content = ''
  noticeForm.scope = 'ALL'
  noticeForm.departmentId = undefined
  noticeForm.departmentName = undefined
  noticeForm.isImportant = false
  noticeForm.sendEmail = false
  noticeForm.sendInternalMessage = true
}

onMounted(() => {})
</script>

<style scoped lang="scss">
.notice-publish {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
  }
}
</style>
