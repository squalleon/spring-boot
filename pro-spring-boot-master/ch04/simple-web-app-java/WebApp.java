package com.apress.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WebApp {

    @RequestMapping("/")
    public String greetings() {
        return "<h1>자바에서도 스프링 부트 시작!</h1>";
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
