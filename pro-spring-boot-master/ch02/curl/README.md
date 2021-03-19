# cURL로 스프링 초기화하는 방법

최소 메이븐 프로젝트
```bash
$ curl -s https://start.spring.io/starter.zip -o myapp.zip
```

최소 그레이들 프로젝트
```bash
$ curl -s https://start.spring.io/starter.zip -o myapp.zip ?밺 type=gradle-project
```

스프링 부트 웹 (스프링 MVC)
Spring Boot with Web Features (Spring MVC).
```bash
$ curl -s https://start.spring.io/starter.zip -o myapp.zip -d type=maven-project -d dependencies=web
```

웹과 JPA 의존체만 포함한 pom.xml 생성
```bash
$ curl -s https://start.spring.io/pom.xml -d packaging=war -o pom.xml -d dependencies=web,data-jpa
```

웹과 JPA 의존체만 포함한 build.gradle 생성
```bash
$ curl -s https://start.spring.io/build.gradle -o build.gradle -d dependencies=web,data-jpa
```

도움말 및 사용 가능한 전체 명령어 출력
```bash
$ curl start.spring.io
```
