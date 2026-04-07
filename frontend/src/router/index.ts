// @ts-ignore
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      redirect: '/employees'
    },
    {
      path: '/employees',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/employees/list'
        },
        {
          path: 'list',
          name: 'EmployeeList',
          component: () => import('@/views/EmployeeList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'detail/:id',
          name: 'EmployeeDetail',
          component: () => import('@/views/EmployeeDetail.vue'),
          props: true,
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'create',
          name: 'EmployeeCreate',
          component: () => import('@/views/EmployeeCreate.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/settings',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'Settings',
          component: () => import('@/views/Settings.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/logs',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'Logs',
          component: () => import('@/views/Logs.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/attendance',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/attendance/check-in'
        },
        {
          path: 'check-in',
          name: 'CheckInRecords',
          component: () => import('@/views/attendance/CheckInRecords.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'leave',
          name: 'LeaveManagement',
          component: () => import('@/views/attendance/LeaveManagement.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'overtime',
          name: 'OvertimeManagement',
          component: () => import('@/views/attendance/OvertimeManagement.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'statistics',
          name: 'AttendanceStatistics',
          component: () => import('@/views/attendance/AttendanceStatistics.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/changelog',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'Changelog',
          component: () => import('@/views/Changelog.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/salary',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/salary/payslips'
        },
        {
          path: 'payslips',
          name: 'PayslipList',
          component: () => import('@/views/salary/PayslipList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'structure',
          name: 'SalaryStructure',
          component: () => import('@/views/salary/SalaryStructure.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/performance',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/performance/kpi'
        },
        {
          path: 'kpi',
          name: 'KpiIndicatorList',
          component: () => import('@/views/performance/KpiIndicatorList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'cycle',
          name: 'PerformanceCycleList',
          component: () => import('@/views/performance/PerformanceCycleList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'result',
          name: 'PerformanceResultList',
          component: () => import('@/views/performance/PerformanceResultList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/recruitment',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/recruitment/jobs'
        },
        {
          path: 'jobs',
          name: 'RecruitmentJobs',
          component: () => import('@/views/recruitment/JobList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'resumes',
          name: 'RecruitmentResumes',
          component: () => import('@/views/recruitment/ResumeList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'interviews',
          name: 'RecruitmentInterviews',
          component: () => import('@/views/recruitment/InterviewList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'offers',
          name: 'RecruitmentOffers',
          component: () => import('@/views/recruitment/OfferList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/notice',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/notice/list'
        },
        {
          path: 'list',
          name: 'NoticeList',
          component: () => import('@/views/notice/NoticeList.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'publish',
          name: 'NoticePublish',
          component: () => import('@/views/notice/NoticePublish.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'stats',
          name: 'NoticeStats',
          component: () => import('@/views/notice/NoticeStats.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    },
    {
      path: '/statistics',
      component: () => import('@/layouts/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/statistics/dashboard'
        },
        {
          path: 'dashboard',
          name: 'StatisticsDashboard',
          component: () => import('@/views/statistics/Dashboard.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'custom-report',
          name: 'CustomReport',
          component: () => import('@/views/statistics/CustomReport.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        },
        {
          path: 'turnover-analysis',
          name: 'TurnoverAnalysis',
          component: () => import('@/views/statistics/TurnoverAnalysis.vue'),
          meta: { keepAlive: true, requiresAuth: true }
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.state.isAuthenticated) {
    // 需要认证但未登录，跳转到登录页
    next('/login')
  } else if (to.path === '/login' && authStore.state.isAuthenticated) {
    // 已登录但访问登录页，跳转到首页
    next('/')
  } else {
    next()
  }
})

export default router