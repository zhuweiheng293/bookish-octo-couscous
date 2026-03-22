package com.dormitory.mapper;

import com.dormitory.entity.RepairOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RepairOrderMapper {
    @Insert("INSERT INTO repair_order(order_no, student_id, device_type, description, " + "status, priority) VALUES(#{orderNo}, #{studentId}, #{deviceType}, " + "#{description}, #{status}, #{priority})")
    int insert(RepairOrder order);

    @Select("SELECT * FROM repair_order WHERE student_id = #{studentId} ORDER BY create_time DESC")
    List<RepairOrder> findByStudentId(Long studentId);

    @Select("SELECT * FROM repair_order WHERE id = #{id}")
    RepairOrder findById(Long id);

    @Select("SELECT * FROM repair_order ORDER BY create_time DESC")
    List<RepairOrder> findAll();

    @Select("SELECT * FROM repair_order WHERE status = #{status} " +
            "ORDER BY create_time DESC")
    List<RepairOrder> findByStatus(Integer status);

    @Update("UPDATE repair_order SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM repair_order WHERE id = #{id}")
    int deleteById(Long id);

}
