var pass_status_count = 0;

(function ($) {
    $.passUtil = {

        passAuth: function (formObj, callbackPassFnc) {

            $plugins.uiLoading({visible: true});
            var callback = ajaxCmmn(formObj, "/Cmmn/PassAuthNoticeCall.do", "", "", "N");
            callback.done(function (data) {
                $plugins.uiLoading({visible: false});
                console.log(JSON.stringify(data));
                var data = data.resMap;
                pass_status_count = 0;
                if (data.CODE == "0000") {
                    callbackPassFnc(data);
                } else {
                    alert(data.MSG + "[" + data.RTNJSON.errorCd + "]" + data.RTNJSON.errorMessage);
                }
            });
        }
        , passAuthStatus: function (formObj, callbackPassFnc, recursiveYN) {

            if (recursiveYN == "N") {
                pass_status_count = 0;
            }
            if (parseInt(pass_status_count) < 36) {

                $plugins.uiLoading({visible: true});
                var callback = ajaxCmmn(formObj, "/Cmmn/passAuthStatusCall.do", "", "", "N");
                callback.done(function (data) {
                    $plugins.uiLoading({visible: false});
                    console.log(JSON.stringify(data));
                    var data = data.resMap;
                    if (data.CODE == "0000" && data.RTNJSON.statusCd == "C") {
                        $.passUtil.passAuthResult(formObj, callbackPassFnc);
                    } else if (data.CODE == "E000" && data.status == "ERROR") {
                        alert(data.MSG);
                    } else {
                        pass_status_count++;
                        setTimeout(function () {
                            $.passUtil.passAuthStatus(formObj, callbackPassFnc, "Y");
                        }, 5000);
                    }
                });
            } else {
                alert("PASS인증시간이 초과 하였습니다. 다시 시도해주십시요.");

                $('#passAuthReqNoti').hide();
                $('#passAuthReqNoti').text('인증확인');
                $('#passAuthReqNoti').show();

                pass_status_count = 0;
                $plugins.uiLoading({visible: false});
            }
        }
        , passAuthResult: function (formObj, callbackPassFnc) {

            $plugins.uiLoading({visible: true});
            var callback = ajaxCmmn(formObj, "/Cmmn/passAuthResultCall.do", "", "", "", "");
            callback.done(function (data) {
                console.log(JSON.stringify(data));
                $plugins.uiLoading({visible: false});
                var data = data.resMap;
                if (data.CODE = "0000") {
                    callbackPassFnc(data);
                } else {
                    alert(data.MSG + "(" + data.RTNJSON.resultTyNm + ")");
                }
            });
        }
    }
})(jQuery);