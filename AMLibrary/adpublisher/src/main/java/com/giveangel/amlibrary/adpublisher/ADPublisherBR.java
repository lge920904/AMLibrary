package com.giveangel.amlibrary.adpublisher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.giveangel.amlibrary.adpublisher.telephony.ITelephony;

import java.lang.reflect.Method;

public class ADPublisherBR extends BroadcastReceiver {
    private final static String TAG = "ABPublisherBR";

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG,
                            "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_IDLE "
                                    + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG,
                            "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_OFFHOOK "
                                    + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG,
                            "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_RINGING "
                                    + incomingNumber);
                    break;
                default:
                    Log.i(TAG,
                            "MyPhoneStateListener->onCallStateChanged() -> default -> "
                                    + Integer.toString(state));
                    break;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        MyPhoneStateListener phoneListener = new MyPhoneStateListener();
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

//        Log.i(TAG, "ADPublisherBR.onReceive()");
//        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//            Log.i(TAG, "ACTION_NEW_OUTGONING_CALL -> " + bundle.getString(Intent.EXTRA_PHONE_NUMBER));
//
        intent = new Intent(context, ADPublisherService.class);
        intent.putExtra("phoneNumber", bundle.getString(Intent.EXTRA_PHONE_NUMBER));
        context.startService(intent);
//        }

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Log.d(TAG, "Get getTeleService...");
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
        }
    }
}

