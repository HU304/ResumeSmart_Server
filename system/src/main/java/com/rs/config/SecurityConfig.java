package com.rs.config;

import com.rs.filter.JwtAuthenticationTokenFilter;
import com.rs.handler.security.AuthenticationEntryPointImpl;
import com.rs.handler.security.MyAccessDeniedHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * 安全配置
 */
@Configuration
public class SecurityConfig {



    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler; //方法权限被拒绝，无权访问处理
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; //前置过滤器
    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint; //处理登录失败


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                        // 不通过Session获取SecurityContext
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(auth -> auth
                                // 对于登录接口 允许匿名访问
                                .requestMatchers("/login").anonymous()
                                .requestMatchers("/logout").authenticated() // 登出接口需要认证
                                .requestMatchers("/user/info").authenticated()
                                .requestMatchers("/comment/send").authenticated()
                                .requestMatchers("/article/my").authenticated()
                                // 除上面外的所有请求全部不需要认证即可访问
                                .anyRequest().permitAll()
                        )
                                // 禁用掉SpringSecurity默认的注销功能
                                .logout(AbstractHttpConfigurer::disable)
                                // 处理异常
                                .exceptionHandling(exception -> exception
                                    .accessDeniedHandler(myAccessDeniedHandler) // 处理权限不足
                                    .authenticationEntryPoint(authenticationEntryPoint) // 处理未登录
                        )
                        // 自定义过滤器
                        .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                        // 允许跨域
                            .cors(cors -> {});

        return http.build();
    }


    /**
     * 暴露 AuthenticationManager 为 Spring Bean
     * 让其他组件（如登录控制器、自定义过滤器等）可以通过依赖注入获取并使用它
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 密码加密：将用户原始密码转换为不可逆的哈希值
     * 密码验证：比较输入的密码与存储的哈希值是否匹配
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}