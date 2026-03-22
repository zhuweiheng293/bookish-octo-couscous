package com.dormitory.ui;

import com.dormitory.entity.RepairOrder;
import com.dormitory.entity.User;
import com.dormitory.service.AdminService;
import com.dormitory.service.UserService;
import com.dormitory.util.ConsoleUtil;

import java.util.List;

public class AdminUI {
    private User nowUser;
    private AdminService adminService;
    private UserService userService;

    public AdminUI(User user){
        this.nowUser=user;
        this.adminService = new AdminService();
        this.userService = new UserService();
    }


    public void showMenu() {
        while (true) {
            System.out.println("\n===== 管理员菜单 =====");
            System.out.println("1. 查看所有报修单");
            System.out.println("2. 查看报修单详情");
            System.out.println("3. 更新报修单状态");
            System.out.println("4. 删除报修单");
            System.out.println("5. 修改密码");
            System.out.println("6. 查看个人信息");
            System.out.println("7. 退出");
            System.out.println("=====================");

            int choice = ConsoleUtil.readInt("请选择操作（输入 1-7）：");

            switch (choice) {
                case 1:
                    viewAllOrders();
                    break;
                case 2:
                    viewOrderDetail();
                    break;
                case 3:
                    updateOrderStatus();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    userService.view(nowUser.getId());
                    break;
                case 7:
                    System.out.println("正在退出...");
                    return;
                default:
                    System.out.println("无效选择！");
            }
        }
    }

    private void viewAllOrders() {
        System.out.println("\n请选择筛选方式：");
        System.out.println("1. 查看全部");
        System.out.println("2. 按状态筛选");
        int choice = ConsoleUtil.readInt("请选择（1-2）：");

        List<RepairOrder> orders;
        if (choice == 2) {
            System.out.println("状态码：0-待处理 1-处理中 2-已完成 3-已取消");
            int status = ConsoleUtil.readInt("请输入状态码：");
            orders = adminService.getOrdersByStatus(status);
        } else {
            orders = adminService.getAllOrders();
        }

        if (orders.isEmpty()) {
            System.out.println("暂无报修单！");
            return;
        }

        System.out.println("\n======== 报修单列表 ========");
        System.out.printf("%-6s %-20s %-15s %-10s %-10s%n",
                "序号", "单号", "设备类型", "状态", "创建时间");
        System.out.println("--------------------------------------------------------");

        int index = 1;
        for (RepairOrder order : orders) {
            System.out.printf("%-6d %-20s %-15s %-10s %-10s%n",
                    index++,
                    order.getOrderNo(),
                    order.getDeviceType(),
                    order.getStatusText(),
                    order.getCreateTime().toString().substring(0, 19));
        }
    }

    private void viewOrderDetail() {
        Long orderId = ConsoleUtil.readLong("请输入报修单ID：");
        adminService.viewOrder(orderId);
    }

    private void updateOrderStatus() {
        Long orderId = ConsoleUtil.readLong("请输入报修单ID：");

        System.out.println("状态码：0-待处理 1-处理中 2-已完成 3-已取消");
        System.out.println("当前状态请先查看详情");
        int status = ConsoleUtil.readInt("请输入新状态码：");

        if (adminService.updateOrderStatus(orderId, status)) {
            System.out.println("状态更新成功！");
        } else {
            System.out.println("更新失败！");
        }
    }

    private void deleteOrder() {
        Long orderId = ConsoleUtil.readLong("请输入报修单ID：");
        boolean confirm = ConsoleUtil.confirm("确定要删除该报修单吗？");

        if (confirm && adminService.deleteOrder(orderId)) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败或已取消！");
        }
    }

    private void changePassword() {
        System.out.println("\n===== 修改密码 =====");
        String oldPwd = ConsoleUtil.readPassword("请输入原密码：");
        String newPwd = ConsoleUtil.readPassword("请输入新密码：");
        String confirmPwd = ConsoleUtil.readPassword("请确认新密码：");

        if (userService.changePassword(nowUser.getId(), oldPwd, newPwd, confirmPwd)) {
            System.out.println("密码修改成功！请重新登录。");
            nowUser = null;
        } else {
            System.out.println("密码修改失败！");
        }
    }
}


