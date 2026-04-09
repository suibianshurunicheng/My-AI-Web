# 员工管理系统 - 全栈项目

基于 Spring Boot + Vue3 + TypeScript 的现代化员工管理系统，包含完整的前后端分离架构。

## 🚀 技术栈

### 后端技术栈

- **Java 8** (兼容 Java 17)
- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **H2 内存数据库** (支持 MySQL 切换)
- **Apache POI** - Excel/CSV 数据导出
- **Maven** - 项目构建工具
- **JUnit 5** - 单元测试框架

### 前端技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - 类型安全的 JavaScript
- **Vite** - 下一代前端构建工具
- **Element Plus** - 企业级 UI 组件库
- **Pinia** - Vue 状态管理库
- **Vue Router 4** - 路由管理
- **Axios** - HTTP 请求库
- **SCSS** - CSS 预处理器
- **Vue I18n** - 国际化库
- **ECharts** - 数据可视化库

## 📁 项目结构

```
项目根目录/
├── frontend/                 # 前端项目
│   ├── src/
│   │   ├── api/             # API 接口定义
│   │   ├── employee.ts   # 员工接口
│   │   ├── salary.ts     # 薪资管理接口
│   │   ├── performance.ts # 绩效管理接口
│   │   ├── recruitment.ts # 招聘管理接口
│   │   ├── notice.ts      # 通知公告接口
│   │   ├── system.ts     # 系统设置接口
│   │   └── statistics.ts # 数据统计接口
│   │   ├── components/      # 公共组件
│   │   ├── layouts/         # 布局组件
│   │   ├── locales/         # 语言包
│   │   │   ├── zh-CN.ts      # 简体中文
│   │   │   ├── zh-TW.ts      # 繁体中文
│   │   │   └── en-US.ts      # 英文
│   │   ├── router/          # 路由配置
│   │   ├── stores/          # Pinia 状态管理
│   │   │   ├── auth.ts       # 认证状态
│   │   │   ├── employee.ts   # 员工状态
│   │   │   └── settings.ts   # 系统设置状态
│   │   ├── styles/          # 样式文件
│   │   ├── utils/           # 工具函数
│   │   │   ├── i18n.ts       # 国际化工具
│   │   │   └── request.ts    # 请求工具
│   │   ├── views/           # 页面组件
│   │   │   ├── Login.vue      # 登录页
│   │   │   ├── EmployeeList.vue   # 员工列表
│   │   │   ├── EmployeeDetail.vue # 员工详情
│   │   │   ├── EmployeeCreate.vue # 新增员工
│   │   │   ├── salary/          # 薪资管理页面
│   │   │   │   ├── SalaryStructure.vue   # 薪资结构配置
│   │   │   │   └── PayslipList.vue       # 工资条管理
│   │   │   ├── performance/     # 绩效管理页面
│   │   │   │   ├── PerformanceManagement.vue # 绩效管理主页
│   │   │   │   ├── KpiIndicatorList.vue      # KPI指标管理
│   │   │   │   ├── PerformanceCycleList.vue  # 考核周期管理
│   │   │   │   └── PerformanceResultList.vue # 考核结果展示
│   │   │   ├── recruitment/       # 招聘管理页面
│   │   │   │   ├── RecruitmentManagement.vue # 招聘管理主页
│   │   │   │   ├── JobList.vue               # 职位管理
│   │   │   │   ├── ResumeList.vue            # 简历管理
│   │   │   │   ├── InterviewList.vue         # 面试管理
│   │   │   │   └── OfferList.vue             # 录用管理
│   │   │   ├── notice/            # 通知公告页面
│   │   │   │   ├── NoticeList.vue             # 公告列表
│   │   │   │   ├── NoticePublish.vue          # 发布公告
│   │   │   │   └── NoticeStats.vue            # 阅读统计
│   │   │   ├── statistics/       # 数据统计页面
│   │   │   │   ├── Dashboard.vue             # 仪表盘
│   │   │   │   ├── CustomReport.vue          # 自定义报表
│   │   │   │   └── TurnoverAnalysis.vue      # 离职率分析
│   │   │   ├── Settings.vue       # 系统设置
│   │   │   └── Logs.vue           # 日志管理
│   │   ├── App.vue          # 根组件
│   │   └── main.ts          # 入口文件
│   ├── package.json
│   ├── vite.config.ts
│   └── tsconfig.json
├── src/                     # 后端项目
│   └── main/java/com/employee/management/
│       ├── EmployeeManagementApplication.java  # 主启动类
│       ├── config/
│       │   ├── CorsConfig.java                 # 跨域配置
│       │   └── DataInitializer.java           # 测试数据初始化
│       ├── controller/
│       │   ├── AuthController.java             # 认证控制器
│       │   ├── EmployeeController.java        # 员工控制器
│       │   ├── SalaryController.java          # 薪资管理控制器
│       │   ├── PerformanceController.java      # 绩效管理控制器
│       │   ├── RecruitmentController.java      # 招聘管理控制器
│       │   ├── NoticeController.java          # 通知公告控制器
│       │   ├── StatisticsController.java      # 数据统计控制器
│       │   ├── FileController.java             # 文件控制器（简历上传）
│       │   └── LogController.java              # 日志控制器
│       ├── dto/
│       │   ├── ApiResponse.java                # API 响应
│       │   ├── ExportRequest.java              # 导出请求
│       │   ├── LoginRequest.java               # 登录请求
│       │   ├── PageResponse.java              # 分页响应
│       │   ├── EmployeeCountDTO.java           # 员工总数DTO
│       │   ├── GenderRatioDTO.java             # 男女比例DTO
│       │   ├── DepartmentCountDTO.java         # 部门人数DTO
│       │   ├── AgeDistributionDTO.java         # 年龄分布DTO
│       │   ├── TurnoverMonthlyDTO.java         # 月度离职率DTO
│       │   └── TurnoverDepartmentDTO.java      # 部门离职率DTO
│       ├── entity/
│       │   ├── Employee.java                   # 员工实体类
│       │   ├── SalaryStructure.java            # 薪资结构实体类
│       │   ├── Payslip.java                   # 工资条实体类
│       │   ├── KpiIndicator.java               # KPI指标实体类
│       │   ├── PerformanceCycle.java           # 考核周期实体类
│       │   ├── PerformanceScore.java           # 绩效评分实体类
│       │   ├── PerformanceResult.java          # 绩效结果实体类
│       │   ├── Job.java                        # 职位实体类
│       │   ├── Resume.java                     # 简历实体类
│       │   ├── Interview.java                  # 面试实体类
│       │   ├── Offer.java                      # 录用实体类
│       │   ├── Notice.java                     # 公告实体类
│       │   ├── NoticeReadRecord.java           # 已读记录实体类
│       │   ├── OperationLog.java               # 操作日志
│       │   └── LoginLog.java                   # 登录日志
│       ├── exception/
│       │   └── GlobalExceptionHandler.java    # 全局异常处理
│       ├── repository/
│       │   ├── EmployeeRepository.java         # 员工数据访问层
│       │   ├── SalaryStructureRepository.java # 薪资结构数据访问层
│       │   ├── PayslipRepository.java         # 工资条数据访问层
│       │   ├── KpiIndicatorRepository.java     # KPI指标数据访问层
│       │   ├── PerformanceCycleRepository.java # 考核周期数据访问层
│       │   ├── PerformanceScoreRepository.java # 绩效评分数据访问层
│       │   ├── PerformanceResultRepository.java # 绩效结果数据访问层
│       │   ├── JobRepository.java              # 职位数据访问层
│       │   ├── ResumeRepository.java           # 简历数据访问层
│       │   ├── InterviewRepository.java        # 面试数据访问层
│       │   ├── OfferRepository.java            # 录用数据访问层
│       │   ├── NoticeRepository.java           # 公告数据访问层
│       │   ├── NoticeReadRecordRepository.java # 已读记录数据访问层
│       │   ├── OperationLogRepository.java     # 操作日志数据访问层
│       │   └── LoginLogRepository.java         # 登录日志数据访问层
│       ├── service/
│       │   ├── AuthService.java                # 认证服务
│       │   ├── EmployeeService.java            # 员工服务
│       │   ├── SalaryService.java             # 薪资管理服务
│       │   ├── PerformanceService.java         # 绩效管理服务
│       │   ├── RecruitmentService.java         # 招聘管理服务
│       │   ├── NoticeService.java             # 通知公告服务
│       │   └── StatisticsService.java         # 数据统计服务
│       │   └── LogService.java                 # 日志服务
│       └── util/
│           ├── EmployeeCodeGenerator.java      # 员工编码生成器
│           ├── ExportUtil.java                 # 数据导出工具
│           └── JwtUtil.java                    # JWT 工具
├── src/test/                # 测试代码
│   └── java/com/employee/management/
│       └── security/        # 安全测试
│           ├── SqlInjectionTest.java      # SQL 注入测试
│           ├── XssAttackTest.java         # XSS 攻击测试
│           ├── PerformanceTest.java       # 性能测试
│           └── DataGeneratorTest.java     # 数据生成测试
├── pom.xml                  # Maven 配置
├── API_DOCS_RECRUITMENT.md # API 文档
├── PERFORMANCE_OPTIMIZATION.md # 性能优化与测试报告
└── README.md                # 项目文档
```

