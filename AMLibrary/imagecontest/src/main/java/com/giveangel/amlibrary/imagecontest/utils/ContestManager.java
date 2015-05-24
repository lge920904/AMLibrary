package com.giveangel.amlibrary.imagecontest.utils;

import android.content.Context;

import com.giveangel.sender.AMLCostants;
import com.giveangel.sender.Helper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kyungman on 2015-05-14.
 */
public class ContestManager {
    private Context context;
    private ContestController controller;
    private String appName;

    public ContestManager(Context context, String appName) {
        this.context = context;
        this.appName = appName;
        this.controller = new ContestController(context);
    }

    public boolean checkValidApp() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            String returnFlag = controller.sendValidCheck(params);
            if (AMLCostants.VALUE_RETURN_VALID_TRUE.equals(returnFlag)) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getInformationURL() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            String url = controller.getInformationUrl(params);
            if ("".equals(url)) return null;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getLottoNumberList() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            return controller.getLottoNumberList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String getRecentLottoNumberList() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            return controller.getRecentLottoNumber(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "아직 발급이 되지 않았습니다. 차후에 다시 확인해주시면 감사하겠습니다.";
    }

    public ArrayList<String> getImageList() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, "test");
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(context));
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(context));
            return controller.getImageList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean checkValidContestJoin() {
        return true;
    }

    public boolean checkValidContestJudge() {
        return true;
    }
}
