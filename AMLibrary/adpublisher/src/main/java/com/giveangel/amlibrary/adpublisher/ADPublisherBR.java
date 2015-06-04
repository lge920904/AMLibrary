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
    private MyPhoneStateListener phoneListener;
    private TelephonyManager telephony;

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (isCallOffIdle) {
                        /* 통화 종료하면 광고 뜨도록 */
                        isCallOffIdle = false;
                        callActionHandler.postDelayed(runCallOffActivity, 500);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (!action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                        isCallOffIdle = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(this.getClass().getSimpleName(), "in Broadcast");
        bContext = context;
        bIntent = intent;
        action = bIntent.getAction();

        /* 통화 걸 때 */
        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            callActionHandler.postDelayed(runRingingActivity, 500);
        }
        /* 통화 종료했을 때 */
        phoneListener = new MyPhoneStateListener();
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