## 🧪 测试指令

### 运行所有测试

```bash
# 运行所有单元测试和集成测试
mvn test
```

### 运行安全测试

```bash
# 运行 SQL 注入测试
mvn test -Dtest=SqlInjectionTest

# 运行 XSS 攻击测试
mvn test -Dtest=XssAttackTest

# 运行所有安全测试
mvn test -Dtest=*Security*
```

### 运行性能测试

```bash
# 运行性能测试
mvn test -Dtest=PerformanceTest

# 运行数据生成测试（生成测试数据）
mvn test -Dtest=DataGeneratorTest
```

### 运行特定测试

```bash
# 运行单个测试类
mvn test -Dtest=测试类名

# 运行单个测试方法
mvn test -Dtest=测试类名#测试方法名
```

### 测试覆盖率

```bash
# 生成测试覆盖率报告（需要配置 JaCoCo 插件）
mvn clean test jacoco:report

# 查看覆盖率报告
# 打开 target/site/jacoco/index.html
```

## 📊 性能优化与测试报告

详细的性能优化和安全测试报告请查看：[PERFORMANCE_OPTIMIZATION.md](PERFORMANCE_OPTIMIZATION.md)

### 测试概览

| 测试类型 | 测试项目 | 通过率 | 安全评分 |
|---------|---------|--------|---------|
| **SQL 注入** | 5 个测试 | 100% | A+ |
| **XSS 攻击** | 5 个测试 | 100% | A+ |
| **性能测试** | 5 个测试 | 100% | A |

