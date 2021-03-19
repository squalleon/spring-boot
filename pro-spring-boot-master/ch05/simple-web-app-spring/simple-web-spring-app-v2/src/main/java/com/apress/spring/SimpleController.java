package com.apress.spring;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/showMessage.html")
public class SimpleController{
 
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView helloWorld() {
 
		ModelAndView model = new ModelAndView("showMessage");
		model.addObject("message", "애너테이션을 사용한 스프링 MVC 웹 애플리케이션");
 
		return model;
	}
}
