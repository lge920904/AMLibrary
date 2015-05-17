package com.giveangel.amlibrary.imagecontest;

import android.app.Activity;

/**
 * Created by Kyungman on 2015-05-17.
 */
public class MessageSender extends com.giveangel.sender.MessageSender {
    public MessageSender(Activity activity, String appName) {
        super(activity, appName);
    }

    @Override
    public void run() {
        String imgPath = ImageManager.getImage(getActivity());
        setImgPath(imgPath);
        setMessageType(TYPE_SHOT_SINGLE);
        super.run();
        ImageManager.deleteImage(imgPath);
    }

    public void sendMessage(String message) {
        // 스냅샷 촬영 루틴
        // 이미지를 주지 않은 경우
        setMessage(message);
        new Thread(this).start();
    }
}
