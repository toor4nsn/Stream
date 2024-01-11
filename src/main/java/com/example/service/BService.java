package com.example.service;


import com.example.mapper.StudentMapper;
import com.example.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BService {
    @Autowired
    StudentMapper studentMapper;

    @Transactional(rollbackFor = Exception.class)
    public void insertB() throws Exception{
        Student s2 = new Student();
        s2.setId(16);
        s2.setAge(16);
        s2.setName("B service");
        studentMapper.insert(s2);
        int i = 1 / 0;
    }
}
