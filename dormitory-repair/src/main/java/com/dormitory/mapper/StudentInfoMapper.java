package com.dormitory.mapper;

import com.dormitory.entity.StudentInfo;
import org.apache.ibatis.annotations.*;

public interface StudentInfoMapper {
    @Insert("INSERT INTO student_info(user_id, building, room, is_bound) " + "VALUES(#{userId}, #{building}, #{room}, #{isBound})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(StudentInfo studentInfo);

    @Select("SELECT * FROM student_info WHERE user_id=#{userId}")
    StudentInfo findUserId(Long userId);

    @Update("UPDATE student_info SET building=#{building},room=#{room},"+"is_bound=1 WHERE user_id=#{userId}")
    int boundDormitory(@Param("userId")Long userId,
                       @Param("building")String building,
                       @Param("room")String room);
}
