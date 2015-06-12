package com.giveangel.sender.utils;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.giveangel.sender.AMLCostants;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Kyungman on 2015-05-08.
 */
public class MMSController extends Controller {
    // default context
    private static final String URL_MMS = URL_ROOT;

    public MMSController(Context context) {
        super(context);
    }

    /* MMS를 전송해도 되는(유효한{제휴된}) 앱인지 확인 */
    public String sendValidCheck(HashMap<Object, Object> params)
            throws JSONException,
            IOException {
        // TODO Auto-generated method stub
        Log.i(MMSController.class.getSimpleName(), "in sendValidCheck");
        String functionURL = URL_MMS + FUNCTION_LIST.valid_check.getUrl();
        String url = this.getUrl(functionURL, params);
//        System.out.println(url);
        String jsonValue = getStringFromUrl(url);
//        System.out.println("jsonvalue = " + jsonValue);
        HashMap<String, Object> map = fromJSON(
                new TypeReference<HashMap<String, Object>>() {
                }, jsonValue);
        return map.get(AMLCostants.KEY_RETURN_VALID_OK).toString();
    }

    /* 전송 됬다는 사실을 통지 */
    public void informSentMMS(HashMap<Object, Object> params) throws JSONException,
            IOException {
        Log.i(MMSController.class.getSimpleName(), "in informSentMMS");
        String functionURL = URL_MMS + FUNCTION_LIST.inform_sentmms.getUrl();
        String url = this.getUrl(functionURL, params);
//        System.out.println(url);
        getStringFromUrl(url);
    }

    /* 메세지를 보내야 하는 발신지의 번호를 받아옴 */
    public String getTargetNumber(HashMap<Object, Object> params) throws JSONException,
            IOException {
        Log.i(MMSController.class.getSimpleName(), "in getTargetNumber");
        String functionURL = URL_MMS + FUNCTION_LIST.get_targetnumber.getUrl();
        String url = this.getUrl(functionURL, params);
//        System.out.println(url);
        String jsonValue = getStringFromUrl(url);
//        System.out.println("jsonvalue = " + jsonValue);
        HashMap<String, Object> map = fromJSON(
                new TypeReference<HashMap<String, Object>>() {
                }, jsonValue);
        return map.get(AMLCostants.KEY_RETURN_TARGET_NUMBER).toString();
    }

    /* URL정보를 저장하는 열거형 변수 */
    public static enum FUNCTION_LIST {
        valid_check("mms_stop.asp"), inform_sentmms("cont.asp"), get_targetnumber("callednum_check.asp");
        private String url;

        private FUNCTION_LIST(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
