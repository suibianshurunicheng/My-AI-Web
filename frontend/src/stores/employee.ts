import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Employee, PageResponse } from '@/api/employee'
import * as employeeApi from '@/api/employee'

interface EmployeeState {
  employees: Employee[]
  currentEmployee: Employee | null
  pagination: {
    total: number
    page: number
    size: number
  }
  loading: boolean
  searchParams: {
    name: string
    code: string
  }
}

export const useEmployeeStore = defineStore('employee', () => {
  const state = ref<EmployeeState>({
    employees: [],
    currentEmployee: null,
    pagination: {
      total: 0,
      page: 1,
      size: 10
    },
    loading: false,
    searchParams: {
      name: '',
      code: ''
    }
  })

  // 获取员工列表
  const fetchEmployees = async (page = 1, size = 10) => {
    state.value.loading = true
    try {
      const response = await employeeApi.getEmployees({
        name: state.value.searchParams.name,
        code: state.value.searchParams.code,
        page,
        size
      })
      
      state.value.employees = response.data.list
      state.value.pagination = {
        total: response.data.total,
        page: response.data.page,
        size: response.data.size
      }
    } catch (error) {
      console.error('获取员工列表失败:', error)
      throw error
    } finally {
      state.value.loading = false
    }
  }

  // 获取员工详情
  const fetchEmployeeById = async (id: number) => {
    state.value.loading = true
    try {
      const response = await employeeApi.getEmployeeById(id)
      state.value.currentEmployee = response.data
      return response.data
    } catch (error) {
      console.error('获取员工详情失败:', error)
      throw error
    } finally {
      state.value.loading = false
    }
  }

  // 新增员工
  const createEmployee = async (employeeData: Omit<Employee, 'id' | 'employeeCode' | 'createdAt' | 'updatedAt'>) => {
    try {
      const response = await employeeApi.createEmployee(employeeData)
      await fetchEmployees(state.value.pagination.page, state.value.pagination.size)
      return response.data
    } catch (error) {
      console.error('新增员工失败:', error)
      throw error
    }
  }

  // 修改员工信息
  const updateEmployee = async (id: number, employeeData: Partial<Employee>) => {
    try {
      const response = await employeeApi.updateEmployee(id, employeeData)
      await fetchEmployees(state.value.pagination.page, state.value.pagination.size)
      if (state.value.currentEmployee?.id === id) {
        state.value.currentEmployee = { ...state.value.currentEmployee, ...employeeData }
      }
      return response.data
    } catch (error) {
      console.error('修改员工信息失败:', error)
      throw error
    }
  }

  // 删除员工
  const deleteEmployee = async (id: number) => {
    try {
      await employeeApi.deleteEmployee(id)
      await fetchEmployees(state.value.pagination.page, state.value.pagination.size)
      if (state.value.currentEmployee?.id === id) {
        state.value.currentEmployee = null
      }
    } catch (error) {
      console.error('删除员工失败:', error)
      throw error
    }
  }

  // 设置搜索参数
  const setSearchParams = (params: { name?: string; code?: string }) => {
    state.value.searchParams = {
      ...state.value.searchParams,
      ...params
    }
  }

  // 设置当前选中的员工
  const setCurrentEmployee = (employee: Employee | null) => {
    state.value.currentEmployee = employee
  }

  // 重置搜索参数
  const resetSearchParams = () => {
    state.value.searchParams = {
      name: '',
      code: ''
    }
  }

  return {
    state,
    fetchEmployees,
    fetchEmployeeById,
    createEmployee,
    updateEmployee,
    deleteEmployee,
    setSearchParams,
    setCurrentEmployee,
    resetSearchParams
  }
})