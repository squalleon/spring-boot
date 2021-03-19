package com.apress.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleWebController {

    @RequestMapping("/")
    public String greetings() {
        return "<h1>자바에서도 스프링 부트 시작!</h1>";
    }

}
