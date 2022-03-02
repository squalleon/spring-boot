### Spring Boot 프로젝트 구성
- 컨트롤러: 웹 MVC의 컨트롤러 역할
- 서비스: 핵심 비즈니스 로직 구현
- 리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨

### 소스 구성
- 컨트롤러
  - java/hello/hellospring/controller/HelloController.java
- 도메인
  - java/hello/hellospring/domain/Member.java
- 리포지토리
  - java/hello/hellospring/repository/MemberRepository.java
  - java/hello/hellospring/repository/MemoryMemberRepository.java
- 서비스

### H2 database
- Connection Type : Embedded
- Driver : H2
- User : Sa
- Password : <hidden>
- URL : jdbc:h2:~/test

