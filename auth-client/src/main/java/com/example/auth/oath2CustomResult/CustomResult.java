package com.example.auth.oath2CustomResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: gaoyang
 * @Description: 认证服务器自定义返回
 */
@RestController
@RequestMapping("/oauth")
public class CustomResult {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @GetMapping("/token")
    public Object getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        return this.result(principal,parameters);
    }

    @PostMapping("/token")
    public Object postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        return this.result(principal,parameters);
    }

    public Object result(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.getAccessToken(principal, parameters);
        OAuth2AccessToken body = accessToken.getBody();
        Map<String, Object> customMap = body.getAdditionalInformation();
        String value = body.getValue();
        OAuth2RefreshToken refreshToken = body.getRefreshToken();
        Set<String> scope = body.getScope();
        int expiresIn = body.getExpiresIn();
        customMap.put("token",value);
        customMap.put("scope",scope);
        customMap.put("expiresIn",expiresIn);
        customMap.put("refreshToken",refreshToken);
        Map map = new HashMap();
        map.put("code",0);
        map.put("msg","success");
        map.put("data",customMap);
        return map;
    }
}
