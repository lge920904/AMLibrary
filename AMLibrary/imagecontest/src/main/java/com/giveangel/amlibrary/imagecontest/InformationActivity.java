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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giveangel.amlibrary.imagecontest.utils.ContestManager;
import com.giveangel.sender.AMLCostants;
import com.giveangel.sender.MessageSender;
import com.squareup.picasso.Picasso;


public class InformationActivity extends ActionBarActivity implements View.OnClickListener {
    // Constants
    private static final int SELECT_PHOTO = 100;
    private static final String CONTEST_MSG_EXIT = "허가된 앱이 아닙니다.";
    private static final String CONTEST_MSG_THANK = "응모 감사합니다";
    private static final String CONTEST_MSG_PICKING = "선택하신 사진으로 공모전에 참여하시겠습니까?";
    private static final String BUTTON_POSITIVE = "확인";
    private static final String BUTTON_NEGATIVE = "취소";
    private static final String CONTEST_MSG_JOIN = "join";
    private static final String CONTEST_MSG_CONFIRM_EXIT = "심사를 완료하시면 약 1천만원의 경품의 \n" +
            "응모가 가능한 경품권을 받을 수 있습니다. \n정말 닫으시겠습니까? ";
    private static final String BUTTON_EXIT = "닫기";
    private static final String BUTTON_CANCEL = "취소";
    // UIs
    private Button joinButton;
    private Button judgeButton;
    private TextView specificInfoText;
    private ImageView contestJudgeEventImg;
    private ImageView eventSummaryImg;
    private TextView eventSummaryText;
    // variables
    private ContestManager contestManager;
    private MessageSender sender;
    private String appName;
    private BackPressCloseHandler backPressCloseHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appName = getIntent().getExtras().getString(AMLCostants.KEY_APP_NAME);
        contestManager = new ContestManager(this, appName);
        backPressCloseHandler = new BackPressCloseHandler(this);
        try {
            new ValidCheckTask().execute().get();
            setContentView(R.layout.activity_information);
            eventSummaryImg = (ImageView) findViewById(R.id.eventSummaryImg); // 이벤트 설명 이미지.
            joinButton = (Button) findViewById(R.id.contestJoinButton);
            judgeButton = (Button) findViewById(R.id.contestJudgeButton);
            specificInfoText = (TextView) findViewById(R.id.specificInfoText); // 상세설명

            joinButton.setOnClickListener(this);
            judgeButton.setOnClickListener(this);
            specificInfoText.setOnClickListener(this);
            settingTemp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
    }

    private void settingTemp() {

        Picasso.with(this)
                .load("http://cafeptthumb4.phinf.naver.net/20150515_158/joonggo_safe_14316834508065pKXS_JPEG/%C0%A5_%BB%F3%BC%BC.jpg?type=w740")
                .fit().centerCrop()
                .into(eventSummaryImg);
    }

    public class ValidCheckTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return contestManager.checkValidApp();
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

    class GetInformation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url = contestManager.getInformationURL();
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            if (url == null) return;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    private void judgeContest() {
        Intent intent = new Intent(this, JudgeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AMLCostants.KEY_APP_NAME, appName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        boolean flag;
        if (v.getId() == joinButton.getId()) {
            flag = contestManager.checkValidContestJoin();
            if (!flag) return;
            chooseImage();
        } else if (v.getId() == judgeButton.getId()) {
            flag = contestManager.checkValidContestJudge();
            if (!flag) return;
            judgeContest();
        } else if (v.getId() == specificInfoText.getId()) {
            flag = contestManager.checkValidContestJudge();
            if (!flag) return;
            openWebPage();
        }
    }

    private void openWebPage() {
        try {
            new GetInformation().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == SELECT_PHOTO) {
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
//                        sender = new MessageSender(InformationActivity.this, "contest");
                        sender.sendMessage(filePath, CONTEST_MSG_JOIN);
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
        if (id == R.id.contest_information_exit) {
            lottoNumberAlertDialog().show();
        } else if (id == R.id.contestinformation_reload) {
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }
}
