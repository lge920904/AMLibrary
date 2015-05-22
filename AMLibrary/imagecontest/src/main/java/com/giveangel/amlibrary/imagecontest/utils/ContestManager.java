package com.giveangel.amlibrary.imagecontest.utils;

import android.content.Context;

import com.giveangel.sender.AMLCostants;
import com.giveangel.sender.Helper;

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

    public boolean checkValidContestJoin(String appName) {
        return true;
    }

    public boolean checkValidContestJudge(String appName) {
        return true;
    }

}
