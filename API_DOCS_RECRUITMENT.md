# 员工管理系统 API 文档

## 1. 认证模块

### 1.1 登录
- **接口**: `POST /api/auth/login`
- **参数**:
  ```json
  {
    "username": "admin",
    "password": "123456"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "username": "admin",
      "role": "admin"
    }
  }
  ```

### 1.2 验证 Token
- **接口**: `GET /api/auth/verify`
- **请求头**: `Authorization: Bearer {token}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "Token 有效",
    "data": {
      "username": "admin",
      "valid": true
    }
  }
  ```

### 1.3 登出
- **接口**: `POST /api/auth/logout`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "登出成功",
    "data": null
  }
  ```

## 2. 员工管理

### 2.1 获取员工列表
- **接口**: `GET /api/employees`
- **参数**:
  - `name`: 员工姓名模糊搜索（可选）
  - `code`: 员工编码模糊搜索（可选）
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "name": "张三",
          "employeeCode": "EMP001",
          "gender": "男",
          "age": 28,
          "phone": "13812345678",
          "email": "zhangsan@example.com",
          "departmentId": 1,
          "departmentName": "技术部",
          "positionId": 1,
          "positionName": "前端开发工程师",
          "status": "ACTIVE"
        }
      ]
    }
  }
  ```

### 2.2 获取员工详情
- **接口**: `GET /api/employees/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "张三",
      "employeeCode": "EMP001",
      "gender": "男",
      "age": 28,
      "birthDate": "1995-03-12",
      "phone": "13812345678",
      "email": "zhangsan@example.com",
      "departmentId": 1,
      "departmentName": "技术部",
      "positionId": 1,
      "positionName": "前端开发工程师",
      "status": "ACTIVE"
    }
  }
  ```

### 2.3 创建员工
- **接口**: `POST /api/employees`
- **参数**:
  ```json
  {
    "name": "张三",
    "gender": "男",
    "age": 28,
    "birthDate": "1995-03-12",
    "phone": "13812345678",
    "email": "zhangsan@example.com",
    "departmentId": 1,
    "positionId": 1
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "张三",
      "employeeCode": "EMP001",
      "gender": "男",
      "age": 28,
      "birthDate": "1995-03-12",
      "phone": "13812345678",
      "email": "zhangsan@example.com",
      "departmentId": 1,
      "departmentName": "技术部",
      "positionId": 1,
      "positionName": "前端开发工程师",
      "status": "ACTIVE"
    }
  }
  ```

### 2.4 更新员工
- **接口**: `PUT /api/employees/{id}`
- **参数**:
  ```json
  {
    "name": "张三",
    "gender": "男",
    "age": 29,
    "phone": "13912345678",
    "email": "zhangsan_new@example.com",
    "departmentId": 1,
    "positionId": 2
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "张三",
      "employeeCode": "EMP001",
      "gender": "男",
      "age": 29,
      "phone": "13912345678",
      "email": "zhangsan_new@example.com",
      "departmentId": 1,
      "departmentName": "技术部",
      "positionId": 2,
      "positionName": "高级前端开发工程师",
      "status": "ACTIVE"
    }
  }
  ```

### 2.5 删除员工
- **接口**: `DELETE /api/employees/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "员工已成功删除",
    "data": null
  }
  ```

### 2.6 导出员工数据
- **接口**: `POST /api/employees/export`
- **参数**:
  ```json
  {
    "format": "excel",
    "fields": ["name", "gender", "age", "position", "email", "phone", "employeeCode"],
    "filters": {
      "name": "张三",
      "code": "EMP001"
    }
  }
  ```
- **返回**: Excel/CSV 文件下载

## 3. 薪资管理

