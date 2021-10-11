package hello.hellospring.controller;

import hello.hellospring.service.NaverService;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NaverController {

    @Autowired
    private NaverService naverService;

    @GetMapping("/auth/naver")
    public void naver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String loginUrl = naverService.getRequestLoginUrl(request);
        response.sendRedirect(loginUrl);
    }

    @GetMapping("/auth/naver/callback")
    public String requestAccessCallback(HttpServletRequest request, @RequestParam(value = "code") String code
            , @RequestParam(value = "state") String state, Model model) throws ParseException {
//http://localhost:8080/auth/naver/callback?code=MuUL6hlXCIUt25zgEl&state=1295540687490528991210698014294887520753
        ResponseEntity<?> responseEntity = naverService.requestAccessToken(code, state);

//        Object responseMessage = responseEntity.getBody();
        Map map = (Map) responseEntity.getBody();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // 자바 객체로 변환
            // 레파지토리에 객체 저장
//            Map map = (Map) responseMessage;
//            System.out.println("access_token = " + map.get("access_token"));
//            System.out.println("refresh_token = " + map.get("refresh_token"));
//            System.out.println("token_type = " + map.get("token_type"));
//            System.out.println("expires_in = " + map.get("expires_in"));

            // 방법1 : RestTemplate().exchange
            ResponseEntity<?> responseEntity2 = naverService.getUserProfile1((String) map.get("access_token"));
            Map map2 = (Map) responseEntity2.getBody();
            if (responseEntity2.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("sessionId", "test1234");
                model.addAttribute("data", map2);
                model.addAttribute("map", map);
                request.getSession().setAttribute("sessionId", "test1234");
            }else{
                model.addAttribute("error", map2);
                return "error";
            }

            // 방법2 : HttpURLConnection
//            String rtnData = naverService.getUserProfile2((String) map.get("access_token"));
//            System.out.println("rtnData = " + rtnData);
//            JSONParser parser = new JSONParser(rtnData);
//            Object obj = parser.parse();

            return "hello";
        } else {
            // 응답 에러 처리
            return "error";
        }
    }


}
