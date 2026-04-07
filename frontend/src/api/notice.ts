import { get, post, put, del } from '@/utils/request'

export interface Notice {
  id?: number
  title: string
  content: string
  scope: string
  departmentId?: number
  departmentName?: string
  isImportant?: boolean
  sendEmail?: boolean
  sendInternalMessage?: boolean
  publisherId?: number
  publisherName?: string
  publishTime?: string
  viewCount?: number
  isRead?: boolean
  createdAt?: string
  updatedAt?: string
}

export interface NoticeReadRecord {
  id?: number
  noticeId: number
  employeeId: number
  employeeName?: string
  departmentName?: string
  isRead?: boolean
  readTime?: string
  createdAt?: string
  updatedAt?: string
}

export interface NoticeStats {
  noticeId: number
  totalEmployees: number
  readCount: number
  unreadCount: number
  readRate: number
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface PageResponse<T> {
  total: number
  page: number
  size: number
  list: T[]
}

export const noticeApi = {
  // 获取员工可见的公告列表
  getEmployeeNotices: (params?: {
    employeeId?: number
    page?: number
    size?: number
  }) => {
    return get<ApiResponse<PageResponse<Notice>>>('/api/notice/list', params)
  },

  // 获取管理员的公告列表
  getAdminNotices: (params?: {
    scope?: string
    page?: number
    size?: number
  }) => {
    return get<ApiResponse<PageResponse<Notice>>>('/api/notice/admin/list', params)
  },

  // 获取公告详情
  getNoticeById: (id: number) => {
    return get<ApiResponse<Notice>>(`/api/notice/${id}`)
  },

  // 发布公告
  publishNotice: (data: Notice) => {
    return post<ApiResponse<Notice>>('/api/notice', data)
  },

  // 更新公告
  updateNotice: (id: number, data: Partial<Notice>) => {
    return put<ApiResponse<Notice>>(`/api/notice/${id}`, data)
  },

  // 删除公告
  deleteNotice: (id: number) => {
    return del<ApiResponse<void>>(`/api/notice/${id}`)
  },

  // 标记公告已读
  markAsRead: (id: number, employeeId: number) => {
    return post<ApiResponse<NoticeReadRecord>>(`/api/notice/${id}/read`, { employeeId })
  },

  // 获取公告阅读统计
  getNoticeStats: (id: number) => {
    return get<ApiResponse<NoticeStats>>(`/api/notice/${id}/stats`)
  },

  // 获取阅读详情列表
  getReadDetails: (id: number, params?: {
    page?: number
    size?: number
  }) => {
    return get<ApiResponse<PageResponse<NoticeReadRecord>>>(`/api/notice/${id}/read-details`, params)
  }
}
