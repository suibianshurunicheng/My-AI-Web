import { post, get } from '@/utils/request'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  username: string
  expiresIn: number
}

export interface VerifyTokenResponse {
  username: string
  valid: boolean
}

// 登录
export const login = (data: LoginRequest): Promise<any> => {
  return post('/api/auth/login', data)
}

// 验证 token
export const verifyToken = (): Promise<any> => {
  return get('/api/auth/verify')
}

// 登出
export const logout = (): Promise<any> => {
  return post('/api/auth/logout')
}
