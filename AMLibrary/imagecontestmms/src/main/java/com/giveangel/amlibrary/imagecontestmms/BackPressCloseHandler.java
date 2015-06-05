package com.giveangel.amlibrary.imagecontestmms;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Kyungman on 2015-05-24.
 */
class BackPressCloseHandler {
    /* Back 버튼 두번 연속 누를시 이동할 수 있게 해주는 코드 */
    private static final String CONTEST_MSG_CONFIRM_EXIT = "심사를 완료하시면 약 1천만원의 경품의 \n" +
            "응모가 가능한 경품권을 받을 수 있습니다. \n정말 닫으시려면 닫기버튼을 한번 더 눌러주세요. ";
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        /* 이전 back버튼을 누른지
        2초가 넘었다면 안내 토스트 출력.
        2초가 안됬다면 finish */
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                CONTEST_MSG_CONFIRM_EXIT, Toast.LENGTH_SHORT);
        toast.show();
    }
}
