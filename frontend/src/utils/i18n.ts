import { ref, computed } from 'vue'
import { useSettingsStore, type Language } from '@/stores/settings'
import zhCN from '@/locales/zh-CN'
import zhTW from '@/locales/zh-TW'
import enUS from '@/locales/en-US'

const messages = {
  'zh-CN': zhCN,
  'zh-TW': zhTW,
  'en-US': enUS
}

export function useI18n() {
  const settingsStore = useSettingsStore()

  const t = (key: string): any => {
    const keys = key.split('.')
    let value: any = messages[settingsStore.language]
    
    for (const k of keys) {
      if (value && value[k] !== undefined) {
        value = value[k]
      } else {
        return key
      }
    }
    
    return value
  }

  return {
    t,
    locale: computed(() => settingsStore.language)
  }
}
