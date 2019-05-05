package com.kakuiwong.mechant.client;

import com.kakuiwong.auth.TestResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("AUTH")
public interface TestResourceClient extends TestResource {

    @PostMapping("/test")
    @Override
    String test(@RequestParam("s") String var);
}
