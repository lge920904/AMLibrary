package com.giveangel.amlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.giveangel.amlibrary.imagecontest.InformationActivity;
import com.giveangel.amlibrary.snapshotmms.MessageSender;
import com.giveangel.amlibrary.snapshotmms.TranparentActivity;
import com.giveangel.sender.AMLCostants;

import java.util.Timer;
import java.util.TimerTask;


public class KyungmanMainActivity extends Activity {
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("로그캣", "create logcat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyungman_main);
        Button mmsButton = (Button) findViewById(R.id.mms);
        Button contestButton = (Button) findViewById(R.id.contest);
        Button timerMmsButton = (Button) findViewById(R.id.timer_sendmms);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendTimerMMS();
            }
        };

        mmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMMS();
            }
        });
        contestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContestActivity();
            }
        });
        timerMmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                timer.schedule(timerTask, 3000);
            }
        });
    }

    private void openContestActivity() {
        Intent intent = new Intent(this, InformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AMLCostants.KEY_APP_NAME, "app");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void sendMMS() {
        MessageSender sender = new MessageSender(KyungmanMainActivity.this, "test");
        sender.sendMessage("test");
    }

    private void sendTimerMMS() {
//        Intent intent = new Intent(this, TranslucentActivity.class);
        Intent intent = new Intent(this, TranparentActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kyungman_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
