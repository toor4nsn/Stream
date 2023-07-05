package com.example.controller;

import com.example.dto.ResultDTO;
import com.example.mapper.StudentMapper;
import com.example.po.Student;
import com.github.benmanes.caffeine.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class CaffeineController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    Cache<String, Object> caffeineCache;


    @PostMapping("caffeine/add")
    public ResultDTO addStudent(@RequestBody Student student){
        int i = studentMapper.insert(student);
        caffeineCache.put(String.valueOf(student.getId()),student);
        return ResultDTO.success(i);
    }

    @PostMapping("caffeine/update")
    public ResultDTO updateStudent(@RequestBody Student student){
        int i = studentMapper.updateById(student);
        caffeineCache.put(String.valueOf(student.getId()),student);
        return ResultDTO.success(i);
    }

    @GetMapping("caffeine/delete")
    public ResultDTO deleteStudent(@RequestParam Integer id){
        int i = studentMapper.deleteById(id);
        // 移除一个缓存元素
        caffeineCache.invalidate(String.valueOf(id));
        return ResultDTO.success(i);
    }


    @GetMapping("caffeine/select")
    public ResultDTO CaffeineCacheTestOne(@RequestParam Integer id){
        //cache.get(key, k -> value) 操作来在缓存中不存在该key对应的缓存元素的时候进行计算生成并直接写入至缓存内
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        Student stu = (Student) caffeineCache.get(String.valueOf(id),
                (key) -> studentMapper.selectById(key));
        return ResultDTO.success(stu);
    }

    @GetMapping("caffeine/selectTwo")
    public ResultDTO CaffeineCacheTestTwo(@RequestParam Integer id){
        //cache.get(key, k -> value) 操作来在缓存中不存在该key对应的缓存元素的时候进行计算生成并直接写入至缓存内
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        Student stu = (Student) caffeineCache.get(String.valueOf(id),
                (key) -> studentMapper.selectById(key));
        return ResultDTO.success(stu);
    }






    @Autowired
    LoadingCache<String, Object> caffeineLoadingCache;
    @GetMapping("caffeine/loadingCache")
    public ResultDTO CaffeineCacheLoadingCache(@RequestParam Integer id){
        //cache.get(key, k -> value) 操作来在缓存中不存在该key对应的缓存元素的时候进行计算生成并直接写入至缓存内
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        Student student = (Student) caffeineLoadingCache.get(String.valueOf(id));
        return ResultDTO.success(student);
    }




    @Autowired
    AsyncCache<String, Object> caffeineAsyncCache;

    @GetMapping("caffeine/async")
    public ResultDTO CaffeineCacheTestAsyncCache(@RequestParam Integer id) throws ExecutionException, InterruptedException {
        @NonNull CompletableFuture<Object> future = caffeineAsyncCache.get(String.valueOf(id), key -> studentMapper.selectById(key));
        Student stu = (Student) future.get();
        return ResultDTO.success(stu);
    }

}
