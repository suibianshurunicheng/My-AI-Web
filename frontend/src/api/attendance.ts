import { get, post, put } from '@/utils/request'

export interface AttendanceRecord {
  id?: number
  employeeId: number
  employeeName: string
  attendanceDate: string
  checkInTime?: string
  checkOutTime?: string
  status?: string
  isMakeup?: boolean
  makeupStatus?: string
  makeupReason?: string
  createdAt?: string
  updatedAt?: string
}

export interface LeaveApplication {
  id?: number
  employeeId: number
  employeeName: string
  leaveType: string
  startDate: string
  endDate: string
  days?: number
  reason: string
  status?: string
  approverId?: number
  approverName?: string
  approvalComment?: string
  approvedAt?: string
  createdAt?: string
  updatedAt?: string
}

export interface OvertimeRecord {
  id?: number
  employeeId: number
  employeeName: string
  startTime: string
  endTime: string
  hours?: number
  reason: string
  status?: string
  conversionType?: string
  compensatoryDays?: number
  overtimePay?: number
  approverId?: number
  approverName?: string
  approvalComment?: string
  approvedAt?: string
  convertedAt?: string
  createdAt?: string
  updatedAt?: string
}

export interface PageResponse<T> {
  total: number
  page: number
  size: number
  list: T[]
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export const attendanceApi = {
  getAttendanceRecords: (page: number = 1, size: number = 10) => {
    return get<ApiResponse<PageResponse<AttendanceRecord>>>('/api/attendance/records', { page, size })
  },

  checkIn: (data: { employeeId: number; employeeName: string }) => {
    return post<ApiResponse<AttendanceRecord>>('/api/attendance/check-in', data)
  },

  checkOut: (data: { employeeId: number; employeeName: string }) => {
    return post<ApiResponse<AttendanceRecord>>('/api/attendance/check-out', data)
  },

  applyMakeup: (data: {
    employeeId: number
    employeeName: string
    date: string
    type: string
    reason: string
  }) => {
    return post<ApiResponse<AttendanceRecord>>('/api/attendance/makeup', data)
  },

  approveMakeup: (id: number, data: { approved: boolean; comment?: string }) => {
    return put<ApiResponse<AttendanceRecord>>(`/api/attendance/makeup/${id}/approve`, data)
  }
}

export const leaveApi = {
  getLeaveApplications: (page: number = 1, size: number = 10) => {
    return get<ApiResponse<PageResponse<LeaveApplication>>>('/api/attendance/leave/applications', { page, size })
  },

  applyLeave: (data: LeaveApplication) => {
    return post<ApiResponse<LeaveApplication>>('/api/attendance/leave/apply', data)
  },

  approveLeave: (id: number, data: {
    approverId?: number
    approverName?: string
    approved: boolean
    comment?: string
  }) => {
    return put<ApiResponse<LeaveApplication>>(`/api/attendance/leave/applications/${id}/approve`, data)
  },

  getLeaveBalance: (employeeId: number) => {
    return get<ApiResponse<Record<string, number>>>(`/api/attendance/leave/balance/${employeeId}`)
  }
}

export const overtimeApi = {
  getOvertimeRecords: (page: number = 1, size: number = 10) => {
    return get<ApiResponse<PageResponse<OvertimeRecord>>>('/api/attendance/overtime/records', { page, size })
  },

  applyOvertime: (data: OvertimeRecord) => {
    return post<ApiResponse<OvertimeRecord>>('/api/attendance/overtime/apply', data)
  },

  approveOvertime: (id: number, data: {
    approverId?: number
    approverName?: string
    approved: boolean
    comment?: string
  }) => {
    return put<ApiResponse<OvertimeRecord>>(`/api/attendance/overtime/records/${id}/approve`, data)
  },

  convertOvertime: (data: { id: number; conversionType: string }) => {
    return post<ApiResponse<OvertimeRecord>>('/api/attendance/overtime/convert', data)
  }
}

export const statisticsApi = {
  getMonthlyStatistics: (year: number, month: number) => {
    return get<ApiResponse<Record<string, any>>>('/api/attendance/statistics/monthly', { year, month })
  }
}
