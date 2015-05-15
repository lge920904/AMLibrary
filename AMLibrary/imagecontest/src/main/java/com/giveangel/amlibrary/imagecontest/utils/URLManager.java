package com.giveangel.amlibrary.imagecontest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kyungman on 2015-05-07.
 */
class URLManager {

    public static String getUrl(String rootURL, HashMap<Object, Object> params) {
        // TODO Auto-generated method stub
        String createdURL = rootURL + "?";
        boolean flag = false;
        for (Map.Entry<Object, Object> entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (key != null || value != null) {
                if (entry.getValue() instanceof ArrayList<?>) {
                    ArrayList<Object> list = ((ArrayList<Object>) entry.getValue());
                    for (Object urlValue : list) {
                        createdURL = createdURL + key + "=" + urlValue + "&";
                    }
                } else {
                    createdURL = createdURL + key + "=" + value + "&";
                }
                flag = true;
            } else {
                createdURL = "";
                break;
            }
        }
        if (flag)
            createdURL = removeLastChar(createdURL);
        return createdURL;
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
