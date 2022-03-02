var kakao_status_count = 0;

(function ($) {
    $.kakaoPayUtil = {

        kakaoPaySign: function (formObj, callbackKakaoFnc) {

            $plugins.uiLoading({visible: true});
            var callback = ajaxCmmn(formObj, "/Cmmn/kakaoPaySignCall.do", "", "", "N");
            callback.done(function (data) {
                $plugins.uiLoading({visible: false});
                console.log(JSON.stringify(data));
                var data = data.resMap;
                kakao_status_count = 0;
                if (data.CODE == "0000") {
                    $.kakaoPayUtil.kakaoPaySignStatus(formObj, callbackKakaoFnc, "N");
                } else {
                    alert(data.MSG);
                }
            });
        }
        , kakaoPaySignStatus: function (formObj, callbackKakaoFnc, recursiveYN) {

            if (recursiveYN == "N") {
                kakao_status_count = 0;
            }
            if (parseInt(kakao_status_count) < 36) {

                $plugins.uiLoading({visible: true});
                var callback = ajaxCmmn(formObj, "/Cmmn/kakaoPaySignStatusCall.do", "", "", "N");
                callback.done(function (data) {
                    $plugins.uiLoading({visible: false});
                    console.log(JSON.stringify(data));
                    var data = data.resMap;
                    if (data.CODE == "0000" && data.RTNJSON.status == "COMPLEATE") {
                        callbackKakaoFnc(data);
                    } else if (data.CODE == "E000" && data.status == "ERROR") {
                        alert(data.MSG);
                        $('#kakaoPaySignReq').hide();
                        $('#kakaoPaySign').show();
                    } else {
                        kakao_status_count++;
                        setTimeout(function () {
                            $.kakaoPayUtil.kakaoPaySignStatus(formObj, callbackKakaoFnc, "Y");
                        }, 5000);
                    }
                });
            } else {
                alert("카카오페이 인증시간이 초과 하였습니다. 다시 시도해주십시요.");

                $('#kakaoPaySignReq').hide();
                $('#kakaoPaySign').show();
                kakao_status_count = 0;
                $plugins.uiLoading({visible: false});
            }
        }
        , kakaoPaySignVerify: function (formObj, callbackKakaoFnc) {

            $plugins.uiLoading({visible: true});
            var callback = ajaxCmmn(formObj, "/Cmmn/kakaoPaySignVerifyCall.do", "", "", "", "");
            callback.done(function (data) {
                console.log(JSON.stringify(data));
                $plugins.uiLoading({visible: false});
                var data = data.resMap;
                if (data.CODE = "0000") {
                    callbackKakaoFnc(data);
                } else {
                    alert(data.MSG);
                }
            });
        }
    }
})(jQuery);