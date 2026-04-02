package com.dormitory.dormitoryrepairspringboot.controller;

import com.dormitory.dormitoryrepairspringboot.entity.User;
import com.dormitory.dormitoryrepairspringboot.service.UserService;
import com.dormitory.dormitoryrepairspringboot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register(String userNo,String name,String phone, String password, String confirmPwd, int role){
        boolean success=userService.register(userNo,name,phone,password,confirmPwd,role);
        return success?"注册成功！":"注册失败！";
    }

    @PostMapping("/login")
    public Map<String, Object> login(String userNo, String password){
        User user=userService.login(userNo,password);
        Map<String,Object> result=new HashMap<>();

        if(user!=null){
            Map<String,Object> claims=new HashMap<>();
            claims.put("userId",user.getId());
            claims.put("userNo",user.getUserNo());
            claims.put("role",user.getRole());

            String token= JwtUtil.genToken(claims);

            result.put("success",true);
            result.put("token",token);
            result.put("user",user);
        }else{
            result.put("success",false);
            result.put("message","账号或密码错误!");
        }
        return result;
    }

    @RequestMapping("/password")
    public String changePassword(Long userId, String oldPwd, String newPwd, String confirmPwd){
        boolean success=userService.changePassword(userId,oldPwd,newPwd,confirmPwd);
        return success?"密码修改成功！":"密码修改失败！";
    }

    @RequestMapping("/name")
    public String updateName(Long userId,String newName){
        boolean success=userService.updateName(userId,newName);
        return success?"昵称修改成功！":"昵称修改失败！";
    }

    @RequestMapping("/phone")
    public String updatePhone(Long userId,String newPhone){
        boolean success=userService.updatePhone(userId,newPhone);
        return success?"手机号修改成功！":"手机号修改失败！";
    }

    @RequestMapping("/information")
    public User view(Long userId){
        return userService.view(userId);
    }

}
