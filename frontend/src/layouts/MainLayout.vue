<template>
  <div class="layout-container">
    <StarryBackground v-if="settingsStore.theme === 'starry'" />
    <ChangelogDialog ref="changelogDialogRef" />
    <div class="sidebar">
      <div class="sidebar-header">
        <h3>{{ t('app.title') }}</h3>
      </div>
      <div class="sidebar-menu">
        <template v-for="menu in menus" :key="menu.key">
          <div 
            :class="['menu-item', { active: activeMenu === menu.key }]"
            @click="handleMenuClick(menu)"
          >
            <el-icon class="menu-icon">
              <component :is="menu.icon" />
            </el-icon>
            <span class="menu-text">{{ t(menu.nameKey) }}</span>
            <el-icon v-if="menu && menu.children" class="menu-arrow" :class="{ expanded: expandedMenus.includes(menu.key) }">
              <ArrowDown />
            </el-icon>
          </div>
          
          <div v-if="menu && menu.children && expandedMenus.includes(menu.key)">
            <div 
              v-for="child in menu.children" 
              :key="child.key"
              :class="['submenu-item', { active: activeSubmenu === child.key }]"
              @click="handleSubmenuClick(child)"
            >
              <span class="submenu-text">{{ t(child.nameKey) }}</span>
            </div>
          </div>
        </template>
      </div>
    </div>
    
    <div class="main-content">
      <div class="top-bar">
        <div class="top-bar-left">
          <h2 class="page-title">{{ currentPageTitle }}</h2>
        </div>
        <div class="top-bar-right">
          <div class="top-bar-actions">
            <el-tooltip :content="t('settings.theme')" placement="bottom">
              <el-button 
                :icon="themeIcon" 
                circle 
                @click="settingsStore.toggleTheme"
              />
            </el-tooltip>
            
            <el-dropdown @command="handleLanguageChange">
              <el-button circle>
                <el-icon><ChatDotRound /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="zh-CN" :divided="true">
                    {{ t('settings.chinese') }}
                  </el-dropdown-item>
                  <el-dropdown-item command="zh-TW">
                    {{ t('settings.traditional') }}
                  </el-dropdown-item>
                  <el-dropdown-item command="en-US">  
                    {{ t('settings.english') }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          
          <div class="user-info">
            <el-icon class="user-icon"><User /></el-icon>
            <span class="username">{{ authStore.getUsername() }}</span>
          </div>
          <el-button type="primary" link @click="handleLogout" class="logout-button">
            {{ t('auth.logout') }}
          </el-button>
        </div>
      </div>
      
      <div class="content-area">
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Setting, Document, Notebook, Sunny, Moon, ChatDotRound, Timer, ArrowDown, Tickets, Star, Money, TrendCharts, UserFilled, Bell, DataLine } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { useSettingsStore } from '@/stores/settings'
import { useI18n } from '@/utils/i18n'
import ChangelogDialog from '@/components/ChangelogDialog.vue'
import StarryBackground from '@/components/StarryBackground.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const settingsStore = useSettingsStore()
const { t } = useI18n()

const themeIcon = computed(() => {
  if (settingsStore.theme === 'light') return Moon
  if (settingsStore.theme === 'dark') return Star
  return Sunny
})

const changelogDialogRef = ref<InstanceType<typeof ChangelogDialog>>()
const activeMenu = ref('employees')
const activeSubmenu = ref('check-in')
const expandedMenus = ref<string[]>(['attendance', 'recruitment', 'notice', 'statistics'])

