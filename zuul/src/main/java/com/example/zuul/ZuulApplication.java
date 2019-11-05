package com.example.zuul;

import com.example.zuul.feignclient.MyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@EnableZuulProxy
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }


    @Autowired
    private MyClient myClient;

    @GetMapping("t1")
    public Object t() {
        return myClient.noauth();
    }

    @GetMapping("ping")
    public Object ping() {
        return myClient.ping();
    }

    @GetMapping("t2")
    public Object t2() {
        return "zuul";
    }
}