### 3.1 获取薪资结构列表
- **接口**: `GET /api/salary/structures`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "基本工资",
        "type": "ALLOWANCE",
        "amount": 8000,
        "isActive": true
      }
    ]
  }
  ```

### 3.2 创建/更新薪资结构
- **接口**: `POST /api/salary/structures`
- **参数**:
  ```json
  {
    "name": "基本工资",
    "type": "ALLOWANCE",
    "amount": 8000,
    "isActive": true
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "基本工资",
      "type": "ALLOWANCE",
      "amount": 8000,
      "isActive": true
    }
  }
  ```

### 3.3 删除薪资结构
- **接口**: `DELETE /api/salary/structures/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 3.4 获取工资条列表
- **接口**: `GET /api/salary/payslips`
- **参数**:
  - `employeeId`: 员工ID（可选）
  - `year`: 年份（可选）
  - `monthNum`: 月份（可选）
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "employeeId": 1,
          "employeeName": "张三",
          "year": 2026,
          "month": 4,
          "basicSalary": 8000,
          "totalAllowance": 2000,
          "totalDeduction": 1000,
          "grossSalary": 10000,
          "netSalary": 9000,
          "status": "GENERATED"
        }
      ]
    }
  }
  ```

### 3.5 获取工资条详情
- **接口**: `GET /api/salary/payslips/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "year": 2026,
      "month": 4,
      "basicSalary": 8000,
      "totalAllowance": 2000,
      "totalDeduction": 1000,
      "grossSalary": 10000,
      "netSalary": 9000,
      "status": "GENERATED"
    }
  }
  ```

### 3.6 生成工资条
- **接口**: `POST /api/salary/payslips/generate`
- **参数**:
  ```json
  {
    "year": 2026,
    "monthNum": 4,
    "employeeIds": [1, 2, 3]
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "employeeId": 1,
        "employeeName": "张三",
        "year": 2026,
        "month": 4,
        "grossSalary": 10000,
        "netSalary": 9000
      }
    ]
  }
  ```

### 3.7 导出工资条
- **接口**: `POST /api/salary/payslips/export`
- **参数**:
  ```json
  {
    "format": "excel",
    "fields": ["employeeName", "year", "month", "grossSalary", "netSalary"],
    "filters": {
      "year": 2026,
      "monthNum": 4
    }
  }
  ```
- **返回**: Excel/CSV 文件下载

## 4. 绩效管理

### 4.1 获取 KPI 指标列表
- **接口**: `GET /api/performance/kpi-indicators`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "代码质量",
        "description": "代码质量评分",
        "weight": 0.3,
        "positionId": 1,
        "positionName": "前端开发工程师"
      }
    ]
  }
  ```

### 4.2 获取 KPI 指标详情
- **接口**: `GET /api/performance/kpi-indicators/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "代码质量",
      "description": "代码质量评分",
      "weight": 0.3,
      "positionId": 1,
      "positionName": "前端开发工程师"
    }
  }
  ```

### 4.3 创建 KPI 指标
- **接口**: `POST /api/performance/kpi-indicators`
- **参数**:
  ```json
  {
    "name": "代码质量",
    "description": "代码质量评分",
    "weight": 0.3,
    "positionId": 1
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "代码质量",
      "description": "代码质量评分",
      "weight": 0.3,
      "positionId": 1,
      "positionName": "前端开发工程师"
    }
  }
  ```

### 4.4 更新 KPI 指标
- **接口**: `PUT /api/performance/kpi-indicators/{id}`
- **参数**:
  ```json
  {
    "name": "代码质量",
    "description": "代码质量评分",
    "weight": 0.4,
    "positionId": 1
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "代码质量",
      "description": "代码质量评分",
      "weight": 0.4,
      "positionId": 1,
      "positionName": "前端开发工程师"
    }
  }
  ```

### 4.5 删除 KPI 指标
- **接口**: `DELETE /api/performance/kpi-indicators/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 4.6 获取考核周期列表
- **接口**: `GET /api/performance/cycles`
- **参数**:
  - `cycleType`: 周期类型（可选，MONTHLY/QUARTERLY/YEARLY）
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 1,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "name": "2026年Q2",
          "cycleType": "QUARTERLY",
          "startDate": "2026-04-01",
          "endDate": "2026-06-30",
          "status": "ACTIVE"
        }
      ]
    }
  }
  ```

### 4.7 获取考核周期详情
- **接口**: `GET /api/performance/cycles/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "2026年Q2",
      "cycleType": "QUARTERLY",
      "startDate": "2026-04-01",
      "endDate": "2026-06-30",
      "status": "ACTIVE"
    }
  }
  ```

### 4.8 创建考核周期
- **接口**: `POST /api/performance/cycles`
- **参数**:
  ```json
  {
    "name": "2026年Q2",
    "cycleType": "QUARTERLY",
    "startDate": "2026-04-01",
    "endDate": "2026-06-30"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "2026年Q2",
      "cycleType": "QUARTERLY",
      "startDate": "2026-04-01",
      "endDate": "2026-06-30",
      "status": "ACTIVE"
    }
  }
  ```

### 4.9 更新考核周期
- **接口**: `PUT /api/performance/cycles/{id}`
- **参数**:
  ```json
  {
    "name": "2026年Q2",
    "cycleType": "QUARTERLY",
    "startDate": "2026-04-01",
    "endDate": "2026-06-30",
    "status": "COMPLETED"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "2026年Q2",
      "cycleType": "QUARTERLY",
      "startDate": "2026-04-01",
      "endDate": "2026-06-30",
      "status": "COMPLETED"
    }
  }
  ```

