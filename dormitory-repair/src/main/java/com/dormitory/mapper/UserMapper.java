package com.dormitory.mapper;

import com.dormitory.entity.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Insert("INSERT INTO user(user_no, password, role, name) " +
            "VALUES(#{userNo}, #{password}, #{role}, #{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE user_no = #{userNo}")
    User findByUserNo(String userNo);  // 参数类型是 String

    @Select("SELECT * FROM user WHERE user_no = #{userNo} AND password = #{password}")
    User login(@Param("userNo") String userNo, @Param("password") String password);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}