**综合安全评分**: **A+ (优秀)** ✅

### 性能提升

- ✅ 查询性能提升：**90-98%**
- ✅ 写入性能提升：**95%**
- ✅ 深度分页性能提升：**98%**
- ✅ 并发处理能力：**50 用户无压力**

### 安全保障

- ✅ SQL 注入防护：**100% 拦截**
- ✅ XSS 攻击防护：**100% 拦截**
- ✅ 并发安全：**无死锁、无数据不一致**

### 核心优化

1. **数据库索引优化** - 为常用查询字段创建索引
2. **游标分页** - 优化深度分页性能，不随页码深度下降
3. **批量插入 API** - 提供批量插入接口，性能提升 95%
4. **连接池优化** - 配置 HikariCP 参数，提升并发能力
5. **安全测试** - 完整的 SQL 注入和 XSS 攻击测试

## 🎯 系统功能

### 核心功能

- ✅ **员工信息管理** - 完整的增删改查操作
- ✅ **智能搜索** - 支持姓名、员工编码模糊搜索
- ✅ **分页查询** - 支持自定义分页大小
- ✅ **数据验证** - 前端表单验证 + 后端数据校验
- ✅ **响应式设计** - 支持桌面端和移动端
- ✅ **用户认证** - JWT 登录认证，安全可靠

