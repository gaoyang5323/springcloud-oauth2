package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author: gaoyang
 * @Description:认证服务器配置
 */
@Order(2)
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserDetailsServiceConfig myUserDetailsService;

    //为了测试客户端与凭证存储在内存(生产应该用数据库来存储,oauth有标准数据库模板)
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1-code") // client_id
                .secret(bCryptPasswordEncoder.encode("123")) // client_secret
                .authorizedGrantTypes("authorization_code") // 该client允许的授权类型
                .scopes("app") // 允许的授权范围
                .redirectUris("https://www.baidu.com")
                .resourceIds("goods", "mechant")    //资源服务器id,需要与资源服务器对应

                .and()
                .withClient("client2-credentials")
                .secret(bCryptPasswordEncoder.encode("123"))
                .authorizedGrantTypes("client_credentials")
                .scopes("app")
                .resourceIds("goods", "mechant")

                .and()
                .withClient("client3-password")
                .secret(bCryptPasswordEncoder.encode("123"))
                .authorizedGrantTypes("password")
                .scopes("app")
                .resourceIds("mechant")

                .and()
                .withClient("client4-implicit")
                .authorizedGrantTypes("implicit")
                .scopes("app")
                .resourceIds("mechant");
    }

    //配置token仓库
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //authenticationManager配合password模式使用
        endpoints.authenticationManager(authenticationManager)
                //这里使用内存存储token,也可以使用redis和数据库
                .tokenStore(new InMemoryTokenStore());
    }

    //配置token状态查询
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //开启支持通过表单方式提交client_id和client_secret,否则请求时以basic auth方式,头信息传递Authorization发送请求
        security.allowFormAuthenticationForClients();
    }

    //以下数据库配置
    /**
     *
     *     @Bean
     *     @Primary
     *     @ConfigurationProperties(prefix = "spring.datasource")
     *     public DataSource dataSource() {
     *         // 配置数据源（注意，我使用的是 HikariCP 连接池），以上注解是指定数据源，否则会有冲突
     *         return DataSourceBuilder.create().build();
     *     }
     *
     *     @Bean
     *     public TokenStore tokenStore() {
     *         // 基于 JDBC 实现，令牌保存到数据
     *         return new JdbcTokenStore(dataSource());
     *     }
     *
     *     @Bean
     *     public ClientDetailsService jdbcClientDetails() {
     *         // 基于 JDBC 实现，需要事先在数据库配置客户端信息
     *         return new JdbcClientDetailsService(dataSource());
     *     }
     *
     *     @Override
     *     public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
     *         // 设置令牌
     *         endpoints.tokenStore(tokenStore());
     *     }
     *
     *     @Override
     *     public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
     *         // 读取客户端配置
     *         clients.withClientDetails(jdbcClientDetails());
     *     }
     *
     */
}
