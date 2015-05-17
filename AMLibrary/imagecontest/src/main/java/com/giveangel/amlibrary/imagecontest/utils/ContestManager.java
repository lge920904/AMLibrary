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

    public ContestManager(Context context) {
        this.context = context;
        this.controller = new ContestController(context);
    }
    public boolean checkValidApp(String appName) {
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

    public boolean checkValidContestJoin(String appName) {
        return true;
    }

    public boolean checkValidContestJudge(String appName) {
        return true;
    }

}
