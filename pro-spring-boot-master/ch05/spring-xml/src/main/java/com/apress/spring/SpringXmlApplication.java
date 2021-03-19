package com.apress.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apress.spring.service.SimpleService;

@RestController
@ImportResource({ "META-INF/spring/services-context.xml" })
@SpringBootApplication
public class SpringXmlApplication {

    @Autowired
    SimpleService html;

    @RequestMapping("/")
    public String index() {
        return html.getHtmlH1From("스프링 XML 빈은 여기서 사용!");
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringXmlApplication.class, args);
    }
}
