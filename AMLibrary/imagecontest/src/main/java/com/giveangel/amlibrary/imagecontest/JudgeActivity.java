package com.giveangel.amlibrary.imagecontest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.giveangel.sender.AMLCostants;


public class JudgeActivity extends ActionBarActivity implements View.OnClickListener {
    private Button judgeButton;
    private String appName;

    private static final String CONTEST_MSG_CONFIRM_EXIT = "심사를 완료하시면 약 1천만원의 경품의 \n" +
            "응모가 가능한 경품권을 받을 수 있습니다. \n정말 닫으시겠습니까? ";
    private static final String BUTTON_EXIT = "닫기";
    private static final String BUTTON_CANCEL = "취소";
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge);
        backPressCloseHandler = new BackPressCloseHandler(this);
        appName = getIntent().getExtras().getString(AMLCostants.KEY_APP_NAME);
        Log.i(appName, appName);
        judgeButton = (Button) findViewById(R.id.doJudgeButton);
        judgeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == judgeButton.getId()) {
            Intent intent = new Intent(this, GalleryJudgeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AMLCostants.KEY_APP_NAME, appName);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contest_information_exit) {
            lottoNumberAlertDialog().show();
        } else if (id == R.id.contestinformation_reload) {
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }


    /* 액티비티를 종료하려고 할때 경품 안내를 해주는 다이얼로그 생성 */
    private AlertDialog lottoNumberAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(CONTEST_MSG_CONFIRM_EXIT)
                .setCancelable(false).setPositiveButton(BUTTON_EXIT,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        finish();
                    }
                }).setNegativeButton(BUTTON_CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 'NO'
            }
        });
        AlertDialog alert = dialogBuilder.create();
        return alert;
    }

}
