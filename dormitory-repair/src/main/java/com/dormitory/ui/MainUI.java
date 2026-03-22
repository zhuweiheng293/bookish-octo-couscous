package com.dormitory.ui;

import com.dormitory.entity.User;
import com.dormitory.service.UserService;
import com.dormitory.util.ConsoleUtil;

public class MainUI {
    private UserService userService=new UserService();
    private User nowUser=null;

    public void start(){
        while(true){
            if(nowUser==null){
                showLoginMenu();
            }else{
                if(nowUser.isStudent()){
                    new StudentUI(nowUser).showMenu();
                }else{
                    new AdminUI(nowUser).showMenu();
                }
                nowUser=null;
            }
        }
    }
    private void showLoginMenu() {
        System.out.println("\n===========================");
        System.out.println("宿舍报修管理系统");
        System.out.println("===========================");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.println("===========================");

        int choice=ConsoleUtil.readInt("请选择操作（输入 1-3）：");

        switch (choice){
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
            System.out.println("感谢使用，再见！");
            System.exit(0);  // 退出程序
            break;
            default:
                System.out.println("无效选择！");
            }
        }
        private void login() {
            System.out.println("\n===== 用户登录 =====");
            String userNo=ConsoleUtil.readString("请输入账号：");
            String password=ConsoleUtil.readPassword("请输入密码：");

            nowUser=userService.login(userNo,password);
        }

        private void register() {
            System.out.println("\n=====用户注册=====");
            int role=ConsoleUtil.readInt("请选择角色（1-学生，2-维修人员）：");

            String userNo=ConsoleUtil.readString("请输入学号/工号：");
            String password = ConsoleUtil.readPassword("请输入密码：");
            String confirmPwd = ConsoleUtil.readPassword("请确认密码：");

            boolean success= userService.register(userNo,password,confirmPwd,role);

            if(success){
                System.out.println("注册成功！请返回主界面登录!");
            }else{
                System.out.println("注册失败，请重试!");
            }
        }
    }

