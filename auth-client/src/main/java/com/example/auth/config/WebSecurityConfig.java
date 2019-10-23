package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author: gaoyang
 * @Description: 身份认证拦截
 */
@Order(1)
@Configuration
//注解权限拦截
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceConfig userDetailsServiceConfig;

    //认证服务器需配合Security使用
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //websecurity用户密码和认证服务器客户端密码都需要加密算法
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //验证用户权限
        auth.userDetailsService(userDetailsServiceConfig);
        //也可以在内存中创建用户并为密码加密
        // auth.inMemoryAuthentication()
        //         .withUser("user").password(passwordEncoder().encode("123")).roles("USER")
        //         .and()
        //         .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");
    }

    //uri权限拦截,生产可以设置为启动动态读取数据库,具体百度
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //此处不要禁止formLogin,code模式测试需要开启表单登陆,并且/oauth/token不要放开或放入下面ignoring,因为获取token首先需要登陆状态
                .formLogin()
                .and()
                .csrf().disable()

                .authorizeRequests().antMatchers("/test").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }

    //设置不拦截资源服务器的认证请求,我测试时候不加这个也不会拦截
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/oauth/check_token");
    }
}
