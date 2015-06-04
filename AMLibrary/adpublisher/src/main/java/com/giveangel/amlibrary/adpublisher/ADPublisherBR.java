package com.giveangel.amlibrary.adpublisher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

public class ADPublisherBR extends BroadcastReceiver {
    private final static String TAG = "ABPublisherBR";
    private Context bContext;
    private Intent bIntent;
    private String displayName;
    private String phoneNumber;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(this.getClass().getSimpleName(), "in Broadcast");
        bContext = context;
        bIntent = intent;
        String action = bIntent.getAction();
        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            callActionHandler.postDelayed(runRingingActivity, 1000);
        }
    }

    Handler callActionHandler = new Handler();
    Runnable runRingingActivity = new Runnable() {
        @Override
        public void run() {
            phoneNumber = bIntent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, phoneNumber);

            Uri uri;
            String[] projection;

            // If targeting Donut or below, use
            // Contacts.Phones.CONTENT_FILTER_URL and
            // Contacts.Phones.DISPLAY_NAME
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME};

            // Query the filter URI
            Cursor cursor = bContext.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst())
                    displayName = cursor.getString(0);
                cursor.close();
            }

            Log.i(TAG,"Receiver:"+displayName+" - " + phoneNumber);
            bIntent = new Intent(bContext, ADPublisherActivity.class);
            bIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            bIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bIntent.putExtra("phoneNumber", phoneNumber);
            bIntent.putExtra("displayName", displayName);

            bContext.startActivity(bIntent);
        }
    };
}
