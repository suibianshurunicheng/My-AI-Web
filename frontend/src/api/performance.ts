import { get, post, put, del } from '@/utils/request'

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

export interface KpiIndicator {
  id?: number
  name: string
  description?: string
  position?: string
  weight: number
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

export interface PerformanceCycle {
  id?: number
  name: string
  cycleType: string
  startDate: string
  endDate: string
  selfEvalStart?: string
  selfEvalEnd?: string
  superEvalStart?: string
  superEvalEnd?: string
  status?: string
  createdAt?: string
  updatedAt?: string
}

export interface PerformanceScore {
  id?: number
  cycleId: number
  employeeId: number
  employeeName: string
  employeeCode: string
  kpiIndicatorId: number
  kpiIndicatorName: string
  kpiWeight: number
  selfScore?: number
  selfComment?: string
  supervisorScore?: number
  supervisorComment?: string
  finalScore?: number
  createdAt?: string
  updatedAt?: string
}

export interface PerformanceResult {
  id?: number
  cycleId: number
  cycleName: string
  employeeId: number
  employeeName: string
  employeeCode: string
  totalScore: number
  grade: string
  bonusAmount?: number
  salaryAdjustment?: number
  finalComment?: string
  status?: string
  createdAt?: string
  updatedAt?: string
}

export const kpiApi = {
  getIndicators: () => {
    return get<ApiResponse<KpiIndicator[]>>('/api/performance/kpi-indicators')
  },

  getIndicator: (id: number) => {
    return get<ApiResponse<KpiIndicator>>(`/api/performance/kpi-indicators/${id}`)
  },

  createIndicator: (data: KpiIndicator) => {
    return post<ApiResponse<KpiIndicator>>('/api/performance/kpi-indicators', data)
  },

  updateIndicator: (id: number, data: KpiIndicator) => {
    return put<ApiResponse<KpiIndicator>>(`/api/performance/kpi-indicators/${id}`, data)
  },

  deleteIndicator: (id: number) => {
    return del<ApiResponse<void>>(`/api/performance/kpi-indicators/${id}`)
  }
}

export const cycleApi = {
  getCycles: (params?: {
    page?: number
    size?: number
    cycleType?: string
  }) => {
    return get<ApiResponse<PageResponse<PerformanceCycle>>>('/api/performance/cycles', params)
  },

  getCycle: (id: number) => {
    return get<ApiResponse<PerformanceCycle>>(`/api/performance/cycles/${id}`)
  },

  createCycle: (data: PerformanceCycle) => {
    return post<ApiResponse<PerformanceCycle>>('/api/performance/cycles', data)
  },

  updateCycle: (id: number, data: PerformanceCycle) => {
    return put<ApiResponse<PerformanceCycle>>(`/api/performance/cycles/${id}`, data)
  },

  deleteCycle: (id: number) => {
    return del<ApiResponse<void>>(`/api/performance/cycles/${id}`)
  },

  calculateResults: (cycleId: number) => {
    return post<ApiResponse<void>>(`/api/performance/cycles/${cycleId}/calculate`)
  }
}

export const scoreApi = {
  getScoresByCycle: (cycleId: number) => {
    return get<ApiResponse<PerformanceScore[]>>(`/api/performance/scores/cycle/${cycleId}`)
  },

  getScoresByCycleAndEmployee: (
    cycleId: number,
    employeeId: number
  ) => {
    return get<ApiResponse<PerformanceScore[]>>(`/api/performance/scores/cycle/${cycleId}/employee/${employeeId}`)
  },

  saveScore: (data: PerformanceScore) => {
    return post<ApiResponse<PerformanceScore>>('/api/performance/scores', data)
  }
}

export const resultApi = {
  getResultsByCycle: (
    cycleId: number,
    params?: { page?: number; size?: number }
  ) => {
    return get<ApiResponse<PageResponse<PerformanceResult>>>(`/api/performance/results/cycle/${cycleId}`, params)
  },

  getResult: (id: number) => {
    return get<ApiResponse<PerformanceResult>>(`/api/performance/results/${id}`)
  },

  saveResult: (data: PerformanceResult) => {
    return post<ApiResponse<PerformanceResult>>('/api/performance/results', data)
  }
}
