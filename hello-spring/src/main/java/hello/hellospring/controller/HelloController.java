package hello.hellospring.controller;

import hello.hellospring.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(HttpServletRequest request, Model model) throws NoSuchAlgorithmException {
        model.addAttribute("data", "hello!!!");
        model.addAttribute("sessionId", "test111");
        request.getSession().setAttribute("data", "hello!!!");

//        for (int i = 0; i < 20; i++) {
//            System.out.println("uuid = " + StringUtil.getUUID());
//        }
//        System.out.println("RandomString = " + StringUtil.getRandomString());
        String pwd = "안녕하세요. 자바!!!";
        String result = StringUtil.sha256(pwd);
        System.out.println("sha256 = " + result);
        System.out.println("result = " + result.equals(StringUtil.sha256(pwd)));

        StringUtil.chainCheck("PEMDATA");

        return "hello";
    }

    @GetMapping("main")
    public String main(HttpServletRequest request, Model model) {
//        model.addAttribute("data", "hello!!!");
//        request.getSession().setAttribute("data", "hello!!!");
        return "main";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam String name, Model model) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam String name, Model model) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