### 4.10 删除考核周期
- **接口**: `DELETE /api/performance/cycles/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 4.11 获取评分列表（按周期）
- **接口**: `GET /api/performance/scores/cycle/{cycleId}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "cycleId": 1,
        "employeeId": 1,
        "employeeName": "张三",
        "kpiIndicatorId": 1,
        "kpiIndicatorName": "代码质量",
        "selfScore": 90,
        "managerScore": 85,
        "finalScore": 86.5
      }
    ]
  }
  ```

### 4.12 获取评分列表（按周期和员工）
- **接口**: `GET /api/performance/scores/cycle/{cycleId}/employee/{employeeId}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "cycleId": 1,
        "employeeId": 1,
        "employeeName": "张三",
        "kpiIndicatorId": 1,
        "kpiIndicatorName": "代码质量",
        "selfScore": 90,
        "managerScore": 85,
        "finalScore": 86.5
      }
    ]
  }
  ```

### 4.13 创建或更新评分
- **接口**: `POST /api/performance/scores`
- **参数**:
  ```json
  {
    "cycleId": 1,
    "employeeId": 1,
    "kpiIndicatorId": 1,
    "selfScore": 90,
    "managerScore": 85
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "cycleId": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "kpiIndicatorId": 1,
      "kpiIndicatorName": "代码质量",
      "selfScore": 90,
      "managerScore": 85,
      "finalScore": 86.5
    }
  }
  ```

### 4.14 获取考核结果列表
- **接口**: `GET /api/performance/results/cycle/{cycleId}`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 1,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "cycleId": 1,
          "employeeId": 1,
          "employeeName": "张三",
          "totalScore": 88,
          "grade": "A",
          "bonusSuggestion": 1000,
          "salaryAdjustmentSuggestion": 500
        }
      ]
    }
  }
  ```

### 4.15 获取考核结果详情
- **接口**: `GET /api/performance/results/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "cycleId": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "totalScore": 88,
      "grade": "A",
      "bonusSuggestion": 1000,
      "salaryAdjustmentSuggestion": 500
    }
  }
  ```

### 4.16 创建或更新考核结果
- **接口**: `POST /api/performance/results`
- **参数**:
  ```json
  {
    "cycleId": 1,
    "employeeId": 1,
    "totalScore": 88,
    "grade": "A",
    "bonusSuggestion": 1000,
    "salaryAdjustmentSuggestion": 500
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "cycleId": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "totalScore": 88,
      "grade": "A",
      "bonusSuggestion": 1000,
      "salaryAdjustmentSuggestion": 500
    }
  }
  ```

### 4.17 计算考核结果
- **接口**: `POST /api/performance/cycles/{cycleId}/calculate`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

## 5. 考勤管理

### 5.1 获取打卡记录
- **接口**: `GET /api/attendance/records`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取打卡记录成功",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "employeeId": 1,
          "employeeName": "张三",
          "date": "2026-04-01",
          "checkInTime": "2026-04-01T09:00:00",
          "checkOutTime": "2026-04-01T18:00:00",
          "status": "NORMAL"
        }
      ]
    }
  }
  ```

### 5.2 上班打卡
- **接口**: `POST /api/attendance/check-in`
- **参数**:
  ```json
  {
    "employeeId": 1,
    "employeeName": "张三"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "上班打卡成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "date": "2026-04-01",
      "checkInTime": "2026-04-01T09:00:00",
      "status": "NORMAL"
    }
  }
  ```

### 5.3 下班打卡
- **接口**: `POST /api/attendance/check-out`
- **参数**:
  ```json
  {
    "employeeId": 1,
    "employeeName": "张三"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "下班打卡成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "date": "2026-04-01",
      "checkInTime": "2026-04-01T09:00:00",
      "checkOutTime": "2026-04-01T18:00:00",
      "status": "NORMAL"
    }
  }
  ```

### 5.4 补卡申请
- **接口**: `POST /api/attendance/makeup`
- **参数**:
  ```json
  {
    "employeeId": 1,
    "employeeName": "张三",
    "date": "2026-03-31",
    "type": "CHECK_IN",
    "reason": "忘记打卡"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "补卡申请提交成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "date": "2026-03-31",
      "type": "CHECK_IN",
      "reason": "忘记打卡",
      "approvalStatus": "PENDING"
    }
  }
  ```

### 5.5 审批补卡
- **接口**: `PUT /api/attendance/makeup/{id}/approve`
- **参数**:
  ```json
  {
    "approved": true,
    "comment": "同意"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "补卡审批成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "date": "2026-03-31",
      "type": "CHECK_IN",
      "reason": "忘记打卡",
      "approvalStatus": "APPROVED",
      "approvalComment": "同意"
    }
  }
  ```

### 5.6 获取请假申请列表
- **接口**: `GET /api/attendance/leave/applications`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取请假申请成功",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "employeeId": 1,
          "employeeName": "张三",
          "leaveType": "ANNUAL",
          "startDate": "2026-04-10",
          "endDate": "2026-04-12",
          "days": 3,
          "reason": "休假",
          "approvalStatus": "PENDING"
        }
      ]
    }
  }
  ```

