package com.mcit.AdmissionSystem.config;

import com.mcit.AdmissionSystem.web.handlers.RESTAuthenticationEntryPoint;
import com.mcit.AdmissionSystem.web.handlers.RESTAuthenticationFailureHandler;
import com.mcit.AdmissionSystem.web.handlers.RESTAuthenticationSuccessHandler;
import com.mcit.AdmissionSystem.web.providers.ASAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ASAuthenticationProvider authenticationProvider;

    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/student/**").hasAnyRole("ADMIN")
                .antMatchers("/professor/**").hasAnyRole("ADMIN")
                .antMatchers("/course/**").hasAnyRole("ADMIN")
                .antMatchers("/course-assignment/**").hasAnyRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .permitAll()
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authenticationProvider);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
