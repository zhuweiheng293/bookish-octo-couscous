<template>
  <div>
    <h2>我的报修记录</h2>
    <button @click="loadOrders">刷新</button>
    <div v-if="loading">加载中...</div>
    <table v-else>
      <thead><tr><th>单号</th><th>设备类型</th><th>状态</th><th>创建时间</th><th>图片</th><th>操作</th></tr></thead>
      <tbody>
      <tr v-for="order in orders" :key="order.id">
        <td>{{ order.orderNo }}</td><td>{{ order.deviceType }}</td><td>{{ getStatusText(order.status) }}</td>
        <td>{{ formatDate(order.createTime) }}</td>
        <td><a v-if="order.uploadPhoto" :href="order.uploadPhoto" target="_blank">查看图片</a></td>
        <td><button v-if="order.status === 0" class="danger" @click="handleCancel(order.id)">取消</button>
          <button @click="viewDetail(order)">详情</button></td>
      </tr>
      </tbody>
    </table>
    <div v-if="detailVisible" class="modal"><div class="modal-content">
      <h3>报修单详情</h3><p>单号：{{ currentOrder?.orderNo }}</p><p>设备类型：{{ currentOrder?.deviceType }}</p>
      <p>问题描述：{{ currentOrder?.description }}</p><p>状态：{{ getStatusText(currentOrder?.status) }}</p>
      <p>创建时间：{{ formatDate(currentOrder?.createTime) }}</p><p>更新时间：{{ formatDate(currentOrder?.updateTime) }}</p>
      <p v-if="currentOrder?.uploadPhoto">图片：<a :href="currentOrder.uploadPhoto" target="_blank">查看</a></p>
      <button @click="detailVisible = false">关闭</button>
    </div></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { getMyOrders, cancelOrder } from '@/api'

const authStore = useAuthStore()
const userId = authStore.user?.id
const orders = ref([])
const loading = ref(false)
const detailVisible = ref(false)
const currentOrder = ref(null)

const getStatusText = (status) => ({ 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }[status] || '未知')
const formatDate = (dateStr) => dateStr ? dateStr.replace('T', ' ').substring(0, 19) : ''

const loadOrders = async () => {
  loading.value = true
  try { const res = await getMyOrders(userId); orders.value = res.data || [] } catch (err) {}
  loading.value = false
}

const handleCancel = async (orderId) => {
  if (!confirm('确定要取消吗？')) return
  try { await cancelOrder({ orderId, studentId: userId }); loadOrders() } catch (err) { alert('取消失败') }
}

const viewDetail = (order) => { currentOrder.value = order; detailVisible.value = true }

onMounted(() => loadOrders())
</script>

<style scoped>
.modal { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; }
.modal-content { background: white; padding: 20px; border-radius: 8px; min-width: 300px; }
</style>