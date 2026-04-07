
<template>
  <div class="employee-detail-view">
    <div class="page-title">
      <el-icon><User />
      </el-icon>
      员工详情
      <el-button 
        type="primary" 
        @click="handleEdit" :disabled="saving" 
        v-if="!isEditing"
        style="margin-left: auto;"
      >
        <el-icon><Edit />
        </el-icon>
        编辑
      </el-button>
      <div v-else style="margin-left: auto; display: flex; gap: 10px;">
        <el-button @click="handleCancel" :disabled="saving">取消</el-button> 
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </div>
    </div>
    
    <div class="page-container">
      <el-form 
        :model="formData" 
        :rules="rules" 
        ref="formRef"
        label-width="100px"
        v-loading="employeeStore.state.loading"
      >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="姓名" prop="name">
              <el-input 
                v-model="formData.name" 
                :disabled="!isEditing"
                placeholder="请输入姓名"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别" prop="gender">
              <el-select 
                v-model="formData.gender" 
                :disabled="!isEditing"
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
                :disabled="!isEditing"
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
                :disabled="!isEditing"
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
                :disabled="!isEditing"
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
                :disabled="!isEditing"
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
                :disabled="!isEditing"
                placeholder="请输入职位"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工编码" prop="employeeCode">
              <el-input 
                v-model="formData.employeeCode" 
                disabled
                placeholder="员工编码"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="formData.email" 
                :disabled="!isEditing"
                placeholder="请输入邮箱"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input 
                v-model="formData.phone" 
                :disabled="!isEditing"
                placeholder="请输入电话"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="创建时间">
              <el-input 
                :value="formatDate(formData.createdAt)" 
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更新时间">
              <el-input 
                :value="formatDate(formData.updatedAt)" 
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useEmployeeStore } from '@/stores/employee'
import { User, Edit } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

const route = useRoute()
const router = useRouter()
const employeeStore = useEmployeeStore()
const formRef = ref<FormInstance>()
const saving = ref(false)

const isEditing = ref(false)
const originalData = ref<any>(null)

const formData = reactive({
  id: undefined as number | undefined,
  name: '',
  gender: '',
  age: 0,
  birthDate: '',
  height: 0,
  weight: 0,
  position: '',
  email: '',
  phone: '',
  employeeCode: '',
  createdAt: '',
  updatedAt: ''
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

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

// 加载员工数据
const loadEmployeeData = async (id: number) => {
  try {
    const employee = await employeeStore.fetchEmployeeById(id)
    Object.assign(formData, employee)
    originalData.value = { ...employee }
  } catch (error) {
    ElMessage.error('加载员工数据失败')
  }
}

// 监听路由参数变化
watch(() => route.params.id, async (newId) => {
  if (newId) {
    await loadEmployeeData(parseInt(newId as string))
  }
}, { immediate: true })

// 编辑模式
const handleEdit = () => {
  isEditing.value = true
}

// 取消编辑
const handleCancel = () => {
  isEditing.value = false
  Object.assign(formData, originalData.value)
}

// 保存编辑
const handleSave = async () => {
  if (!formRef.value || saving.value) return
  
  try {
    await formRef.value.validate()
    saving.value = true
    
    const updateData = { ...formData }
    delete updateData.id
    ;(updateData as any).employeeCode = undefined
    delete updateData.createdAt
    delete updateData.updatedAt
    
    await employeeStore.updateEmployee(formData.id!, updateData)
    ElMessage.success('保存成功')
    
    // 跳回列表页面
    router.push('/employees/list')
    
    // 重新加载列表
    await employeeStore.fetchEmployees(
      employeeStore.state.pagination.page,
      employeeStore.state.pagination.size
    )
    
    isEditing.value = false
    originalData.value = { ...formData }
  } catch (error) {
    console.error('保存失败:', error)
  } finally {
    saving.value = false
  }
}

// 初始化
onMounted(() => {
  if (route.params.id) {
    loadEmployeeData(parseInt(route.params.id as string))
  }
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;
.employee-detail-view {
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

