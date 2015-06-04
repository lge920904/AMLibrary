package com.giveangel.amlibrary.adpublisher;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;

public class ADPublisherActivity extends Activity {
    private TextView receiverName;
    private TextView receiverNumber;
    private ImageView adImage;
    private ToggleButton speakerMode;
    private Button closeActivity;
    private Button callOff;

    private final static String AD_IMAGE_REQUEST_URL = "";
    private final static String TAG = "ADPublisherActivity";
    private TelephonyManager telephony;
    private AudioManager audioManager;

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
            }
        }
    };

    public void changeCallMode() {

/*
      스피커폰 변환이 안됌. 자살각.
  audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        Log.e(TAG, "speakerPhone:" + audioManager.isSpeakerphoneOn());
        audioManager.setSpeakerphoneOn(true);
        Log.e(TAG, "speakerPhone:" + audioManager.isSpeakerphoneOn());*/



    }

    public void callOff() {
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

    public void closeActivity() {
        /* 화면닫기 */
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adpublisher);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String displayName = getIntent().getStringExtra("displayName");

        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        receiverName = (TextView) findViewById(R.id.text_receiver_name);
        receiverName.setText(displayName);
        receiverNumber = (TextView) findViewById(R.id.text_receiver_number);
        receiverNumber.setText(phoneNumber);
        adImage = (ImageView) findViewById(R.id.img_ad);

        Picasso.with(this)
                .load(getADImageFromServer())
                .fit().centerCrop()
                .into(adImage);
        speakerMode = (ToggleButton) findViewById(R.id.btn_speaker_mode);
        callOff = (Button) findViewById(R.id.btn_call_off);
        closeActivity = (Button) findViewById(R.id.btn_close_activity);

        adImage.setOnClickListener(clickListener);
        speakerMode.setOnClickListener(clickListener);
        callOff.setOnClickListener(clickListener);
        closeActivity.setOnClickListener(clickListener);
    }

    private String getADImageFromServer() {
        String adImageUrl = null;
        // request image to AD_IMAGE_REQUEST_URL;
        adImageUrl = "http://image.genie.co.kr/Y/IMAGE/IMG_MUZICAT/IV2/Event/2015/5/19/ban_0_2015519144242.jpg";
        return adImageUrl;
    }

}
