package com.dormitory.dormitoryrepairspringboot.controller;

import com.dormitory.dormitoryrepairspringboot.entity.RepairOrder;
import com.dormitory.dormitoryrepairspringboot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/getAllOrders")
    public List<RepairOrder> getAllOrders(){
        return adminService.getAllOrders();
    }

    @RequestMapping("/orderinformation")
    public RepairOrder viewOrder(Long orderId){
        return adminService.viewOrder(orderId);
    }

    @RequestMapping("/updateStatus")
    public String updateOrderStatus(Long orderId,int newStatus){
        boolean success=adminService.updateOrderStatus(orderId,newStatus);
        return success?"状态更新成功！":"状态更新失败！";
    }

    @RequestMapping("/deleteOrder")
    public String deleteOrder(Long orderId){
        boolean success=adminService.deleteOrder(orderId);
        return success?"取消成功！":"取消失败！";
    }


}
