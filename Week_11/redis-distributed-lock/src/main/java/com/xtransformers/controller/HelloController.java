package com.xtransformers.controller;

import com.xtransformers.annotation.RedisLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * 测试接口
     * 启动项目后，快速调用此接口多次，在日志中就能看到"获取锁失败"的异常信息。
     *
     * @return test
     */
    @GetMapping("/test")
    @RedisLock(key = "hello_test")
    public String test() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test";
    }

}
