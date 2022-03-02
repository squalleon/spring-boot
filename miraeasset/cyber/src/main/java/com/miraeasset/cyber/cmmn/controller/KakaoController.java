package com.miraeasset.cyber.cmmn.controller;

import com.miraeasset.cyber.cmmn.service.CmmnService;
import com.miraeasset.cyber.cmmn.service.KakaoPayService;
import com.miraeasset.cyber.cmmn.util.DateUtil;
import com.miraeasset.cyber.cmmn.util.EncUtil128;
import com.miraeasset.cyber.cmmn.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
public class KakaoController {

    private static final Logger log = LoggerFactory.getLogger(KakaoController.class);

    private static final String COMPANY_CD = "00186";

    @Autowired
    private CmmnService cmmnSerivce;

    @Autowired
    private KakaoPayService sKakaoPayService;

    @Resource(name = "globalsProperties")
    Properties globalsProperties;


    @RequestMapping(value = "/Cmmn/kakaoPaySignCall.do")
    public ModelAndView kakaoPaySignCall(HttpServletRequest request, HttpServletResponse response
            , ModelMap modelMap, @RequestParam Map<Object, Object> paramMap) throws Exception {

        ModelAndView rtnMv = new ModelAndView();
        HttpSession session = request.getSession();
        String KAKAO_URL_CALL = "";
        HashMap resMap = new HashMap();

        session.setAttribute("kakaopayAuthYn", "N");
        session.setAttribute("kakaopayAuthCustJm", "");

        try {
            /**
             * 프로퍼티값 세팅
             */
            String chkServer = com.miraeasset.cyber.cmmn.util.CmmnProperty.getChkServer();
            String kakaoCallDomain = "";
            String aesKey = "";
            if ("L".equals(chkServer) || "D".equals(chkServer) || "Q".equals(chkServer)) {
                kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain.prd");
                aesKey = globalsProperties.getProperty("Globals.kakao.enc_key.prd");
            } else if ("R".equals(chkServer)) {
                kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain.prd");
                aesKey = globalsProperties.getProperty("Globals.kakao.enc_key.prd");
            }

            /**
             * Request 파라미터 세팅
             */
            String svcCode = StringUtil.nvl((String) paramMap.get("service_code"), "");

            /**
             * 성명, 주민번호 세팅
             */
            String custNm = "";
            String custJm = "";
            String custJm1 = "";
            String custJm2 = "";
            String kakaoCustJm2 = StringUtil.nvl((String) paramMap.get("kakaoCustJm2"), "");
            String telCom = StringUtil.nvl((String) paramMap.get("selTelcom"), "").substring(0, 1);
            String phoneNo = StringUtil.nvl((String) paramMap.get("selTel"), "")
                    + StringUtil.nvl((String) paramMap.get("inpTel1"), "")
                    + StringUtil.nvl((String) paramMap.get("inpTel2"), "");

            session.setAttribute("KAKAO_TELCOM", telCom);
            session.setAttribute("KAKAO_PHONE_NO", phoneNo);

            if ("".equals(kakaoCustJm2) == false) { // 1. 입력값 우선
                custNm = StringUtil.nvl((String) paramMap.get("kakaoCustNm"), "");
                custJm1 = StringUtil.nvl((String) paramMap.get("kakaoCustJm1"), "");
                custJm2 = StringUtil.nvl((String) paramMap.get("kakaoCustJm2"), "");
                custJm = custJm1 + custJm2;
                session.setAttribute("CYBER_VIEW_NM", custNm);
                session.setAttribute("CYBER_VIEW_SSN", custJm);
            }
            if ("".equals(custJm)) { // 2. 로그인 정보
                custNm = StringUtil.nvl((String) session.getAttribute("CYBER_LOGIN_NM"), "");
                custJm = StringUtil.nvl((String) session.getAttribute("CYBER_LOGIN_SSN"), "");
            }
            if ("".equals(custJm)) { // 3. memberInfo 조회후

            }

            /**
             * CI값 세팅
             */
            String userCI = "";

            /**
             * 일회성 토큰값 세팅
             */
            String strSignToken = "미래에셋생명보험주식회사:" + DateUtil.getCurrentDateTime() 
                    + ":" + StringUtil.nvl((String) request.getParameter("nowHash"), "").replace("#", "");

            /**
             * 휴대폰번호, 성명, 생년월일, CI 세팅
             */
            HashMap reqMap = new HashMap();
            reqMap.put("ci", userCI);
            reqMap.put("expires_in", "180"); // 처리마감시간(초 단위) 권장값 5분(300 sec)
            reqMap.put("call_center_no", "1588-0220"); // 고객센터 전화번호
            reqMap.put("title", "카카오페이 본인인증"); 
            reqMap.put("token", strSignToken);
            
            // S610 간편로그인 경우
            if ("S610".equals(svcCode)) {
                HashMap kakaoReqMap = new HashMap();
                kakaoReqMap.put("ci", userCI);
                HashMap kakaoResMap = cmmnService.seletContent(kakaoReqMap, "CMMN_LOGIN_SQL.selectRemoMemberKakao", "Madb");

                if (kakaoResMap == null) {
                    resMap.put("CODE", "E001");
                    resMap.put("MSG", "카카오페이에서 이용기관 등록을 하시고 이용해 주시기 바랍니다.");
                    rtnMv.addObject("resMap", resMap);
                    return rtnMv;
                }
                reqMap.put("service_user_id", StringUtil.nvl((String) kakaoResMap.get("CI"), "")); // 서비스 유저아이디
            }
            
            /**
             * API Endpoint
             */
            String API_ENDPOINT = "/api/sign/request/" + svcCode;
            

            /**
             * KAKAO 인증 서비스 콜
             */
            KAKAO_URL_CALL = kakaoCallDomain + API_ENDPOINT + svcCode;
            log.info(" [KAKAOPAY SIGN SEND] [" + custNm + "] [" + EncUtil128.getSSEnc(custJm) + "] [KAKAOPAY 서명요청] [REQ]" + KAKAO_URL_CALL + "|" + userCI + "|" + reqMap);

            if (chkServer.equals("L")) {
                resMap = callKakaoServiceProxy(request, "POST", API_ENDPOINT, reqMap);
            } else {
                resMap = sKakaoPayService.callKakaoService(request, "POST", API_ENDPOINT, reqMap);
            }
            log.info(" [KAKAOPAY SIGN RECV] [" + custNm + "] [" + EncUtil128.getSSEnc(custJm) + "] [KAKAOPAY 서명요청] [RES]" + (String) resMap.get("CODE") + "|" + reqMap);

            /**
             * 응답 세팅
             */
            HashMap rtnJsonMap = (HashMap) resMap.get("RTNJSON");
            if ("0000".equals(resMap.get("CODE")) && rtnJsonMap.get("data") != null) {
                if ("Y".equals(((HashMap) rtnJsonMap.get("data")).get("result"))) {
                    resMap.put("CODE", "0000");
                    resMap.put("MSG", "정상호출되었습니다.");
                    // tx_id 세팅
                    session.setAttribute("kakaopay_sign_tx_id", ((HashMap) rtnJsonMap.get("tx_id")));
                } else {
                    resMap.put("CODE", "E000");
                    resMap.put("MSG", "호출에 실패하였습니다.");
                }
            } else {
                resMap.put("CODE", (String) rtnJsonMap.get("errcode"));
                resMap.put("MSG", (String) rtnJsonMap.get("errmsg"));
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            resMap.put("CODE", "E000");
            resMap.put("MSG", "호출에 실패하였습니다.");
        }

        rtnMv.addObject("resMap", resMap);

        return rtnMv;
    }

    /**
     * KAKAO 전자서명 상태 조회
     * @param request
     * @param response
     * @param modelMap
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/Cmmn/kakaopaySignStatusCall.do"})
    public ModelAndView kakaopaySignStatusCall(HttpServletRequest request, HttpServletResponse response
            , ModelMap modelMap, @RequestParam Map<Object, Object> paramMap) throws Exception {

        ModelAndView rtnMv = new ModelAndView();
        HttpSession session = request.getSession();
        String KAKAO_CALL_URL = "";
        HashMap resMap = new HashMap();

        try {

            /**
             * 프로퍼티값 세팅
             */
            String kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain");

            String API_ENDPOINT = "/v1/certification/status";

            /**
             * 요청 트랜잭션 ID
             */
            String tx_id = StringUtil.nvl((String) session.getAttribute("kakaopay_sign_tx_id"), "");

            /**
             * 횟수제한
             */
            if (session.getAttribute("kakaopay_status_count") == null) {
                session.setAttribute("kakaopay_status_count", "0");
            } else {
                int kakaopay_status_count = Integer.parseInt((String) session.getAttribute("kakopay_status_count"));
                if (kakaopay_status_count > 35) {
                    session.setAttribute("kakaopay_status_count", "0");
                    resMap.put("CODE", "E000");
                    resMap.put("MSG", "카카오페이 인증시간이 초과하였습니다.");
                    resMap.put("status", "ERROR");
                    rtnMv.addObject("resMap", resMap);
                    return rtnMv;
                } else {
                    kakaopay_status_count++;
                    session.setAttribute("kakaopay_status_count");
                    Integer.toString(kakaopay_status_count);
                }
            } 

            /**
             * 성명, 주민번호 세팅
             */
            String kakaoCustNm = StringUtil.nvl((String) paramMap.get("kakaoCustNm"), "");
            String kakaoCustJm1 = StringUtil.nvl((String) paramMap.get("kakaoCustJm1"), "");
            String kakaoCustJm2 = StringUtil.nvl((String) paramMap.get("kakaoCustJm2"), "");
            String kakaoCustJm = kakaoCustJm1 + kakaoCustJm2;

            // CI 값 세팅
            String userCI = "";

            /**
             * KAKAO 호출 파라미터 세팅
             */
            HashMap reqMap = new HashMap();

            /**
             * KAKAO 인증 서비스 콜
             */
            KAKAO_CALL_URL = kakaoCallDomain + API_ENDPOINT + "?tx_id=" + tx_id;
            log.info(" [KAKAOPAY STATUS SEND] [" + kakaoCustNm + "] [" + EncUtil128.getSSEnc(kakaoCustJm) + "] [KAKAOPAY 서명요청] [SEND]" + KAKAO_CALL_URL + "|" + kakaoCustNm + "|" + reqMap);
            resMap = sKakaoPayService.callKakaoService(request, "GET", API_ENDPOINT, reqMap);
            log.info(" [KAKAOPAY STATUS RECV] [" + kakaoCustNm + "] [" + EncUtil128.getSSEnc(kakaoCustJm) + "] [KAKAOPAY 서명요청] [RECV]" + (String) resMap.get("CODE") + "|" + reqMap);

            /**
             * KAKAO 인증 서비스 응답
             * status : PREPARE(대기중), COMPLETE(서명완료), EXPIREED(타임아웃)
             */
            HashMap rtnJsonMap = (HashMap) resMap.get("RTNJSON");
            String rtnStatus = (String) ((HashMap) rtnJsonMap.get("data")).get("status");
            if ("0000".equals(resMap.get("CODE")) && rtnJsonMap.get("data") != null) {
                if ("COMPLEATE".equals(((HashMap) rtnJsonMap.get("data")).get("status"))) {
                    resMap.put("CODE", "0000");
                    resMap.put("MSG", "정상호출되었습니다.");

                    session.setAttribute("kakaopay_status_tx_id", ((HashMap) rtnJsonMap.get("data")).get("tx_id"));
                    reqMap.put("status", rtnStatus);
                } else {
                    resMap.put("CODE", "E000");
                    resMap.put("MSG", "호출에 실패하였습니다.");
                    reqMap.put("status", rtnStatus);
                }
            } else {
                resMap.put("CODE", (String) rtnJsonMap.get("errcode"));
                resMap.put("MSG", (String) rtnJsonMap.get("errmsg"));
                HashMap errorMap = (HashMap) rtnJsonMap.get("error");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resMap.put("CODE", "E000");
            resMap.put("MSG", "호출에 실패하였습니다.");
            resMap.put("status", "ERROR");
        }

        rtnMv.addObject("resMap", resMap);

        return rtnMv;
    }