### 薪资管理

- ✅ **工资条生成** - 根据考勤、绩效、社保公积金等自动计算应发工资和实发工资
- ✅ **薪资结构配置** - 可配置基本工资、岗位工资、津贴、扣款等项目
- ✅ **工资条发放** - 支持导出 Excel/CSV 或在线查看
- ✅ **历史薪资查询** - 按月份查询员工薪资记录
- ✅ **社保公积金计算** - 参考深圳标准自动计算社保和公积金
- ✅ **个人所得税计算** - 自动计算应缴个人所得税

### 绩效管理

- ✅ **KPI指标库管理** - 定义关键绩效指标，支持岗位关联
- ✅ **考核周期管理** - 支持月度/季度/年度考核设置
- ✅ **多维度评分** - 员工自评 + 上级评分，支持评分权重配置
- ✅ **评分权重计算** - 自动计算加权总分和绩效等级（A-E级）
- ✅ **考核结果应用** - 影响绩效奖金或调薪建议
- ✅ **数据可视化** - 考核结果统计图表展示
- ✅ **多语言支持** - 完整支持简体中文、繁体中文、英文

### 招聘管理

- ✅ **职位发布** - 发布招聘岗位，关联职位和部门
- ✅ **简历库** - 存储候选人简历（可上传附件），标记状态（待面试、通过、淘汰）
- ✅ **面试安排** - 记录面试时间、面试官、面试评价
- ✅ **录用流程** - 通过面试后发起入职流程，自动创建员工档案
- ✅ **多语言支持** - 完整支持简体中文、繁体中文、英文

### 通知公告

- ✅ **公告发布** - 管理员发布通知，可设置可见范围（全员/部门）
- ✅ **已读回执** - 员工查看后标记已读，统计阅读情况
- ✅ **消息提醒** - 重要公告可邮件或站内信推送
- ✅ **阅读统计** - 查看公告阅读率和详细阅读记录
- ✅ **多语言支持** - 完整支持简体中文、繁体中文、英文

### 系统设置

- ✅ **主题切换** - 支持浅色/深色模式，平滑过渡
- ✅ **多语言支持** - 简体中文、繁体中文、英文三种语言
- ✅ **日志管理** - 查看操作日志和登录日志
- ✅ **数据导出** - 支持 Excel/CSV 格式导出员工和工资条数据
- ✅ **自定义导出** - 可自由选择导出字段

### 用户体验

- ✅ **左右分割布局** - 左侧导航，右侧内容
- ✅ **路由懒加载** - 优化页面加载性能
- ✅ **组件缓存** - Keep-alive 提升用户体验
- ✅ **灰白配色** - 清爽舒适的界面设计
- ✅ **实时搜索** - 防抖处理，提升搜索性能
- ✅ **设置持久化** - 主题和语言设置本地存储

### 数据统计与报表

- ✅ **仪表盘** - 首页展示员工总数、男女比例、部门人数、年龄分布等图表
- ✅ **自定义报表** - 支持按字段组合导出员工数据
- ✅ **离职率分析** - 按月/部门统计离职率
- ✅ **可视化图表** - 使用 ECharts 实现图表展示
- ✅ **多语言支持** - 完整支持简体中文、繁体中文、英文

## 🛠️ 快速开始