const menus = [
  { key: 'employees', nameKey: 'nav.employees', icon: User, route: '/employees' },
  { 
    key: 'attendance', 
    nameKey: 'nav.attendance', 
    icon: Timer, 
    route: '/attendance',
    children: [
      { key: 'check-in', nameKey: 'attendance.checkInRecords', route: '/attendance/check-in' },
      { key: 'leave', nameKey: 'attendance.leaveManagement', route: '/attendance/leave' },
      { key: 'overtime', nameKey: 'attendance.overtimeManagement', route: '/attendance/overtime' },
      { key: 'statistics', nameKey: 'attendance.attendanceStatistics', route: '/attendance/statistics' }
    ]
  },
  { 
    key: 'salary', 
    nameKey: 'nav.salary', 
    icon: Money, 
    route: '/salary',
    children: [
      { key: 'payslips', nameKey: 'salary.payslips', route: '/salary/payslips' },
      { key: 'structure', nameKey: 'salary.salaryStructure', route: '/salary/structure' }
    ]
  },
  { 
    key: 'performance', 
    nameKey: 'nav.performance', 
    icon: TrendCharts, 
    route: '/performance',
    children: [
      { key: 'kpi', nameKey: 'performance.kpiManagement', route: '/performance/kpi' },
      { key: 'cycle', nameKey: 'performance.cycleManagement', route: '/performance/cycle' },
      { key: 'result', nameKey: 'performance.resultView', route: '/performance/result' }
    ]
  },
  { 
    key: 'recruitment', 
    nameKey: 'nav.recruitment', 
    icon: UserFilled, 
    route: '/recruitment',
    children: [
      { key: 'jobs', nameKey: 'recruitment.jobList', route: '/recruitment/jobs' },
      { key: 'resumes', nameKey: 'recruitment.resumeList', route: '/recruitment/resumes' },
      { key: 'interviews', nameKey: 'recruitment.interviewList', route: '/recruitment/interviews' },
      { key: 'offers', nameKey: 'recruitment.offerList', route: '/recruitment/offers' }
    ]
  },
  { 
    key: 'notice', 
    nameKey: 'nav.notice', 
    icon: Bell, 
    route: '/notice',
    children: [
      { key: 'list', nameKey: 'notice.list', route: '/notice/list' },
      { key: 'publish', nameKey: 'notice.publish', route: '/notice/publish' },
      { key: 'stats', nameKey: 'notice.stats', route: '/notice/stats' }
    ]
  },
  { 
    key: 'statistics', 
    nameKey: 'nav.statistics', 
    icon: DataLine, 
    route: '/statistics',
    children: [
      { key: 'dashboard', nameKey: 'statistics.dashboard', route: '/statistics/dashboard' },
      { key: 'customReport', nameKey: 'statistics.customReport', route: '/statistics/custom-report' },
      { key: 'turnoverAnalysis', nameKey: 'statistics.turnoverAnalysis', route: '/statistics/turnover-analysis' }
    ]
  },
  { key: 'logs', nameKey: 'nav.logs', icon: Document, route: '/logs' },
  { key: 'changelog', nameKey: 'nav.changelog', icon: Tickets, route: '/changelog' },
  { key: 'settings', nameKey: 'nav.settings', icon: Setting, route: '/settings' }
]

const currentPageTitle = computed(() => {
  const path = route.path
  if (path.startsWith('/employees')) {
    return t('employees.title')
  } else if (path.startsWith('/attendance')) {
    if (path === '/attendance/check-in') {
      return t('attendance.checkInRecords')
    } else if (path === '/attendance/leave') {
      return t('attendance.leaveManagement')
    } else if (path === '/attendance/overtime') {
      return t('attendance.overtimeManagement')
    } else if (path === '/attendance/statistics') {
      return t('attendance.attendanceStatistics')
    }
    return t('nav.attendance')
  } else if (path.startsWith('/salary')) {
    if (path === '/salary/payslips') {
      return t('salary.payslips')
    } else if (path === '/salary/structure') {
      return t('salary.salaryStructure')
    }
    return t('nav.salary')
  } else if (path.startsWith('/performance')) {
    if (path === '/performance/kpi') {
      return t('performance.kpiManagement')
    } else if (path === '/performance/cycle') {
      return t('performance.cycleManagement')
    } else if (path === '/performance/result') {
      return t('performance.resultView')
    }
    return t('nav.performance')
  } else if (path.startsWith('/logs')) {
    return t('logs.title')
  } else if (path.startsWith('/changelog')) {
    return t('changelog.title')
  } else if (path.startsWith('/settings')) {
    return t('settings.title')
  } else if (path.startsWith('/recruitment')) {
    if (path === '/recruitment/jobs') {
      return t('recruitment.jobList')
    } else if (path === '/recruitment/resumes') {
      return t('recruitment.resumeList')
    } else if (path === '/recruitment/interviews') {
      return t('recruitment.interviewList')
    } else if (path === '/recruitment/offers') {
      return t('recruitment.offerList')
    }
    return t('recruitment.title')
  } else if (path.startsWith('/notice')) {
    if (path === '/notice/list') {
      return t('notice.list')
    } else if (path === '/notice/publish') {
      return t('notice.publish')
    } else if (path === '/notice/stats') {
      return t('notice.stats')
    }
    return t('notice.title')
  } else if (path.startsWith('/statistics')) {
    if (path === '/statistics/dashboard') {
      return t('statistics.dashboard')
    } else if (path === '/statistics/custom-report') {
      return t('statistics.customReport')
    } else if (path === '/statistics/turnover-analysis') {
      return t('statistics.turnoverAnalysis')
    }
    return t('statistics.title')
  }
  return ''
})

const handleMenuClick = (menu: any) => {
  activeMenu.value = menu.key
  
  if (menu.children) {
    const index = expandedMenus.value.indexOf(menu.key)
    if (index > -1) {
      expandedMenus.value.splice(index, 1)
    } else {
      expandedMenus.value.push(menu.key)
      router.push(menu.children[0].route)
    }
  } else {
    router.push(menu.route)
  }
}