### 5.7 提交请假申请
- **接口**: `POST /api/attendance/leave/apply`
- **参数**:
  ```json
  {
    "employeeId": 1,
    "employeeName": "张三",
    "leaveType": "ANNUAL",
    "startDate": "2026-04-10",
    "endDate": "2026-04-12",
    "reason": "休假"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "请假申请提交成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "leaveType": "ANNUAL",
      "startDate": "2026-04-10",
      "endDate": "2026-04-12",
      "days": 3,
      "reason": "休假",
      "approvalStatus": "PENDING"
    }
  }
  ```

### 5.8 审批请假
- **接口**: `PUT /api/attendance/leave/applications/{id}/approve`
- **参数**:
  ```json
  {
    "approverId": 2,
    "approverName": "李四",
    "approved": true,
    "comment": "同意"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "请假审批成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "leaveType": "ANNUAL",
      "startDate": "2026-04-10",
      "endDate": "2026-04-12",
      "days": 3,
      "reason": "休假",
      "approvalStatus": "APPROVED",
      "approvalComment": "同意"
    }
  }
  ```

### 5.9 获取假期余额
- **接口**: `GET /api/attendance/leave/balance/{employeeId}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取假期余额成功",
    "data": {
      "annual": 10,
      "sick": 5,
      "personal": 3
    }
  }
  ```

### 5.10 获取加班记录
- **接口**: `GET /api/attendance/overtime/records`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取加班记录成功",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "employeeId": 1,
          "employeeName": "张三",
          "date": "2026-04-01",
          "startTime": "2026-04-01T18:00:00",
          "endTime": "2026-04-01T21:00:00",
          "hours": 3,
          "reason": "项目赶工",
          "approvalStatus": "PENDING"
        }
      ]
    }
  }
  ```

### 5.11 提交加班申请
- **接口**: `POST /api/attendance/overtime/apply`
- **参数**:
  ```json
  {
    "employeeId": 1,
    "employeeName": "张三",
    "date": "2026-04-01",
    "startTime": "2026-04-01T18:00:00",
    "endTime": "2026-04-01T21:00:00",
    "reason": "项目赶工"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "加班申请提交成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "date": "2026-04-01",
      "startTime": "2026-04-01T18:00:00",
      "endTime": "2026-04-01T21:00:00",
      "hours": 3,
      "reason": "项目赶工",
      "approvalStatus": "PENDING"
    }
  }
  ```

### 5.12 审批加班
- **接口**: `PUT /api/attendance/overtime/records/{id}/approve`
- **参数**:
  ```json
  {
    "approverId": 2,
    "approverName": "李四",
    "approved": true,
    "comment": "同意"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "加班审批成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "date": "2026-04-01",
      "startTime": "2026-04-01T18:00:00",
      "endTime": "2026-04-01T21:00:00",
      "hours": 3,
      "reason": "项目赶工",
      "approvalStatus": "APPROVED",
      "approvalComment": "同意"
    }
  }
  ```

### 5.13 转换加班
- **接口**: `POST /api/attendance/overtime/convert`
- **参数**:
  ```json
  {
    "id": 1,
    "conversionType": "COMPENSATORY_LEAVE"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "加班转换成功",
    "data": {
      "id": 1,
      "employeeId": 1,
      "employeeName": "张三",
      "conversionType": "COMPENSATORY_LEAVE",
      "hours": 3
    }
  }
  ```

### 5.14 获取月度统计
- **接口**: `GET /api/attendance/statistics/monthly`
- **参数**:
  - `year`: 年份
  - `month`: 月份
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取月度统计成功",
    "data": {
      "totalEmployees": 10,
      "totalAttendanceDays": 220,
      "averageAttendanceRate": 0.95,
      "lateCount": 5,
      "absentCount": 2
    }
  }
  ```

## 6. 日志管理

### 6.1 获取操作日志
- **接口**: `GET /api/logs/operation`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取操作日志成功",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "username": "admin",
          "operation": "创建员工",
          "details": "创建了员工张三",
          "ip": "127.0.0.1",
          "createdAt": "2026-04-01T10:00:00"
        }
      ]
    }
  }
  ```

### 6.2 获取登录日志
- **接口**: `GET /api/logs/login`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "获取登录日志成功",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "username": "admin",
          "status": "success",
          "ip": "127.0.0.1",
          "createdAt": "2026-04-01T10:00:00"
        }
      ]
    }
  }
  ```

