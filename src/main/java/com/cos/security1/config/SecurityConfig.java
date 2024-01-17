package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //csrf().disable();
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/user/**").authenticated(); //antMatcher -> requestMatchers
                    request.requestMatchers("/manager/**")
                            .hasAnyRole("ADMIN", "MANAGER"); //Role은 붙이면 안됨. 자동으로 ROLE_이 붙음
                    request.requestMatchers("/admin/**")
                            .hasRole("ADMIN"); //Role은 붙이면 안됨. 자동으로 ROLE_이 붙음
                    request.anyRequest().permitAll();
                })
                .formLogin(formLogin -> {
                    formLogin.loginPage("/loginForm");
                    formLogin.loginProcessingUrl("/login"); //login 주소가 호출되면 시큐리티가 낚아채서 로그인을 진행함
                    formLogin.defaultSuccessUrl("/"); //login 성공시 이동할 페이지, 만약 이전에 요청한 페이지가 있다면 해당 페이지로 감
                })
                .build();
    }
}