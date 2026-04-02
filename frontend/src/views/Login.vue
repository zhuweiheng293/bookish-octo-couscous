<template>
  <div class="card" style="max-width: 400px; margin: 50px auto;">
    <h2>登录</h2>
    <input type="text" v-model="form.userNo" placeholder="学号/工号" />
    <input type="password" v-model="form.password" placeholder="密码" />
    <button @click="handleLogin">登录</button>
    <p style="margin-top: 10px;">没有账号？<router-link to="/register">立即注册</router-link></p>
    <p v-if="errorMsg" style="color: red;">{{ errorMsg }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { login } from '@/api'

const router = useRouter()
const authStore = useAuthStore()
const form = ref({ userNo: '', password: '' })
const errorMsg = ref('')

const handleLogin = async () => {
  try {
    const res = await login(form.value)
    if (res.data.success) {
      authStore.setAuth(res.data.token, res.data.user)
      const role = res.data.user.role
      router.push(role === 1 ? '/student' : '/admin')
    } else {
      errorMsg.value = res.data.message || '登录失败'
    }
  } catch (err) {
    errorMsg.value = '请求失败，请检查网络'
  }
}
</script>