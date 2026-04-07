import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { useAuthStore } from '@/stores/auth'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: '',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: any) =>{
    // 在发送请求之前做些什么
    const authStore = useAuthStore()
    const token = authStore.getToken()
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    console.log('请求配置:', config)
    return config
  },
  (error: any) =>{
    // 对请求错误做些什么
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) =>{
    // 如果是 blob 响应类型，直接返回整个 response
    if (response.config.responseType === 'blob') {
      return response
    }
    
    const { data } = response
    
    // 如果响应状态码不是 200，抛出错误
    if (data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    
    return data
  },
  (error: any) =>{
    // 对响应错误做点什么
    let message = '网络错误，请稍后重试'
    
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = `网络错误: ${error.response.status}`
      }
    } else if (error.request) {
      message = '网络连接失败，请检查网络设置'
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// 封装 GET 请求
export const get = <T = any>(url: string, params?: any, config?: AxiosRequestConfig): Promise<T> =>{  
  return service.get(url, { params, ...config })
}

// 带取消功能的 GET 请求
export const getWithCancel = <T = any>(url: string, params?: any, signal?: AbortSignal, config?: AxiosRequestConfig): Promise<T> =>{  
  const finalConfig = {
    ...config,
    signal
  }
  return service.get(url, { params, ...finalConfig })
}

// 封装 POST 请求
export const post = <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> =>{
  return service.post(url, data, config)
}

// 封装 PUT 请求
export const put = <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> =>{
  return service.put(url, data, config)
}

// 封装 DELETE 请求
export const del = <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> =>{
  return service.delete(url, config)
}

export default service