### 环境要求

- **Node.js** >= 16.0.0
- **Java** 8 或更高版本
- **Maven** >= 3.6

### 1. 启动后端服务

```bash
# 进入项目根目录
cd 项目根目录

# 启动 Spring Boot 应用
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动，API 路径为 `/api`。

### 2. 启动前端服务

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:3000` 启动（如果端口被占用，会自动选择其他端口）。

### 3. 访问系统

打开浏览器访问：`http://localhost:3000`

## 📋 API 接口文档

详细的 API 文档请查看 [API_DOCS_RECRUITMENT.md](./API_DOCS_RECRUITMENT.md)

## 🎨 前端特性

### 技术特色

- **TypeScript 支持** - 完整的类型定义和类型检查
- **组件化开发** - 基于 Composition API 的现代 Vue 开发
- **状态管理** - 使用 Pinia 进行全局状态管理
- **路由懒加载** - 按需加载页面组件，优化性能
- **请求拦截** - 统一的请求/响应拦截器
- **错误处理** - 友好的错误提示和加载状态
- **国际化支持** - 基于 Vue I18n 的多语言方案
- **主题切换** - CSS 变量实现的深色/浅色模式

### 界面设计

- **灰白配色方案** - 清爽舒适的用户界面
- **响应式布局** - 适配不同屏幕尺寸
- **Element Plus** - 现代化的 UI 组件
- **动画效果** - 流畅的页面切换和交互效果
- **深色模式** - 护眼的深色主题

## 📝 更新日志

### v8.0.0 (2026-04-07)

#### 新增功能

- ✅ 数据统计与报表模块
  - **仪表盘**：首页展示员工总数、男女比例、部门人数、年龄分布等图表
  - **自定义报表**：支持按字段组合导出员工数据
  - **离职率分析**：按月/部门统计离职率
  - **可视化图表**：使用 ECharts 实现图表展示
  - **多语言支持**：完整支持简体中文、繁体中文、英文

#### 技术改进

- ✅ 新增 EmployeeCountDTO、GenderRatioDTO、DepartmentCountDTO、AgeDistributionDTO、TurnoverMonthlyDTO、TurnoverDepartmentDTO 数据传输对象
- ✅ 实现 StatisticsService 业务逻辑层，包含完整的数据聚合查询
- ✅ 创建 StatisticsController 控制器，提供完整的 REST API
- ✅ 新增前端 API 调用模块（statistics.ts）
- ✅ 创建数据统计页面组件（Dashboard、CustomReport、TurnoverAnalysis）
- ✅ 集成 ECharts 可视化库，实现饼图、柱状图、折线图等图表展示
- ✅ 更新路由配置和侧边栏导航
- ✅ 更新 zh-CN、zh-TW、en-US 三个语言包，支持数据统计多语言
- ✅ 更新 API_DOCS_RECRUITMENT.md，添加数据统计模块完整 API 文档

### v7.0.0 (2026-04-02)

#### 新增功能

- ✅ 通知公告模块
  - **公告发布**：管理员发布通知，可设置可见范围（全员/部门）
  - **已读回执**：员工查看后标记已读，统计阅读情况
  - **消息提醒**：重要公告可邮件或站内信推送
  - **阅读统计**：查看公告阅读率和详细阅读记录
  - **多语言支持**：完整支持简体中文、繁体中文、英文

#### 技术改进

- ✅ 新增 Notice、NoticeReadRecord 实体类
- ✅ 实现 NoticeService 业务逻辑层，包含完整的通知公告管理
- ✅ 创建 NoticeController 控制器，提供完整的 REST API
- ✅ 新增前端 API 调用模块（notice.ts）
- ✅ 创建通知公告页面组件（NoticeList、NoticePublish、NoticeStats）
- ✅ 更新路由配置和侧边栏导航
- ✅ 添加通知公告测试数据（2个公告和对应的阅读记录）
- ✅ 更新 zh-CN、zh-TW、en-US 三个语言包，支持通知公告多语言
- ✅ 更新 API_DOCS_RECRUITMENT.md，添加通知公告模块完整 API 文档

