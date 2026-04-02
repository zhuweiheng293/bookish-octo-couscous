package com.dormitory.dormitoryrepairspringboot.controller;

import com.dormitory.dormitoryrepairspringboot.entity.RepairOrder;
import com.dormitory.dormitoryrepairspringboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Value("${file.upload.path:./upload/}")
    private String uploadPath;

    //绑定宿舍
    @RequestMapping("/bind")
    public String bindDormitory(Long userId,String building,String room){
        boolean success=studentService.bindDormitory(userId,building,room);
        return success?"绑定成功!":"绑定失败！";
    }

    //检查是否绑定宿舍
    @RequestMapping("/isBound")
    public boolean isBound(Long userId){
        return studentService.isBound(userId);
    }

    //创建报修单
    @RequestMapping("/create")
    public String createRepairOrder(Long studentId, String deviceType, String description, MultipartFile upload_photo){
        String photo=null;
        if(upload_photo!=null&&!upload_photo.isEmpty()){
            try{
                String fileName= UUID.randomUUID().toString();

                File uploadDir=new File(uploadPath);
                if(!uploadDir.exists()){
                    uploadDir.mkdirs();
                }

                upload_photo.transferTo(new File(uploadPath+fileName));
                photo="/upload/"+fileName;

            }catch (IOException e){
                e.printStackTrace();
                return "图片上传失败！";
            }
        }
        boolean success=studentService.createRepairOrder(studentId,deviceType,description,photo);
        return success?"创建成功！":"创建失败！";
    }

    //取消报修单
    @RequestMapping("/delete")
    public String deleteRepairOrder(Long orderId,Long studentId){
        boolean success=studentService.deleteRepairOrder(orderId,studentId);
        return success?"取消成功！":"取消失败！";
    }

    //查看报修记录
    @RequestMapping("/repair_history")
    public List<RepairOrder> getMyRepairOrder(Long studentId){
        return studentService.getMyRepairOrder(studentId);
    }


}
