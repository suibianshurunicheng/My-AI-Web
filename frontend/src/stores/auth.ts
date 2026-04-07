import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as authApi from '@/api/auth'

const TOKEN_KEY = 'auth_token'
const USERNAME_KEY = 'auth_username'

interface AuthState {
  token: string | null
  username: string | null
  isAuthenticated: boolean
}

export const useAuthStore = defineStore('auth', () => {
  const state = ref<AuthState>({
    token: localStorage.getItem(TOKEN_KEY),
    username: localStorage.getItem(USERNAME_KEY),
    isAuthenticated: !!localStorage.getItem(TOKEN_KEY)
  })

  // 登录
  const login = async (username: string, password: string) => {
    try {
      const response = await authApi.login({ username, password })
      const { token, username: tokenUsername } = response.data
      
      // 保存到 localStorage
      localStorage.setItem(TOKEN_KEY, token)
      localStorage.setItem(USERNAME_KEY, tokenUsername)
      
      // 更新状态
      state.value.token = token
      state.value.username = tokenUsername
      state.value.isAuthenticated = true
      
      return response
    } catch (error) {
      throw error
    }
  }

  // 登出
  const logout = () => {
    // 清除 localStorage
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USERNAME_KEY)
    
    // 更新状态
    state.value.token = null
    state.value.username = null
    state.value.isAuthenticated = false
  }

  // 验证 token
  const verifyToken = async () => {
    if (!state.value.token) {
      return false
    }
    
    try {
      await authApi.verifyToken()
      return true
    } catch (error) {
      logout()
      return false
    }
  }

  // 获取 token
  const getToken = () => state.value.token

  // 获取用户名
  const getUsername = () => state.value.username

  return {
    state,
    login,
    logout,
    verifyToken,
    getToken,
    getUsername
  }
})