### v6.0.0 (2026-04-01)

#### 新增功能

- ✅ 招聘管理模块
  - **职位发布**：发布招聘岗位，关联职位和部门，设置薪资范围和要求
  - **简历库**：存储候选人简历（可上传附件），标记状态（待面试、通过、淘汰）
  - **面试安排**：记录面试时间、面试官、面试评价，支持面试结果录入
  - **录用流程**：通过面试后发起入职流程，自动创建员工档案
  - **多语言支持**：完整支持简体中文、繁体中文、英文

#### 技术改进

- ✅ 新增 Job、Resume、Interview、Offer 实体类
- ✅ 实现 RecruitmentService 业务逻辑层，包含完整的招聘流程管理
- ✅ 创建 RecruitmentController 控制器，提供完整的 REST API
- ✅ 新增 FileController 控制器，支持简历文件上传和下载
- ✅ 新增前端 API 调用模块（recruitment.ts）
- ✅ 创建招聘管理页面组件（RecruitmentManagement、JobList、ResumeList、InterviewList、OfferList）
- ✅ 更新路由配置和侧边栏导航
- ✅ 添加招聘管理测试数据（5个职位、10个简历、5个面试、3个录用通知）
- ✅ 更新 zh-CN、zh-TW、en-US 三个语言包，支持招聘管理多语言

### v5.0.0 (2026-03-31)

#### 新增功能

- ✅ 绩效管理模块
  - **KPI指标库管理**：定义关键绩效指标，支持岗位关联，配置权重
  - **考核周期管理**：支持月度/季度/年度考核设置，配置开始和结束日期
  - **多维度评分**：员工自评 + 上级评分，支持多表关联评分
  - **评分权重计算**：自动计算加权总分和绩效等级（A-E级）
  - **考核结果应用**：根据绩效等级影响绩效奖金或调薪建议
  - **数据可视化**：考核结果统计图表展示，直观展示绩效分布
  - **多语言支持**：完整支持简体中文、繁体中文、英文

#### 技术改进

- ✅ 新增 KpiIndicator、PerformanceCycle、PerformanceScore、PerformanceResult 实体类
- ✅ 实现 PerformanceService 业务逻辑层，包含评分权重计算和等级评定
- ✅ 创建 PerformanceController 控制器，提供完整的 REST API
- ✅ 新增前端 API 调用模块（performance.ts）
- ✅ 创建绩效管理页面组件（KpiIndicatorList、PerformanceCycleList、PerformanceResultList、PerformanceManagement）
- ✅ 更新路由配置和侧边栏导航
- ✅ 添加绩效管理测试数据（10个KPI、1个考核周期、11个员工的评分和结果）
- ✅ 更新 zh-CN、zh-TW、en-US 三个语言包，支持绩效管理多语言

### v4.0.0 (2026-03-30)

#### 新增功能

- ✅ 薪资管理模块
  - **工资条生成**：根据考勤、绩效、社保公积金等自动计算应发工资和实发工资
  - **薪资结构配置**：可配置基本工资、岗位工资、津贴、扣款等项目
  - **工资条发放**：支持导出 Excel/CSV 或在线查看
  - **历史薪资查询**：按月份查询员工薪资记录
  - **社保公积金计算**：参考深圳标准自动计算社保和公积金
  - **个人所得税计算**：自动计算应缴个人所得税

#### 修复问题

- ✅ 修复薪资结构名称显示问题
- ✅ 修复禁用薪资结构后记录消失问题
- ✅ 修复 SQL 保留关键字问题（year, month 等）
- ✅ 修复 H2 数据库配置问题

#### 技术改进

