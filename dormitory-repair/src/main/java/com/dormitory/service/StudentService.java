package com.dormitory.service;

import com.dormitory.entity.RepairOrder;
import com.dormitory.entity.StudentInfo;
import com.dormitory.mapper.RepairOrderMapper;
import com.dormitory.mapper.StudentInfoMapper;
import com.dormitory.util.MyBatisUtil;

import java.util.List;

public class StudentService {
    public boolean bindDormitory(Long userId,String building,String room) {
        StudentInfoMapper mapper=MyBatisUtil.getMapper(StudentInfoMapper.class);
        StudentInfo info= mapper.findUserId(userId);

        if(info==null){
            info=new StudentInfo();
            info.setUserId(userId);
            info.setBuilding(building);
            info.setRoom(room);
            info.setIsBound(1);

            return  mapper.insert(info)>0;
        }else{
            return mapper.boundDormitory(userId, building, room)>0;
        }
    }


    public boolean isBound(Long userId) {
        StudentInfoMapper mapper=MyBatisUtil.getMapper(StudentInfoMapper.class);
        StudentInfo info= mapper.findUserId(userId);

        return info!=null&&info.getIsBound()==1;
    }

    public boolean createRepairOrder(Long studentId,String deviceType,String description){
        RepairOrderMapper mapper=MyBatisUtil.getMapper(RepairOrderMapper.class);
        RepairOrder order=new RepairOrder();
        order.setOrderNo(RepairOrder.generateOrderNo());
        order.setStudentId(studentId);
        order.setDeviceType(deviceType);
        order.setDescription(description);
        order.setStatus(0);
        order.setPriority(1);

        return mapper.insert(order)>0;
    }

    public List<RepairOrder>getMyRepairOrder(Long studentId){
        RepairOrderMapper mapper=MyBatisUtil.getMapper(RepairOrderMapper.class);
        return mapper.findByStudentId(studentId);
    }

    public boolean deleteRepairOrder(Long orderId,Long studentId){
        RepairOrderMapper mapper=MyBatisUtil.getMapper(RepairOrderMapper.class);

        RepairOrder order= mapper.findById(orderId);

        if(order==null){
            System.out.println("报修单不存在！");
            return false;
        }

        if(!order.getStudentId().equals(studentId)){
            System.out.println("无权修改他人的报修单！");
            return false;
        }

        if(order.getStatus()!=0){
            System.out.println("只能取消待处理的报修单！");
            return false;
        }
        return mapper.updateStatus(orderId,3)>0;
    }
}