### 6.3 记录操作日志
- **接口**: `POST /api/logs/operation`
- **参数**:
  ```json
  {
    "username": "admin",
    "operation": "创建员工",
    "details": "创建了员工张三",
    "ip": "127.0.0.1"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "记录操作日志成功",
    "data": {
      "id": 1,
      "username": "admin",
      "operation": "创建员工",
      "details": "创建了员工张三",
      "ip": "127.0.0.1",
      "createdAt": "2026-04-01T10:00:00"
    }
  }
  ```

### 6.4 记录登录日志
- **接口**: `POST /api/logs/login`
- **参数**:
  ```json
  {
    "username": "admin",
    "status": "success",
    "ip": "127.0.0.1"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "记录登录日志成功",
    "data": {
      "id": 1,
      "username": "admin",
      "status": "success",
      "ip": "127.0.0.1",
      "createdAt": "2026-04-01T10:00:00"
    }
  }
  ```

## 7. 招聘管理

### 7.1 职位管理

#### 7.1.1 获取职位列表
- **接口**: `GET /api/recruitment/jobs`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页大小，默认10
  - `departmentId`: 部门ID（可选）
  - `positionId`: 职位ID（可选）
  - `status`: 状态（可选，ACTIVE/INACTIVE）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "title": "前端开发工程师",
          "departmentId": 1,
          "departmentName": "技术部",
          "positionId": 1,
          "positionName": "前端开发",
          "requirement": "熟悉Vue3、TypeScript...",
          "salaryRange": "15K-25K",
          "recruitCount": 2,
          "status": "ACTIVE",
          "createdAt": "2026-03-31T00:00:00"
        }
      ]
    }
  }
  ```

#### 7.1.2 创建职位
- **接口**: `POST /api/recruitment/jobs`
- **参数**:
  ```json
  {
    "title": "前端开发工程师",
    "departmentId": 1,
    "positionId": 1,
    "requirement": "熟悉Vue3、TypeScript...",
    "salaryRange": "15K-25K",
    "recruitCount": 2,
    "status": "ACTIVE"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "前端开发工程师",
      "departmentId": 1,
      "departmentName": "技术部",
      "positionId": 1,
      "positionName": "前端开发",
      "requirement": "熟悉Vue3、TypeScript...",
      "salaryRange": "15K-25K",
      "recruitCount": 2,
      "status": "ACTIVE",
      "createdAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.1.3 更新职位
- **接口**: `PUT /api/recruitment/jobs/{id}`
- **参数**:
  ```json
  {
    "title": "高级前端开发工程师",
    "requirement": "5年以上前端开发经验...",
    "salaryRange": "20K-30K",
    "status": "ACTIVE"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "高级前端开发工程师",
      "departmentId": 1,
      "departmentName": "技术部",
      "positionId": 1,
      "positionName": "前端开发",
      "requirement": "5年以上前端开发经验...",
      "salaryRange": "20K-30K",
      "recruitCount": 2,
      "status": "ACTIVE",
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.1.4 删除职位
- **接口**: `DELETE /api/recruitment/jobs/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 7.2 简历管理

#### 7.2.1 获取简历列表
- **接口**: `GET /api/recruitment/resumes`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页大小，默认10
  - `jobId`: 职位ID（可选）
  - `status`: 状态（可选，PENDING/INTERVIEW/PASSED/REJECTED）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "name": "张三",
          "phone": "13800138000",
          "email": "zhangsan@example.com",
          "jobId": 1,
          "jobTitle": "前端开发工程师",
          "resumeFile": "http://localhost:8080/api/files/resume1.pdf",
          "status": "PENDING",
          "createdAt": "2026-03-31T00:00:00"
        }
      ]
    }
  }
  ```

#### 7.2.2 上传简历
- **接口**: `POST /api/recruitment/resumes/upload`
- **参数**:
  - `name`: 姓名
  - `phone`: 电话
  - `email`: 邮箱
  - `jobId`: 职位ID
  - `resumeFile`: 文件（multipart/form-data）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "张三",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "jobId": 1,
      "jobTitle": "前端开发工程师",
      "resumeFile": "http://localhost:8080/api/files/resume1.pdf",
      "status": "PENDING",
      "createdAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.2.3 更新简历状态
- **接口**: `PUT /api/recruitment/resumes/{id}/status`
- **参数**:
  ```json
  {
    "status": "INTERVIEW"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "张三",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "jobId": 1,
      "jobTitle": "前端开发工程师",
      "resumeFile": "http://localhost:8080/api/files/resume1.pdf",
      "status": "INTERVIEW",
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

### 7.3 面试管理

#### 7.3.1 获取面试列表
- **接口**: `GET /api/recruitment/interviews`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页大小，默认10
  - `resumeId`: 简历ID（可选）
  - `status`: 状态（可选，SCHEDULED/COMPLETED）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "resumeId": 1,
          "candidateName": "张三",
          "jobTitle": "前端开发工程师",
          "interviewTime": "2026-04-01T10:00:00",
          "interviewers": "李四, 王五",
          "location": "公司会议室A",
          "status": "SCHEDULED",
          "evaluation": null,
          "createdAt": "2026-03-31T00:00:00"
        }
      ]
    }
  }
  ```

#### 7.3.2 安排面试
- **接口**: `POST /api/recruitment/interviews`
- **参数**:
  ```json
  {
    "resumeId": 1,
    "interviewTime": "2026-04-01T10:00:00",
    "interviewers": "李四, 王五",
    "location": "公司会议室A"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobTitle": "前端开发工程师",
      "interviewTime": "2026-04-01T10:00:00",
      "interviewers": "李四, 王五",
      "location": "公司会议室A",
      "status": "SCHEDULED",
      "evaluation": null,
      "createdAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.3.3 更新面试信息
- **接口**: `PUT /api/recruitment/interviews/{id}`
- **参数**:
  ```json
  {
    "interviewTime": "2026-04-02T14:00:00",
    "interviewers": "李四, 赵六",
    "location": "公司会议室B"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobTitle": "前端开发工程师",
      "interviewTime": "2026-04-02T14:00:00",
      "interviewers": "李四, 赵六",
      "location": "公司会议室B",
      "status": "SCHEDULED",
      "evaluation": null,
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.3.4 提交面试评价
- **接口**: `PUT /api/recruitment/interviews/{id}/evaluate`
- **参数**:
  ```json
  {
    "evaluation": "技术能力强，沟通能力良好",
    "result": "PASSED"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobTitle": "前端开发工程师",
      "interviewTime": "2026-04-01T10:00:00",
      "interviewers": "李四, 王五",
      "location": "公司会议室A",
      "status": "COMPLETED",
      "evaluation": "技术能力强，沟通能力良好",
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

### 7.4 录用管理

#### 7.4.1 获取录用列表
- **接口**: `GET /api/recruitment/offers`
- **参数**:
  - `page`: 页码，默认1
  - `size`: 每页大小，默认10
  - `status`: 状态（可选，PENDING/APPROVED/ACCEPTED/REJECTED）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "resumeId": 1,
          "candidateName": "张三",
          "jobId": 1,
          "jobTitle": "前端开发工程师",
          "salary": "20000",
          "joinDate": "2026-04-15",
          "status": "PENDING",
          "createdAt": "2026-03-31T00:00:00"
        }
      ]
    }
  }
  ```

#### 7.4.2 发起录用
- **接口**: `POST /api/recruitment/offers`
- **参数**:
  ```json
  {
    "resumeId": 1,
    "salary": "20000",
    "joinDate": "2026-04-15"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobId": 1,
      "jobTitle": "前端开发工程师",
      "salary": "20000",
      "joinDate": "2026-04-15",
      "status": "PENDING",
      "createdAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.4.3 更新录用信息
- **接口**: `PUT /api/recruitment/offers/{id}`
- **参数**:
  ```json
  {
    "salary": "22000",
    "joinDate": "2026-04-20"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobId": 1,
      "jobTitle": "前端开发工程师",
      "salary": "22000",
      "joinDate": "2026-04-20",
      "status": "PENDING",
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.4.4 审批录用通知
- **接口**: `PUT /api/recruitment/offers/{id}/approve`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobId": 1,
      "jobTitle": "前端开发工程师",
      "salary": "20000",
      "joinDate": "2026-04-15",
      "status": "APPROVED",
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.4.5 处理录用结果
- **接口**: `PUT /api/recruitment/offers/{id}/process`
- **参数**:
  ```json
  {
    "status": "ACCEPTED"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "resumeId": 1,
      "candidateName": "张三",
      "jobId": 1,
      "jobTitle": "前端开发工程师",
      "salary": "20000",
      "joinDate": "2026-04-15",
      "status": "ACCEPTED",
      "updatedAt": "2026-03-31T00:00:00"
    }
  }
  ```

#### 7.4.6 创建员工档案
- **接口**: `POST /api/recruitment/offers/{id}/create-employee`
- **参数**:
  ```json
  {
    "employeeCode": "EMP001",
    "departmentId": 1,
    "positionId": 1,
    "entryDate": "2026-04-15"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "employeeId": 12,
      "employeeCode": "EMP001",
      "name": "张三",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "departmentId": 1,
      "departmentName": "技术部",
      "positionId": 1,
      "positionName": "前端开发",
      "entryDate": "2026-04-15",
      "status": "ACTIVE"
    }
  }
  ```

## 8. 文件管理

### 8.1 上传简历文件
- **接口**: `POST /api/files/upload/resume`
- **参数**:
  - `file`: 文件（multipart/form-data）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "fileUrl": "http://localhost:8080/api/files/resume/xxx.pdf"
    }
  }
  ```