const handleSubmenuClick = (child: any) => {
  activeSubmenu.value = child.key
  router.push(child.route)
}

const handleLanguageChange = (lang: string) => {
  settingsStore.setLanguage(lang as any)
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm(t('common.confirm') + '？', t('common.confirm'), {
      confirmButtonText: t('common.confirm'),
      cancelButtonText: t('common.cancel'),
      type: 'warning'
    })
    
    authStore.logout()
    ElMessage.success(t('auth.logout') + t('common.successSuffix'))
    router.push('/login')
  } catch {
  }
}

const setActiveMenuByRoute = () => {
  const currentPath = router.currentRoute.value.path
  
  for (const menu of menus) {
    if (currentPath.startsWith(menu.route)) {
      activeMenu.value = menu.key
      
      if (menu.children) {
        if (!expandedMenus.value.includes(menu.key)) {
          expandedMenus.value.push(menu.key)
        }
        
        let foundSubmenu = false
        for (const child of menu.children) {
          if (currentPath === child.route) {
            activeSubmenu.value = child.key
            foundSubmenu = true
            break
          }
        }
        if (!foundSubmenu) {
          activeSubmenu.value = ''
        }
      } else {
        activeSubmenu.value = ''
      }
      break
    }
  }
}

router.afterEach(() => {
  setActiveMenuByRoute()
})

onMounted(() => {
  setActiveMenuByRoute()
  
  if (router.currentRoute.value.path === '/') {
    router.push('/employees')
  }
  
  const hasSeenWelcome = localStorage.getItem('hasSeenWelcome_v1_0_2')
  if (!hasSeenWelcome) {
    setTimeout(() => {
      changelogDialogRef.value?.open()
      localStorage.setItem('hasSeenWelcome_v1_0_2', 'true')
    }, 500)
  }
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.layout-container {
  display: flex;
  height: 100vh;
  background-color: var(--bg-color);
  transition: background-color 0.3s ease;
}

.sidebar {
  width: $sidebar-width;
  background-color: var(--sidebar-bg);
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease, border-color 0.3s ease;
  position: relative;
  z-index: 10;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid var(--border-light);
  transition: border-color 0.3s ease;
  
  h3 {
    margin: 0;
    color: var(--text-primary);
    font-size: 18px;
    font-weight: 600;
    transition: color 0.3s ease;
  }
}

.sidebar-menu {
  flex: 1;
  padding: 10px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--sidebar-text);
  
  &:hover {
    background-color: var(--sidebar-hover-bg);
    color: var(--sidebar-active-text);
  }
  
  &.active {
    background-color: var(--sidebar-active-bg);
    color: var(--sidebar-active-text);
    border-right: 3px solid $primary-color;
  }
}

.menu-icon {
  margin-right: 10px;
  font-size: 18px;
}

.menu-text {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
}

.menu-arrow {
  font-size: 14px;
  transition: transform 0.3s ease;
  
  &.expanded {
    transform: rotate(180deg);
  }
}

.submenu-item {
  display: flex;
  align-items: center;
  padding: 10px 20px 10px 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--sidebar-text);
  
  &:hover {
    background-color: var(--sidebar-hover-bg);
    color: var(--sidebar-active-text);
  }
  
  &.active {
    background-color: var(--sidebar-active-bg);
    color: var(--sidebar-active-text);
    border-right: 3px solid $primary-color;
  }
}

.submenu-text {
  font-size: 13px;
  font-weight: 500;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
  z-index: 5;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 25px;
  background-color: var(--bg-color-lighter);
  border-bottom: 1px solid var(--border-light);
  transition: background-color 0.3s ease, border-color 0.3s ease;
  
  .top-bar-left {
    .page-title {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: var(--text-primary);
      transition: color 0.3s ease;
    }
  }
  
  .top-bar-right {
    display: flex;
    align-items: center;
    gap: 20px;
    
    .top-bar-actions {
      display: flex;
      gap: 10px;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 16px;
      background-color: var(--bg-color);
      border-radius: 8px;
      transition: background-color 0.3s ease;
      
      .user-icon {
        font-size: 18px;
        color: $primary-color;
      }
      
      .username {
        font-size: 14px;
        font-weight: 500;
        color: var(--text-primary);
        transition: color 0.3s ease;
      }
    }
    
    .logout-button {
      font-size: 14px;
      font-weight: 500;
    }
  }
}

.content-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: var(--bg-color);
  transition: background-color 0.3s ease;
}

@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }
  
  .top-bar {
    padding: 12px 15px;
  }
  
  .content-area {
    padding: 15px;
  }
}
</style>

