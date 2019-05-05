package com.kakuiwong.mechant;

import com.kakuiwong.auth.TestResource;
import com.kakuiwong.mechant.client.TestResourceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@EnableFeignClients
@RestController
@EnableEurekaClient
@SpringBootApplication
public class MechantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MechantApplication.class, args);
    }

    @GetMapping("ping")
    public Object test() {
        return "pong";
    }

    @GetMapping("noauth")
    public Object noauth() {
        return "noauth";
    }

    @Autowired
    TestResourceClient testResourceClient;

    @GetMapping("/test")
    public Object test2(@RequestParam(required = true) String s) {
        return testResourceClient.test(s);
    }
}
