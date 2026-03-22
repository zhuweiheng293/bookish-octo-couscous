package com.dormitory;

import com.dormitory.util.MyBatisUtil;

class TestConnection {
    public static void main(String[] args) {
        System.out.println("正在测试数据库连接...");

        try {
            MyBatisUtil.getSqlSession();
        } catch (Exception e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }

        System.out.println("测试完成！");
    }
}
