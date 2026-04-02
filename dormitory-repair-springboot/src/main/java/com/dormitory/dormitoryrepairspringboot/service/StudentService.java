package com.dormitory.dormitoryrepairspringboot.service;

import com.dormitory.dormitoryrepairspringboot.entity.RepairOrder;
import com.dormitory.dormitoryrepairspringboot.entity.StudentInfo;
import com.dormitory.dormitoryrepairspringboot.mapper.RepairOrderMapper;
import com.dormitory.dormitoryrepairspringboot.mapper.StudentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentInfoMapper studentInfoMapper;

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    public boolean bindDormitory(Long userId,String building,String room) {

        StudentInfo info= studentInfoMapper.findUserId(userId);

        if(info==null){
            info=new StudentInfo();
            info.setUserId(userId);
            info.setBuilding(building);
            info.setRoom(room);
            info.setIsBound(1);

            return  studentInfoMapper.insert(info)>0;
        }else{
            return studentInfoMapper.boundDormitory(userId, building, room)>0;
        }
    }


    public boolean isBound(Long userId) {

        StudentInfo info= studentInfoMapper.findUserId(userId);

        return info!=null&&info.getIsBound()==1;
    }

    public boolean createRepairOrder(Long studentId,String deviceType,String description,String uploadPhoto){

        RepairOrder order=new RepairOrder();
        order.setOrderNo(RepairOrder.generateOrderNo());
        order.setStudentId(studentId);
        order.setDeviceType(deviceType);
        order.setDescription(description);
        order.setStatus(0);
        order.setPriority(1);
        order.setUploadPhoto(uploadPhoto);

        return repairOrderMapper.insert(order)>0;
    }

    public List<RepairOrder>getMyRepairOrder(Long studentId){

        return repairOrderMapper.findByStudentId(studentId);
    }

    public boolean deleteRepairOrder(Long orderId,Long studentId){


        RepairOrder order= repairOrderMapper.findById(orderId);
        boolean orderIsExist=(order!=null);

        if(!orderIsExist){
            System.out.println("报修单不存在！");
            return  false;
        }

        Long orderStudentId= order.getStudentId();
        boolean isOwnOrder=orderStudentId.equals(studentId);

        if (!isOwnOrder) {
            System.out.println("无权操作他人的报修单！");
            return false;
        }

        int nowStatus= order.getStatus();
        boolean Processing=(nowStatus==0);

        if(!Processing){
            System.out.println("只能取消待处理的报修单");
            return false;
        }


        return repairOrderMapper.updateStatus(orderId,3)>0;
    }
}
