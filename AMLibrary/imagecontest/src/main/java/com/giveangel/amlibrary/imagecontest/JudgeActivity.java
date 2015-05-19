package com.giveangel.amlibrary.imagecontest;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge);
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
        }
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
            finish();
        } else if (id == R.id.contestinformation_reload) {
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }
}
