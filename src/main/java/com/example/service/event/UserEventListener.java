package com.example.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class UserEventListener {

    @Async("commonExecutor")
    @EventListener(UserLoggedInEvent.class)
    public void handleUserLogin(UserLoggedInEvent event) {
        String username = event.getUsername();
        // 这里模拟处理登录后的异步任务，比如发送欢迎邮件、更新统计信息等
        log.info("异步处理用户登录事件：{}" ,username);
        try {
            Thread.sleep(30000); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info( "{}的登录处理完成。",username);
    }
}