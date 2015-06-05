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
    /* 각 액티비티에서 서버와의 통신을 대행해주는 클래스 */
    private Context context;
    private ContestController controller;
    private String appName;

    public ContestManager(Context context, String appName) {
        this.context = context;
        this.appName = appName;
        this.controller = new ContestController(context);
    }

    /* 앱이 유효한 앱인지(제휴된) */
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

    /* 상품 및 상세 정보를 안내하는 URL을 가져오는 */
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

    /* 관리자가 호출하기 위한
    * 여태 사용자가 사용하지 않은
    * 경품 정보들을 수신하는 함수 */
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

    /* 가장 최근의 요청에 대응하는
    경품 정보를 가져오는 함수 */
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

    /* 심사 이미지 URL 리스트를 가져오는 함수 */
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

    /* 공모전 응모를 할 수 있는지 검사하는 함수(현재 미사용) */
    public boolean checkValidContestJoin() {
        return true;
    }

    /* 공모전 심사를 할 수 있는지 검사하는 함수(현재 미사용) */
    public boolean checkValidContestJudge() {
        return true;
    }
}
