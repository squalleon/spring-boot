
# 목차
- hello-spring Spring-Boot 프로젝트
- Spring Boot 프로젝트 구성
- Spring Boot H2 연동
- H2 TCP 서버 생성

### hello-spring

Caused by: org.codehaus.groovy.control.MultipleCompilationErrorsException: startup failed:

[해결]
- Project Structure(Ctrl+Alt+Shift+S)  
  - Download JDK  
    Version: 1.8  
    Vendor: Amazon Corretto 1.8.0_282  
    Location: C:\Users\spiri\.jdks\corretto-1.8.0_282  
    Version: 11  
    Vendor: Amazon Corretto 11.0.10  
    Location: C:\Users\spiri\.jdks\corretto-11.0.10  

- Settings(Ctrl+Alt+S)  
  Build, Execution, Deployment > Build Tools > Gradle  
  
  Build and run using: IntelliJ IDEA  
  Run tests using: IntelliJ IDEA  
  
  Gradle JVM: corretto-11  

----


http://localhost:8080/
http://localhost:8080/hello
http://localhost:8080/hello-mvc?name=spring
http://localhost:8080/hello-string?name=spring
http://localhost:8080/hello-api?name=spring

----


