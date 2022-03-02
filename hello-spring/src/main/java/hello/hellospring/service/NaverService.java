package hello.hellospring.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverService {

    private final String NAVER_CLIENT_ID = "_zUMRWgdicaUDBAKI6hV";
    private final String NAVER_CLIENT_SECRET = "RACeSzDklU";
    private final String NAVER_BASE_URL = "https://nid.naver.com/oauth2.0/authorize";
    private final String NAVER_TOKEN_BASE_URL = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_CALLBACK_URL = "http://localhost:8080/auth/naver/callback";
    private final String NAVER_OPEN_API_PROFILE = "https://openapi.naver.com/v1/nid/me";

    /**
     * 네아로 url get
     */
    public String getRequestLoginUrl(HttpServletRequest request) {

        final String state = new BigInteger(130, new SecureRandom()).toString();

        request.getSession().setAttribute("NAVER_STATE", state);

        HashMap map = new HashMap();
        map.put("a", "aa");
        map.put("b", "bb");
        map.put("a", "a1");

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.add("response_type", "code");
        requestParam.add("state", state);
        requestParam.add("client_id", NAVER_CLIENT_ID);
        requestParam.add("redirect_uri", NAVER_CALLBACK_URL);

        String rtn = UriComponentsBuilder.fromUriString(NAVER_BASE_URL)
                .queryParams(requestParam)
                .build().encode()
                .toString();

        System.out.println("getRequestLoginUrl = "+ rtn);

        return rtn;
    }

    /**
     * code, state, client_id, client_secret, grant_type 값으로 access_token 값 가져오기
     * @param code
     * @param state
     * @return
     */
    public ResponseEntity<Map> requestAccessToken(String code, String state) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("state", state);
        requestBody.add("client_id", NAVER_CLIENT_ID);
        requestBody.add("client_secret", NAVER_CLIENT_SECRET);
        requestBody.add("grant_type", "authorization_code");
        System.out.println("requestAccessToken requestBody = "+requestBody.toString());
//        return new RestTemplate().postForEntity(NAVER_TOKEN_BASE_URL, requestBody, Map.class);

        HttpHeaders headers = new HttpHeaders();
        // HttpEntity<>(headers)
        // HttpEntity<>(requestBody, headers) : 파라미터 순서 주의
        HttpEntity<?> requestParam = new HttpEntity<>(requestBody, headers);
        return new RestTemplate().exchange(NAVER_TOKEN_BASE_URL, HttpMethod.POST, requestParam, Map.class);
    }

    /**
     * access_token 값으로 네이버 프로파일 가져오기
     * @param accessToken
     * @return
     */
    public ResponseEntity<Map> getUserProfile1(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<Map> requestParam = new HttpEntity<>(headers);

        return new RestTemplate().exchange(NAVER_OPEN_API_PROFILE, HttpMethod.GET, requestParam, Map.class);
    }

    public String getUserProfile2(String accessToken) {

        String token = accessToken; // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println(responseBody);
        return responseBody;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