    /**
     * KAKAOPAY 전자서명 검증
     * @param request
     * @param response
     * @param modelMap
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/Cmmn/kakaoPaySignVerifyCall.do"})
    public ModelAndView kakaoPaySignVerifyCall(HttpServletRequest request, HttpServletResponse response
            , ModelMap modelMap, @RequestParam Map<Object, Object> paramMap) throws Exception {

        ModelAndView rtnMv = new ModelAndView();
        HttpSession session = request.getSession();
        String KAKAO_CALL_URL = "";
        HashMap resMap = new HashMap();

        try {
            /**
             * 프로퍼티값 세팅
             */
            String chkServer = com.miraeasset.cyber.cmmn.util.CmmnProperty.getChkServer();
            String kakaoCallDomain = "";
            if ("L".equals(chkServer) || "D".equals(chkServer) || "Q".equals(chkServer)) {
                kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain.prd");
            } else if ("R".equals(chkServer)) {
                kakaoCallDomain = globalsProperties.getProperty("Globals.kakao.call_domain.prd");
            }

            /**
             * Request 파라미터 세팅
             */
            String tx_id = StringUtil.nvl((String) session.getAttribute("kakaopay_status_tx_id"), "");

            /**
             * API Endpoint
             */
            String API_ENDPOINT = "/api/v1/sign/verify";

