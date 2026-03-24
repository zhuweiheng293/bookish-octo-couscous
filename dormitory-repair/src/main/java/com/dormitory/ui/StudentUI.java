package com.dormitory.ui;

import com.dormitory.entity.RepairOrder;
import com.dormitory.entity.User;
import com.dormitory.service.StudentService;
import com.dormitory.service.UserService;
import com.dormitory.util.ConsoleUtil;

import java.util.List;

public class StudentUI {

    private User nowUser;
    private StudentService studentService;
    private UserService userService;

    public StudentUI(User user) {
        this.nowUser = user;
        this.studentService = new StudentService();
        this.userService = new UserService();

        if (!studentService.isBound(nowUser.getId())) {
            System.out.println("\n请先绑定宿舍!");
            bindDormitory();
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== 学生菜单 =====");
            System.out.println("1. 绑定/修改宿舍");
            System.out.println("2. 创建报修单");
            System.out.println("3. 查看我的报修记录");
            System.out.println("4. 取消报修单");
            System.out.println("5. 修改密码");
            System.out.println("6. 查看个人信息");
            System.out.println("7. 退出");
            System.out.println("====================");

            int choice = ConsoleUtil.readInt("请选择操作（输入 1-7）：");

            switch (choice) {
                case 1:
                    bindDormitory();
                    break;
                case 2:
                    createRepairOrder();
                    break;
                case 3:
                    viewMyOrders();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    userService.view(nowUser.getId());

                    boolean wantToUpdate = ConsoleUtil.confirm("是否更改个人信息？");
                    if (wantToUpdate) {
                        updateInfo();
                    }
                    break;
                case 7:
                    System.out.println("正在退出...");
                    return;
                default:
                    System.out.println("无效选择！");
            }
        }
    }

    private void bindDormitory() {
        System.out.println("\n===== 绑定宿舍 =====");
        String building = ConsoleUtil.readString("请输入楼栋号（如：A栋）：");
        String room = ConsoleUtil.readString("请输入房间号（如：101）：");

        if (studentService.bindDormitory(nowUser.getId(), building, room)) {
            System.out.println("宿舍绑定成功！");
        } else {
            System.out.println("绑定失败！");
        }
    }

    private void createRepairOrder() {
        System.out.println("\n===== 创建报修单 =====");

        // 选择设备类型
        System.out.println("设备类型：");
        System.out.println("1. 水龙头");
        System.out.println("2. 马桶");
        System.out.println("3. 灯管");
        System.out.println("4. 其他");

        int typeChoice = ConsoleUtil.readInt("请选择设备类型（1-4）：");

        String deviceType;
        switch (typeChoice) {
            case 1:
                deviceType = "水龙头";
                break;
            case 2:
                deviceType = "马桶";
                break;
            case 3:
                deviceType = "灯管";
                break;
            default:
                deviceType = ConsoleUtil.readString("请输入设备类型：");
        }

        String description = ConsoleUtil.readString("请描述问题：");

        if (studentService.createRepairOrder(nowUser.getId(), deviceType, description)) {
            System.out.println("报修单创建成功！");
        } else {
            System.out.println("创建失败！");
        }
    }

    private void viewMyOrders() {
        List<RepairOrder> orders = studentService.getMyRepairOrder(nowUser.getId());

        boolean hasOrders = !orders.isEmpty();

        if (!hasOrders) {
            System.out.println("暂无报修记录！");
            return;
        }

        System.out.println("\n======== 我的报修记录 ========");
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

        int choice = ConsoleUtil.readInt("\n输入序号查看详情，输入0返回：");
        if (choice > 0 && choice <= orders.size()) {
            RepairOrder order = orders.get(choice - 1);
            System.out.println("\n======== 报修单详情 ========");
            System.out.println("单号：" + order.getOrderNo());
            System.out.println("设备类型：" + order.getDeviceType());
            System.out.println("问题描述：" + order.getDescription());
            System.out.println("状态：" + order.getStatusText());
            System.out.println("创建时间：" + order.getCreateTime());
            System.out.println("更新时间：" + order.getUpdateTime());
            System.out.println("============================");
        }
    }

    private void deleteOrder() {
        List<RepairOrder> orders = studentService.getMyRepairOrder(nowUser.getId());

        if (orders.isEmpty()) {
            System.out.println("暂无报修记录！");
            return;
        }

        System.out.println("\n======== 可取消的报修单 ========");
        int index = 1;
        for (RepairOrder order : orders) {
            if (order.getStatus() == 0) {
                System.out.printf("%d. %s - %s%n", index++, order.getOrderNo(), order.getDeviceType());
            }
        }

        if (index == 1) {
            System.out.println("没有可取消的报修单！");
            return;
        }

        int choice = ConsoleUtil.readInt("请选择要取消的报修单序号（输入0返回）：");
        if (choice > 0 && choice <= orders.size()) {
            RepairOrder order = orders.get(choice - 1);
            if (order.getStatus() == 0) {
                if (studentService.deleteRepairOrder(order.getId(), nowUser.getId())) {
                    System.out.println("取消成功！");
                } else {
                    System.out.println("取消失败！");
                }
            } else {
                System.out.println("只能取消待处理的报修单！");
            }
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

    private void updateInfo() {
        while (true) {
            System.out.println("\n====修改个人数据====");
            System.out.println("1.修改昵称");
            System.out.println("2.修改手机号");
            System.out.println("3.返回");

            int choice = ConsoleUtil.readInt("请选择操作（输入1-3）：");

            if (choice == 1) {
                String newName = ConsoleUtil.readString("请输入新昵称：");
                boolean success = userService.updateName(nowUser.getId(), newName);
                if (success) {
                    System.out.println("昵称修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
            } else if(choice==2){
                String newPhone=ConsoleUtil.readString("请输入新的手机号：");
                boolean success=userService.updatePhone(nowUser.getId(),newPhone);
                if(success){
                    System.out.println("手机号修改成功！");
                }else {
                    System.out.println("修改失败！");
                }
            } else if (choice==3) {
                break;
            }else {
                System.out.println("无效输入！");
            }
        }
    }
}




