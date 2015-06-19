package com.giveangel.sender;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.mms.APN;
import com.klinker.android.send_message.ApnUtils;
import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;


/**
 * Created by Kyungman on 2015-05-05.
 */
class Sender {
    private static final String SEPARATOR_INIT = "#:";
    private static final String SEPARATOR_CONTENT = "*:";
    //        private static final int LIBRARY_VERSION = 2; // 1 - send to me 2 - send to target
    private static final int LIBRARY_VERSION = 1; // 1 - send to me 2 - send to target

    private com.giveangel.sender.Settings defaultSetting;
    // Context
    private Activity context;
    // members
    private String imgPath;
    private String message;
    private String phoneNumber;
    private String targetNumber;

    // variables
    private Bitmap sendImg;
    private MessageSender.SentMessageCallback callback;

    public void setMessageCallback(MessageSender.SentMessageCallback callback) {
        this.callback = callback;
    }

    public Sender(Activity context, String imgPath, String message) {
        this.context = context;
        this.imgPath = imgPath;
        this.message = message;
        this.initApn();
        this.init();
    }

    public static int getBitmapScaleSize(String fileName) {
        /* 이미지를 리사이징 하기 위한 최적 Scale값 도출 */
        try {
            int MAX_IMAGE_SIZE = 768;
            int scale = 0;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            if (options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_IMAGE_SIZE /
                        (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
            }
            return scale;
        } catch (Exception e) {
            return 0;
        }
    }

    public Bitmap getResizedBitmap(String imagePath) {
        /* MMS 전송 전 이미지를 리사이징 */
        int scaleSize = getBitmapScaleSize(imagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = scaleSize;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        Log.i(getClass().getSimpleName(), "Width = " + bitmap.getWidth() + " Height = " + bitmap.getHeight() + " scale = " + scaleSize);
        return (bitmap);
    }

    private void init() {
        /* 내 핸드폰 번호 및 보내야 하는 이미지 초기화 */
        this.phoneNumber = Helper.getPhoneNumber(context);
        try {
            if (imgPath.equals("") || imgPath.equals("path"))
                sendImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
            else {
                //sendImg = BitmapFactory.decodeFile(imgPath);
                sendImg = getResizedBitmap(imgPath);
            }
        } catch (Exception e) {
            sendImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
        }
    }

    private void initApn() {
        /* Sender가 생성됬을떄, APN 정보를 가져오는 코드 */
        ApnUtils.initDefaultApns(context, new ApnUtils.OnApnFinishedListener() {
            @Override
            public void onFinished() {
                defaultSetting = com.giveangel.sender.Settings.get(Sender.this.context, true);
                Log.i(Sender.class.getSimpleName(), "APN default setting end");
            }
        });
    }

    public void init(String imgPath, String message) {
        /* 기본 초기화 */
        this.imgPath = imgPath;
        this.message = message;
        this.init();
    }

    public void init(String message) {
        /* 전송 중 디폴트 이미지를 보내야 하는 경우 초기화 */
        this.message = message;
        this.init();
    }

    public void initTargetNumber(String targetNumber) {
        /* 데모용 앱의 경우 자기 자신에게 보내도록,
         아닌 경우 서버와 통신을 통해 메세지를 전송하도록 설정 */
        if (LIBRARY_VERSION == 1) {
            this.targetNumber = Helper.getPhoneNumber(context);
        } else if (LIBRARY_VERSION == 2) {
            this.targetNumber = targetNumber;
        }
    }

    // get APN infomation
    private APN getAPNInfo() {
        /* 기본 APN정보를 인터넷에서 수신 혹은 Helper에 저장된값 호출 */
        if (defaultSetting == null)
            return Helper.getAPNInfo(context);
        APN apn = new APN();
        apn.MMSCenterUrl = defaultSetting.getMmsc();
        apn.MMSPort = defaultSetting.getMmsPort();
        apn.MMSProxy = defaultSetting.getMmsProxy();
        return apn;
    }

    private Settings getSetting() {
        /* 메세지를 전송하기전 전송지 설정 */
        APN apn = this.getAPNInfo();
        Log.i(Sender.class.getSimpleName(), "apn = " + apn.MMSCenterUrl);
        Settings settings = new Settings();
        settings.setMmsc(apn.MMSCenterUrl);
        if ((!apn.MMSProxy.equals(""))) {
            settings.setProxy(apn.MMSProxy);
        }
        settings.setPort(apn.MMSPort);
        settings.setDeliveryReports(true);
        return settings;
    }

    private Message generateMessage(String separator) {
        /* 전송할 메세지를 만드는 함수 */
        Message sendMessage = new Message(separator + message, targetNumber);
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
            if (code.equals("+82") || code.equals("010")) {
                return true;
            }
        }
        return false;
    }

    public void run() {
        /* 실제 메세지 전송 부분 */
        if (!checkValidDevice()) return;
        Settings setting = this.getSetting();
        Transaction sendTransaction = new Transaction(context, setting);
        Message initMsg = this.generateMessage(SEPARATOR_INIT);
        Message msg = this.generateMessage(SEPARATOR_CONTENT);

        sendTransaction.sendNewMessage(initMsg, Transaction.NO_THREAD_ID);
        if (callback != null)
            sendTransaction.sendNewMessage(msg, Transaction.NO_THREAD_ID, callback);
        else
            sendTransaction.sendNewMessage(msg, Transaction.NO_THREAD_ID);

        Log.i(getClass().getSimpleName(), "SENDMSG ADD = "
                + targetNumber + " MMSC = " + setting.getMmsc() + " MMSP = "
                + setting.getProxy() + " MMSPort = " + setting.getPort());
        Log.i(this.getClass().getSimpleName(), "end");
    }
}
