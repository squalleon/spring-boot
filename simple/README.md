# Getting Started

## Reference document
* [spring-boot-oauth2 가이드](https://spring.io/guides/tutorials/spring-boot-oauth2/)

## 1. 프로젝트 생성
### New Project
### Spring Initializr
- Server Url : spring.start.io
- Name : Simple
- Type : Maven
- Java 8
- Package : Jar
### Spring Boot : 2.5.6
- Dependencies :
  - Spring Web
  - OAuth2 Client
    
## 2. index 추가
- /resources/static/index.html 추가
```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Demo</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width"/>
    <base href="/"/>
    <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/webjars/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
<h1>Demo</h1>
<div class="container"></div>
</body>
</html>
```
## 2. pom.xml jquery, bootstrap, webjars 추가
- pom.xml
```java
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>jquery</artifactId>
            <version>3.4.1</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>4.3.1</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>webjars-locator-core</artifactId>
        </dependency>
```

## 4. github.com OAuth Application 추가
- [To use GitHub’s OAuth 2.0 authentication system for login, you must first Add a new GitHub app.](https://github.com/settings/applications/new)
- Register a new OAuth application
- Application name : MukgiApp
- Homepage URL : 
- http://localhost:8080
- Application description : OAuth2.0 Test
- Authorization callback URL : http://localhost:8080/login/oauth2/code/github

- Client ID : c681deb99e7684e59f52
- Client secrets : c1a65752c5fd8cfa742ad01aadf8d01a5c58113e

## 4. /resources/application.yml 추가
```
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: github-client-id
            clientSecret: github-client-secret
```

## 5. simple app run again http://localhost:8080

http://localhost:8080

==> redirected GitHub login

https://github.com/login/oauth/authorize
?response_type=code
&client_id=c681deb99e7684e59f52
&scope=read:user
&state=uklVj6lQaC7qmWsvD_1n1d62LFm2Q-27LjhqUmLQWVc%3D
&redirect_uri=http://localhost:8080/login/oauth2/code/github

==> Accept authorization then home page index.html 페이지 보인다.

Now, instead of the home page, you should be redirected to login with GitHub. 
If you do that, and accept any authorizations you are asked to make, 
you will be redirected back to the local app, and the home page will be visible.

이제 홈 페이지 대신 GitHub로 로그인하도록 리디렉션되어야 합니다.
그렇게 하고 요청받은 승인을 수락하면
로컬 앱으로 다시 리디렉션되고 홈 페이지가 표시됩니다.

## 6. What Just Happened?

> It then uses the access token to ask GitHub for some personal details (only what you permitted it to do), including your login ID and your name. 
> In this phase, GitHub is acting as a Resource Server, decoding the token that you send and checking if it gives the app permission to access the user’s details. 
> If that process is successful, the app inserts the user details into the Spring Security context so that you are authenticated.
- 그런 다음 액세스 토큰을 사용하여 GitHub에 로그인 ID 및 이름을 포함한 일부 개인 세부 정보(허용한 작업만)를 요청합니다.
- 이 단계에서 GitHub는 리소스 서버 역할을 하여 전송한 토큰을 디코딩하고 앱이 사용자 세부 정보에 액세스할 수 있는 권한을 앱에 부여하는지 확인합니다. 
- 해당 프로세스가 성공하면 앱이 사용자 세부 정보를 Spring Security 컨텍스트에 삽입하여 인증을 받습니다.

> If you look in the browser tools (F12 on Chrome or Firefox) and follow the network traffic for all the hops, you will see the redirects back and forth with GitHub, 
> and finally you’ll land back on the home page with a new Set-Cookie header. 
> This cookie (JSESSIONID by default) is a token for your authentication details for Spring (or any servlet-based) applications.
- 브라우저 도구(Chrome 또는 Firefox에서 F12)를 살펴보고 모든 홉에 대한 네트워크 트래픽을 추적하면 GitHub에서 앞뒤로 리디렉션되는 것을 볼 수 있으며 
- 마지막으로 새로운 Set-Cookie header를 갖고 홈페이지로 돌아갑니다. 
- 이 쿠키(기본적으로 JSESSIONID)는 Spring(또는 모든 서블릿 기반) 애플리케이션에 대한 인증 세부 정보에 대한 토큰입니다.

> So we have a secure application, in the sense that to see any content a user has to authenticate with an external provider (GitHub).
- 따라서 사용자가 외부 공급자(GitHub)를 통해 인증해야 콘텐츠를 볼 수 있다는 점에서 보안 애플리케이션이 있습니다.

> We wouldn’t want to use that for an internet banking website. 
> But for basic identification purposes, and to segregate content between different users of your site, it’s an excellent starting point. 
> That’s why this kind of authentication is very popular these days.
- 우리는 그것을 인터넷 뱅킹 웹사이트에 사용하고 싶지 않습니다.
- 다음 섹션에서는 애플리케이션에 몇 가지 기본 기능을 추가할 것입니다. 또한 사용자가 GitHub로 초기 리디렉션을 받았을 때 어떤 일이 벌어지는지 좀 더 명확하게 알릴 것입니다. 
  그러나 기본적인 식별 목적과 사이트의 여러 사용자 간에 콘텐츠를 분리하는 것은 훌륭한 출발점입니다. 
- 그래서 요즘 이런 인증 방식이 인기가 많습니다.

> In the next section, we are going to add some basic features to the application. 
> We’ll also make it a bit more obvious to users what is going on when they get that initial redirect to GitHub.
- 다음 섹션에서는 애플리케이션에 몇 가지 기본 기능을 추가할 것입니다. 또한 사용자가 GitHub로 초기 리디렉션을 받았을 때 어떤 일이 벌어지는지 좀 더 명확하게 알릴 것입니다.

## 7. F12 on Chrome

1) http://localhost:8080/
```
General
- Request URL : http://localhost:8080/
- Request Method : GET
- Status Code : 302
```
```
Request Headers
- Cookie: Idea-49e3359e=78808b50-3583-489c-b697-1e015bae043b; JSESSIONID=B22A98AC69451BA50F56E44CBD5149AC
```
```
Response Headers
- Location: http://localhost:8080/oauth2/authorization/github
- Set-Cookie: JSESSIONID=4BEE03BE416475457A56347E27B594C4; Path=/; HttpOnly
```

2) click here : /oauth2/authorization/github
   
