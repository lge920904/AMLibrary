package com.giveangel.amlibrary.adpublisher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ADPublisherBR extends BroadcastReceiver {
    private final static String TAG = "ABPublisherBR";
    private Context bContext;
    private Intent bIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        bContext = context;
        bIntent = intent;
        String action = bIntent.getAction();
        String state = bIntent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            Log.i(TAG, "extra_state_offhook");
            bIntent.setClass(bContext, ADPublisherService.class);
            bContext.startService(bIntent);
        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            Log.i(TAG, "extra_state_idle");
            bContext.stopService(bIntent);
        }
    }
}
