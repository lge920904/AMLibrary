package com.giveangel.amlibrary.adpublisher.utils;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kyungman on 2015-05-15.
 */
public class ADController extends Controller {
    private static final String URL_CONTEST_FRAD = URL_ROOT_FRAD;
    private static final String URL_CONTEST_AD = URL_ROOT_AD;

    public ADController(Context context) {
        super(context);
    }
    public String checkValidApp(HashMap<Object, Object> params) throws JSONException,
            IOException {
        String functionURL = URL_CONTEST_FRAD + FUNCTION_LIST.valid_check.getUrl();
        String url = this.getUrl(functionURL, params);
        String jsonValue = getStringFromUrl(url);
        HashMap<Object, Object> map = fromJSON(
                new TypeReference<HashMap<Object, Object>>() {
                }, jsonValue);
        Log.i(this.getClass().getSimpleName(), "jsonvalue = " + jsonValue);
        if (map != null)
            return map.get("ok").toString();
        return "";
    }
    public Map<String, Object> getImageUrl(HashMap<Object, Object> params) throws JSONException,
            IOException {
        String functionURL = URL_CONTEST_FRAD + FUNCTION_LIST.image_url.getUrl();
        String url = this.getUrl(functionURL, params);
        String jsonValue = getStringFromUrl(url);
        HashMap<String, Object> map = fromJSON(
                new TypeReference<HashMap<String, Object>>() {
                }, jsonValue);
        Log.i(this.getClass().getSimpleName(), "jsonvalue = " + jsonValue);
        if (map != null)
            return map;
        return new HashMap<>();
    }

    public void sendResultReport(HashMap<Object, Object> params) throws JSONException,
            IOException {
        String functionURL = URL_CONTEST_AD + FUNCTION_LIST.last_insert.getUrl();
        String url = this.getUrl(functionURL, params);
        System.out.println(url);
        String jsonValue = getStringFromUrl(url);
        return;
    }

    public static enum FUNCTION_LIST {
        valid_check("ad_stop.asp"), image_url("ad_check.asp"), last_insert("cont_insert.asp");
        private String url;

        private FUNCTION_LIST(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