### 8.2 下载简历文件
- **接口**: `GET /api/files/{filename}`
- **返回**: 文件下载

## 附录

### 状态值说明

#### 职位状态 (Job Status)
- `ACTIVE`: 招聘中
- `INACTIVE`: 已关闭

#### 简历状态 (Resume Status)
- `PENDING`: 待处理
- `INTERVIEW`: 待面试
- `PASSED`: 通过
- `REJECTED`: 淘汰

#### 面试状态 (Interview Status)
- `SCHEDULED`: 已安排
- `COMPLETED`: 已完成

#### 录用状态 (Offer Status)
- `PENDING`: 待审批
- `APPROVED`: 已审批
- `ACCEPTED`: 已接受
- `REJECTED`: 已拒绝

#### 员工状态 (Employee Status)
- `ACTIVE`: 在职
- `INACTIVE`: 离职

#### 工资条状态 (Payslip Status)
- `GENERATED`: 已生成
- `SENT`: 已发送

#### 考核周期状态 (Cycle Status)
- `ACTIVE`: 进行中
- `COMPLETED`: 已完成

#### 请假类型 (Leave Type)
- `ANNUAL`: 年假
- `SICK`: 病假
- `PERSONAL`: 事假
- `MARRIAGE`: 婚假
- `MATERNITY`: 产假

