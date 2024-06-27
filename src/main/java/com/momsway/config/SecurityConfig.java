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
                                .requestMatchers("/insertrep/**").permitAll()

                                // 모든사람
                                .requestMatchers("/join","/login","/logout","/main"
                                        ,"/entexam","/academy","/notice"
                                        ,"/checkEmail","/checkNickname").permitAll()

                                // 관리자
                                .requestMatchers("/insertNotice","/updateNotice/*","/delnotice/*"
                                        ,"/report","/repdetail/*","/delreport/*"
                                        ,"/useradmin","/userdetail/*", "/userdelete/*").hasRole("ADMIN")

                                // 학부모, 관리자
                                .requestMatchers("/insertent","/entupdate/*","/delentexam/*"
                                        ,"/insertrep").hasAnyRole("PARENT", "ADMIN")

                                // 학원, 관리자
                                .requestMatchers("/insertAcademy","/updateAcademy/*"
                                        ,"/delacademy/*").hasAnyRole("ACADEMY", "ADMIN")

                                // 회원(정지회원 제외)
                                .requestMatchers("/academy/*","/notice/*","/entdetail/*"
                                        ,"/checklike","/insertlike","/dellike/*" ,"/replist/*"
                                        ,"/entreport").hasAnyRole("PARENT", "ACADEMY", "ADMIN")

                                // 회원(정지회원 포함)
                                .requestMatchers("/mypage","myinfo").authenticated()
//
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
