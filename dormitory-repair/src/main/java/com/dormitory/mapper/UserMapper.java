package com.dormitory.mapper;

import com.dormitory.entity.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Insert("INSERT INTO user(user_no, password, role, name,phone) " +
            "VALUES(#{userNo}, #{password}, #{role}, #{name}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE user_no = #{userNo}")
    User findByUserNo(String userNo);

    @Select("SELECT * FROM user WHERE user_no = #{userNo} AND password = #{password}")
    User login(@Param("userNo") String userNo, @Param("password") String password);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE user SET name = #{name} WHERE id = #{id}")
    int updateName(@Param("id")Long id,@Param("name") String name);

    @Update("UPDATE user SET phone = #{phone} WHERE id = #{id}")
    int updatePhone(@Param("id")Long id,@Param("phone")String phone);

}