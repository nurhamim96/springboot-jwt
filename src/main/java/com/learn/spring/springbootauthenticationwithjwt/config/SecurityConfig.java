package com.learn.spring.springbootauthenticationwithjwt.config;

import com.learn.spring.springbootauthenticationwithjwt.security.JwtAuthenticationEntryPoint;
import com.learn.spring.springbootauthenticationwithjwt.security.JwtAuthenticationFilter;
import com.learn.spring.springbootauthenticationwithjwt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/v2/api-docs",
                        "/swagger-ui.html",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/webjar/**").permitAll()
                .antMatchers("/favicon.ico", "/auth/login", "auth/register").permitAll()
                .anyRequest().authenticated();
        // enable h2-console
        http.headers().frameOptions().sameOrigin();
        // remove session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // default page auth
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        // filter jwt
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
