package com.dormitory.service;

import com.dormitory.entity.RepairOrder;
import com.dormitory.entity.User;
import com.dormitory.mapper.RepairOrderMapper;
import com.dormitory.mapper.UserMapper;
import com.dormitory.util.MyBatisUtil;

import java.util.List;

public class AdminService {
    public List<RepairOrder>getAllOrders(){
        RepairOrderMapper mapper=MyBatisUtil.getMapper(RepairOrderMapper.class);
        return mapper.findAll();
    }

    public List<RepairOrder>getOrdersByStatus(int status){
        RepairOrderMapper mapper=MyBatisUtil.getMapper(RepairOrderMapper.class);
        return mapper.findByStatus(status);
    }

    public void viewOrder(Long orderId){
        RepairOrderMapper orderMapper=MyBatisUtil.getMapper(RepairOrderMapper.class);
        UserMapper userMapper=MyBatisUtil.getMapper(UserMapper.class);

        RepairOrder order=orderMapper.findById(orderId);
        if(order==null){
            System.out.println("报修单不存在！");
            return;
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
    }
    public  boolean updateOrderStatus(Long orderId,int newStatus) {
        RepairOrderMapper mapper = MyBatisUtil.getMapper(RepairOrderMapper.class);

        RepairOrder order = mapper.findById(orderId);
        if(order==null){
            System.out.println("报修单不存在！");
            return false;
        }
        int result= mapper.updateStatus(orderId,newStatus);

        if(result>0){
            System.out.println("状态更新为："+getOrdersByStatus(newStatus)+"!");
        }
        return result>0;
    }
    public boolean deleteOrder(Long orderId){
        RepairOrderMapper mapper=MyBatisUtil.getMapper(RepairOrderMapper.class);

        RepairOrder order= mapper.findById(orderId);
        if(order==null){
            System.out.println("报修单不存在！");
            return false;
        }
        return mapper.deleteById(orderId)>0;
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
