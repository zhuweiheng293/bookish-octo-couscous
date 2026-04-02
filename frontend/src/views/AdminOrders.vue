<template>
  <div>
    <h2>报修单管理</h2>
    <div style="margin-bottom: 10px;">
      <button @click="loadAllOrders">全部</button>
      <button @click="loadByStatus(0)">待处理</button>
      <button @click="loadByStatus(1)">处理中</button>
      <button @click="loadByStatus(2)">已完成</button>
      <button @click="loadByStatus(3)">已取消</button>
    </div>
    <div v-if="loading">加载中...</div>
    <table v-else>
      <thead><tr><th>ID</th><th>单号</th><th>设备类型</th><th>状态</th><th>创建时间</th><th>图片</th><th>操作</th></tr></thead>
      <tbody>
      <tr v-for="order in orders" :key="order.id">
        <td>{{ order.id }}</td><td>{{ order.orderNo }}</td><td>{{ order.deviceType }}</td>
        <td>{{ getStatusText(order.status) }}</td><td>{{ formatDate(order.createTime) }}</td>
        <td><a v-if="order.uploadPhoto" :href="order.uploadPhoto" target="_blank">查看</a></td>
        <td><button @click="viewDetail(order)">详情</button>
          <select v-model="order.newStatus" @change="updateStatus(order)">
            <option :value="0">待处理</option><option :value="1">处理中</option>
            <option :value="2">已完成</option><option :value="3">已取消</option>
          </select>
          <button class="danger" @click="handleDelete(order.id)">删除</button></td>
      </tr>
      </tbody>
    </table>
    <div v-if="detailVisible" class="modal"><div class="modal-content">
      <h3>报修单详情</h3><p>单号：{{ currentOrder?.orderNo }}</p><p>报修人：{{ currentOrder?.student?.name || '未知' }}</p>
      <p>学号：{{ currentOrder?.student?.userNo || '未知' }}</p><p>设备类型：{{ currentOrder?.deviceType }}</p>
      <p>问题描述：{{ currentOrder?.description }}</p><p>状态：{{ getStatusText(currentOrder?.status) }}</p>
      <p>创建时间：{{ formatDate(currentOrder?.createTime) }}</p><p>更新时间：{{ formatDate(currentOrder?.updateTime) }}</p>
      <p v-if="currentOrder?.uploadPhoto">图片：<a :href="currentOrder.uploadPhoto" target="_blank">查看</a></p>
      <button @click="detailVisible = false">关闭</button>
    </div></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllOrders, getOrdersByStatus, getOrderById, updateOrderStatus, deleteOrder } from '@/api'

const orders = ref([])
const loading = ref(false)
const detailVisible = ref(false)
const currentOrder = ref(null)

const getStatusText = (status) => ({ 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }[status] || '未知')
const formatDate = (dateStr) => dateStr ? dateStr.replace('T', ' ').substring(0, 19) : ''

const loadAllOrders = async () => {
  loading.value = true
  try { const res = await getAllOrders(); orders.value = (res.data || []).map(o => ({ ...o, newStatus: o.status })) } catch (err) {}
  loading.value = false
}

const loadByStatus = async (status) => {
  loading.value = true
  try { const res = await getOrdersByStatus(status); orders.value = (res.data || []).map(o => ({ ...o, newStatus: o.status })) } catch (err) {}
  loading.value = false
}

const viewDetail = async (order) => {
  try { const res = await getOrderById(order.id); currentOrder.value = res.data; detailVisible.value = true } catch (err) { alert('获取详情失败') }
}

const updateStatus = async (order) => {
  if (order.newStatus === order.status) return
  try { await updateOrderStatus({ orderId: order.id, newStatus: order.newStatus }); order.status = order.newStatus; alert('状态更新成功') }
  catch (err) { alert('更新失败'); order.newStatus = order.status }
}

const handleDelete = async (orderId) => {
  if (!confirm('确定要删除吗？')) return
  try { await deleteOrder(orderId); loadAllOrders() } catch (err) { alert('删除失败') }
}

onMounted(() => loadAllOrders())
</script>

<style scoped>
.modal { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; }
.modal-content { background: white; padding: 20px; border-radius: 8px; min-width: 350px; }
select { width: 100px; margin: 0 5px; }
</style>