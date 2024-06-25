package com.momsway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**")
                .requestMatchers("/css/**")
                .requestMatchers("/js/**")
                .requestMatchers("/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception{

        http.csrf(csrf->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

//     http.csrf(csrf-> csrf.disable());

        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/insertrep/**").permitAll()
//                        .requestMatchers("/join","/login","/logout","/main").permitAll()
                                .anyRequest().authenticated()
        );

        // 로그인
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("pwd")
                .defaultSuccessUrl("/main")
                .permitAll()
        );

        // 로그아웃
        http.logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/main")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
