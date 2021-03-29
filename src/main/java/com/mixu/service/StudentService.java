package com.mixu.service;

import com.mixu.mapper.StudentMapper;
import com.mixu.pojo.Student;
import com.mixu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public Student getStuById(Integer id){
        return studentMapper.getStu(id);
    }
}
