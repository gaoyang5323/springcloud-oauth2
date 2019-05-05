package com.kakuiwong.mechant.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.HashMap;

@Configuration
public class MyConfig {

    //配置资源服务器
    @Configuration
    @EnableResourceServer
    public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().httpBasic().disable().exceptionHandling().authenticationEntryPoint((req, resp, exception) -> {
                resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap() {{
                    put("status", 0);
                    put("error", "没有权限");
                }}));
            })
                    .and().authorizeRequests().antMatchers("/noauth").permitAll()
                    .and().authorizeRequests().anyRequest().authenticated();
        }
    }
}
