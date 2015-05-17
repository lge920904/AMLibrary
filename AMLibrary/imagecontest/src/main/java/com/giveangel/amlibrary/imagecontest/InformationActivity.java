package com.giveangel.amlibrary.imagecontest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.giveangel.amlibrary.imagecontest.utils.ContestManager;
import com.giveangel.sender.MessageSender;


public class InformationActivity extends ActionBarActivity implements View.OnClickListener {
    // Constants
    private static final int SELECT_PHOTO = 100;
    private static final String CONTEST_MSG_EXIT = "허가된 앱이 아닙니다.";
    private static final String CONTEST_MSG_THANK = "응모 감사합니다";
    private static final String CONTEST_MSG_PICKING = "선택하신 사진으로 공모전에 참여하시겠습니까?";
    private static final String BUTTON_POSITIVE = "확인";
    private static final String BUTTON_NEGATIVE = "취소";
    // UIs
    private Button joinButton;
    private Button judgeButton;

    // variables
    private ContestManager contestManager;
    private MessageSender sender;
    private String appName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appName = "";
        contestManager = new ContestManager(this);
        try {
            new ValidCheckTask().execute().get();

            setContentView(R.layout.activity_information);

            joinButton = (Button) findViewById(R.id.contest_join);
            judgeButton = (Button) findViewById(R.id.contest_judge);

            joinButton.setOnClickListener(this);
            judgeButton.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ValidCheckTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return contestManager.checkValidApp(appName);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Toast.makeText(getApplicationContext(),
                        CONTEST_MSG_EXIT, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    private void judgeContest() {
        startActivity(new Intent(this, JudgeActivity.class));
    }

    @Override
    public void onClick(View v) {
        boolean flag;
        if (v.getId() == joinButton.getId()) {
            flag = contestManager.checkValidContestJoin(appName);
            if (!flag) return;
            chooseImage();
        } else if (v.getId() == judgeButton.getId()) {
            flag = contestManager.checkValidContestJudge(appName);
            if (!flag) return;
            judgeContest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == SELECT_PHOTO) {
            // mms 전송하여야함
            String filePath = this.getFilePath(data);
            getCheckJoinContest(filePath).show();
        }
    }

    private String getFilePath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(
                selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private AlertDialog getCheckJoinContest(final String filePath) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(InformationActivity.this);
        dialogBuilder.setMessage(CONTEST_MSG_PICKING).setCancelable(false).setPositiveButton(BUTTON_POSITIVE,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        sender = new MessageSender(InformationActivity.this, "contest");
                        sender.sendMessage(filePath, "join");
                        Toast.makeText(InformationActivity.this,
                                CONTEST_MSG_THANK, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(BUTTON_NEGATIVE,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        return alert;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
