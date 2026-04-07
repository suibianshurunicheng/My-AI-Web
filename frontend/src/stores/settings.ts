import { defineStore } from 'pinia'
import { ref } from 'vue'

export type Theme = 'light' | 'dark' | 'starry'
export type Language = 'zh-CN' | 'zh-TW' | 'en-US'

const THEME_KEY = 'theme'
const LANGUAGE_KEY = 'language'

export const useSettingsStore = defineStore('settings', () => {
  const theme = ref<Theme>((localStorage.getItem(THEME_KEY) as Theme) || 'light')
  const language = ref<Language>((localStorage.getItem(LANGUAGE_KEY) as Language) || 'zh-CN')

  const applyTheme = () => {
    const html = document.documentElement
    html.classList.remove('dark', 'starry')
    if (theme.value === 'dark') {
      html.classList.add('dark')
    } else if (theme.value === 'starry') {
      html.classList.add('starry')
    }
  }

  const setTheme = (newTheme: Theme) => {
    theme.value = newTheme
    localStorage.setItem(THEME_KEY, newTheme)
    applyTheme()
  }

  const setLanguage = (newLanguage: Language) => {
    language.value = newLanguage
    localStorage.setItem(LANGUAGE_KEY, newLanguage)
  }

  const toggleTheme = () => {
    let newTheme: Theme
    if (theme.value === 'light') {
      newTheme = 'dark'
    } else if (theme.value === 'dark') {
      newTheme = 'starry'
    } else {
      newTheme = 'light'
    }
    setTheme(newTheme)
  }

  applyTheme()

  return {
    theme,
    language,
    setTheme,
    setLanguage,
    toggleTheme
  }
})
