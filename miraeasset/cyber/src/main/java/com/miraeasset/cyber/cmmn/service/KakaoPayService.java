package com.miraeasset.cyber.cmmn.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service("sKakaoService")
public class KakaoPayService {

    private static final Logger log = LoggerFactory.getLogger(KakaoPayService.class);

    @Resource(name = "globalsProperties")
    Properties globalsProperties;

    /**
     * 본인인증/전자서명 서비스 호출
     * @param request
     * @param httpMethod
     * @param API_ENDPOINT
     * @param REQ_MAP
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> callKakaoService(HttpServletRequest request
            , String httpMethod, String API_ENDPOINT, HashMap REQ_MAP) throws Exception {

        HashMap rtnMap = new HashMap();

        try {
            /**
             * 프로퍼티값 세팅
             */
            String chkServer = com.miraeasset.cyber.cmmn.util.CmmnProperty.getChkServer();
            String kakaoCallDomain = "";
            String kakaoToken = "";
            if ("L".equals(chkServer) || "D".equals(chkServer) || "Q".equals(chkServer)) {
                kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain.prd");
                kakaoToken = globalsProperties.getProperty("Globals.kakao.token.prd");
            } else if ("R".equals(chkServer)) {
                kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain.prd");
                kakaoToken = globalsProperties.getProperty("Globals.kakao.token.prd");
            }

            /**
             * 헤더 작성
             */
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + kakaoToken);
            headers.add("Content-Type", "application/json;charset=UTF-8");

            /**
             * 호출 파라미터 세팅
             */

            /**
             * HttpEntity 세팅
             */
            HttpEntity<?> requestEntity = new HttpEntity<>(REQ_MAP, headers);

            /**
             * RestTemplate 요청
             */
            RestTemplate restTemplate = new RestTemplate();

            Object httpMethodObj = HttpMethod.POST;
            if ("GET".equals(httpMethod)) {
                httpMethodObj = HttpMethod.GET;
            }

            ResponseEntity<?> responseEntity = restTemplate.exchange(kakaoCallDomain + API_ENDPOINT, (HttpMethod) httpMethodObj, requestEntity, REQ_MAP.getClass());

            /**
             * 응답값 세팅
             */
            rtnMap.put("CODE", "0000");
            rtnMap.put("MSG", "정상호출되었습니다.");
            rtnMap.put("RTNJSON", responseEntity.getBody());


        } catch (HttpStatusCodeException e) {

            String strRtnMsg = new String((e.getResponseBodyAsString().getBytes(StandardCharsets.UTF_8)), "UTF-8");

            log.info(e.getStatusCode() + ", " + strRtnMsg);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> data = objectMapper.readValue(strRtnMsg, new TypeReference<Map<String, Object>>() {
            });

            rtnMap.put("CODE", "E000");
            rtnMap.put("MSG", "호출에 실패하였습니다.");
            rtnMap.put("RTNJSON", data);

            return rtnMap;
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }

        return rtnMap;
    }

}
