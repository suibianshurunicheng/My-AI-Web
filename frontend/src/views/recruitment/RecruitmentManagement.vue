<template>
  <div class="recruitment-management">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('recruitment.title') }}</h2>
      </div>
    </div>

    <el-card>
      <el-tabs v-model="activeTab" class="recruitment-tabs">
        <el-tab-pane :label="t('recruitment.jobs')" name="jobs">
          <JobList />
        </el-tab-pane>
        <el-tab-pane :label="t('recruitment.resumes')" name="resumes">
          <ResumeList />
        </el-tab-pane>
        <el-tab-pane :label="t('recruitment.interviews')" name="interviews">
          <InterviewList />
        </el-tab-pane>
        <el-tab-pane :label="t('recruitment.offers')" name="offers">
          <OfferList />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from '@/utils/i18n'
import JobList from './JobList.vue'
import ResumeList from './ResumeList.vue'
import InterviewList from './InterviewList.vue'
import OfferList from './OfferList.vue'

const { t } = useI18n()
const activeTab = ref('jobs')
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.recruitment-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .page-title {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary);
    transition: color 0.3s ease;
  }
}

.recruitment-tabs {
  height: calc(100vh - 180px);
  overflow: hidden;
}
</style>