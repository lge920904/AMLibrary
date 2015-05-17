package com.giveangel.sender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.mms.APN;
import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

/**
 * Created by Kyungman on 2015-05-05.
 */
class Sender {
    private static final String SEPARATOR_INIT = "_#";
    private static final String SEPARATOR_CONTENT = "_*";
    // Context
    private Context context;
    // members
    private String imgPath;
    private String message;
    private String phoneNumber;

    // variables
    private Bitmap sendImg;

    public Sender(Context context, String imgPath, String message) {
        this.context = context;
        this.imgPath = imgPath;
        this.message = message;
        this.init();
    }

    // init
    private void init() {
        TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.phoneNumber = systemService.getLine1Number();
        if (imgPath.equals("") || imgPath.equals("path"))
            sendImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
        else
            sendImg = BitmapFactory.decodeFile(imgPath);
    }

    public void init(String imgPath, String message) {
        this.imgPath = imgPath;
        this.message = message;
        this.init();
    }

    public void init(String message) {
        this.message = message;
        this.init();
    }


    // get APN infomation
    private APN getAPNInfo() {
        return Helper.getAPNInfo(context);
    }

    private Settings getSetting() {
        // 브로드캐스트리시버 안받아지는 문제.
        // setDeliveryReports 일단 false
        APN apn = this.getAPNInfo();
        Settings settings = new Settings();
        settings.setMmsc(apn.MMSCenterUrl);
        if ((!apn.MMSProxy.equals(""))) {
            settings.setProxy(apn.MMSProxy);
        }
        settings.setPort(apn.MMSPort);
        settings.setDeliveryReports(false);
        return settings;
    }

    private Message generateMessage(String separator) {
        // generate message
        Message sendMessage = new Message(message + separator, phoneNumber);
        sendMessage.setImage(sendImg);   // not necessary for voice or sms messages
        sendMessage.setType(Message.TYPE_SMSMMS);  // could also be Message.TYPE_VOICE
        return sendMessage;
    }

    private boolean checkValidDevice() {
        // 현재 한국 번호가 아니면, 하지 말라는 조건 존재.
        // 나중에 다른 제약조건 늘어날시 여기에 추가
        if (phoneNumber != null & (!"".equals(phoneNumber))) {
            String code = phoneNumber.substring(0, 3);
            Log.i("phoneNumber", "code = " + code);
            if (code.equals("+82")) {
                return true;
            }
        }
        return false;
    }

    public void run() {

        if (!checkValidDevice()) return;
        Settings setting = this.getSetting();
        Message initMsg = this.generateMessage(SEPARATOR_INIT);
        Message msg = this.generateMessage(SEPARATOR_CONTENT);
        Transaction sendTransaction = new Transaction(context, setting);
        sendTransaction.sendNewMessage(initMsg, Transaction.NO_THREAD_ID);
        sendTransaction.sendNewMessage(msg, Transaction.NO_THREAD_ID);
        Log.i(this.getClass().getSimpleName(), "end");
    }
}
