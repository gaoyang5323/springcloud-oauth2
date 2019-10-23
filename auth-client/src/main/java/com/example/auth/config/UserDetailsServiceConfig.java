package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.channels.AcceptPendingException;

/**
 * @author: gaoyang
 * @Description: security验证用户身份配置
 */
@Component
public class UserDetailsServiceConfig implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //生产环境使用数据库进行验证
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("user")) {
            throw new AcceptPendingException();
        }
        return new User(username, passwordEncoder.encode("123"),
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
