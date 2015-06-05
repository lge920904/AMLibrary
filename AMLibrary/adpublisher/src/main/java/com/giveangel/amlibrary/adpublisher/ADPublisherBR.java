package com.giveangel.amlibrary.adpublisher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ADPublisherBR extends BroadcastReceiver {
    private final static String TAG = "ABPublisherBR";
    private Context bContext;
    private Intent bIntent;
    private String displayName;
    private String phoneNumber;
    private String action;
    private boolean isCallOffIdle = false;
    private TelephonyManager telephony;

    /* 브로드 캐스트 생길때마다 계속 생성하고 붙이는 작업을 계속하고있었음.
     * 따라서 static 으로 변경 후 null 일때만 재생성 하도록 수정  */
    static MyPhoneStateListener phoneListener;

    private class MyPhoneStateListener extends PhoneStateListener {
        private int lastState = TelephonyManager.CALL_STATE_IDLE;
        private boolean isOutgoingCall = false;

        public void setOutgoingCall(boolean isOutgoingCall) {
            this.isOutgoingCall = isOutgoingCall;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (lastState == state) {
                /* 이전 상태와 같으면 처리할 필요 없으니 return */
                isCallOffIdle = true;
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (isCallOffIdle) {
                        /* 통화 종료하면 광고 뜨도록 */
                        isCallOffIdle = false;
                        if (isOutgoingCall)
                            callActionHandler.postDelayed(runCallOffActivity, 700);
                        setOutgoingCall(false);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                default:
                    break;
            }
            lastState = state;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        bContext = context;
        bIntent = intent;
        action = bIntent.getAction();
        Log.i(this.getClass().getSimpleName(), "in Broadcast - " + action);
        /* init Listener */
        if (phoneListener == null)
            phoneListener = new MyPhoneStateListener();

        /* 통화 걸 때 */
        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            callActionHandler.postDelayed(runRingingActivity, 500);
            phoneListener.setOutgoingCall(true);
        }
        /* set Listener - onReceive
         * 마다 계속 호출되어야함 */
        telephony = (TelephonyManager) bContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private Handler callActionHandler = new Handler();
    private Runnable runRingingActivity = new Runnable() {
        @Override
        public void run() {
            phoneNumber = bIntent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);


            /* 발신 번호로 주소록에 저장된 번호면 이름 찾아옴 */
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cursor = bContext.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst())
                    displayName = cursor.getString(0);
                cursor.close();
            }

            Log.i(TAG, "Receiver:" + displayName + " - " + phoneNumber);
            bIntent = new Intent(bContext, ADPublisherActivity.class);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            bIntent.putExtra("phoneNumber", phoneNumber);
            bIntent.putExtra("displayName", displayName);
            bContext.startActivity(bIntent);
        }
    };
    private Runnable runCallOffActivity = new Runnable() {
        @Override
        public void run() {
            bIntent = new Intent(bContext, CallOffActivity.class);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            bContext.startActivity(bIntent);
        }
    };
}
