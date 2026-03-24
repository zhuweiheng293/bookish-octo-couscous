package com.dormitory.service;

import com.dormitory.entity.User;
import com.dormitory.mapper.UserMapper;
import com.dormitory.util.MyBatisUtil;

public class UserService {
    public boolean register(String userNo,String name,String phone, String password, String confirmPwd, int role) {
        System.out.println("========== 开始注册 ==========");
        System.out.println("账号: " + userNo);

        boolean passwordCompare=password.equals(confirmPwd);
        if(!passwordCompare){
            System.out.println("两次输入的密码不一致！");
            return false;
        }

        boolean isStudentUser=(role==1);
        boolean isAdminUser=(role==2);

        if(isStudentUser){
            boolean startsWith3125=userNo.startsWith("3125");
            boolean startsWith3225=userNo.startsWith("3225");

            if(!(startsWith3125||startsWith3225)){
                System.out.println("学号格式不正确！必须以3125或3225开头");
                return false;
            }
        } else if (isAdminUser) {
            boolean startsWith0025=userNo.startsWith("0025");
            if(!startsWith0025){
                System.out.println("工号格式不正确！必须以0025开头");
                return false;
            }
        }

        UserMapper mapper=MyBatisUtil.getMapper(UserMapper.class);
        User userIsExist= mapper.findByUserNo(userNo);
        boolean existUser=(userIsExist!=null);

        if(existUser){
            System.out.println("账号已存在！");
            return  false;
        }

        User user = new User();
        user.setUserNo(userNo);
        user.setPassword(password);
        user.setRole(role);
        user.setName(name);
        user.setPhone(phone);


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

    public boolean changePassword(Long userId, String oldPwd, String newPwd, String confirmPwd) {

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
        System.out.println("账号："+user.getUserNo());
        System.out.println("昵称："+(user.getName()!=null?user.getName():"未设置"));
        System.out.println("手机号："+(user.getPhone()!=null?user.getPhone():"未设置"));
        System.out.println("角色："+(user.isStudent()?"学生":"管理员"));
        System.out.println("==========================\n");
    }

    public boolean updateName(Long userId,String newName){
        if(newName==null||newName.trim().isEmpty()){
            System.out.println("昵称不能为空！");
            return false;
        }
        UserMapper mapper=MyBatisUtil.getMapper(UserMapper.class);
        int result= mapper.updateName(userId,newName);
        return result>0;
    }

    public boolean updatePhone(Long userId,String newPhone){
        if(newPhone==null||newPhone.trim().isEmpty()){
            System.out.println("手机号不能为空！");
            return false;
        }
        UserMapper mapper=MyBatisUtil.getMapper(UserMapper.class);
        int result= mapper.updatePhone(userId,newPhone);
        return result>0;
    }
}