http://localhost:8080/oauth2/authorization/github
```
General
- Request URL: http://localhost:8080/oauth2/authorization/github
- Request Method: GET
- Status Code: 302 
```
```
Request Headers
- Cookie: Idea-49e3359e=78808b50-3583-489c-b697-1e015bae043b; JSESSIONID=4BEE03BE416475457A56347E27B594C4
```
```
Response Headers
- Location: https://github.com/login/oauth/authorize?response_type=code&client_id=c681deb99e7684e59f52&scope=read:user&state=sYpRDN-UU8orG-SsZH08qoo9zxR5HYt8LFPrW6Dw9GY%3D&redirect_uri=http://localhost:8080/login/oauth2/code/github
```

3) Redirect : https://github.com/login/oauth/authorize

https://github.com/login/oauth/authorize?response_type=code&client_id=c681deb99e7684e59f52&scope=read:user&state=sYpRDN-UU8orG-SsZH08qoo9zxR5HYt8LFPrW6Dw9GY%3D&redirect_uri=http://localhost:8080/login/oauth2/code/github
```
General
- Request URL: https://github.com/login/oauth/authorize?response_type=code&client_id=c681deb99e7684e59f52&scope=read:user&state=sYpRDN-UU8orG-SsZH08qoo9zxR5HYt8LFPrW6Dw9GY%3D&redirect_uri=http://localhost:8080/login/oauth2/code/github
- Request Method: GET
- Status Code: 302 
```
```
Request Headers
- cookie: _octo=GH1.1.1598574160.1616832379; _device_id=8f06223a098f57cfc9f6d8594132af72; _locale=ko; _locale_experiment=ko; user_session=T9Hm3bRi7prCMx0I_lQjbz4THC60rH2xDxbtL6KTjbu3X4Mj; __Host-user_session_same_site=T9Hm3bRi7prCMx0I_lQjbz4THC60rH2xDxbtL6KTjbu3X4Mj; logged_in=yes; dotcom_user=squalleon; has_recent_activity=1; color_mode=%7B%22color_mode%22%3A%22auto%22%2C%22light_theme%22%3A%7B%22name%22%3A%22light%22%2C%22color_mode%22%3A%22light%22%7D%2C%22dark_theme%22%3A%7B%22name%22%3A%22dark%22%2C%22color_mode%22%3A%22dark%22%7D%7D; tz=Asia%2FSeoul; _gh_sess=LtNtrQXzGcVsprAvAUbRv1Bj5wriJKmEkcA%2BHQtDSd8X5Q0C6fDiWd2S%2BRpmoX1MACUQdynVI5smtSoWKjIeH%2FDEuGGWxin58S1zq%2BWA39g8U%2BZ3cfWpdPxvQAqWTNPc8WTTWK0Tw%2FgIJPFO0sd3nzejrBrmHQj2g1unrSKYhbcYNZ0JhmD1R2ZEjmRWC3WXm10w9bXDI89Ek1tHHFtKtYYvu7a6DtugpSNfvRmWWLNt1T9t4%2F4Wu4CgnGhcZSMGkRc2EmZr0pr3NWTckZgii3NQWNa917yeuXwBfQJb%2BcYH2Bl%2BqHnKRDunKPY6AesmbZ3Zc70gZd1IcMmnRxw3EtzewNs3IJEgu2Z2NXm%2FsCoix21D04TfWl7TazXrdCUL4wMwBE7LcUFk2ANjQ0fywDO45jTMb0jQaqEQinNiSBFZVHVkLqqNSNx3KiOGSuNJ0FYiOBPfNgP9GOSrTTtTn%2BccnFGUlgO%2Bn0aoNABoZdhIpVJzbIr61IquWBEorV9Z--qxZiWDke4Q%2BtK5q6--FoSGodeh2AKdOWnm2zQO9g%3D%3D
```
```
Response Headers
- location: http://localhost:8080/login/oauth2/code/github?code=a5513300d8b9f40f4e9d&state=sYpRDN-UU8orG-SsZH08qoo9zxR5HYt8LFPrW6Dw9GY%3D
- server: GitHub.com
- set-cookie: has_recent_activity=1; path=/; expires=Sat, 23 Oct 2021 07:42:35 GMT; secure; HttpOnly; SameSite=Lax
- set-cookie: _gh_sess=X0iP3AUJfnEbTi0hq%2Fy9F2q7zKv9EtVDymKkq63yIqeNROuAoYeSb4Srd6KG5bJ%2Fh596z0sETWcRX7zhflc%2B6OWcFAtMujVie1HtvA584A4A8llHPxJHSt3yEm94T7z8uNv7xcnEBKKn8Tfwiq5d6f4MTxMoPkPrYldpe3KOMTBoMTVpmuufKRe6UdhwO1KY1H8O1BYedX54Q8F8ZUNxxsowoHEgpOQndsQh9Tqajy5yzIZwE7WF1gnSryRsZzkxSK3kyyBk0XajStkqneG3i1L1yKpJKAQJa4OKK3DGfpK758xZHyAshPKzJPOB5o4GZ%2FttrGSDAGiuRCs0lLaXmv0nPdbw1XZW2O2KPjL5RJi9Gk1qpvJPuVpZTngpAk8JRMPv6TvWOGYiuKVFm3GpSb%2FYjH7K0vaprjFkfJt1%2FNy1kTPgpg35lJLbO%2BXM8XSfexTO%2B0vse0p4iO1Oh0o%2FXS%2BieFdPDIW6FAgwcLEJVCVNaodroENYetsA2sgVQisT--Q4NvnVao2MLZOJKR--U%2BYenV0f1ySDTiosiKKWLg%3D%3D; path=/; secure; HttpOnly; SameSite=Lax
```

