package com.giveangel.amlibrary.imagecontest;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Created by Kyungman on 2015-05-17.
 */
public class MessageSender extends com.giveangel.sender.MessageSender {
    public MessageSender(Activity activity, String appName) {
        super(activity, appName);
    }

    private View view;

    @Override
    public void run() {
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
        // 이미지 뷰를 줌
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
