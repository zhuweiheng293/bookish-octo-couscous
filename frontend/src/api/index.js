import axios from 'axios'

const api = axios.create({
    baseURL: '/api',
    timeout: 10000
})

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = token
    }
    return config
})

export default api

export const register = (data) => api.post('/user/register', null, { params: data })
export const login = (data) => api.post('/user/login', null, { params: data })

export const bindDormitory = (data) => api.post('/student/bind', null, { params: data })
export const isBound = (userId) => api.get('/student/isBound', { params: { userId } })
export const createRepairOrder = (data) => {
    const formData = new FormData()
    formData.append('studentId', data.studentId)
    formData.append('deviceType', data.deviceType)
    formData.append('description', data.description)
    if (data.upload_photo) {
        formData.append('upload_photo', data.upload_photo)
    }
    return api.post('/student/create', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}
export const getMyOrders = (studentId) => api.get('/student/repair_history', { params: { studentId } })
export const cancelOrder = (data) => api.delete('/student/delete', { params: data })

export const getAllOrders = () => api.get('/admin/getAllOrders')
export const getOrdersByStatus = (status) => api.get('/admin/getOrdersByStatus', { params: { status } })
export const getOrderById = (orderId) => api.get('/admin/orderinformation', { params: { orderId } })
export const updateOrderStatus = (data) => api.post('/admin/updateStatus', null, { params: data })
export const deleteOrder = (orderId) => api.post('/admin/deleteOrder', null, { params: { orderId } })