#### 审批状态 (Approval Status)
- `PENDING`: 待审批
- `APPROVED`: 已通过
- `REJECTED`: 已拒绝

## 9. 通知公告

### 9.1 公告列表

#### 9.1.1 获取公告列表（全员/员工）
- **接口**: `GET /api/notice/list`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
  - `employeeId`: 员工ID（可选，用于查看员工可见的公告）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "title": "关于2026年五一放假安排的通知",
          "content": "各部门注意，2026年五一放假时间为4月29日至5月3日...",
          "scope": "ALL",
          "departmentId": null,
          "departmentName": null,
          "isImportant": true,
          "sendEmail": true,
          "sendInternalMessage": true,
          "publisherId": 1,
          "publisherName": "管理员",
          "publishTime": "2026-04-01T10:00:00",
          "viewCount": 50,
          "isRead": false
        }
      ]
    }
  }
  ```

#### 9.1.2 获取公告列表（管理员）
- **接口**: `GET /api/notice/admin/list`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
  - `scope`: 可见范围（可选，ALL/DEPARTMENT）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 10,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "title": "关于2026年五一放假安排的通知",
          "content": "各部门注意，2026年五一放假时间为4月29日至5月3日...",
          "scope": "ALL",
          "departmentId": null,
          "departmentName": null,
          "isImportant": true,
          "sendEmail": true,
          "sendInternalMessage": true,
          "publisherId": 1,
          "publisherName": "管理员",
          "publishTime": "2026-04-01T10:00:00",
          "viewCount": 50
        }
      ]
    }
  }
  ```

### 9.2 公告详情

#### 9.2.1 获取公告详情（员工/管理员）
- **接口**: `GET /api/notice/{id}`
- **参数**:
  - `employeeId`: 员工ID（可选，用于标记已读）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "关于2026年五一放假安排的通知",
      "content": "各部门注意，2026年五一放假时间为4月29日至5月3日...",
      "scope": "ALL",
      "departmentId": null,
      "departmentName": null,
      "isImportant": true,
      "sendEmail": true,
      "sendInternalMessage": true,
      "publisherId": 1,
      "publisherName": "管理员",
      "publishTime": "2026-04-01T10:00:00",
      "viewCount": 50
    }
  }
  ```

#### 9.2.2 标记公告已读
- **接口**: `POST /api/notice/{id}/read`
- **参数**:
  ```json
  {
    "employeeId": 1
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 9.3 公告发布

#### 9.3.1 发布公告
- **接口**: `POST /api/notice`
- **参数**:
  ```json
  {
    "title": "关于2026年五一放假安排的通知",
    "content": "各部门注意，2026年五一放假时间为4月29日至5月3日...",
    "scope": "ALL",
    "departmentId": null,
    "isImportant": true,
    "sendEmail": true,
    "sendInternalMessage": true,
    "publisherId": 1,
    "publisherName": "管理员"
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "关于2026年五一放假安排的通知",
      "content": "各部门注意，2026年五一放假时间为4月29日至5月3日...",
      "scope": "ALL",
      "departmentId": null,
      "departmentName": null,
      "isImportant": true,
      "sendEmail": true,
      "sendInternalMessage": true,
      "publisherId": 1,
      "publisherName": "管理员",
      "publishTime": "2026-04-01T10:00:00"
    }
  }
  ```

#### 9.3.2 更新公告
- **接口**: `PUT /api/notice/{id}`
- **参数**:
  ```json
  {
    "title": "关于2026年五一放假安排的通知（更新）",
    "content": "各部门注意，2026年五一放假时间为4月30日至5月4日...",
    "scope": "ALL",
    "isImportant": true
  }
  ```
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "关于2026年五一放假安排的通知（更新）",
      "content": "各部门注意，2026年五一放假时间为4月30日至5月4日...",
      "scope": "ALL",
      "departmentId": null,
      "departmentName": null,
      "isImportant": true,
      "sendEmail": true,
      "sendInternalMessage": true,
      "publisherId": 1,
      "publisherName": "管理员",
      "publishTime": "2026-04-01T10:00:00"
    }
  }
  ```

#### 9.3.3 删除公告
- **接口**: `DELETE /api/notice/{id}`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 9.4 已读统计

#### 9.4.1 获取公告阅读统计
- **接口**: `GET /api/notice/{id}/stats`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "noticeId": 1,
      "totalEmployees": 100,
      "readCount": 50,
      "unreadCount": 50,
      "readRate": 0.5
    }
  }
  ```

