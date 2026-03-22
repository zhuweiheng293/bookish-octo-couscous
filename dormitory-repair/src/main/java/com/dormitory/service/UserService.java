package com.dormitory.service;

import com.dormitory.entity.User;
import com.dormitory.mapper.UserMapper;
import com.dormitory.util.MyBatisUtil;

public class UserService {
    public boolean register(String userNo, String password, String confirmPwd, int role) {
        System.out.println("========== 开始注册 ==========");
        System.out.println("账号: " + userNo);

        // 1. 检查两次密码是否一致
        if (!password.equals(confirmPwd)) {
            System.out.println("两次输入的密码不一致！");
            return false;
        }

        // 2. 根据角色检查账号格式
        if (role == 1) {
            if (!userNo.startsWith("3125") && !userNo.startsWith("3225")) {
                System.out.println("学号格式不正确！必须以3125或3225开头");
                return false;
            }
        } else if (role == 2) {
            if (!userNo.startsWith("0025")) {
                System.out.println("工号格式不正确！必须以0025开头");
                return false;
            }
        }

        // ========== 临时注释掉检查账号存在的代码 ==========
        // 3. 检查账号是否已存在
        // UserMapper mapper = MyBatisUtil.getMapper(UserMapper.class);
        // User existUser = mapper.findByUserNo(userNo);
        // System.out.println("查询结果: " + (existUser == null ? "null (不存在)" : "存在! ID=" + existUser.getId()));
        // if (existUser != null) {
        //     System.out.println("账号已存在！");
        //     return false;
        // }

        // 4. 创建新用户
        User user = new User();
        user.setUserNo(userNo);
        user.setPassword(password);
        user.setRole(role);
        user.setName("新用户");

        // 5. 插入数据库
        UserMapper mapper = MyBatisUtil.getMapper(UserMapper.class);
        int result = mapper.insert(user);
        System.out.println("插入结果: " + result);
        System.out.println("========== 注册结束 ==========");

        return result > 0;
    }

    public User login(String userNo,String password){
        UserMapper mapper=MyBatisUtil.getMapper(UserMapper.class);
        User user=mapper.login(userNo,password);

        if(user!=null){
            System.out.println("登陆成功！");
        }else{
            System.out.println("账号或密码错误！");
        }

        return user;
    }

    public boolean changePassword(Long userId,String oldPwd,String newPwd,String confirmPwd){

        UserMapper mapper=MyBatisUtil.getMapper(UserMapper.class);

        User user=mapper.findById(userId);
        if(user==null){
            System.out.println("用户不存在！");
            return false;
        }

        if(!user.getPassword().equals(oldPwd)){
            System.out.println("原密码错误！");
            return false;
        }

        if(!newPwd.equals(confirmPwd)){
            System.out.println("两次输入的密码不一致！");
            return false;
        }

        int result= mapper.updatePassword(userId,newPwd);

        return result>0;
    }

    public void view(Long userId){
        UserMapper mapper=MyBatisUtil.getMapper(UserMapper.class);

        User user= mapper.findById(userId);

        System.out.println("\n======== 个人信息 ========");
        System.out.println("账号"+user.getUserNo());
        System.out.println("姓名"+(user.getName()!=null?user.getName():"未设置"));
        System.out.println("手机号"+(user.getPhone()!=null?user.getPhone():"未设置"));
        System.out.println("角色"+(user.isStudent()?"学生":"管理员"));
        System.out.println("==========================\n");
    }

}
