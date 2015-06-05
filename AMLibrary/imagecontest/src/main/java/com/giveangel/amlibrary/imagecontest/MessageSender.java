package com.giveangel.amlibrary.imagecontest;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Created by Kyungman on 2015-05-17.
 */
public class MessageSender extends com.giveangel.sender.MessageSender {
    /* Sender의 MessageSender를 오버라이드 한 클래스 */
    public MessageSender(Activity activity, String appName) {
        super(activity, appName);
    }

    private View view;

    @Override
    public void run() {
        /* 쓰레드 Run
         * Lollipop과 이전 버전의 행동이 달라야함
         * 아래 함수에 자세히 설명 */
        setMessageType(TYPE_SHOT_SINGLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // kitkat and jellybean
            String imgPath = ImageManager.getImage(view);
            setImgPath(imgPath);
            super.run();
            ImageManager.deleteImage(imgPath);
        } else {
            // lollipop
            super.run();
            ImageManager.deleteImage(getImgPath());
        }
    }

    public void sendMessage(View view, String message) {
        /* 뷰를 가져와 해당 뷰의 사진을 찍고, 해당 사진으로 메세지 전송
         * 후 사진을 다시 삭제하는 코드.
          * Lollipop 버전은 View의 사진을 찍는 코드가 UIThread에서 실행되어야만함(잠시 멈춤)
          * 이외 버전은 다른 쓰레드를 생성 후 해당 쓰레드에서 사진을 찍고, 메세지를 전송 */
        this.view = view;
        setMessage(message);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.i(getClass().getSimpleName(), "OS Version > Lollipop");
            new Thread(this).start();
        } else {
            Log.i(getClass().getSimpleName(), "OS Version = Lollipop");
            String imgPath = ImageManager.getImage(view);
            setImgPath(imgPath);
            new Thread(this).start();
        }
    }
}
