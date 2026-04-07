<template>
  <div class="changelog-page">
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">{{ t('changelog.title') }}</h2>
        <p class="page-description">{{ t('app.subtitle') }}</p>
      </div>
    </div>

    <div class="version-list">
      <el-card class="version-card" v-for="version in versions" :key="version.key">
        <div class="version-header">
          <h3 class="version-title">{{ t(`changelog.${version.key}.title`) }}</h3>
          <span class="version-date">{{ t(`changelog.${version.key}.date`) }}</span>
        </div>
        <div class="version-description">{{ t(`changelog.${version.key}.description`) }}</div>
        
        <el-divider v-if="hasFeatures(version.key)" />
        <div v-if="hasFeatures(version.key)">
          <h4 class="section-title">
            <el-icon><Star /></el-icon>
            {{ t('changelog.features') }}
          </h4>
          <ul class="feature-list">
            <li v-for="(feature, idx) in getFeatures(version.key)" :key="idx">
              {{ feature }}
            </li>
          </ul>
        </div>

        <el-divider v-if="hasImprovements(version.key)" />
        <div v-if="hasImprovements(version.key)">
          <h4 class="section-title">
            <el-icon><TrendCharts /></el-icon>
            {{ t('changelog.improvements') }}
          </h4>
          <ul class="feature-list">
            <li v-for="(improvement, idx) in getImprovements(version.key)" :key="idx">
              {{ improvement }}
            </li>
          </ul>
        </div>

        <el-divider v-if="hasBugs(version.key)" />
        <div v-if="hasBugs(version.key)">
          <h4 class="section-title">
            <el-icon><Warning /></el-icon>
            {{ t('changelog.bugs') }}
          </h4>
          <ul class="feature-list">
            <li v-for="(bug, idx) in getBugs(version.key)" :key="idx">
              {{ bug }}
            </li>
          </ul>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from '@/utils/i18n'
import { Star, TrendCharts, Warning } from '@element-plus/icons-vue'

const { t } = useI18n()

const versions = [
  { key: 'v2_0_0' },
  { key: 'v1_0_2' },
  { key: 'v1_0_1' },
  { key: 'v1_0_0' }
]

const hasFeatures = (key: string) => {
  const features = t(`changelog.${key}.features`)
  return features && Array.isArray(features) && features.length > 0
}

const getFeatures = (key: string) => {
  return t(`changelog.${key}.features`) || []
}

const hasImprovements = (key: string) => {
  const improvements = t(`changelog.${key}.improvements`)
  return improvements && Array.isArray(improvements) && improvements.length > 0
}

const getImprovements = (key: string) => {
  return t(`changelog.${key}.improvements`) || []
}

const hasBugs = (key: string) => {
  const bugs = t(`changelog.${key}.bugs`)
  return bugs && Array.isArray(bugs) && bugs.length > 0
}

const getBugs = (key: string) => {
  return t(`changelog.${key}.bugs`) || []
}
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.changelog-page {
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

.version-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.version-card {
  :deep(.el-card__body) {
    padding: 24px;
  }
}

.version-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.version-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.version-date {
  font-size: 14px;
  color: var(--text-secondary);
}

.version-description {
  font-size: 14px;
  color: var(--text-secondary);
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
  }
}
</style>
