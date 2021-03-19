@RestController
class WebApp{

    @Autowired
    MyService service

    @RequestMapping("/")
    String greetings() {
       service.getH3HeaderFrom("스프링 부트 시작!")
    }
}
