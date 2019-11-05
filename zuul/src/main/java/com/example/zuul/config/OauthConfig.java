package com.example.zuul.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: gaoyang
 * @Description:
 */
@Component
public class OauthConfig  implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.query("access_token","71f422b5-2204-4653-8a85-9cf2c62aac81");
    }
}