4) Redirect : /login/oauth2/code/github

http://localhost:8080/login/oauth2/code/github?code=a5513300d8b9f40f4e9d&state=sYpRDN-UU8orG-SsZH08qoo9zxR5HYt8LFPrW6Dw9GY%3D
```
General
- Request URL: http://localhost:8080/login/oauth2/code/github?code=a5513300d8b9f40f4e9d&state=sYpRDN-UU8orG-SsZH08qoo9zxR5HYt8LFPrW6Dw9GY%3D
- Request Method: GET
- Status Code: 302 
```
```
Request Headers
Cookie: Idea-49e3359e=78808b50-3583-489c-b697-1e015bae043b; JSESSIONID=4BEE03BE416475457A56347E27B594C4
```
```
Response Headers
- Set-Cookie: JSESSIONID=1C331B6CE9396F9B400A1A269BC27480; Path=/; HttpOnly
```

5) http://localhost:8080/

4번 Response Headers 
- Set-Cookie: JSESSIONID=1C331B6CE9396F9B400A1A269BC27480; Path=/; HttpOnly
로 수신한 JSESSIONID 로 Request Headers Cookie 로 요청한다.
- Cookie: Idea-49e3359e=78808b50-3583-489c-b697-1e015bae043b; JSESSIONID=1C331B6CE9396F9B400A1A269BC27480

