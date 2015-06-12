package com.giveangel.amlibrary.adpublisher.utils;

import android.content.Context;

import com.giveangel.amlibrary.adpublisher.AMLCostants;
import com.giveangel.amlibrary.adpublisher.Helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kyungman on 2015-05-14.
 */
public class ADManager {
    /* 각 액티비티에서 서버와의 통신을 대행해주는 클래스 */
    private Context context;
    private ADController controller;
    private String appName;

    public ADManager(Context context, String appName) {
        this.context = context;
        this.appName = appName;
        this.controller = new ADController(context);
    }

    /* 앱이 유효한 앱인지(제휴된) */
    public boolean checkValidApp(String type) {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            params.put(AMLCostants.KEY_G_TYPE, type);
            if (AMLCostants.VALUE_RETURN_VALID_TRUE.equals(controller.checkValidApp(params)))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* 광고이미지 가져오기 */
    public Map<String,Object> getImageUrl() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            return controller.getImageUrl(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* AD끝나고 정보 전송 */
    public void sendResultReport(int adNumber, String clickCheck) {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            params.put(AMLCostants.KEY_AD_NUMBER, adNumber);
            params.put(AMLCostants.KEY_CLICK_CHECK, clickCheck);
            params.put(AMLCostants.KEY_LIBRARY_VER, AMLCostants.LIBRARY_VER);
            controller.sendResultReport(params);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