            /**
             * 성명, 주민번호 세팅
             */
            String kakaoCustNm = "";
            String kakaoCustJm1 = "";
            String kakaoCustJm2 = "";
            String kakaoCustJm = "";
            if (StringUtil.nvl((String) session.getAttribute("isLogin"), "").equals("true")) {
                kakaoCustNm = (String) session.getAttribute("CYBER_LOGIN_NM");
                kakaoCustJm = (String) session.getAttribute("CYBER_LOGIN_SSN");
            } else {
                kakaoCustNm = StringUtil.nvl((String) paramMap.get("kakaoCustNm"));
                kakaoCustJm = StringUtil.nvl((String) paramMap.get("kakaoCustJm"));
            }

            // CI 값 세팅
            String userCI = "";

            /**
             * KAKAO 인증 결과 확인 호출 파라미터 세팅
             */
            HashMap reqMap = new HashMap();

            /**
             * KAKAO 인증 결과 확인 서비스 콜
             */
            KAKAO_CALL_URL = kakaoCallDomain + API_ENDPOINT + "?tx_id=" + tx_id;
            log.info(" [KAKAO SIGN RESULT CALL SEND] [" + kakaoCustNm + "] [" + EncUtil128.getSSEnc(kakaoCustJm) + "] [KAKAO 서명요청] [SEND]" + kakaoCustNm + "|" + userCI + "|" + reqMap);
            resMap = sKakaoPayService.callKakaoService(request, "GET", API_ENDPOINT, reqMap);
            log.info(" [KAKAO SIGN RESUTL CALL RECV] [" + kakaoCustNm + "] [" + EncUtil128.getSSEnc(kakaoCustJm) + "] [KAKAO 서명요청] [RECV]" + (String) resMap.get("CODE") + "|" + reqMap);

