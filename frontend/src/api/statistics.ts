import { get, post } from '@/utils/request'

export interface EmployeeCountDTO {
  total: number
  active: number
  inactive: number
}

export interface GenderRatioDTO {
  gender: string
  count: number
  percentage: number
}

export interface DepartmentCountDTO {
  departmentName: string
  count: number
}

export interface AgeDistributionDTO {
  range: string
  count: number
}

export interface TurnoverMonthlyDTO {
  month: number
  totalEmployees: number
  departures: number
  turnoverRate: number
}

export interface TurnoverDepartmentDTO {
  departmentName: string
  totalEmployees: number
  departures: number
  turnoverRate: number
}

export interface ExportRequest {
  format: string
  fields: string[]
  filters?: Record<string, any>
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export const getEmployeeCount = (): Promise<ApiResponse<EmployeeCountDTO>> => {
  return get('/api/statistics/employee-count')
}

export const getGenderRatio = (): Promise<ApiResponse<GenderRatioDTO[]>> => {
  return get('/api/statistics/gender-ratio')
}

export const getDepartmentCount = (): Promise<ApiResponse<DepartmentCountDTO[]>> => {
  return get('/api/statistics/department-count')
}

export const getAgeDistribution = (): Promise<ApiResponse<AgeDistributionDTO[]>> => {
  return get('/api/statistics/age-distribution')
}

export const getTurnoverByMonth = (year?: number): Promise<ApiResponse<TurnoverMonthlyDTO[]>> => {
  const params = year ? { year } : undefined
  return get('/api/statistics/turnover/monthly', params)
}

export const getTurnoverByDepartment = (year?: number): Promise<ApiResponse<TurnoverDepartmentDTO[]>> => {
  const params = year ? { year } : undefined
  return get('/api/statistics/turnover/department', params)
}

export const exportCustomReport = (exportRequest: ExportRequest): Promise<any> => {
  return post('/api/statistics/custom-report/export', exportRequest, {
    responseType: 'blob'
  })
}
