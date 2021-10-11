package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(HttpServletRequest request, Model model) {
        model.addAttribute("data", "hello!!!");
        model.addAttribute("sessionId", "test111");
        request.getSession().setAttribute("data", "hello!!!");
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

