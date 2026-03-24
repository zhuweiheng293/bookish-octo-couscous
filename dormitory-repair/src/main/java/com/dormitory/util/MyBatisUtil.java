package com.dormitory.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory;
    private static boolean initialized = false;

    static {
        init();
    }

    private static synchronized void init() {
        if (initialized) {
            return;
        }

        try {
            //System.out.println("========== MyBatis 初始化开始 ==========");

            Properties props = new Properties();
            InputStream propsStream = Resources.getResourceAsStream("mybatis.properties");
            props.load(propsStream);

            //System.out.println("数据库URL: " + props.getProperty("jdbc.url"));
            //System.out.println("用户名: " + props.getProperty("jdbc.username"));

            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);

            try (SqlSession session = sqlSessionFactory.openSession()) {
                //System.out.println("数据库连接测试成功！");
            }

            initialized = true;
            //System.out.println("========== MyBatis 初始化成功 ==========");
        } catch (Exception e) {
            System.out.println("========== MyBatis 初始化失败 ==========");
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        if (sqlSessionFactory == null) {
            init();
        }
        return sqlSessionFactory.openSession(true);
    }

    public static <T> T getMapper(Class<T> mapperClass) {
        return getSqlSession().getMapper(mapperClass);
    }
}