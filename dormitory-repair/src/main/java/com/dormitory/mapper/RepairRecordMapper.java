package com.dormitory.mapper;

import com.dormitory.entity.RepairRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RepairRecordMapper {
    @Insert("INSERT INTO repair_record(order_id, admin_id, action, remark) " + "VALUES(#{orderId}, #{adminId}, #{action}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RepairRecord record);

    @Select("SELECT * FROM repair_record WHERE order_id=#{order_id}"+"ORDER BY create_time DESC")
    List<RepairRecord> findByOrderId(Long orderId);
}
