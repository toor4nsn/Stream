package com.example.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void loginUser(String username) {
        // 模拟用户登录逻辑
        log.info("用户:{} 登录...",username);
        
        // 发布用户登录事件
        UserLoggedInEvent event = new UserLoggedInEvent(this, username);
        publisher.publishEvent(event);
        
        log.info("用户登录事件已发布，继续执行其他操作...");
    }
}