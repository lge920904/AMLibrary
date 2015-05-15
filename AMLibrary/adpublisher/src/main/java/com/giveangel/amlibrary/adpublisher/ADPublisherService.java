package com.giveangel.amlibrary.adpublisher;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ADPublisherService extends Service {
    private final static String TAG = "ABPublisherService";
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, TAG+".onCreate()");

        Intent intent = new Intent(this, ADPublisherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT|intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
        stopSelf();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, TAG+".onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
