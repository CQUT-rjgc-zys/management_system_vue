<template>
  <div id = "login">
    <div class = "container">
      <h2>企业外勤管理系统</h2>
      <el-row>
        <el-col :span="12" v-for="item in menuTab" :key="item.id" :class="{'current': item.current}" @click="toggleMenu(item)" > {{ item.txt }} </el-col>
        <!-- <el-col :span="12"><div class="grid-content">外勤人员系统登录</div></el-col> -->
      </el-row>

      <el-form
          ref="ruleFormRef"
          style="max-width: 600px"
          :model="ruleForm"
          status-icon
          :rules="rules"
          label-width="auto"
          class="demo-ruleForm"
          v-show="model === 'admin'"
          >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="ruleForm.username" type="text" autocomplete="off" />
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input
              v-model="ruleForm.password"
              type="password"
              autocomplete="off"
              show-password="true"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm(ruleFormRef,model)" class="block">
              登录
            </el-button>
          </el-form-item>
      </el-form>

      <el-form
          ref="ruleFormRef"
          style="max-width: 600px"
          :model="ruleForm"
          status-icon
          :rules="rules"
          label-width="auto"
          class="demo-ruleForm"
          v-show="model === 'outworker'"
          >
          <el-form-item label="工号" prop="jobNumber">
            <el-input v-model="ruleForm.jobNumber" type="text" autocomplete="off" />
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input
              v-model="ruleForm.password"
              type="password"
              autocomplete="off"
              show-password="true"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm(ruleFormRef,model)" class="block">
              登录
            </el-button>

          </el-form-item>
        </el-form>
    </div>

  </div>

</template>

<script lang="ts" setup>
import { stripscript } from '@/utils/validata.js'
import { reactive, ref } from 'vue'
import { FormRules } from 'element-plus'
import { useRouter } from "vue-router";

const router = useRouter();
const API_URL = 'http://localhost:8080';

  const ruleFormRef = ref()

  const menuTab = reactive([
    {txt: '管理人员登录', current: true, type: 'admin'},
    {txt: '外勤人员登录', current:false, type: 'outworker'}
  ])

  // 模块值
  const model = ref('admin')

  // 点击切换
  const toggleMenu = (data => {
    console.log(data);
    menuTab.forEach((elem, index) => {
      elem.current = false
    });
    // 高光
    data.current = true;
    // 修改模块值
    model.value = data.type
  })

  // 验证用户名
  const validateUsername = (rule, value, callback) => {
    if(model.value == 'outworker') {
        callback();
    }
    let reg = /^[a-z0-9]{4,20}$/     //数字或字母
    if (value === '') {
      callback(new Error('请输入用户名'))
    } else if(!reg.test(value)){
      callback(new Error('用户名格式有误(4-20位数字或字母)'))
    }else{
      callback(); //true
    }
  }

  // 验证工号
  const validateJobNumber = (rule, value, callback) => {
    if(model.value == 'admin') {
      callback();
    }
    let reg = /^[a-z0-9]{4,20}$/     //数字或字母
    if (value === '') {
      callback(new Error('请输入工号'))
    } else if(!reg.test(value)){
      callback(new Error('工号格式有误(4-20位数字或字母)'))
    }else{
      callback(); //true
    }
  }


  // 验证密码
  const validatePass = (rule, value, callback) => {
    console.log(stripscript(value));
    ruleForm.password = stripscript(value)
    value = ruleForm.password

    let reg = /^(?!\D+$)(?![^a-zA-Z]+$)\S{6,20}$/  //数字+字母
    if (value === '') {
      callback(new Error('请输入密码'))
    } else if(!reg.test(value)){
      callback(new Error('密码为6-20位的字母+数字'))
    }else{
      callback(); //true
    }
  }

  const ruleForm = reactive({
    username: '',
    jobNumber:'',
    password: '',
  })

  // 验证
  const rules = reactive<FormRules<typeof ruleForm>>({
    username: [{ validator: validateUsername, trigger: 'blur' }],
    jobNumber: [{ validator: validateJobNumber, trigger: 'blur' }],
    password: [{ validator: validatePass, trigger: 'blur' }],

  })

  // 提交表单
  const submitForm = (formEl) => {
  if (!formEl) return;
  formEl.validate(async (valid) => {
    if (valid) {
      let url = '';
      let data = new URLSearchParams();
      if (model.value === 'admin') {
        url = `${API_URL}/user/adminLogin`;
        data.append('username', ruleForm.username);
        data.append('password', ruleForm.password);
      } else {
        url = `${API_URL}/user/employeeLogin`;  // 假设外勤人员登录接口不同
        data.append('jobNumber', ruleForm.jobNumber);
        data.append('password', ruleForm.password);
      }

      try {
        const response = await fetch(url, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: data
        });

        const result = await response.json();

        if (result.code === 200) {
          console.log('Login successful:', result);
          if (model.value === 'admin') {
            const token = result.data;
            localStorage.setItem('token', token); // 将token存储在localStorage中
          } else {
            const {user, token} = result.data; // 假设token存储在data属性中
            console.log(result.data)
            localStorage.setItem("user", JSON.stringify(user));
            localStorage.setItem('token', token); // 将token存储在localStorage中
          }
          if (model.value === 'admin') {
            router.push("home");
          } else {
            router.push("workerhome");
          }
        } else {
          console.log('Login failed:', result.message);
          alert(`Login failed: ${result.message}`);
        }
      } catch (error) {
        console.error('Error during login:', error);
      }
    } else {
      console.log('error submit!');
      return false;
    }
  });
};

const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields();
};
</script>


<!-- css -->

<style scoped>
#login {
  width: 100%;
  height: 100%;
  background: url("@/assets/inbg.jpg");
  background-size:100% 100%;
  position: fixed;
  top: 0;
  left: 0;
}

.container{
      width: 400px;
      height: 300px;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%,-50%);
      background:#d6eaef90;
      text-align: center;
      border-radius: 20px;
      padding: 20px 50px;
}

.el-row {
    margin-bottom: 20px;
}
.el-row:last-child {
    margin-bottom: 0;
}
.el-col {
    border-radius: 4px;
    padding: 5px;
    border-radius: 5px;
    cursor: pointer;
}

.current {
  background-color: #64656859;
}

.block {
        display:block;
        width:100%;
      }
</style>
