package com.mymongotest.springmongtest3.config;

import com.mymongotest.springmongtest3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {



    	http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/admin")
                .usernameParameter("email")
                .failureUrl("/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
        ;

        http.authorizeRequests()
                .requestMatchers("/css/**", "/js/**","/image/**").permitAll()
                .requestMatchers("/", "/main", "/login", "/joinUser","/joinForm","/findAll","/images/**").permitAll()
                .requestMatchers("/admin","/images").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}