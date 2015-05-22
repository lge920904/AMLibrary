package com.giveangel.amlibrary.snapshotmms;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

/**
 * Created by Kyungman on 2015-05-06.
 */
public class MessageSender extends com.giveangel.sender.MessageSender {
    public MessageSender(Activity activity, String appName) {
        super(activity, appName);
    }

    @Override
    public void run() {
//        String imgPath = SnapshotManager.shootingSnapshot(getActivity());
//        setImgPath(imgPath);
//        super.run();
//        SnapshotManager.deleteSnapshot(imgPath);
        setMessageType(TYPE_SHOT_SINGLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // kitkat and jellybean
            String imgPath = SnapshotManager.shootingSnapshot(getActivity());
            setImgPath(imgPath);
            super.run();
            SnapshotManager.deleteSnapshot(imgPath);
        } else {
            // lollipop
            super.run();
            SnapshotManager.deleteSnapshot(getImgPath());
        }
    }

    public void sendMessage(String message) {
        setMessage(message);
        // 스냅샷 촬영 루틴
        // 이미지를 주지 않은 경우
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.i(getClass().getSimpleName(), "OS Version > Lollipop");
            new Thread(this).start();
        } else {
            Log.i(getClass().getSimpleName(), "OS Version = Lollipop");
            String imgPath = SnapshotManager.shootingSnapshot(getActivity());
            setImgPath(imgPath);
            new Thread(this).start();
        }
    }

    @Override
    public void sendMessage(String imgPath, String message) {
        // 이미지 경로를 넘긴경우
        // 사용할 일 없겠지만. 혹시나
        super.sendMessage(imgPath, message);
    }
}
