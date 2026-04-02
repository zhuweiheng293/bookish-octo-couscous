<template>
  <div>
    <h2>学生首页</h2>
    <div class="card">
      <h3>绑定/修改宿舍</h3>
      <input type="text" v-model="dorm.building" placeholder="楼栋号（如：A栋）" />
      <input type="text" v-model="dorm.room" placeholder="房间号（如：101）" />
      <button @click="handleBind">绑定宿舍</button>
      <p v-if="bindMsg" :style="{ color: bindSuccess ? 'green' : 'red' }">{{ bindMsg }}</p>
    </div>
    <div class="card">
      <h3>创建报修单</h3>
      <select v-model="repair.deviceType">
        <option value="水龙头">水龙头</option>
        <option value="马桶">马桶</option>
        <option value="灯管">灯管</option>
        <option value="其他">其他</option>
      </select>
      <input type="text" v-model="repair.deviceTypeOther" v-if="repair.deviceType === '其他'" placeholder="请输入设备类型" />
      <textarea v-model="repair.description" placeholder="问题描述"></textarea>
      <input type="file" @change="handleFileChange" />
      <button @click="handleCreate">创建报修单</button>
      <p v-if="createMsg" :style="{ color: createSuccess ? 'green' : 'red' }">{{ createMsg }}</p>
    </div>
    <button @click="goToOrders">查看我的报修记录</button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { bindDormitory, createRepairOrder, isBound } from '@/api'

const router = useRouter()
const authStore = useAuthStore()
const userId = authStore.user?.id

const dorm = ref({ building: '', room: '' })
const bindMsg = ref('')
const bindSuccess = ref(false)

const repair = ref({ deviceType: '水龙头', deviceTypeOther: '', description: '' })
const createMsg = ref('')
const createSuccess = ref(false)
const selectedFile = ref(null)

const finalDeviceType = computed(() => repair.value.deviceType === '其他' ? repair.value.deviceTypeOther : repair.value.deviceType)

const handleFileChange = (e) => { selectedFile.value = e.target.files[0] }

const checkBound = async () => {
  try { const res = await isBound(userId); if (res.data) bindMsg.value = '您已绑定宿舍' } catch (err) {}
}
checkBound()

const handleBind = async () => {
  try {
    const res = await bindDormitory({ userId, building: dorm.value.building, room: dorm.value.room })
    bindSuccess.value = res.data === '绑定成功!'
    bindMsg.value = res.data
  } catch (err) {
    bindSuccess.value = false
    bindMsg.value = '请求失败'
  }
}

const handleCreate = async () => {
  if (!finalDeviceType.value) { createMsg.value = '请输入设备类型'; return }
  try {
    const formData = {
      studentId: userId,
      deviceType: finalDeviceType.value,
      description: repair.value.description,
      upload_photo: selectedFile.value
    }
    const res = await createRepairOrder(formData)
    createSuccess.value = res.data === '创建成功！'
    createMsg.value = res.data
    if (createSuccess.value) {
      repair.value = { deviceType: '水龙头', deviceTypeOther: '', description: '' }
      selectedFile.value = null
    }
  } catch (err) {
    createSuccess.value = false
    createMsg.value = '请求失败'
  }
}

const goToOrders = () => router.push('/student/orders')
</script>