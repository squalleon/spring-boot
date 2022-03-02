# 네이버 연동

- 소스코드 
  - NaverController.java
  - NaverService.java


- 동작
  - http://localhost:8080/
  - naver 클릭
  - naver 아이디로 로그인
  - 로그인 성공하면 main.html 로 이동하여 네이버 닉네임으로 표시한다.
    

- 네이버 아이디로 로그인 url(/auth/naver)로 이동
  -     https://nid.naver.com/oauth2.0/authorize
        requestParam.add("response_type", "code");
        requestParam.add("state", state);
        requestParam.add("client_id", NAVER_CLIENT_ID);
        requestParam.add("redirect_uri", NAVER_CALLBACK_URL);
- 로그인 성공 후 콜백 url(/auth/naver/callback) 호출
    -     @GetMapping("/auth/naver/callback")
          public String requestAccessCallback(HttpServletRequest request, @RequestParam(value = "code") String code
          , @RequestParam(value = "state") String state, Model model) throws ParseException {
    - code 값으로 access_code 값을 가져옴 
    -       ResponseEntity<?> responseEntity = naverService.requestAccessToken(code, state);
            ResponseEntity<?> responseEntity2 = naverService.getUserProfile1((String) map.get("access_token"));
- 네이버 프로파일 api 호출
  -     ResponseEntity<?> responseEntity2 = naverService.getUserProfile1((String) map.get("access_token"));
        Map map2 = (Map) responseEntity2.getBody();

- NaverController.java
```java
    @GetMapping("/auth/naver")
    public void naver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String loginUrl = naverService.getRequestLoginUrl(request);
        response.sendRedirect(loginUrl);
    }
```

- NaverService.java
```java
    /**
     * 네아로 url get
     */
    public String getRequestLoginUrl(HttpServletRequest request) {

        final String state = new BigInteger(130, new SecureRandom()).toString();

        request.getSession().setAttribute("NAVER_STATE", state);

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
```