```
General
- Request URL: http://localhost:8080/
- Request Method: GET
- Status Code: 200 
```
```
Request Headers
- Cookie: Idea-49e3359e=78808b50-3583-489c-b697-1e015bae043b; JSESSIONID=1C331B6CE9396F9B400A1A269BC27480
```
```
Response Headers
```

## 8. Add a Welcome Page
> In this section, you’ll modify the simple app you just built by adding an explicit link to login with GitHub. 
> Instead of being redirected immediately, the new link will be visible on the home page, and the user can choose to login or to stay unauthenticated. 
> Only when the user has clicked on the link will the secure content be rendered.

- 이 섹션에서는 Git Hub로 로그인할 수 있는 명시적 링크를 추가하여 방금 만든 간단한 앱을 수정합니다. 
- 즉시 리디렉션되는 대신 홈 페이지에 새 링크가 표시되며 사용자는 로그인하거나 인증되지 않은 상태를 유지할 수 있습니다. 
- 사용자가 링크를 클릭한 경우에만 보안 콘텐츠가 렌더링됩니다.

- Conditional Content on the Home Page
```html
<div class="container unauthenticated">
    With GitHub: <a href="/oauth2/authorization/github">click here</a>
</div>
<div class="container authenticated" style="display:none">
    Logged in as: <span id="user"></span>
</div>
```
```javascript
<script type="text/javascript">
    $.get("/user", function(data) {
        $("#user").html(data.login);
        $(".unauthenticated").hide()
        $(".authenticated").show()
    });
</script>
```

- The /user Endpoint
```java
@SpringBootApplication
@RestController
public class SocialApplication {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("login", principal.getAttribute("login"));
    }

    public static void main(String[] args) {
        SpringApplication.run(SocialApplication.class, args);
    }
}
```

- Making the Home Page Public

> There’s one final change you’ll need to make.
> This app will now work fine and authenticate as before, but it’s still going to redirect before showing the page. To make the link visible, we also need to switch off the security on the home page by extending

- 마지막으로 변경해야 할 것이 있습니다.  
- 이 응용 프로그램은 이제 잘 작동하고 이전과 같이 인증하지만, 여전히 페이지를 표시하기 전에 리디렉션될 것입니다. 링크를 표시하려면 홈 페이지의 보안을 확장하여 보안을 해제해야 합니다.

```java
@SpringBootApplication
@RestController
public class SocialApplication extends WebSecurityConfigurerAdapter {

    // ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
        http
            .authorizeRequests(a -> a
                .antMatchers("/", "/error", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .oauth2Login();
        // @formatter:on
    }

}
```

> Spring Boot attaches special meaning to a WebSecurityConfigurerAdapter on the class annotated with @SpringBootApplication: It uses it to configure the security filter chain that carries the OAuth 2.0 authentication processor.
- Spring Boot는 @Spring 부팅 응용 프로그램이 추가된 클래스의 웹 보안 구성어 어댑터에 특별한 의미를 부여합니다.
> The above configuration indicates a whitelist of permitted endpoints, with every other endpoint requiring authentication.
- 위의 구성은 허용된 끝점의 화이트리스트를 나타내며, 다른 모든 끝점은 인증이 필요합니다.

You want to allow:

/           since that’s the page you just made dynamic, with some of its content visible to unauthenticated users

/error      since that’s a Spring Boot endpoint for displaying errors, and

