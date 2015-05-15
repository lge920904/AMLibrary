package com.giveangel.sender;

/**
 * Created by Kyungman on 2015-05-05.
 */
public class AMLCostants {
    public static final int CODE_DEFAULT = -9999;

    public static final int CODE_KT_4G = 111;
    public static final int CODE_SKT = 222;
    public static final int CODE_LGU = 333;
    public static final String GIVEANGEL_REPRESENT_NUMBER = "";

    // PARAMS
    public static final String KEY_CALLINGNUM = "callingnum";
    public static final String KEY_APP_NAME = "app";
    public static final String KEY_AGENCY_NAME = "agency";
    public static final String KEY_MMS_COUNT = "mms_count";

    public static final String KEY_RETURN_VALID_OK = "ok";
    public static final String VALUE_RETURN_VALID_TRUE = "y";
    public static final String VALUE_RETURN_VALID_FALSE = "n";

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
        KT_4G("olleh", CODE_KT_4G), KT("KT", CODE_KT_4G), SKT_4G("SKTelecom", CODE_SKT);
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
