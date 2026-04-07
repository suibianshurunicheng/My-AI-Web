<template>  
  <el-dialog
    v-model="visible"
    :title="t('changelog.welcomeTitle')"
    width="600px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="changelog-dialog"
  >
    <div class="dialog-content">
      <div class="welcome-section">
        <h3 class="welcome-subtitle">{{ t('changelog.welcomeSubtitle') }}</h3>
        <div class="welcome-description">
          {{ t('changelog.v1_0_2.description') }}
        </div>
      </div>

      <el-divider />

      <div class="features-section" v-if="hasFeatures">
        <h4 class="section-title">
          <el-icon><Star /></el-icon>
          {{ t('changelog.features') }}
        </h4>
        <ul class="feature-list">
          <li v-for="(feature, idx) in t('changelog.v1_0_2.features')" :key="idx">
            {{ feature }}
          </li>
        </ul>
      </div>

      <el-divider v-if="hasFeatures && (hasImprovements || hasBugs)" />

      <div class="improvements-section" v-if="hasImprovements">
        <h4 class="section-title">
          <el-icon><TrendCharts /></el-icon>
          {{ t('changelog.improvements') }}
        </h4>
        <ul class="feature-list">
          <li v-for="(improvement, idx) in t('changelog.v1_0_2.improvements')" :key="idx">
            {{ improvement }}
          </li>
        </ul>
      </div>

      <el-divider v-if="hasImprovements && hasBugs" />

      <div class="bugs-section" v-if="hasBugs">
        <h4 class="section-title">
          <el-icon><Warning /></el-icon>
          {{ t('changelog.bugs') }}
        </h4>
        <ul class="feature-list">
          <li v-for="(bug, idx) in t('changelog.v1_0_2.bugs')" :key="idx">
            {{ bug }}
          </li>
        </ul>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">{{ t('changelog.close') }}</el-button>
        <el-button type="primary" @click="handleViewMore">{{ t('changelog.viewMore') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '@/utils/i18n'
import { Warning, TrendCharts, Star } from '@element-plus/icons-vue'

const { t } = useI18n()
const router = useRouter()

const visible = ref(false)

const hasFeatures = computed(() => {
  const features = t('changelog.v1_0_2.features')
  return features && Array.isArray(features) && features.length > 0
})

const hasBugs = computed(() => {
  const bugs = t('changelog.v1_0_2.bugs')
  return bugs && Array.isArray(bugs) && bugs.length > 0
})

const hasImprovements = computed(() => {
  const improvements = t('changelog.v1_0_2.improvements')
  return improvements && Array.isArray(improvements) && improvements.length > 0
})

const open = () => {
  visible.value = true
}

const handleClose = () => {
  visible.value = false
}

const handleViewMore = () => {
  visible.value = false
  router.push('/changelog')
}

defineExpose({
  open
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.changelog-dialog {
  :deep(.el-dialog__header) {
    padding-bottom: 16px;
  }
  
  :deep(.el-dialog__title) {
    font-size: 20px;
    font-weight: 600;
  }
}

.dialog-content {
  padding: 8px 0;
}

.welcome-section {
  text-align: center;
  
  .welcome-subtitle {
    margin: 0 0 12px 0;
    font-size: 18px;
    font-weight: 500;
    color: var(--text-primary);
  }
  
  .welcome-description {
    font-size: 14px;
    color: var(--text-secondary);
  }
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 16px 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.feature-list {
  margin: 0;
  padding-left: 20px;
  list-style-type: disc;
  
  li {
    margin-bottom: 8px;
    color: var(--text-primary);
    line-height: 1.6;
    font-size: 14px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>

