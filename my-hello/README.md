https://www.youtube.com/watch?v=DvarFBhLepI

IDE는 에디터 이상의 역할이지만 여전히 에디터와 비교되는 것 같습니다. 
이 세션에서는 IntelliJ IDEA에 대해 살펴보며 효율적이고 생산적인 개발업자가 될 수 있는 팁과 트릭을 보여줍니다. 
본 세션에서는 IntelliJ IDEA를 이용한 Gradle + Spring Boot 프로젝트 빌드 및 Unit Test 만드는 과정을 바탕으로 
다양한 IntelliJ IDEA 의 기능 및 2017 버전에 추가된 새로운 기능을 공개합니다.

Intellij + Gradle로 Spring Boot + JPA빌드 및 DB 연동
Unit Test 만들기

1. Spring Boop 빌드
- Spring Boot, JPA, MySQL, Lombok
2. Test code 만들기
3. application.properties
- spring.datasource "" 없이 넣는다.
- MySQL 자동
4. settings -> enable annotation


## 0. MaraiDB

@local_MariaDB10.6
  마우스 우클릭 New > Schema : hello_db
  마우스 우클릭 New > Table : users
  Columns, Keys(Primary key) 추가

## 1. New Project

- New Project
Spring Initializr
Name : my-hello
Location : ~\OneDrive\문서\Github\spring-boot\my-hello
Type : Gradle
Language : Java
Group : com
Artifact : my-hello
Package name : com.myhello
Project SDK : corretto-1.8
Java : 11
Package : Jar

- Next

Spring Boot : 2.5.4
Dependencies :
Spring Data JPA
MySQL
Lombok

## 2. domain 패키지 만들고 User Poso bean 생성

```java
@Entity
@Data

@Id
String id;
String password;
String name;
```

## 3. repository 패키지 만들고 UserRepository 인터페이스 생성

UserRepository 인터페이스 만들고 extends JpaRepository<User, String> 으로 확장한다.
@Repository 어노테이션을 추가한다.

```java
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
```

## 4. UserRepositoryTest 만들기

UserRepository 인터페이스 커서에서 Alt+Enter하면 Create Test 클릭
Testing library : JUnit4
Class name : UserRepositoryTest
Destination package : com.myhello.repository 

그런 다음 아래 2개 어노테이션을 추가한다.
@RunWith(SpringRunner)
@SpringBootTest

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
  //Alt+Ins 하면 테스트 메소드를 만들어준다.
  @Test
  public void save() {
    userRepository.save(new User("test", "1234", "kim"));
    User selectedUser = userRepository.findOne("test");
    assertEquals("test", selectedUser.getId());
  }
}
```

Execution failed for task ':test'.
> No tests found for given includes: [com.myhello.repository.UserRepositoryTest.save](filter.includeTestsMatching)

=> Settings > Build, Execution, Deployment > Build Tools > Gradle > "Run tests using: IntellJ IDEA"
로 변경


org.springframework.orm.jpa.JpaSystemException: No default constructor for entity:

=> @NoArgsConstructor 추가하면 해결됨
   또는 빈 생성자 만들면 됨
    public User() {

    }

userRepository.save(new User("test", "1234", "kim"));
Ctrl+Shift+F10 실행하면 Test passed. users 테이블 조회해 보면 insert 되었다.


User selectedUser = userRepository.findOne("test");
assertEquals("test", selectedUser.getId());
추가하여 Ctrl+Shift+F10 실행하면 오류 발생

C:\Users\spiri\OneDrive\문서\GitHub\spring-boot\my-hello\src\test\java\com\myhello\repository\UserRepositoryTest.java:21:43
java: method findOne in interface org.springframework.data.repository.query.QueryByExampleExecutor<T> cannot be applied to given types;
required: org.springframework.data.domain.Example<S>
found: java.lang.String
reason: cannot infer type-variable(s) S
(argument mismatch; java.lang.String cannot be converted to org.springframework.data.domain.Example<S>)


User selectedUser = userRepository.findOne("test");
=> 이 부분을 아래로 변경하여 fix
Optional<User> selectedUser = userRepository.findById("test1");


google > application.properties 검색
datasource 로 검색하여 url, username, pasword 선언한다.
spring.datasource.url = jdbc:mysql://localhost:3306/hello_db
spring.datasource.username = root
spring.datasource.password = passw0rd

