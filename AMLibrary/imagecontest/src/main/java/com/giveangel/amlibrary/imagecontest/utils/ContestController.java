package com.giveangel.amlibrary.imagecontest.utils;

import android.content.Context;

/**
 * Created by Kyungman on 2015-05-15.
 */
public class ContestController extends Controller {

    public ContestController(Context context) {
        super(context);
    }

    public static enum FUNCTION_LIST {
        valid_check("mms_stop.asp"), inform_sentmms("cont.asp");
        private String url;

        private FUNCTION_LIST(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
