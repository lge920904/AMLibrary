package com.giveangel.amlibrary.adpublisher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class CallOffActivity extends Activity {
    private ImageView callOffADImage;
    private Button closeActivity;
    private String adImageUrl = null; // 화면에 보여질 사진주소
    private String adRequestUrl = null; // 사진을 누르면 이동할 주소

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_close_activity) {
                /* 현재 액티비티 종료 */
                closeActivity();
            } else if (id == R.id.img_calloff_ad) {
                /*광고 이미지 클릭 */
                showAD();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_off);

        callOffADImage = (ImageView) findViewById(R.id.img_calloff_ad);
        callOffADImage.setOnClickListener(clickListener);
        if (sendADImageRequestToServer()) {
            Picasso.with(this)
                    .load(adImageUrl)
                    .fit().centerCrop()
                    .into(callOffADImage);
        } else {
            // 서버로부터 이미지를 가져오지 못했을 때. (이미지가 null)
        }

        closeActivity = (Button) findViewById(R.id.btn_close_activity);
        closeActivity.setOnClickListener(clickListener);
    }

    private void showAD() {

    }

    private void closeActivity() {
        /* 화면닫기 */
        this.finish();
    }

    private boolean sendADImageRequestToServer() {
        // request image to AD_IMAGE_REQUEST_URL;
        adImageUrl = "http://image.genie.co.kr/Y/IMAGE/IMG_MUZICAT/IV2/Event/2015/5/19/ban_0_2015519144242.jpg";
        adRequestUrl = "http://www.naver.com";
        return true;
    }
}
