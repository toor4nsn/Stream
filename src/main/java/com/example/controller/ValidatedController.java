package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.ResultDTO;
import com.example.mapper.StudentMapper;
import com.example.po.Student;
import com.example.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@Validated
public class ValidatedController {

   @Autowired
    StudentMapper mapper;

    /**
     * GET散装参数校验：ConstraintViolationException
     * @param id
     * @return
     */
    @GetMapping("getUserById")
    public ResultDTO<Student> getUser(@NotNull(message = "学生id不能为空") Integer id) {
        Student student = mapper.selectById(id);
        return ResultDTO.success(student);
    }


    /**
     * 如果都是用DTO包装参数，那么Controller可以不加@Validated（但建议还是都加上吧）
     * 参数列表里用@Validated或@Valid都可以
     *
     * @param student
     * @return
     */
    @GetMapping("getStudent")
    public ResultDTO<List<Student>> getStudent(@Validated Student student) {
        log.info("student:{}", student);
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(student.getId() != null, Student::getId, student.getId())
                .eq(student.getName() != null, Student::getName, student.getName())
                .eq(student.getAge() != null, Student::getAge, student.getAge());
        List<Student> res = mapper.selectList(wrapper);
        return ResultDTO.success(res);
    }

    @PostMapping("insertStudent")
    public ResultDTO insertStudent(@Validated @RequestBody Student student) {
        int res = mapper.insert(student);
        return ResultDTO.success(res);
    }

    @PostMapping("updateStudent")
    public ResultDTO updateStudent(@RequestBody Student student) {
        ValidationUtils.validateReq(student, true);
        int res = mapper.updateById(student);
        return ResultDTO.success(res);
    }
}
