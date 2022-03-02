package com.miraeasset.cyber.cmmn.controller;

import com.miraeasset.cyber.cmmn.service.PassService;
import com.miraeasset.cyber.cmmn.util.AESCipher;
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
import java.net.URLEncoder;
import java.util.*;

@Controller
public class PassController {

    private static final Logger log = LoggerFactory.getLogger(PassController.class);

    private static final String COMPANY_CD = "00186";

    @Autowired
    private PassService sPassService;

    @Resource(name = "globalsProperties")
    Properties globalsProperties;


    @RequestMapping(value = "/Cmmn/PassAuthNoticeCall.do")
    public ModelAndView passAuthNoticeCall(HttpServletRequest request, HttpServletResponse response
            , ModelMap modelMap, @RequestParam Map<Object, Object> paramMap) throws Exception {

        ModelAndView rtnMv = new ModelAndView();
        HttpSession session = request.getSession();
        String PASS_URL_CALL = "";
        HashMap resMap = new HashMap();

        session.setAttribute("passAuthYn", "N");
        session.setAttribute("passAuthCustJm", "");

        try {
            /**
             * 프로퍼티값 세팅
             */
            String chkServer = com.miraeasset.cyber.cmmn.util.CmmnProperty.getChkServer();
            String passCallDomain = "";
            String aesKey = "";
            if ("L".equals(chkServer) || "D".equals(chkServer) || "Q".equals(chkServer)) {
                passCallDomain = globalsProperties.getProperty("Globals.pass.call_domain.prd");
                aesKey = globalsProperties.getProperty("Globals.pass.enc_key.prd");
            } else if ("R".equals(chkServer)) {
                passCallDomain = globalsProperties.getProperty("Globals.pass.call_domain.prd");
                aesKey = globalsProperties.getProperty("Globals.pass.enc_key.prd");
            }

            /**
             * 성명, 주민번호 세팅
             */
            String custNm = "";
            String custJm = "";
            String custJm1 = "";
            String custJm2 = "";
            String passCustJm2 = StringUtil.nvl((String) paramMap.get("passCustJm2"), "");
            String telCom = StringUtil.nvl((String) paramMap.get("selTelcom"), "").substring(0, 1);
            String phoneNo = StringUtil.nvl((String) paramMap.get("selTel"), "")
                    + StringUtil.nvl((String) paramMap.get("inpTel1"), "")
                    + StringUtil.nvl((String) paramMap.get("inpTel2"), "");

            session.setAttribute("PASS_TELCOM", telCom);
            session.setAttribute("PASS_PHONE_NO", phoneNo);

            if ("".equals(passCustJm2) == false) { // 1. 입력값 우선
                custNm = StringUtil.nvl((String) paramMap.get("passCustNm"), "");
                custJm1 = StringUtil.nvl((String) paramMap.get("passCustJm1"), "");
                custJm2 = StringUtil.nvl((String) paramMap.get("passCustJm2"), "");
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
             * API Endpoint
             */
            String API_ENDPOINT = "/v1/certification/notice";

            /**
             * PASS 인증 서비스 호출 파라미터 세팅
             */
            AESCipher ci = new AESCipher(aesKey);
            HashMap reqMap = new HashMap();
            reqMap.put("companyCd", COMPANY_CD); // 이용기관 코드
            reqMap.put("sericeTycd", "S3002"); // S3001:간편로그인 서비스, S3002:간편인증 서비스
            reqMap.put("telcoTycd", session.getAttribute("PASS_TELCOM")); // S:SKT, K:KT, L:LGU+
            reqMap.put("phoneNo", ci.encrypt((String) session.getAttribute("PASS_PHONE_NO"))); // S:SKT, K:KT, L:LGU+ AES256 암호화
            reqMap.put("userNm", ci.encrypt((String) session.getAttribute("CYBER_LOGIN_NM"))); // 사용자 이름 AES256 암호화
            reqMap.put("reqTitle", "본인확인(전자서명) 동의"); // 인증요청 알림
            reqMap.put("reqContent", "본인확인(전자서명) 동의합니다."); // 인증요청 알림
            reqMap.put("reqCSPhoneNo", "1588-0220"); // 이용기관 고객센터 연락처
            String s = DateUtil.getTimeAddSecond("", 720);
            String cpDate = s.substring(0, 4) + s.substring(8, 10) + ":" + s.substring(10, 12) + ":" + s.substring(12, 14);
            reqMap.put("reqEndDttm", cpDate); // 인증 만료일시 "YYYY-MM-DD hh:mi:ss"
            reqMap.put("isNotification", "Y"); // 사용자 단말로 인증 알림내용에 대한 통지(Notification) 여부
            reqMap.put("isPASSVerify", "Y"); // Y:서명검증을 PASS 인증서 플랫폼에서 수행
            reqMap.put("signTargetTycd", "1"); // 서명대상 유형 코드
            String nowHash = StringUtil.nvl(request.getParameter("nowHash"), "Main").replace("#", "");
            log.debug(nowHash);
            String nm = "";
            if (StringUtil.nvl((String) session.getAttribute("isLogin"), "") == "true") {
                nm = (String) session.getAttribute("CYBER_LOGIN_NM");
            } else {
                nm = (String) session.getAttribute("CYBER_VIEW_NM");
            }
            reqMap.put("signTarget", ci.encrypt(nm + "님은 본인인증(전자서명)에 동의합니다." + nowHash));
            String s1 = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            String s2 = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            session.setAttribute("PASS_REQ_TXID", s1 + s2);
            reqMap.put("reqTxId", s1 + s2); // 이용기관에서 생성한 트랜잭션 ID (20자리 영문자+숫자, 특수문자제외)
            reqMap.put("isDigitalSign", "Y"); // Y: 서명 완료 후 전자서명값 수신

            /**
             * PASS 인증 서비스 콜
             */
            PASS_URL_CALL = passCallDomain + API_ENDPOINT;
            log.info(" [PASS SIGN SEND] [" + custNm + "] [" + EncUtil128.getSSEnc(custJm) + "] [PASS 서명요청] [REQ]" + PASS_URL_CALL + "|" + userCI + "|" + reqMap);

            if (chkServer.equals("L")) {
                resMap = callPassServiceProxy(request, "POST", API_ENDPOINT, reqMap);
            } else {
                resMap = sPassService.callPassService(request, "POST", API_ENDPOINT, reqMap);
            }
            log.info(" [PASS SIGN RECV] [" + custNm + "] [" + EncUtil128.getSSEnc(custJm) + "] [PASS 서명요청] [RES]" + (String) resMap.get("CODE") + "|" + reqMap);

            /**
             * 응답 세팅
             */
            HashMap rtnJsonMap = (HashMap) resMap.get("RTNJSON");
            if ("0000".equals(resMap.get("CODE"))) {
                resMap.put("CODE", "0000");
                resMap.put("MSG", "정상호출되었습니다.");
                // reqTxId, certTxId 세션에 세팅
                session.setAttribute("PASS_REQ_TXID", rtnJsonMap.get("reqTxId"));
                session.setAttribute("PASS_CERT_TXID", rtnJsonMap.get("certTxId"));
            } else {
                resMap.put("CODE", "E000");
                resMap.put("MSG", "호출에 실패하였습니다.");

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
     * PASS 인증 처리상태 조회 요청(이용기관/대행사 => PASS 인증서 플랫폼)
     * @param request
     * @param response
     * @param modelMap
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/Cmmn/passAuthStatusCall.do"})
    public ModelAndView passAuthStatusCall(HttpServletRequest request, HttpServletResponse response
            , ModelMap modelMap, @RequestParam Map<Object, Object> paramMap) throws Exception {

        ModelAndView rtnMv = new ModelAndView();
        HttpSession session = request.getSession();
        HashMap resMap = new HashMap();

        try {
            String API_ENDPOINT = "/v1/certification/status";

            /**
             * 요청 트랜잭션 ID
             */
            String reqTxId = StringUtil.nvl((String) session.getAttribute("PASS_REQ_TXID"), "");
            String certTxId = StringUtil.nvl((String) session.getAttribute("PASS_CERT_TXID"), "");
            API_ENDPOINT += "?reqTxId=" + reqTxId + "&certTxId=" + certTxId;

            /**
             * 성명, 주민번호 세팅
             */
            String passCustNm = StringUtil.nvl((String) paramMap.get("passCustNm"), "");
            String passCustJm1 = StringUtil.nvl((String) paramMap.get("passCustJm1"), "");
            String passCustJm2 = StringUtil.nvl((String) paramMap.get("passCustJm2"), "");
            String passCustJm = passCustJm1 + passCustJm2;

            // CI 값 세팅
            String userCI = "";

            /**
             * PASS 호출 파라미터 세팅
             */
            HashMap reqMap = new HashMap();
            reqMap.put("reqTxId", reqTxId);
            reqMap.put("certTxId", certTxId);

            /**
             * PASS 인증 서비스 콜
             */
            log.info(" [PASS SIGN SEND] [" + passCustNm + "] [" + EncUtil128.getSSEnc(passCustJm) + "] [PASS 서명요청] [SEND]" + passCustNm + "|" + userCI + "|" + reqMap);
            resMap = sPassService.callPassService(request, "GET", API_ENDPOINT, reqTxId);
            log.info(" [PASS SIGN RECV] [" + passCustNm + "] [" + EncUtil128.getSSEnc(passCustJm) + "] [PASS 서명요청] [RECV]" + (String) resMap.get("CODE") + "|" + reqMap);

            /**
             * PASS 인증 서비스 응답
             */
            HashMap rtnJsonMap = (HashMap) resMap.get("RTNJSON");
            if ("0000".equals(resMap.get("CODE"))) {
                resMap.put("CODE", "0000");
                resMap.put("MSG", "정상호출되었습니다.");
            } else {
                resMap.put("CODE", "E000");
                HashMap errorMap = (HashMap) rtnJsonMap.get("error");
                if (errorMap != null) {
                    if ("CE0001".equals(StringUtil.nvl((String) errorMap.get("errorCode"), ""))) {
                        resMap.put("MSG", "호출에 실패하였습니다.");
                    } else {
                        resMap.put("MSG", "호출에 실패하였습니다.");
                    }
                } else {
                    resMap.put("MSG", "호출에 실패하였습니다.");
                }
                resMap.put("HTTPCODE", resMap.get("HTTPCODE"));
            }
        } catch (Exception e) {
            log.error(e);
            resMap.put("CODE", "E000");
            resMap.put("MSG", "호출에 실패하였습니다.");
        }

        rtnMv.addObject("resMap", resMap);

        return rtnMv;
    }

    /**
     * PASS 인증 결과 확인(이용기관/대행사 => PASS 인증서 플랫폼)
     * @param request
     * @param response
     * @param modelMap
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/Cmmn/passAuthResultCall.do"})
    public ModelAndView passAuthResultCall(HttpServletRequest request, HttpServletResponse response
            , ModelMap modelMap, @RequestParam Map<Object, Object> paramMap) throws Exception {

        ModelAndView rtnMv = new ModelAndView();
        HttpSession session = request.getSession();
        HashMap resMap = new HashMap();

        try {
            /**
             * 프로퍼티값 세팅
             */
            String chkServer = com.miraeasset.cyber.cmmn.util.CmmnProperty.getChkServer();
            String passCallDomain = "";
            String aesKey = "";
            if ("L".equals(chkServer) || "D".equals(chkServer) || "Q".equals(chkServer)) {
                passCallDomain = globalsProperties.getProperty("Globals.pass.call_domain.prd");
                aesKey = globalsProperties.getProperty("Globals.pass.enc_key.prd");
            } else if ("R".equals(chkServer)) {
                passCallDomain = globalsProperties.getProperty("Globals.pass.call_domain.prd");
                aesKey = globalsProperties.getProperty("Globals.pass.enc_key.prd");
            }

            /**
             * API Endpoint
             */
            String API_ENDPOINT = "/certification/result";

            /**
             * 성명, 주민번호 세팅
             */
            String passCustNm = "";
            String passCustJm1 = "";
            String passCustJm2 = "";
            String passCustJm = "";
            if (StringUtil.nvl((String) session.getAttribute("isLogin"), "").equals("true")) {
                passCustNm = (String) session.getAttribute("CYBER_LOGIN_NM");
                passCustJm = (String) session.getAttribute("CYBER_LOGIN_SSN");
            } else {
                passCustNm = (String) StringUtil.nvl(paramMap.get("passCustNm"));
                passCustJm = (String) StringUtil.nvl(paramMap.get("passCustJm"));
            }

            // CI 값 세팅
            String userCI = "";

            /**
             * PASS 인증 결과 확인 호출 파라미터 세팅
             */
            HashMap reqMap = new HashMap();
            AESCipher ci = new AESCipher();
            reqMap.put("companyCd", COMPANY_CD);
            reqMap.put("reqTxId", session.getAttribute("PASS_REQ_TXID"));
            reqMap.put("certTxId", session.getAttribute("PASS_CERT_TXID"));
            reqMap.put("phoneNo", ci.encrypt((String) session.getAttribute("PASS_PHONE_NO")));
            String nm = "";
            if (StringUtil.nvl((String) session.getAttribute("isLogin"), "").equals("true")) {
                nm = (String) session.getAttribute("CYBER_LOGIN_NM");
            } else {
                nm = (String) session.getAttribute("CYBER_VIEW_NM");
            }
            reqMap.put("userNm", ci.encrypt(nm));

            /**
             * PASS 인증 결과 확인 서비스 콜
             */
            log.info(" [PASS SIGN RESULT CALL SEND] [" + passCustNm + "] [" + EncUtil128.getSSEnc(passCustJm) + "] [PASS 서명요청] [SEND]" + passCustNm + "|" + userCI + "|" + reqMap);
            resMap = sPassService.callPassService(request, "POST", API_ENDPOINT, reqMap);
            log.info(" [PASS SIGN RESUTL CALL RECV] [" + passCustNm + "] [" + EncUtil128.getSSEnc(passCustJm) + "] [PASS 서명요청] [RECV]" + (String) resMap.get("CODE") + "|" + reqMap);

            /**
             * PASS 인증 결화 확인 서비스 응답
             */
            HashMap rtnJsonMap = (HashMap) reqMap.get("RTNJSON");
            if ("0000".equals(resMap.get("CODE")) && "1".equals(rtnJsonMap.get("resultTycd"))) {
                String passCI = ci.decrypt((String) rtnJsonMap.get("CI"));
                if (userCI.equals(passCI)
                        || userCI.equals("gyJTI~")) { // 패스55 테스트용
                    resMap.put("CODE", "0000");
                    resMap.put("MSG", "정상호출되었습니다.");

                    session.setAttribute("passAuthYN", "Y");
                    session.setAttribute("passAuthCustNm", passCustNm);
                    session.setAttribute("passAuthCustJm", passCustJm);
                } else {
                    resMap.put("CODE", "E000");
                    resMap.put("MSG", "호출에 실패하였습니다.");

                    log.info(passCI);
                    session.setAttribute("passAuthYN", "N");
                    session.setAttribute("passAuthCustNm", passCustNm);
                    session.setAttribute("passAuthCustJm", passCustJm);
                }
            } else {
                resMap.put("CODE", "E000");
                resMap.put("MSG", "호출에 실패하였습니다.");

                String resultTyNm = "";
                if ("2".equals(rtnJsonMap.get("resultTycd"))) {
                    resultTyNm = "인증 대기중";
                } else if ("3".equals(rtnJsonMap.get("resultTycd"))) {
                    resultTyNm = "서명검증 Fail";
                } else if ("4".equals(rtnJsonMap.get("resultTycd"))) {
                    resultTyNm = "인증요청 거절(취소)";
                } else if ("5".equals(rtnJsonMap.get("resultTycd"))) {
                    resultTyNm = "인증요청 만료";
                }
                log.info("resultTycd = " + rtnJsonMap.get("resultTycd") + ", " + resultTyNm);
                rtnJsonMap.put("resultTyNm", resultTyNm);

                session.setAttribute("passAuthYN", "N");
                session.setAttribute("passAuthCustNm", passCustNm);
                session.setAttribute("passAuthCustJm", passCustJm);
            }

        } catch (Exception e) {
            log.error(String.valueOf(e));
            resMap.put("CODE", "E000");
            resMap.put("MSG", "호출에 실패하였습니다.");
        }

        rtnMv.addObject("resMap", resMap);

        return rtnMv;
    }

    public HashMap<String, Object> callPassServiceProxy(HttpServletRequest request, String httpMethod
            , String API_ENDPOINT, HashMap REQ_MAP) throws URISyntaxException, UnsupportedEncodingException {

        String requestUrl = "/Cmmn/callPassService.do";
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
//        paramertes.add("PASS_CALL_URL", URLEncoder.encode(API_ENDPOINT, "UTF-8"));

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
