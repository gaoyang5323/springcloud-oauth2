package com.example.zuul.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: gaoyang
 * @Description:
 */
@FeignClient(value = "MECHANT")
public interface MyClient {

    @GetMapping("noauth")
    public String noauth();

    @GetMapping("ping")
    public String ping();
}
