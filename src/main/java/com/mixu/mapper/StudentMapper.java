package com.mixu.mapper;

import com.mixu.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

//告诉Mybatis该接口就是一个Mapper用来操作数据库
@Mapper
public interface StudentMapper {

    public Student getStu(Integer id);
}
