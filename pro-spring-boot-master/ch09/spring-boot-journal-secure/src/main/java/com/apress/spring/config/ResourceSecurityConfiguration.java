package com.apress.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ResourceSecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
           .antMatchers("/").permitAll()
           .antMatchers("/api/**").authenticated()

           // 버전 1
           //.and()
           //.httpBasic();

           // 버전 2
           //.and()
           //.formLogin();

           // 버전 3
           .and()
           .formLogin().loginPage("/login").permitAll()
           .and()
           .logout().permitAll();

    }

    //JournalController에 로그인 매핑이 없을 경우 주석 해제한다.
    //@Configuration
    //static protected class LoginController extends WebMvcConfigurerAdapter{
    //	@Override
    //	public void addViewControllers(ViewControllerRegistry registry) {
    //		registry.addViewController("/login").setViewName("login");
    //	}
    //}

}
