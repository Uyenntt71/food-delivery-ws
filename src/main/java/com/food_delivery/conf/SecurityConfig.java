package com.food_delivery.conf;

import static com.food_delivery.constant.CommonParams.ADD_ADDRESS_PATH;
import static com.food_delivery.constant.CommonParams.FORGOT_PASSWORD_PATH;
import static com.food_delivery.constant.CommonParams.LOGIN_PATH;
import static com.food_delivery.constant.CommonParams.REGISTER_PATH;
import static com.food_delivery.constant.CommonParams.RESET_PASSWORD_PATH;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    AccessControlInterceptor accessControlInterceptor() {
        return new AccessControlInterceptor();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.cors()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(LOGIN_PATH).permitAll()
            .antMatchers(RESET_PASSWORD_PATH).permitAll()
            .antMatchers(REGISTER_PATH).permitAll()
            .antMatchers(FORGOT_PASSWORD_PATH).permitAll();

        http.addFilterBefore(accessControlInterceptor(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
