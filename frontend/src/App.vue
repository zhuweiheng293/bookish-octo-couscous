<template>
  <div id="app">
    <nav v-if="authStore.token">
      <span>欢迎，{{ authStore.user?.name }}</span>
      <button @click="handleLogout">退出登录</button>
    </nav>
    <router-view />
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: Arial, sans-serif; background: #f5f5f5; padding: 20px; }
nav { background: #fff; padding: 10px 20px; border-radius: 8px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
button { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; background: #409eff; color: white; }
button.danger { background: #f56c6c; }
button:hover { opacity: 0.8; }
.card { background: white; border-radius: 8px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
input, select, textarea { width: 100%; padding: 8px 12px; margin: 5px 0 15px; border: 1px solid #ddd; border-radius: 4px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
th { background: #f0f0f0; }
</style>