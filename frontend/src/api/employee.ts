import { get, post, put, del } from '@/utils/request'

export interface Employee {
  id?: number
  name: string
  gender: string
  age: number
  birthDate: string
  height: number
  weight: number
  position: string
  email: string
  phone: string
  employeeCode?: string
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

// 获取员工列表
export const getEmployees = (params: {
  name?: string
  code?: string
  page?: number
  size?: number
}): Promise<ApiResponse<PageResponse<Employee>>> =>{
  return get('/api/employees', params)
}

// 获取员工详情
export const getEmployeeById = (id: number): Promise<ApiResponse<Employee>> =>{
  return get(`/api/employees/${id}`)
}

// 新增员工
export const createEmployee = (data: Omit<Employee, 'id' | 'employeeCode' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<Employee>> =>{
  return post('/api/employees', data)
}

// 修改员工信息
export const updateEmployee = (id: number, data: Partial<Omit<Employee, 'id' | 'employeeCode' | 'createdAt' | 'updatedAt'>>): Promise<ApiResponse<Employee>> =>{
  return put(`/api/employees/${id}`, data)
}

// 删除员工
export const deleteEmployee = (id: number): Promise<ApiResponse<void>> =>{
  return del(`/api/employees/${id}`)
}

