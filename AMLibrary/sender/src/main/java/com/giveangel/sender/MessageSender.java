package com.giveangel.sender;

import android.app.Activity;

import com.giveangel.sender.utils.MMSController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyungman on 2015-05-05.
 */
public class MessageSender implements Runnable {
    // Constants
    protected static final int TYPE_SHOT_INIT = 0;
    protected static final int TYPE_SHOT_SINGLE = 1;
    protected static final int TYPE_SHOT_MULTIPLE = 2;
    // members
    private Activity activity;
    private String appName;
    // variables
    private MMSController controller;
    private int messageType; // 0 - 초기화 전 1 - 단발성 2 - 다발성
    private int sentMessageCount;
    private String imgPath;
    private String message;
    private String targetNumber;
    private List<String> imgPaths;
    private List<String> messages;

    public MessageSender(Activity activity, String appName) {
        this.messageType = TYPE_SHOT_INIT;
        this.activity = activity;
        this.appName = appName;
        this.controller = new MMSController(activity);
    }

    public void sendMessage(String imgPath, String message) {
        this.messageType = TYPE_SHOT_SINGLE;
        this.imgPath = imgPath;
        this.message = message;
        new Thread(this).start();
    }

    public void sendMessage(List<String> imgPaths, List<String> messages) {
        // 여러개 보낼 시 thread 런한번에 처리하도록.
        // 센더도 여러개 보낼떄 대비해 오버로드시킬것.
        // thread
        if (imgPaths.size() != messages.size()) return;
        this.messageType = TYPE_SHOT_MULTIPLE;
        this.imgPaths = imgPaths;
        this.messages = messages;
        new Thread(this).start();
    }

    @Override
    public void run() {
        // 여러번 보내야할 경우 반복문을 통해, run 여러번 호출하도록.
        // 반복 후 count 값을 통해 컨트롤러에 발송내역 보내도록 호출.
        if (messageType == TYPE_SHOT_INIT) return; // 비정상 경로 접근
        if (this.checkAppValidation()) {
            // 전송
            this.setTargetNumber();
            if (messageType == TYPE_SHOT_SINGLE) {
                Sender sender = new Sender(activity, imgPath, message);
                sender.initTargetNumber(targetNumber);
                sender.run();
                this.sentMessageCount = 1;
            } else if (messageType == TYPE_SHOT_MULTIPLE) {
                Sender sender = new Sender(activity, "", "");
                for (int i = 0; i < messages.size(); i++) {
                    sender.init(imgPaths.get(i), messages.get(i));
                    sender.initTargetNumber(targetNumber);
                    sender.run();
                }
                this.sentMessageCount = imgPaths.size();
            }
            // 발송결과 전송
            this.informSentMMS();
        }
    }

    private void setTargetNumber() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(activity));
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(activity));
            targetNumber = controller.getTargetNumber(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void informSentMMS() {
        try {
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(activity));
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_MMS_COUNT, sentMessageCount);
            controller.informSentMMS(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAppValidation() {
        // network 연결하고, boolean 리턴 받아서 처리
        try {
            String returnFlag;
            HashMap<Object, Object> params = new HashMap<>();
            params.put(AMLCostants.KEY_APP_NAME, appName);
            params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(activity));
            params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(activity));
            returnFlag = controller.sendValidCheck(params);
            if (AMLCostants.VALUE_RETURN_VALID_TRUE.equals(returnFlag)) return true;
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
