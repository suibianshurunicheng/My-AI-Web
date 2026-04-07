import { get, post, put, del, getWithCancel } from '@/utils/request'
//import type { PageResponse, ApiResponse } from './types'

export interface Job {
  id?: number
  title: string
  departmentId: number
  departmentName?: string
  positionId: number
  positionName?: string
  requirement: string
  salaryRange: string
  recruitCount: number
  status: string
  createdAt?: string
  updatedAt?: string
}

export interface Resume {
  id?: number
  name: string
  phone: string
  email: string
  jobId: number
  jobTitle?: string
  resumeFile: string
  status: string
  createdAt?: string
  updatedAt?: string
}

export interface Interview {
  id?: number
  resumeId: number
  candidateName?: string
  jobTitle?: string
  interviewTime: string
  interviewers: string
  location: string
  status: string
  evaluation?: string
  result?: string
  createdAt?: string
  updatedAt?: string
}

export interface Offer {
  id?: number
  resumeId: number
  candidateName?: string
  jobId?: number
  jobTitle?: string
  salary: string
  joinDate: string
  status: string
  createdAt?: string
  updatedAt?: string
}

export interface CreateEmployeeRequest {
  employeeCode: string
  departmentId: number
  positionId: number
  entryDate: string
}

export interface EmployeeResponse {
  employeeId: number
  employeeCode: string
  name: string
  phone: string
  email: string
  departmentId: number
  departmentName: string
  positionId: number
  positionName: string
  entryDate: string
  status: string
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

export const jobApi = {
  getJobs: (params?: {
    page?: number
    size?: number
    departmentId?: number
    positionId?: number
    status?: string
  }) => {
    return get<ApiResponse<PageResponse<Job>>>('/api/recruitment/jobs', params)
  },

  createJob: (data: Job) => {
    return post<ApiResponse<Job>>('/api/recruitment/jobs', data)
  },

  updateJob: (id: number, data: Job) => {
    return put<ApiResponse<Job>>(`/api/recruitment/jobs/${id}`, data)
  },

  deleteJob: (id: number) => {
    return del<ApiResponse<void>>(`/api/recruitment/jobs/${id}`)
  }
}

export const resumeApi = {
  getResumes: (params?: {
    page?: number
    size?: number
    jobId?: number
    status?: string
  }) => {
    return get<ApiResponse<PageResponse<Resume>>>('/api/recruitment/resumes', params)
  },

  uploadResume: (formData: FormData) => {
    return post<ApiResponse<Resume>>('/api/recruitment/resumes/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  updateStatus: (id: number, status: string) => {
    return put<ApiResponse<Resume>>(`/api/recruitment/resumes/${id}/status`, { status })
  }
}

export const interviewApi = {
  getInterviews: (params?: {
    page?: number
    size?: number
    resumeId?: number
    status?: string
  }) => {
    return get<ApiResponse<PageResponse<Interview>>>('/api/recruitment/interviews', params)
  },

  scheduleInterview: (data: {
    resumeId: number
    interviewTime: string
    interviewers: string
    location: string
  }) => {
    return post<ApiResponse<Interview>>('/api/recruitment/interviews', data)
  },

  evaluateInterview: (id: number, data: {
    evaluation: string
    result: string
  }) => {
    return put<ApiResponse<Interview>>(`/api/recruitment/interviews/${id}/evaluate`, data)
  },

  updateInterview: (id: number, data: Partial<Interview>) => {
    return put<ApiResponse<Interview>>(`/api/recruitment/interviews/${id}`, data)
  }
}

export const offerApi = {
  getOffers: (params?: {
    page?: number
    size?: number
    status?: string
  }) => {
    return get<ApiResponse<PageResponse<Offer>>>('/api/recruitment/offers', params)
  },

  createOffer: (data: {
    resumeId: number
    salary: string
    joinDate: string
  }) => {
    return post<ApiResponse<Offer>>('/api/recruitment/offers', data)
  },

  processOffer: (id: number, status: string) => {
    return put<ApiResponse<Offer>>(`/api/recruitment/offers/${id}/process`, { status })
  },

  updateOffer: (id: number, data: Partial<Offer>) => {
    return put<ApiResponse<Offer>>(`/api/recruitment/offers/${id}`, data)
  },

  approveOffer: (id: number) => {
    return put<ApiResponse<Offer>>(`/api/recruitment/offers/${id}/approve`, {})
  },

  createEmployeeFromOffer: (id: number, data: CreateEmployeeRequest) => {
    return post<ApiResponse<EmployeeResponse>>(`/api/recruitment/offers/${id}/create-employee`, data)
  }
}

export const recruitmentApi = {
  ...jobApi,
  ...resumeApi,
  ...interviewApi,
  ...offerApi
}