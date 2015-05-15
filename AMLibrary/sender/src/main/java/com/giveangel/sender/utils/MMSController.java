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

    public String sendValidCheck(HashMap<Object, Object> params)
            throws JSONException,
            IOException {
        // TODO Auto-generated method stub
        Log.i(MMSController.class.getSimpleName(), "in sendValidCheck");
        String functionURL = URL_MMS + FUNCTION_LIST.valid_check.getUrl();
        String url = this.getUrl(functionURL, params);
        System.out.println(url);
        String jsonValue = getStringFromUrl(url);
        System.out.println("jsonvalue = " + jsonValue);
        HashMap<String, Object> map = fromJSON(
                new TypeReference<HashMap<String, Object>>() {
                }, jsonValue);
        return map.get(AMLCostants.KEY_RETURN_VALID_OK).toString();
    }

    public void informSentMMS(HashMap<Object, Object> params) throws JSONException,
            IOException {
        Log.i(MMSController.class.getSimpleName(), "in informSentMMS");
        String functionURL = URL_MMS + FUNCTION_LIST.inform_sentmms.getUrl();
        String url = this.getUrl(functionURL, params);
        System.out.println(url);
        getStringFromUrl(url);
    }

    public static enum FUNCTION_LIST {
        valid_check("mms_stop.asp"), inform_sentmms("cont.asp");
        private String url;

        private FUNCTION_LIST(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
