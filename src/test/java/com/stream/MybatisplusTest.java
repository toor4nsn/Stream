package com.stream;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.App;
import com.example.mapper.StudentMapper;
import com.example.po.Student;
import com.example.util.RedisHandleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author Liwei
 * @Date 2022/4/23 13:54
 */
@SpringBootTest(classes = App.class)
public class MybatisplusTest {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void insertTest(){
        Student s = new Student();
        s.setName("Frainbow");
        s.setAge(27);
        int i = studentMapper.insert(s);
        System.out.println(i);
    }
    @Test
    public void selectTest(){

        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        String name="toor4nsn";

        wrapper.eq(Student::getName,name);
        List<Student> list = studentMapper.selectList(wrapper);
        System.out.println(JSON.toJSONString(list));

    }
    @Test
    public void QueryWrapperTest(){
        LambdaQueryWrapper<Student> wrapper1 = new QueryWrapper<Student>().lambda().eq(Student::getAge, 20);
        System.out.println(studentMapper.selectList(wrapper1));
        LambdaQueryWrapper<Student> gt = new QueryWrapper<Student>().lambda().gt(Student::getAge, 25);
        System.out.println(studentMapper.selectList(gt));
        LambdaQueryWrapper<Student> likeRight = new QueryWrapper<Student>().lambda().likeRight(Student::getName, "toor");
        System.out.println(studentMapper.selectList(likeRight));
    }

    @Test
    public void UpdateWrapperTest(){
        Student s1 = studentMapper.selectById(1);
        s1.setName("Nokia");
        // ==>  Preparing: UPDATE student SET name=?, age=? WHERE id=?
        // ==> Parameters: Nokia(String), 20(Integer), 1(Integer)
        int i = studentMapper.updateById(s1);
        System.out.println(i);


        ///////////////UpdateWrapper
        LambdaUpdateWrapper<Student> updateWrapper = new UpdateWrapper<Student>().lambda().eq(Student::getName, "Frainbow");
        Student s2 = new Student();
        s2.setAge(1);
        //==>  Preparing: UPDATE student SET age=? WHERE (name = ?)
        //==> Parameters: 1(Integer), Frainbow(String)
        int i2 = studentMapper.update(s2, updateWrapper);
        System.out.println(i2);

    }

    @Test
    public void sdhnxhd(){
        Student s = new Student();
        s.setAge(20);
        s.setName("toor4nsn");
        int row = studentMapper.insert(s);
        System.out.println(row);
    }
    @Autowired
    private RedisHandleService redisHandleService;

    @Test
    public void deleteTest(){
        redisHandleService.del("toor","nsn");
    }
}
