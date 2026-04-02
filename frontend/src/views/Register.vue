<template>
  <div class="card" style="max-width: 400px; margin: 50px auto;">
    <h2>注册</h2>
    <select v-model="form.role">
      <option :value="1">学生</option>
      <option :value="2">管理员</option>
    </select>
    <input type="text" v-model="form.userNo" :placeholder="roleText" />
    <input type="text" v-model="form.name" placeholder="姓名" />
    <input type="text" v-model="form.phone" placeholder="手机号" />
    <input type="password" v-model="form.password" placeholder="密码" />
    <input type="password" v-model="form.confirmPwd" placeholder="确认密码" />
    <button @click="handleRegister">注册</button>
    <p style="margin-top: 10px;">已有账号？<router-link to="/login">去登录</router-link></p>
    <p v-if="msg" :style="{ color: success ? 'green' : 'red' }">{{ msg }}</p>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { register } from '@/api'

const form = ref({ role: 1, userNo: '', name: '', phone: '', password: '', confirmPwd: '' })
const msg = ref('')
const success = ref(false)

const roleText = computed(() => form.value.role === 1 ? '学号（3125或3225开头）' : '工号（0025开头）')

const handleRegister = async () => {
  try {
    const res = await register(form.value)
    if (res.data === '注册成功！') {
      success.value = true
      msg.value = '注册成功！3秒后跳转到登录页'
      setTimeout(() => { window.location.href = '/login' }, 3000)
    } else {
      success.value = false
      msg.value = res.data
    }
  } catch (err) {
    success.value = false
    msg.value = '请求失败'
  }
}
</script>