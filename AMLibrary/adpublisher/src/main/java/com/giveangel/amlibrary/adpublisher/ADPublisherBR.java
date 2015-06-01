package com.giveangel.amlibrary.adpublisher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class ADPublisherBR extends BroadcastReceiver {
    private final static String TAG = "ABPublisherBR";
    private Context bContext;
    private Intent bIntent;
    private String phoneNumber;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(this.getClass().getSimpleName(), "in Broadcast");
        bContext = context;
        bIntent = intent;
        phoneNumber = getResultData();
        if (phoneNumber == null) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        setResultData(phoneNumber);
        callActionHandler.postDelayed(runRingingActivity, 1000);
//        String action = bIntent.getAction();
//        String state = bIntent.getStringExtra(TelephonyManager.EXTRA_STATE);
//
//        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//
//        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//            Log.i(TAG, "extra_state_offhook");
//            bIntent.setClass(bContext, ADPublisherService.class);
//            bContext.startService(bIntent);
//        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//            Log.i(TAG, "extra_state_idle");
//            bContext.stopService(bIntent);
//        }
    }

    Handler callActionHandler = new Handler();
    Runnable runRingingActivity = new Runnable() {
        @Override
        public void run() {

            Intent intentPhoneCall = new Intent(bContext, ADPublisherActivity.class);
            intentPhoneCall.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bContext.startActivity(intentPhoneCall);
        }
    };
}
