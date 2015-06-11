package com.giveangel.amlibrary.adpublisher;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Kyungman on 2015-05-04.
 */
public class Helper {
    /*  APNHelper를 라이브러리에서 제공하나, 제대로된 정보가 넘어오지 않음.
       전부 공란으로 넘어옴.
       통신사별로 따로 설정해줘야함
       현재 KT 4G 기기만 테스트.
       4G, 3G 전부 APN 정보가 다르기때문에,
       정보 찾아서 따로 설정해줘야함.
       3G 기기에 제공 안할지 물어볼것.
       4G기기 APN 정보만 들어가도 되는지.*/
    private static int getTelecomCode(Context context) {
        String agencyName = Helper.getAgencyName(context);
        return AMLCostants.getAgencyCode(agencyName);
    }

    public static String getAgencyName(Context context) {
        TelephonyManager systemService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String agencyName = systemService.getNetworkOperatorName();
        if (agencyName.substring(0, 2).equals("LG")) return "LG";
        return agencyName;
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager systemService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String lineNumber = systemService.getLine1Number();
        return lineNumber;
    }

}
