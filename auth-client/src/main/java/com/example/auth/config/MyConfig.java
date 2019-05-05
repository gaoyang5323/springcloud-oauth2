package com.example.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 设置认证服务器
 */
@Configuration
public class MyConfig {

    //设置用户信息处理类,这里为了测试使用密码123,用户名随意
    @Component
    public static class MyUserDetailsService implements UserDetailsService {
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return new User(username, passwordEncoder.encode("123"),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        }
    }

    //认证服务器
    @EnableAuthorizationServer
    @Configuration
    public static class Authorization extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        BCryptPasswordEncoder bCryptPasswordEncoder;
        @Autowired
        MyUserDetailsService myUserDetailsService;

        //为了测试客户端与凭证存储在内存(生产应该用数据库来存储,oauth有标准数据库模板)
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("client") // client_id
                    .secret(bCryptPasswordEncoder.encode("123")) // client_secret
                    .authorizedGrantTypes("authorization_code", "password") // 该client允许的授权类型
                    .scopes("app") // 允许的授权范围

                    .and()
                    .withClient("client2")
                    .secret(bCryptPasswordEncoder.encode("123"))
                    .authorizedGrantTypes("client_credentials")
                    .scopes("app")

                    .and()
                    .withClient("web")
                    .authorizedGrantTypes("implicit")
                    .scopes("app");
        }

        //authenticationManager配合password模式使用,tokenstore生产可用redis
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager)
                    .tokenStore(new InMemoryTokenStore())
                    .userDetailsService(myUserDetailsService);
        }

        //配置token状态查询
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.tokenKeyAccess("permitAll()");
            security.checkTokenAccess("isAuthenticated()");
        }
    }

    //认证服务器需配合Security使用
    @Configuration
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        //这里只验证是否带有token的失败返回authenticationEntryPoint
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().and()
                    .csrf().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint((req, resp, exception) -> {
                        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                        resp.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap() {{
                            put("status", 0);
                            put("error", "没有权限");
                        }}));
                    }).and()
                    .authorizeRequests().anyRequest().authenticated();
        }
    }

    //配置资源处理器,为了其他客户端访问该登陆用户信息等
    @Configuration
    @EnableResourceServer
    public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().exceptionHandling().authenticationEntryPoint((req, resp, exception) -> {
                resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(new HashMap() {{
                    put("status", 0);
                    put("error", "没有权限");
                }}));
            }).and().authorizeRequests().anyRequest().authenticated();
        }
    }
}
