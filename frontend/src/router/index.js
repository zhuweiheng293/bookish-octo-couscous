import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue') },
  { path: '/student', name: 'StudentHome', component: () => import('@/views/StudentHome.vue'), meta: { requiresAuth: true, role: 1 } },
  { path: '/student/orders', name: 'StudentOrders', component: () => import('@/views/StudentOrders.vue'), meta: { requiresAuth: true, role: 1 } },
  { path: '/admin', name: 'AdminOrders', component: () => import('@/views/AdminOrders.vue'), meta: { requiresAuth: true, role: 2 } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()
  const token = auth.token
  const user = auth.user

  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  if (!token) {
    next('/login')
    return
  }

  if (to.meta.requiresAuth && to.meta.role && user && user.role !== to.meta.role) {
    next(user.role === 1 ? '/student' : '/admin')
    return
  }

  next()
})

export default router