#### 9.4.2 获取阅读详情列表
- **接口**: `GET /api/notice/{id}/read-details`
- **参数**:
  - `page`: 页码，默认 1
  - `size`: 每页大小，默认 10
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 100,
      "page": 1,
      "size": 10,
      "list": [
        {
          "id": 1,
          "noticeId": 1,
          "employeeId": 1,
          "employeeName": "张三",
          "departmentName": "技术部",
          "isRead": true,
          "readTime": "2026-04-01T11:00:00"
        },
        {
          "id": 2,
          "noticeId": 1,
          "employeeId": 2,
          "employeeName": "李四",
          "departmentName": "产品部",
          "isRead": false,
          "readTime": null
        }
      ]
    }
  }
  ```

### 附录 - 通知公告状态值

#### 公告可见范围 (Notice Scope)
- `ALL`: 全员可见
- `DEPARTMENT`: 指定部门可见

## 10. 数据统计与报表

### 10.1 仪表盘数据

#### 10.1.1 获取员工总数
- **接口**: `GET /api/statistics/employee-count`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 100,
      "active": 95,
      "inactive": 5
    }
  }
  ```

#### 10.1.2 获取男女比例
- **接口**: `GET /api/statistics/gender-ratio`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      { "gender": "男", "count": 55, "percentage": 0.55 },
      { "gender": "女", "count": 45, "percentage": 0.45 }
    ]
  }
  ```

#### 10.1.3 获取部门人数统计
- **接口**: `GET /api/statistics/department-count`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      { "departmentName": "技术部", "count": 40 },
      { "departmentName": "产品部", "count": 20 },
      { "departmentName": "市场部", "count": 15 }
    ]
  }
  ```

#### 10.1.4 获取年龄分布
- **接口**: `GET /api/statistics/age-distribution`
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      { "range": "20-25", "count": 20 },
      { "range": "26-30", "count": 35 },
      { "range": "31-35", "count": 25 },
      { "range": "36-40", "count": 12 },
      { "range": "41+", "count": 8 }
    ]
  }
  ```

### 10.2 自定义报表

#### 10.2.1 导出员工自定义报表
- **接口**: `POST /api/statistics/custom-report/export`
- **参数**:
  ```json
  {
    "format": "excel",
    "fields": ["name", "gender", "age", "departmentName", "positionName", "email", "phone", "employeeCode", "entryDate", "status"],
    "filters": {
      "departmentId": 1,
      "status": "ACTIVE",
      "minAge": 20,
      "maxAge": 50
    }
  }
  ```
- **返回**: Excel/CSV 文件下载

### 10.3 离职率分析

#### 10.3.1 按月统计离职率
- **接口**: `GET /api/statistics/turnover/monthly`
- **参数**:
  - `year`: 年份（可选，默认当前年份）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      { "month": 1, "totalEmployees": 90, "departures": 2, "turnoverRate": 0.022 },
      { "month": 2, "totalEmployees": 92, "departures": 1, "turnoverRate": 0.011 }
    ]
  }
  ```

#### 10.3.2 按部门统计离职率
- **接口**: `GET /api/statistics/turnover/department`
- **参数**:
  - `year`: 年份（可选，默认当前年份）
- **返回**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      { "departmentName": "技术部", "totalEmployees": 40, "departures": 3, "turnoverRate": 0.075 },
      { "departmentName": "产品部", "totalEmployees": 20, "departures": 1, "turnoverRate": 0.05 }
    ]
  }
  ```

### 附录 - 数据统计状态值

#### 报表格式 (Report Format)
- `excel`: Excel 格式
- `csv`: CSV 格式

#### 年龄分组 (Age Range)
- `20-25`: 20-25岁
- `26-30`: 26-30岁
- `31-35`: 31-35岁
- `36-40`: 36-40岁
- `41+`: 41岁以上

