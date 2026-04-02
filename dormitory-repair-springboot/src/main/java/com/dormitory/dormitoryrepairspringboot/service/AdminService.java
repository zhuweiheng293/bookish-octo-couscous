package com.dormitory.dormitoryrepairspringboot.service;

import com.dormitory.dormitoryrepairspringboot.entity.RepairOrder;
import com.dormitory.dormitoryrepairspringboot.entity.User;
import com.dormitory.dormitoryrepairspringboot.mapper.RepairOrderMapper;
import com.dormitory.dormitoryrepairspringboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdminService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Autowired
    private UserMapper userMapper;

    public List<RepairOrder>getAllOrders(){

        return repairOrderMapper.findAll();
    }

    public List<RepairOrder>getOrdersByStatus(int status){

        return repairOrderMapper.findByStatus(status);
    }

    public RepairOrder viewOrder(Long orderId){

        RepairOrder order=repairOrderMapper.findById(orderId);
        if(order==null){
            System.out.println("报修单不存在！");
            return null;
        }
        User student=userMapper.findById(order.getStudentId());

        System.out.println("\n======== 报修单详情 ========");
        System.out.println("单号：" + order.getOrderNo());
        System.out.println("报修人：" + (student != null ? student.getName() : "未知"));
        System.out.println("学号：" + (student != null ? student.getUserNo() : "未知"));
        System.out.println("设备类型：" + order.getDeviceType());
        System.out.println("问题描述：" + order.getDescription());
        System.out.println("状态：" + order.getStatusText());
        System.out.println("优先级：" + (order.getPriority() == 1 ? "普通" : "紧急"));
        System.out.println("创建时间：" + order.getCreateTime());
        System.out.println("最后更新：" + order.getUpdateTime());
        System.out.println("============================\n");

        return order;
    }
    public  boolean updateOrderStatus(Long orderId,int newStatus) {

        RepairOrder order = repairOrderMapper.findById(orderId);
        if(order==null){
            System.out.println("报修单不存在！");
            return false;
        }
        int result= repairOrderMapper.updateStatus(orderId,newStatus);

        if(result>0){
            String statusText=getStatusText(newStatus);
            System.out.println("状态更新为：" + statusText + "!");
        }
        return result>0;
    }
    public boolean deleteOrder(Long orderId){
        RepairOrder order= repairOrderMapper.findById(orderId);
        if(order==null){
            System.out.println("报修单不存在！");
            return false;
        }
        return repairOrderMapper.deleteById(orderId)>0;
    }

    private String getStatusText(int status){
        switch (status){
            case 0:return "待处理";
            case 1:return "处理中";
            case 2:return "已完成";
            case 3:return "已取消";
            default:return "未知";
        }
    }
}
