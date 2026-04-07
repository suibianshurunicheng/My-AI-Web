<template>
  <div class="settings-view">
    <div class="page-header">
      <h2 class="page-title">{{ t('settings.title') }}</h2>
    </div>
    
    <div class="settings-container">
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <el-icon><Sunny /></el-icon>
            <span>{{ t('settings.theme') }}</span>
          </div>
        </template>
        
        <div class="theme-options">
          <div 
            :class="['theme-option', { active: settingsStore.theme === 'light' }]"
            @click="settingsStore.setTheme('light')"
          >
            <div class="theme-preview light-preview"></div>
            <span>{{ t('settings.lightTheme') }}</span>
          </div>
          
          <div 
            :class="['theme-option', { active: settingsStore.theme === 'dark' }]"
            @click="settingsStore.setTheme('dark')"
          >
            <div class="theme-preview dark-preview"></div>
            <span>{{ t('settings.darkTheme') }}</span>
          </div>
          
          <div 
            :class="['theme-option', { active: settingsStore.theme === 'starry' }]"
            @click="settingsStore.setTheme('starry')"
          >
            <div class="theme-preview starry-preview"></div>
            <span>{{ t('settings.starryTheme') }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <el-icon><ChatDotRound /></el-icon>
            <span>{{ t('settings.language') }}</span>
          </div>
        </template>
        
        <div class="language-options">
          <el-radio-group v-model="selectedLanguage" @change="handleLanguageChange">
            <el-radio-button value="zh-CN">{{ t('settings.chinese') }}</el-radio-button>
            <el-radio-button value="zh-TW">{{ t('settings.traditional') }}</el-radio-button>
            <el-radio-button value="en-US">{{ t('settings.english') }}</el-radio-button>
          </el-radio-group>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useSettingsStore, type Language } from '@/stores/settings'
import { useI18n } from '@/utils/i18n'
import { Sunny, ChatDotRound } from '@element-plus/icons-vue'

const settingsStore = useSettingsStore()
const { t } = useI18n()

const selectedLanguage = ref<Language>(settingsStore.language)

const handleLanguageChange = (lang: Language) => {
  settingsStore.setLanguage(lang)
}
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.settings-view {
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

.settings-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.settings-card {
  max-width: 800px;
  background-color: var(--bg-color-lighter);
  transition: background-color 0.3s ease;
  
  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    color: var(--text-primary);
    transition: color 0.3s ease;
  }
}

.theme-options {
  display: flex;
  gap: 20px;
}

.theme-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border: 2px solid transparent;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--text-primary);
  transition: color 0.3s ease;
  &:hover {
    border-color: $primary-color;
  }
  
  &.active {
    border-color: $primary-color;
    background-color: rgba($primary-color, 0.05);
  }
}

.theme-preview {
  width: 120px;
  height: 80px;
  border-radius: 8px;
  border: 1px solid var(--border-light);
  transition: border-color 0.3s ease;
}

.light-preview {
  background: linear-gradient(135deg, #ffffff 0%, #f0f2f5 100%);
}

.dark-preview {
  background: linear-gradient(135deg, #1f1f1f 0%, #141414 100%);
}

.starry-preview {
  background: linear-gradient(135deg, #0a0a1e 0%, #1a1a3e 100%);
  position: relative;
  overflow: hidden;
  
  &::before { 
    content: '';
    position: absolute;
    width: 2px;
    height: 2px;
    background: white;
    border-radius: 50%;
    box-shadow: 
      10px 5px 0 white, 30px 15px 0 white, 50px 8px 0 white, 70px 25px 0 white,
      20px 35px 0 white, 40px 40px 0 white, 60px 45px 0 white, 80px 30px 0 white,
      15px 55px 0 white, 35px 60px 0 white, 55px 50px 0 white, 75px 65px 0 white;
    top: 10px;
    left: 10px;
  }
}

.language-options {
  padding: 10px 0;
}

</style>

