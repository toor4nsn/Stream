package com.example.service;


import com.example.mapper.StudentMapper;
import com.example.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AService {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    BService bService;

    @Transactional(rollbackFor = Exception.class)
    public void insertA() {
        Student s = new Student();
        s.setId(14);
        s.setAge(14);
        s.setName("A service");
        studentMapper.insert(s);
        try {
            bService.insertB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
