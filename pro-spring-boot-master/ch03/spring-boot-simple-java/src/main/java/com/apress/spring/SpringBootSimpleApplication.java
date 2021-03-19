package com.apress.spring;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootSimpleApplication {
//버전 11. CommandLineRunner 및 ApplicationRunner 구현
//public class SpringBootSimpleApplication implements CommandLineRunner, ApplicationRunner{
    private static final Logger log = LoggerFactory.getLogger(SpringBootSimpleApplication.class);

    public static void main(String[] args) throws IOException {
        //테스트하려면 주석 처리/해제하세요.
        //버전 1.
        //SpringApplication.run(SpringBootSimpleApplication.class, args);


        //버전 2.
        //SpringApplication app = new SpringApplication(SpringBootSimpleApplication.class);
        //여기서 기능을 추가
        //app.run(args);


        //버전 3.
        //SpringApplication app = new SpringApplication(SpringBootSimpleApplication.class);
        //app.setBanner(new Banner() {
        //	@Override
        //	public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        //		out.print("\n\n\tThis is my own banner!\n\n".toUpperCase());
        //	}
        //});
        //app.run(args);


        //버전 4.
          //SpringApplication app = new SpringApplication(SpringBootSimpleApplication.class);
          //app.setBannerMode(Mode.OFF);
          //app.run(args);


        //버전 5.
        //new SpringApplicationBuilder()
        //.bannerMode(Banner.Mode.OFF)
        //.sources(SpringBootSimpleApplication.class)
        //.run(args);

        //버전 5.1.
        //new SpringApplicationBuilder(SpringBootSimpleApplication.class)
        //.child(MyConfig.class)
        //.run(args);


        //버전 6.
        //new SpringApplicationBuilder(SpringBootSimpleApplication.class)
        //.logStartupInfo(false)
        //.run(args);


        //버전 7. 프로파일 활성화
        //new SpringApplicationBuilder(SpringBootSimpleApplication.class)
        //.profiles("production")
        //.run(args);


        //버전 8. 리스너 추가
        //Logger log = LoggerFactory.getLogger(SpringBootSimpleApplication.class);
        //new SpringApplicationBuilder(SpringBootSimpleApplication.class)
        //.listeners(new ApplicationListener<ApplicationEvent>() {
        //
        //	@Override
        //	public void onApplicationEvent(ApplicationEvent event) {
        //		log.info("#### > " + event.getClass().getCanonicalName());
        //	}
        //
        //})
        //.run(args);


        //버전 9. Web 제거
        //new SpringApplicationBuilder(SpringBootSimpleApplication.class)
        //.web(false)
        //.run(args);


        //버전 10.
        //SpringApplication.run(SpringBootSimpleApplication.class, args);


        //버전 11 and 12.
        SpringApplication.run(SpringBootSimpleApplication.class, args);

    }


    //버전 11. ApplicationRunner 구현체
    /*
    @Bean
    String info() {
        return "단순 스트링 빈";
    }

    @Autowired
    String info;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("## > ApplicationRunner 구현체...");
        log.info("Info 빈에 접근: " + info);
        args.getNonOptionArgs().forEach(file -> log.info(file));
    }
    @Override
    public void run(String... args) throws Exception {
        log.info("## > CommandLineRunner 구현체...");
        log.info("Info 빈에 접근: " + info);
        for(String arg:args)
            log.info(arg);
    }
    */


    //버전 12. CommandLineRunner

    @Bean
    String info() {
        return "단순 스트링 빈";
    }

    @Autowired
    String info;

    @Bean
    CommandLineRunner myMethod() {
        return args -> {
            log.info("## > CommandLineRunner 구현체...");
            log.info("Info 빈에 접근: " + info);
            for(String arg:args)
                log.info(arg);
        };
    }

}

//버전 5.1
//@Configuration
//class MyConfig{

//	@Bean
//	String text() {
//		return "안녕하세요!";
//	}
//}


//버전 10.
/*
@Component
class MyComponent {

     private static final Logger log = LoggerFactory.getLogger(MyComponent.class);

     @Autowired
     public MyComponent(ApplicationArguments args) {
            //--enable 또는 -enable=true 형태로 enable 인자가 전달된 경우,
            boolean enable = args.containsOption("enable");
            if(enable)
                log.info("## > enable 옵션을 주셨네요!");

            //myfile.txt 또는 files=["myfile.txt"], 아니면 enable=true, files=["myfile.txt"] 형태로 인자 파일을 가져온다.
            log.info("## > 다른 인자...");
            List<String> _args = args.getNonOptionArgs();
            if(!_args.isEmpty())
                _args.forEach(file -> log.info(file));
     }
}
*/

