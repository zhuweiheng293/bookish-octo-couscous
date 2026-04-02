package com.dormitory.dormitoryrepairspringboot;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testGen(){
        Map<String,Object> header=new HashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");
        //生成Jwt的代码
        String token=JWT.create()
                .withHeader(header)
                .withClaim("userId",1L)//添加载荷:用户Id
                .withClaim("userNo","3125006666")//添加载荷：学号
                .withClaim("role",1)//添加载荷：角色
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))//添加过期时间
                .sign(Algorithm.HMAC256("dormitory-repair-secret"));//配置密钥
        System.out.println("生成的Token:");
        System.out.println(token);
    }

    @Test
    public void testParse(){
        //定义字符串，模拟用户传递过来的token
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJ1c2VySWQiOjEsInVzZXJObyI6IjMxMjUwMDY2NjYiLCJyb2xlIjoxLCJleHAiOjE3NzQ4MTMyOTJ9" +
                ".s6axtysvwzahGdWjHX5-bH4760Ey_1DJBegigXsEH8E";

        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256("dormitory-repair-secret")).build();

        DecodedJWT decodedJWT=jwtVerifier.verify(token);//解析token，生成一个解析后的JWT对象
        String header = decodedJWT.getHeader();

        Long userId = decodedJWT.getClaim("userId").asLong();
        String userNo = decodedJWT.getClaim("userNo").asString();
        Integer role = decodedJWT.getClaim("role").asInt();

        System.out.println("头部信息: " + header);
        System.out.println("用户ID: " + userId);
        System.out.println("学号: " + userNo);
        System.out.println("角色: " + (role == 1 ? "学生" : "管理员"));
        System.out.println("过期时间: " + decodedJWT.getExpiresAt());
    }
}
