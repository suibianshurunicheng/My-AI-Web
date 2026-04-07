import { get, post, del } from '@/utils/request'

export interface SalaryStructure {
  id?: number
  name: string
  type: string
  formula: string
  amount: number
  isActive: boolean
}

export interface Payslip {
  id?: number
  employeeId: number
  employeeName: string
  employeeCode: string
  year: number
  monthNum: number
  basicSalary: number
  positionSalary?: number
  performanceBonus: number
  allowances: number
  deductions: number
  socialInsurance: number
  housingFund: number
  individualTax: number
  grossSalary: number
  netSalary: number
  status?: string
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

export const salaryStructureApi = {
  getStructures: () =>{
    return get<ApiResponse<SalaryStructure[]>>('/api/salary/structures')
  },

  saveStructure: (data: SalaryStructure) =>{
    return post<ApiResponse<SalaryStructure>>('/api/salary/structures', data)
  },

  deleteStructure: (id: number) =>{
    return del<ApiResponse<void>>(`/api/salary/structures/${id}`)
  }
}

export const payslipApi = {
  getPayslips: (params?: {
    employeeId?: number
    year?: number
    monthNum?: number
    page?: number
    size?: number
  }) =>{
    return get<ApiResponse<PageResponse<Payslip>>>('/api/salary/payslips', params)
  },

  getPayslipById: (id: number) =>{
    return get<ApiResponse<Payslip>>(`/api/salary/payslips/${id}`)
  },

  generatePayslips: (data: {
    year: number
    monthNum: number
    employeeIds?: number[]
  }) =>{
    return post<ApiResponse<Payslip[]>>('/api/salary/payslips/generate', data)
  },

  exportPayslips: (data: {
    format: string
    filters?: {
      year?: number
      monthNum?: number
    }
    fields?: string[]
  }) =>{
    return post<ApiResponse<Blob>>('/api/salary/payslips/export', data, {
      responseType: 'blob'
    })
  }
}

