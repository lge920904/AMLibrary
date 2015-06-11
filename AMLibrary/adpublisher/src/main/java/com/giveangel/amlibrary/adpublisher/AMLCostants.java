package com.giveangel.amlibrary.adpublisher;

/**
 * Created by Kyungman on 2015-05-05.
 */
public class AMLCostants {
    public static final int CODE_DEFAULT = -9999;

    public static final int CODE_KT_4G = 111;
    public static final int CODE_SKT = 222;
    public static final int CODE_LGU = 333;

    // PARAMS
    public static final String KEY_CALLINGNUM = "callingnum";
    public static final String KEY_APP_NAME = "app";
    public static final String KEY_AGENCY_NAME = "agency";
    public static final String KEY_G_TYPE = "g_type";
    public static final String KEY_AD_NUMBER = "ad_number";
    public static final String KEY_CLICK_CHECK = "click_check";
    public static final String KEY_LIBRARY_VER = "ver";

    public static final String VALUE_CLICK_CHECK_TRUE = "y";
    public static final String VALUE_CLICK_CHECK_FALSE = "n";
    public static final String LIBRARY_VER = "1";

    public static final String KEY_RETURN_VALID_OK = "ok";
    public static final String VALUE_RETURN_VALID_TRUE = "y";
    public static final String VALUE_RETURN_VALID_FALSE = "n";

    public static final String KEY_RETURN_TARGET_NUMBER = "number";
    public static final String VALUE_G_HEAD = "f";
    public static final String VALUE_G_TAIL = "r";

    public static int getAgencyCode(String agencyName) {
        for (AGENCY agency : AGENCY.values()) {
            if (agency.getName().equals(agencyName)) {
                return agency.getAgencyCode();
            }
        }
        return CODE_DEFAULT;
    }

    // AGENCIES
    public static enum AGENCY {
        KT_4G("olleh", CODE_KT_4G), KT("KT", CODE_KT_4G),
        SKT_4G("SKTelecom", CODE_SKT), LG_4G("LG U+", CODE_LGU), LG("LG", CODE_LGU);
        private String name;
        private int agencyCode;

        private AGENCY(String name, int agencyCode) {
            this.name = name;
            this.agencyCode = agencyCode;
        }

        public String getName() {
            return name;
        }

        public int getAgencyCode() {
            return agencyCode;
        }
    }
}