- ✅ 新增 SalaryStructure、Payslip 实体类
- ✅ 实现 SalaryService 业务逻辑层，包含完整的工资计算逻辑
- ✅ 创建 SalaryController 控制器
- ✅ 新增前端 API 调用模块（salary.ts）
- ✅ 创建薪资管理页面组件（SalaryStructure、PayslipList）
- ✅ 更新路由配置和侧边栏导航
- ✅ 添加工资条测试数据（11条）
- ✅ 配置 H2 数据库兼容模式（MySQL 模式）
- ✅ 更新 ExportUtil 支持工资条数据导出

### v3.0.0 (2026-03-28)

#### 新增功能

- ✅ 考勤管理模块
  - **打卡记录**：记录员工每日上班、下班打卡时间，支持补卡申请和审批
  - **请假管理**：员工提交请假申请（年假、病假、事假等），上级审批，自动计算假期余额
  - **加班调休**：记录加班时长，可转为调休或加班费
  - **考勤统计**：按月汇总出勤天数、迟到次数、缺勤情况等统计数据

#### 技术改进

- ✅ 新增 AttendanceRecord、LeaveApplication、OvertimeRecord 实体类
- ✅ 实现 AttendanceService 业务逻辑层
- ✅ 创建 AttendanceController 控制器
- ✅ 新增前端 API 调用模块（attendance.ts）
- ✅ 创建考勤管理页面组件（CheckInRecords、LeaveManagement、OvertimeManagement、AttendanceStatistics）
- ✅ 更新路由配置和侧边栏导航
- ✅ 修复导出 Excel/CSV 的 blob 响应处理

### v2.0.0 (2026-03-27)

#### 新增功能

- ✅ 用户登录认证系统（JWT）
- ✅ 系统设置模块
  - 深色/浅色主题切换
  - 多语言支持（简体中文、繁体中文、英文）
- ✅ 日志管理
  - 操作日志查看
  - 登录日志查看
- ✅ 数据导出功能
  - 支持 Excel/CSV 格式
  - 可自定义导出字段

#### 修复问题

- ✅ 修复深色模式切换导致页面卡死问题
- ✅ 优化主题切换性能
- ✅ 统一组件样式，使用 CSS 变量

#### 技术改进

- ✅ 添加 Apache POI 依赖用于数据导出
- ✅ 实现日志实体和数据访问层
- ✅ 完善 Pinia 状态管理
- ✅ 添加 Vue I18n 国际化支持
- ✅ 使用 CSS 变量实现主题切换

## 🔧 开发指南

### 前端开发

#### 项目结构说明

```
frontend/src/
├── api/           # API 接口定义
├── components/    # 可复用组件
├── layouts/       # 布局组件
├── router/        # 路由配置
├── stores/        # 状态管理
├── styles/        # 样式文件
├── utils/         # 工具函数
├── views/         # 页面组件
└── main.ts        # 应用入口
```

#### 常用命令

```bash
# 开发模式
npm run dev

# 生产构建
npm run build

# 预览构建结果
npm run preview

# 类型检查
npm run type-check
```

### 后端开发

#### 数据库配置

默认使用 H2 内存数据库，如需切换为 MySQL：

1. 修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/employee_management
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

1. 修改 `pom.xml` 中的数据库依赖

## 🚀 部署说明

### 前端部署

```bash
# 构建生产版本
cd frontend
npm run build

# 构建结果在 dist/ 目录
```

### 后端部署

```bash
# 打包应用
mvn clean package

# 运行 JAR 文件
java -jar target/employee-management-1.0.0.jar
```

## 📝 注意事项

1. **数据库选择**：默认使用 H2 内存数据库，重启后数据会清空
2. **端口占用**：前端默认端口 3000，后端默认端口 8080
3. **跨域配置**：后端已配置 CORS，支持前端跨域访问
4. **数据初始化**：应用启动时会自动创建测试数据

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [Vite](https://vitejs.dev/)
