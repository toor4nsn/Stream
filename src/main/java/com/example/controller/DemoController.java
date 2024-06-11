package com.example.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.config.CaffeineConfig;
import com.example.dto.ResultDTO;
import com.example.entity.JsonExample;
import com.example.entity.JsonExampleVO;
import com.example.enums.ExceptionCodeEnum;
import com.example.mapper.JsonExampleMapper;
import com.example.mapper.StudentMapper;
import com.example.po.Result;
import com.example.po.Student;
import com.example.service.event.UserService;
import com.example.util.RedisHandleService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liwei
 * @Date 2022/4/16 12:03
 */
@Slf4j
@RestController
public class DemoController {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private RedisHandleService redisHandleService;

    @GetMapping("/demo/{id}")
    public Object insertRecord(@PathVariable Integer id) throws InterruptedException {

        RLock lock = redissonClient.getLock(String.valueOf(id));
        try {
            lock.lock(10, TimeUnit.SECONDS);

            System.out.println("模拟业务..."+"id:"+id+"======"+Thread.currentThread().getName());
            Thread.sleep(5000L);
        } finally {
            lock.unlock();
        }
        return Collections.emptyList();
    }

    @GetMapping("/demo")
    public Object insertRecord2(@RequestParam Integer id) throws InterruptedException {

        RLock lock = redissonClient.getLock(String.valueOf(id));

        boolean b = lock.tryLock(10,TimeUnit.SECONDS);
        if (b){
            System.out.println("模拟业务..."+"id:"+id+"======"+Thread.currentThread().getName());
            return Result.success();
        }

        return Result.error(ExceptionCodeEnum.ERROR);

    }

    @GetMapping("/demo/redis")
    public Object deleteRedis() throws InterruptedException {
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern("prefix_*");
        for (String item : keys) {
            redisTemplate.delete(item);
        }
        return "success";
    }





    @GetMapping("/lock")
    public ResultDTO getLockTest(@RequestParam(value = "name") String name){
        boolean result = redisHandleService.tryRedissionLockInMillis(name, 0, 2000);
        if (result) {
            return ResultDTO.success();
        }else {
            return ResultDTO.fail("is Limited");
        }
    }

    @GetMapping("/limiter")
    public ResultDTO getLimiterTest(@RequestParam(value = "name") String name){
        Integer qps = 1000;
        boolean result = redisHandleService.tryRRateLimiterAcquire(name, qps);
        if (result) {
            return ResultDTO.success();
        }else {
            return ResultDTO.fail("is Limited");
        }
    }

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/student")
    public ResultDTO getStudentById(@RequestParam Integer id){
        Student student = studentMapper.selectById(id);
        return ResultDTO.success(student);
    }


    @Value("${stu.name:zhangsan}")
    private String stuName;

    @Value("#{${car.vtp.config}}")
    private Map<String, Map<String, String>> vtpConfig;

    @Value("#{T(org.apache.commons.lang3.StringUtils).split('${communication:}', ',')}")
    private List<String> comList;

    @Value("#{'${communication:}'.split(',')}")
    private List<String> shortlist;

    @Value("#{${susan.test.map}}")
    private Map<String, String> susanMap;

    @GetMapping("test/apollo")
    public ResultDTO getApolloValueTest(){
        Map<String, String> carOwner = vtpConfig.get("carOwner");
        Map<String, String> verify = vtpConfig.get("verify");
        Map<String, String> illegalFrameNo = vtpConfig.get("illegalFrameNo");
        ArrayList<Map<String, String>> list = Lists.newArrayList(carOwner, verify, illegalFrameNo);

        log.info("comList:{}",comList);
        log.info("shortlist:{}",shortlist);
        log.info("susanMap:{}",susanMap);

        return ResultDTO.success(list);
    }

    @Autowired
    JsonExampleMapper jsonExampleMapper;
    @GetMapping("test/json")
    public ResultDTO getJsonTest(){
        JsonExample jsonDemo = jsonExampleMapper.selectById(1);
        String data = jsonDemo.getData();
        JsonExampleVO jsonExampleVO = JSON.parseObject(data, JsonExampleVO.class);
        return ResultDTO.success(jsonExampleVO);
    }


    @Autowired
    private UserService userService;

    //写一个用户登录Controller
    @GetMapping("/login")
    public ResultDTO login(@RequestParam String username) {
        userService.loginUser(username);
        return ResultDTO.success();
    }




    @PostMapping("/translate")
    public  ResultDTO translate(@RequestBody List<String> list) {
        Map<String, String> resultMap = new HashMap<>();
        for (String item : list) {
            String url = "https://deeplx.yelochick.com/translate";
            String text = item;
            String sourceLang = "ZH";
            String targetLang = "EN";

            // Create JSON payload
            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put("text", text);
            jsonPayload.put("source_lang", sourceLang);
            jsonPayload.put("target_lang", targetLang);

            // Execute POST request
            HttpResponse response = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(jsonPayload.toString())
                    .execute();

            // Process response
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            // Extract data field and create map

            String dataField = jsonObject.getString("data");
            resultMap.put(text, dataField);

            // Print the result map
            System.out.println(text + "===" + dataField);

        }

        return ResultDTO.success(resultMap);

    }
}



