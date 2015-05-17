package com.giveangel.amlibrary.imagecontest.utils;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.giveangel.sender.utils.MMSController;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kyungman on 2015-05-15.
 */
public class ContestController extends Controller {
    private static final String URL_CONTEST = URL_ROOT;

    public ContestController(Context context) {
        super(context);
    }
    public String sendValidCheck(HashMap<Object, Object> params)
            throws JSONException,
            IOException {
        // TODO Auto-generated method stub
        MMSController controller = new MMSController(getContext());
        return controller.sendValidCheck(params);
    }

    public ArrayList<String> getImageList(HashMap<Object, Object> params)
            throws JSONException,
            IOException {
        // TODO Auto-generated method stub
        Log.i(this.getClass().getSimpleName(), "in getImageList");
        String functionURL = URL_CONTEST + FUNCTION_LIST.vote_img.getUrl();
        String url = this.getUrl(functionURL, params);
        System.out.println(url);
        String jsonValue = getStringFromUrl(url);
        System.out.println("jsonvalue = " + jsonValue);
        HashMap<Object, Object> map = fromJSON(
                new TypeReference<HashMap<Object, Object>>() {
                }, jsonValue);
        Log.i(this.getClass().getSimpleName(), "value = " + map.get("pic0"));
        return convertMapToListForImageList(map);
    }

    private ArrayList<String> convertMapToListForImageList(Map<Object, Object> params) {
        ArrayList<String> imgUrlList = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : params.entrySet()) {
            String value = entry.getValue().toString();
            if (value != null) imgUrlList.add(value);
        }
        return imgUrlList;
    }

    public static enum FUNCTION_LIST {
        vote_img("vote_img.asp"), valid_check("mms_stop.asp");
        private String url;

        private FUNCTION_LIST(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
