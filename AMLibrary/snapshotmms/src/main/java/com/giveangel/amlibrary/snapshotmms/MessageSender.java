package com.giveangel.amlibrary.snapshotmms;

import android.app.Activity;

/**
 * Created by Kyungman on 2015-05-06.
 */
public class MessageSender extends com.giveangel.sender.MessageSender {
    public MessageSender(Activity activity, String appName) {
        super(activity, appName);
    }

    @Override
    public void run() {
        String imgPath = SnapshotManager.shootingSnapshot(getActivity());
        setImgPath(imgPath);
        setMessageType(TYPE_SHOT_SINGLE);
        super.run();
        SnapshotManager.deleteSnapshot(imgPath);
    }

    public void sendMessage(String message) {
        // 스냅샷 촬영 루틴
        // 이미지를 주지 않은 경우
        setMessage(message);
        new Thread(this).start();
    }

    @Override
    public void sendMessage(String imgPath, String message) {
        // 이미지 경로를 넘긴경우
        // 사용할 일 없겠지만. 혹시나
        super.sendMessage(imgPath, message);
    }
}
