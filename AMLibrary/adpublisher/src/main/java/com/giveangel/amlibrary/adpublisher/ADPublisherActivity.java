package com.giveangel.amlibrary.adpublisher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.internal.telephony.ITelephony;
import com.giveangel.amlibrary.adpublisher.utils.ADManager;
import com.giveangel.amlibrary.adpublisher.utils.NetworkManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;
import java.util.Map;

public class ADPublisherActivity extends Activity {
    private TextView receiverName;
    private TextView receiverNumber;
    private ImageView adImage;
    private ToggleButton speakerMode;
    private Button closeActivity;
    private Button callOff;

    private String adImageUrl = null; // 화면에 보여질 사진주소
    private String adRequestUrl = null; // 사진을 누르면 이동할 주소

    private final static String AD_IMAGE_REQUEST_URL = "";
    private final static String TAG = "ADPublisherActivity";
    private TelephonyManager telephony;
    private AudioManager audioManager;

    private boolean MODE_SPEAKER;

    private ADManager manager;

    private final BroadcastReceiver finishActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(getClass().getSimpleName(), "in action - " + intent.getAction());
            closeActivity();
        }
    };

    /**
     * 버튼 이벤트 리스너
     */

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_speaker_mode) {
                /* 통화 모드 변경 */
                changeCallMode();
            } else if (id == R.id.btn_call_off) {
                /* 통화 종료 */
                callOff();
            } else if (id == R.id.btn_close_activity) {
                /* 현재 액티비티 종료 */
                closeActivity();
            } else if (id == R.id.img_ad) {
                /*광고 이미지 클릭 */
                showAD();
            }
        }

        private void showAD() {
            // adRequestUrl 띄움.!
        }
    };

    private void changeCallMode() {
        MODE_SPEAKER = !MODE_SPEAKER;
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        Log.e(TAG, "speakerPhone:" + audioManager.isSpeakerphoneOn() + " mode: " + audioManager.getMode());
        audioManager.setSpeakerphoneOn(MODE_SPEAKER);
        Log.e(TAG, "speakerPhone:" + audioManager.isSpeakerphoneOn() + " mode: " + audioManager.getMode());
    }

    private void callOff() {
        if (telephony.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
            try {
                Class c = Class.forName(telephony.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                ITelephony telephonyService = (ITelephony) m.invoke(telephony);
                telephonyService.endCall();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
            }
        }
        closeActivity();
    }

    private void closeActivity() {
        /* 화면닫기 */
        Log.i(getClass().getSimpleName(), "close activity");
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        registerReceiver(finishActionReceiver, new IntentFilter("FINISH_ACTIVITY"));
        if(! NetworkManager.checkNetwork(getApplicationContext())) closeActivity();
        else {
            manager = new ADManager(this,
                    getApplicationInfo().loadLabel(getPackageManager()).toString());
            CheckValidTask task = new CheckValidTask();
            GetImageUrl getImageTask = new GetImageUrl();
            try {
                task.execute();

                setContentView(R.layout.activity_adpublisher);
                String phoneNumber = getIntent().getStringExtra("phoneNumber");
                String displayName = getIntent().getStringExtra("displayName");


                telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

                receiverName = (TextView) findViewById(R.id.text_receiver_name);
                receiverName.setText(displayName);
                receiverNumber = (TextView) findViewById(R.id.text_receiver_number);
                receiverNumber.setText(phoneNumber);
                adImage = (ImageView) findViewById(R.id.img_ad);
                MODE_SPEAKER = false;

                speakerMode = (ToggleButton) findViewById(R.id.btn_speaker_mode);
                callOff = (Button) findViewById(R.id.btn_call_off);
                closeActivity = (Button) findViewById(R.id.btn_close_activity);

                adImage.setOnClickListener(clickListener);
                speakerMode.setOnClickListener(clickListener);
                callOff.setOnClickListener(clickListener);
                closeActivity.setOnClickListener(clickListener);

                getImageTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 유효성 체크 ASYNCTASK */
    class CheckValidTask extends AsyncTask<Void, Void, Boolean> {
        private String CODE = "f";

        @Override
        protected Boolean doInBackground(Void... voids) {
            return manager.checkValidApp(CODE);
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            if (flag) return;
            else finish();
        }
    }

    /* 유효성 체크 ASYNCTASK */
    class GetImageUrl extends AsyncTask<Void, Void, Map<String, Object>> {
        private String CODE = "f";

        @Override
        protected Map<String, Object> doInBackground(Void... voids) {
            return manager.getImageUrl();
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            Picasso.with(ADPublisherActivity.this)
                    .load((String) map.get(CODE + "_img"))
                    .fit().centerInside()
                    .into(adImage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(finishActionReceiver);
    }
}
