package com.kakuiwong.mechant.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gaoyang
 * @Description: 测试接口
 */
@RestController
public class TestController {

    @GetMapping("ping")
    public Object test() {
        return "pong";
    }

    @GetMapping("noauth")
    public Object noauth() {
        return "noauth";
    }

}
