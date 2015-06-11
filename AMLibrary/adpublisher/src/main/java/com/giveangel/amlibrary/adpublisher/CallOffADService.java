package com.giveangel.amlibrary.adpublisher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.giveangel.amlibrary.adpublisher.utils.ADManager;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CallOffADService extends Service {

    private long TIMER_CLOCK = 6000;
    private View callOffView;
    private Button ClosingCallOffViewButton;
    private ImageView callOffADImageView;
    final static String adImageRequestURL = "";
    private String adImageResponseURL;
    private String CODE = "r";

    private String clickType;
    private String clickUrl;
    private String adNumber;
    private String imgUrl;
    private String clickCheck;
    private String mmsNumber;

    private ADManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        manager = new ADManager(CallOffADService.this,
                getApplicationInfo().loadLabel(getPackageManager()).toString());
        callOffView = new View(this);
        callOffView = View.inflate(this, R.layout.activity_call_off, null);
        callOffADImageView = (ImageView) callOffView.findViewById(R.id.img_calloff_ad);
        ClosingCallOffViewButton = (Button) callOffView.findViewById(R.id.btn_close_activity);
        Log.i(getClass().getSimpleName(), "in service1");

        clickCheck = "n";

        SetImageTask task = new SetImageTask();
        task.execute();

        ClosingCallOffViewButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onDestroy();
                return true;
            }
        });

        callOffADImageView.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                clickCheck = "y";
                Log.v(getClass().getSimpleName(), " click");
                if ("www".equals(clickType)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if ("mms".equals(clickType)) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    String smsBody = "";
                    sendIntent.putExtra("sms_body", smsBody); // 보낼 문자
                    sendIntent.putExtra("address", mmsNumber); // 받는사람 번호
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sendIntent);
                }
            }
        });
        return flags;
    }

    private void setImage() {
        Picasso.with(this)
                .load(imgUrl)
                .fit().centerInside()
                .into(callOffADImageView);
    }

    @Override
    public void onDestroy() {
        if (callOffView.isShown()) {
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(callOffView);
        }
        SendResultReportTask task = new SendResultReportTask();
        task.execute();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class SetImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            boolean flag = manager.checkValidApp(CODE);
            if (!flag) stopSelf();

            Map<String, Object> map = manager.getImageUrl();
            if (map != null) {
                clickType = (map.get("click_type") != null) ? (String) map.get("click_type") : "";
                clickUrl = (map.get("click_url") != null) ? (String) map.get("click_url") : "";
                adNumber = (map.get("ad_number") != null) ? (String) map.get("ad_number") : "";
                imgUrl = (map.get(CODE + "_img") != null) ? (String) map.get(CODE + "_img") :
                        "http://image.genie.co.kr/Y/IMAGE/IMG_MUZICAT/IV2/Event/2015/5/19/ban_0_2015519144242.jpg";
                mmsNumber = (map.get("mms_number1") != null) ? (String) map.get("mms_number1") : "";
            } else {
                clickType = "";
                clickUrl = "";
                adNumber = "";
                imgUrl = "http://image.genie.co.kr/Y/IMAGE/IMG_MUZICAT/IV2/Event/2015/5/19/ban_0_2015519144242.jpg";
                mmsNumber = "";
            }
            Log.i("test", clickType);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CallOffADService.this.setImage();
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,//항상 최 상위. 터치 이벤트 받을 수 있음.
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  //포커스를 가지지 않음
                    PixelFormat.TRANSLUCENT);                                                               //투명

            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); //윈도 매니저
            wm.addView(callOffView, params);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    stopSelf();
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, TIMER_CLOCK);
        }
    }

    private class SendResultReportTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            manager.sendResultReport((adNumber.equals("")) ? 0 : Integer.parseInt(adNumber), clickCheck);
            return null;
        }
    }
}
