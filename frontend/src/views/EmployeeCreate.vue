<template>
  <div class="employee-create-view">
    <div class="page-title">
      <el-icon><User /></el-icon>
      新增员工
    </div>
    
    <div class="page-container">
      <el-form 
        :model="formData" 
        :rules="rules" 
        ref="formRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="姓名" prop="name">
              <el-input 
                v-model="formData.name" 
                placeholder="请输入姓名"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别" prop="gender">
              <el-select 
                v-model="formData.gender" 
                placeholder="请选择性别"
                style="width: 100%"
              >
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄" prop="age">
              <el-input-number 
                v-model="formData.age" 
                :min="18" 
                :max="65"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="formData.birthDate"
                type="date"
                placeholder="选择出生日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number 
                v-model="formData.height" 
                :min="100" 
                :max="250"
                :precision="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number 
                v-model="formData.weight" 
                :min="30" 
                :max="200"
                :precision="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位" prop="position">
              <el-input 
                v-model="formData.position" 
                placeholder="请输入职位"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="formData.email" 
                placeholder="请输入邮箱"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input 
                v-model="formData.phone" 
                placeholder="请输入电话"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
          <el-button @click="handleReset" :disabled="submitting">重置</el-button>
          <el-button @click="handleBack" :disabled="submitting">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useEmployeeStore } from '@/stores/employee'
import { User } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

const router = useRouter()
const employeeStore = useEmployeeStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const formData = reactive({
  name: '',
  gender: '',
  age: 25,
  birthDate: '',
  height: 170,
  weight: 60,
  position: '',
  email: '',
  phone: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  birthDate: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
  height: [{ required: true, message: '请输入身高', trigger: 'blur' }],
  weight: [{ required: true, message: '请输入体重', trigger: 'blur' }],
  position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value || submitting.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    await employeeStore.createEmployee(formData)
    ElMessage.success('新增员工成功')
    
    // 重置表单
    handleReset()
    
    // 返回员工列表
    router.push('/employees/list')
    
    // 重新加载列表数据
    await employeeStore.fetchEmployees(
      employeeStore.state.pagination.page,
      employeeStore.state.pagination.size
    )
  } catch (error) {
    console.error('新增员工失败:', error)
  } finally {
    submitting.value = false
  }
}

// 重置表单
const handleReset = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  Object.assign(formData, {
    name: '',
    gender: '',
    age: 25,
    birthDate: '',
    height: 170,
    weight: 60,
    position: '',
    email: '',
    phone: ''
  })
}

// 返回
const handleBack = () => {
  router.back()
}
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.employee-create-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-container {
  flex: 1;
  overflow-y: auto;
}

.el-form {
  max-width: 800px;
}
</style>