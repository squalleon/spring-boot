@RestController
class SimpleWebApp {

    @Autowired
    String text

    @RequestMapping("/")
    String index() {
         "여러분도 할 수 있어요, ${text}!"
    }
}

beans {
      text String, "그루비 빈을 사용한 스프링 부트"
}
