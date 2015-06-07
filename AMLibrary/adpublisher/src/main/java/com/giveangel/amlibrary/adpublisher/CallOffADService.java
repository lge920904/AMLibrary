package com.giveangel.amlibrary.adpublisher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CallOffADService extends Service {
    private View callOffView;
    private Button ClosingCallOffViewButton;
    private ImageView callOffADImageView;
    final static String adImageRequestURL = "";
    private String adImageResponseURL;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callOffView = new View(this);
        callOffView = View.inflate(this, R.layout.activity_call_off, null);
        ClosingCallOffViewButton = (Button) callOffView.findViewById(R.id.btn_close_activity);
        ClosingCallOffViewButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onDestroy();
                return true;
            }
        });
        callOffADImageView = (ImageView) callOffView.findViewById(R.id.img_calloff_ad);

        if(sendADImageRequestToServer()) {
            Picasso.with(this)
                    .load(adImageResponseURL)
                    .fit().centerCrop()
                    .into(callOffADImageView);
        }else{
            // 재요청

        }
        //최상위 윈도우에 넣기 위한 설정
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,//항상 최 상위. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  //포커스를 가지지 않음
                PixelFormat.TRANSLUCENT);                                                               //투명

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); //윈도 매니저
        wm.addView(callOffView, params);
        return flags;
    }



    @Override
    public void onDestroy() {
        if (callOffView.isShown()) {
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(callOffView);
        }
        super.onDestroy();
    }

    private boolean sendADImageRequestToServer() {
        // send Url Request to adImageRequestURL
        // receive url to adImageResponseURL
        adImageResponseURL = "http://image.genie.co.kr/Y/IMAGE/IMG_MUZICAT/IV2/Event/2015/5/19/ban_0_2015519144242.jpg";
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