/webjars/** since you’ll want your JavaScript to run for all visitors, authenticated or not

> You won’t see anything about /user in this configuration, though. Everything, including /user remains secure unless indicated because of the .anyRequest().authenticated() configuration at the end.
- 하지만 이 구성에서는 /user에 대해 아무 것도 표시되지 않습니다. 마지막에 .any Request().인증() 구성으로 인해 표시되지 않는 한/사용자를 포함한 모든 것이 안전하게 유지됩니다.

> Finally, since we are interfacing with the backend over Ajax, we’ll want to configure endpoints to respond with a 401 instead of the default behavior of redirecting to a login page. Configuring the authenticationEntryPoint achieves this for us.
- 마지막으로 Ajax를 통해 백엔드와 상호 작용하고 있으므로 로그인 페이지로 리디렉션하는 기본 동작 대신 401로 응답하도록 끝점을 구성할 것입니다. 인증 진입점을 구성하면 이를 달성할 수 있습니다.

> With those changes in place, the application is complete, and if you run it and visit the home page you should see a nicely styled HTML link to "login with GitHub". The link takes you not directly to GitHub, but to the local path that processes the authentication (and sends a redirect to GitHub). Once you have authenticated, you get redirected back to the local app, where it now displays your name (assuming you have set up your permissions in GitHub to allow access to that data).
- 이러한 변경 내용이 적용되면 응용 프로그램이 완료되었으며, 응용 프로그램을 실행하고 홈 페이지를 방문하면 "Git Hub로 로그인"에 대한 멋지게 스타일이 지정된 HTML 링크가 표시됩니다. 
- 링크는 Git Hub로 직접 이동하지 않고 인증을 처리하는 로컬 경로로 이동합니다(Git Hub로 리디렉션). 인증한 후에는 로컬 앱으로 리디렉션되어 이름이 표시됩니다(Git Hub에서 해당 데이터에 대한 액세스를 허용하도록 권한을 설정했다고 가정).

## 9. Add a Logout Button

> In this section, we modify the click app we built by adding a button that allows the user to log out of the app. This seems like a simple feature, but it requires a bit of care to implement, so it’s worth spending some time discussing exactly how to do it. Most of the changes are to do with the fact that we are transforming the app from a read-only resource to a read-write one (logging out requires a state change), so the same changes would be needed in any realistic application that wasn’t just static content.
- 이 섹션에서는 사용자가 앱에서 로그아웃할 수 있는 단추를 추가하여 빌드한 클릭 앱을 수정합니다. 
- 이것은 간단한 기능처럼 보이지만 구현하는 데 약간의 주의가 필요하므로 정확히 수행하는 방법에 대해 논의하는 데 시간을 할애할 가치가 있습니다. 
- 대부분의 변경 사항은 앱을 읽기 전용 리소스에서 읽기 쓰기 리소스로 변환하고 있다는 사실과 관련이 있으므로(로깅하려면 상태 변경이 필요함), 정적 콘텐츠가 아닌 현실적인 응용 프로그램에서도 동일한 변경 사항이 필요합니다.

- Client Side Changes
```html
<div class="container authenticated">
  Logged in as: <span id="user"></span>
  <div>
    <button onClick="logout()" class="btn btn-primary">Logout</button>
  </div>
</div>
```
```javascript
var logout = function() {
    $.post("/logout", function() {
        $("#user").html('');
        $(".unauthenticated").show();
        $(".authenticated").hide();
    })
    return true;
}
```

- Adding a Logout Endpoint

> Spring Security has built in support for a /logout endpoint which will do the right thing for us (clear the session and invalidate the cookie). To configure the endpoint we simply extend the existing configure() method in our WebSecurityConfigurerAdapter:
- Spring Security는 우리에게 옳은 일을 할 /logout 끝점을 지원하기 위해 구축되었습니다 (세션을 지우고 쿠키를 무효화). 끝점을 구성하기 위해 웹 보안 구성어 어댑터에서 기존 구성() 메서드를 확장하기만 하면 됩니다.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	// @formatter:off
    http
        // ... existing code here
        .logout(l -> l
            .logoutSuccessUrl("/").permitAll()
        )
        // ... existing code here
    // @formatter:on
}
```
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	// @formatter:off
    http
        // ... existing code here
        .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        // ... existing code here
    // @formatter:on
}
```

- Adding the CSRF Token in the Client
- pom.xml
```xml
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>js-cookie</artifactId>
    <version>2.1.0</version>
</dependency>
```
- index.html
```html
<script type="text/javascript" src="/webjars/js-cookie/js.cookie.js"></script>
```
```javascript
$.ajaxSetup({
  beforeSend : function(xhr, settings) {
    if (settings.type == 'POST' || settings.type == 'PUT'
        || settings.type == 'DELETE') {
      if (!(/^http:.*/.test(settings.url) || /^https:.*/
        .test(settings.url))) {
        // Only send the token to relative URLs i.e. locally.
        xhr.setRequestHeader("X-XSRF-TOKEN",
          Cookies.get('XSRF-TOKEN'));
      }
    }
  }
});
```
```
```
```
```
