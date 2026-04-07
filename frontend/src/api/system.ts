import { get } from '@/utils/request'
import service from '@/utils/request'

export interface OperationLog {
  id: number
  username: string
  operation: string
  details?: string
  ip?: string
  createdAt: string
}

export interface LoginLog {
  id: number
  username: string
  status: string
  ip?: string
  createdAt: string
}

export interface ExportRequest {
  format: 'excel' | 'csv'
  fields: string[]
  filters?: {
    name?: string
    code?: string
  }
}

export interface PageResponse<T> {
  total: number
  page: number
  size: number
  list: T[]
}

export const logApi = {
  getOperationLogs: (page: number = 1, size: number = 10) => {
    return get<PageResponse<OperationLog>>('/api/logs/operation', { page, size })
  },

  getLoginLogs: (page: number = 1, size: number = 10) => {
    return get<PageResponse<LoginLog>>('/api/logs/login', { page, size })
  }
}

export const exportApi = {
  exportEmployees: (data: ExportRequest) => {
    return service.post('/api/employees/export', data, {
      responseType: 'blob'
    })
  }
}