            /**
             * KAKAO 인증 결화 확인 서비스 응답
             */
            HashMap rtnJsonMap = (HashMap) reqMap.get("RTNJSON");
            if ("0000".equals(resMap.get("CODE")) && rtnJsonMap.get("data") != null) {
                if (!"".equals((String) ((HashMap) rtnJsonMap.get("data")).get("signed_data")))) {
                    if (session.getAttribute("kakao_verify_time") == null) {

                        session.setAttribute("kakao_verify_time", LocalTime.now());
                        resMap.put("CODE", "0000");
                        resMap.put("MSG", "정상호출되었습니다.");
                        resMap.put("tx_id", ((HashMap) rtnJsonMap.get("data")).get("tx_id"));
                        resMap.put("signed_data", ((HashMap) rtnJsonMap.get("data")).get("signed_data"));
                        resMap.put("memNo", (String) session.getAttribute("CYBER_LOGIN_PID"));

                        session.setAttribute("kakaopayAuthYN", "Y");
                        session.setAttribute("kakaopayAuthCustNm", kakaoCustNm);
                    }
                } else {
                    LocalTime session_now = (LocalTime) session.getAttribute("kakao_verify_time");
                    Duration diff = Duration.between(session_now, LocalTime.now());

                    // 카카오페이 인증시도가 11초 이내라면
                    if (diff.getSeconds() < 12) {
                        resMap.put("CODE", "E000");
                        resMap.put("MSG", "인증요청 처리중입니다. 잠시후 다시 인증요청해 주시기 바랍니다.");
                    } else {
                        session.setAttribute("kakao_verify_time", LocalTime.now());

                        resMap.put("CODE", "0000");
                        resMap.put("MSG", "정상호출되었습니다.");
                        resMap.put("tx_id", ((HashMap) rtnJsonMap.get("data")).get("tx_id"));
                        resMap.put("tx_id", ((HashMap) rtnJsonMap.get("data")).get("tx_id"));
                        resMap.put("memNo", (String) session.getAttribute("CYBER_LOGIN_PID"));

                        session.setAttribute("kakaopayAuthYN", "Y");
                        session.setAttribute("kakaopayAuthCustNm", kakaoCustNm);
                    }
                }
            } else {
                resMap.put("CODE", "E000");
                resMap.put("MSG", "서명값이 올바르지 않습니다.");

                session.setAttribute("kakaopayAuthCustNm", "");
            }

            // 카카오페이 인증 정상일 경우 DB에 인증기록 등록
            if ("0000".equals(resMap.get("CODE"))) {
                // PID 가 없으면 퇴직연금 CSNO 로 변환
                String signPid = StringUtil.nvl((String) session.getAttribute("CYBER_LOGIN_PID"));
                signPid = "".equals(signPid) ? StringUtil.nvl((String) session.getAttribute("CYBER_LOGIN_RETIRE_CSNO"), "") : signPid;

                // 휴대폰 SMS 인증 결과 DB 등록
                HashMap<String, String> certSignMap = new HashMap<String, String>();
                certSignMap.put("PID", signPid);
                certSignMap.put("SIGN_GB", "08"); // 08 : 카카오페이 인증
                certSignMap.put("NAME", kakaoCustNm);
                certSignMap.put("MENU_ID", (String) paramMap.get("nowHash"));
                certSignMap.put("DN", (String) (((HashMap) rtnJsonMap.get("data")).get("signed_data")));
                certSignMap.put("SIGN_DT", DateUtil.getCurrentDateTime());
                cmmnSerivce.insertContent(certSignMap, "CMMN_SQL.insertSignData", "Madb");
            } else {
                resMap.put("CODE", (String) rtnJsonMap.get("errcode"));
                resMap.put("MSG", (String) rtnJsonMap.get("errmsg"));
                session.setAttribute("kakaopayAuthCustJm", "");
            }

        } catch (Exception e) {
            log.error(String.valueOf(e));
            resMap.put("CODE", "E000");
            resMap.put("MSG", "호출에 실패하였습니다.");
        }

        rtnMv.addObject("resMap", resMap);

        return rtnMv;
    }

    public HashMap<String, Object> callKakaoServiceProxy(HttpServletRequest request, String httpMethod
            , String API_ENDPOINT, HashMap REQ_MAP) throws URISyntaxException, UnsupportedEncodingException {

        String requestUrl = "/Cmmn/callKakaoService.do";
        URI uri = new URI("https", null, "life-q2.miraeasset.com", 443, null, null, null);

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        MultiValueMap<String, String> paramertes = new LinkedMultiValueMap<>();
//        Iterator<String> key = REQ_MAP.keySet().iterator();
//        while (key.hasNext()) {
//            String strKey = key.next();
//            paramertes.add(strKey, (String) REQ_MAP.get(strKey));
//        }
//        paramertes.add("KAKAO_CALL_URL", URLEncoder.encode(API_ENDPOINT, "UTF-8"));

        HttpEntity<?> httpEntity = new HttpEntity<>(REQ_MAP, headers);

        RestTemplate restTemplate = new RestTemplate();

        Object httpMethodObj = HttpMethod.POST;

        if ("GET".equals(httpMethod)) {
            httpMethodObj = HttpMethod.GET;
            uri = UriComponentsBuilder.fromUri(uri).path(requestUrl).query(request.getQueryString())
                    .queryParams(paramertes)
                    .build(true).toUri();
        } else {
            uri = UriComponentsBuilder.fromUri(uri).path(requestUrl).query(request.getQueryString())
                    .build(true).toUri();
        }

        ResponseEntity<?> responseEntity = restTemplate.exchange(uri, (HttpMethod) httpMethodObj, httpEntity, Map.class);
        HashMap<String, Object> rtn = (HashMap<String, Object>) responseEntity.getBody();
        HashMap<String, Object> rtn2 = (HashMap<String, Object>) rtn.get("resMap");

        return rtn2;
    }

























}
