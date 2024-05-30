<template>
  <div class='personal'>
    <div class='per-form'>

      <div class="form-content">
        <el-form :model="form" label-width="auto" style="max-width: 1000px">
          <el-form-item>
            <el-button type="primary" plain @click="dialogFormVisible = true">修改密码</el-button>
          </el-form-item>
          <el-form-item label="工号">
            <el-input v-model="form.jobNumber"/>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.name"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" placeholder="please select your zone">
              <el-option label="男" value="男"/>
              <el-option label="女" value="女"/>
            </el-select>
          </el-form-item>
          <el-form-item label="所属部门">
            <el-select v-model="form.department" placeholder="please select your zone">
              <el-option label="部门A" value="部门A"/>
              <el-option label="部门B" value="部门B"/>
              <el-option label="部门C" value="部门C"/>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 修改个人信息弹窗 -->
    <el-dialog v-model="dialogFormVisible" title="修改个人信息" width="400" padding="20px" draggable>
      <el-scrollbar max-height="300px" padding="20px">
        <div class='eidt-per-form'>
          <div class="form-content">
            <el-form :model="updateForm" label-width="auto" style="max-width: 1000px" ref="ruleFormRef" :rules="rules"
                     status-icon>
              <el-form-item label="旧密码" prop="password">
                <el-input v-model="updateForm.password"/>
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="updateForm.newPassword"/>
              </el-form-item>
              <el-form-item label="确认密码" prop="checkPassword">
                <el-input v-model="updateForm.checkPassword"/>
              </el-form-item>
            </el-form>
          </div>
          <div class="btn-box">
            <el-button type="primary" @click="submitForm(ruleFormRef)">
              确定
            </el-button>
            <el-button type="primary" @click="resetForm(ruleFormRef)">取消</el-button>

          </div>
        </div>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script setup lang='ts'>
import {ref, reactive, toRefs, onMounted} from 'vue'
import type {FormInstance, FormRules} from 'element-plus'
import axios from "axios";

// 编辑弹出框
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'

let form = ref(JSON.parse(localStorage.getItem("user")))

let updateForm = reactive({
  password: '',
  newPassword: '',
  checkPassword: ''
})

const ruleFormRef = ref<FormInstance>()

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入旧密码'))
  } else {
    callback()
  }
}

const validateNewPass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入新密码'))
  } else {
    if (updateForm.checkPassword !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('checkPassword')
    }
    callback()
  }
}

const validateCheckPass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== updateForm.newPassword) {
    callback(new Error("两次输入不一致"))
  } else {
    callback()
  }
}

const rules = reactive<FormRules<typeof updateForm>>({
  password: [{validator: validatePass, trigger: 'blur'}],
  newPassword: [{validator: validateNewPass, trigger: 'blur'}],
  checkPassword: [{validator: validateCheckPass, trigger: 'blur'}],
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      const token = localStorage.getItem('token');
      axios({
        url: 'http://localhost:8080/user/updatePassword',
        method: 'put',
        headers: {
          'Authorization': `${token}`,
          'Content-Type': 'application/json'
        },
        params: {
          id: form.value.id,
          password: updateForm.newPassword
        }
      }).then(res => {
        dialogFormVisible.value = false
        updateForm = {
          password: '',
          newPassword: '',
          checkPassword: ''
        }
      }, err => {
        console.log(err)
      })
    } else {
      console.log('error submit!')
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

function genderChange(gender) {
  switch (gender) {
    case '0':
      return "女";
    case '1':
      return "男";
  }
}

function departmentGetOne() {
  const token = localStorage.getItem('token');
  axios.get("http://localhost:8080/department/getDepartment/" + form.value.department,
      {
        headers: {
          'Authorization': `${token}`,
          'Content-Type': 'application/json'
        }
      }).then(res => {
    form.value.department = res.data.data.name
    form.value.gender = genderChange(form.value.gender)
  }, err => {
    console.log(err)
  })
}

const onSubmit = () => {
  console.log('submit!')
}


onMounted(() => {
  console.log(form.value)
  departmentGetOne()
})
</script>

<style lang='scss' scoped>
.per-form {
  /* padding: 20px; */
  /* display: flex; */
  /* flex-direction: row; */
  /* justify-content: center; */
  /* align-items: flex-start; */

  .form-avatar {
    padding: 20px;
  }

  .form-content {
    padding: 20px;
    width: 600px;
  }
}

.eidt-per-form {
  height: auto;

  .eidt-form-avatar {
    margin-bottom: 20px;
  }
}

.btn-box {
  display: flex;
  justify-content: space-around;

  button {
    width: 100px;
  }
}
</style>
