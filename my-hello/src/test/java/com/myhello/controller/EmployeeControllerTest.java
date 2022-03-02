package com.myhello.controller;

import ch.qos.logback.classic.Logger;
import com.myhello.domain.Employee;
import com.sun.org.slf4j.internal.LoggerFactory;
import net.minidev.json.JSONUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {

    final static String BASE_URL = "http://localhost:8080";

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    Employee employee = testRestTemplate.getForObject(BASE_URL + "/{id}", Employee.class, 25);

}