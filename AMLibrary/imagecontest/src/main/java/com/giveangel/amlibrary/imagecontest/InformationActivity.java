package com.giveangel.amlibrary.imagecontest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.giveangel.amlibrary.imagecontest.utils.ContestManager;
import com.giveangel.sender.MessageSender;
import com.squareup.picasso.Picasso;


public class InformationActivity extends Activity implements View.OnClickListener {
    // Constants
    private static final int SELECT_PHOTO = 100;
    private static final String CONTEST_MSG_THANK = "응모 감사합니다";
    private static final String CONTEST_MSG_PICKING = "선택하신 사진으로 공모전에 참여하시겠습니까?";
    private static final String BUTTON_POSITIVE = "확인";
    private static final String BUTTON_NEGATIVE = "취소";
    // UIs
    private ImageView contestJudgeEventImg;
    private ImageView eventSummaryImg;
    private TextView eventSummaryText;
    private Button joinToContestButton;
    private Button judgeEntryButton;
    private Button specificInfoButton;
    // variables
    private ContestManager contestManager;
    private MessageSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        contestJudgeEventImg = (ImageView) findViewById(R.id.contestJudgeEventImg); // 공모전이벤트
        eventSummaryImg = (ImageView) findViewById(R.id.eventSummaryImg); // 이벤트 설명 이미지
        eventSummaryText = (TextView) findViewById(R.id.eventSummaryText); //이벤트 설명 텍스트
        joinToContestButton = (Button) findViewById(R.id.joinToContestButton); // 공모전 참여
        judgeEntryButton = (Button) findViewById(R.id.judgeEntryButton); // 심사
        specificInfoButton = (Button) findViewById(R.id.specificInfoButton); // 상세설명

        contestManager = new ContestManager(this);

        joinToContestButton.setOnClickListener(this);
        judgeEntryButton.setOnClickListener(this);
        specificInfoButton.setOnClickListener(this);

        settingTemp();

    }

    private void settingTemp() {
        Picasso.with(this)
                .load("http://cafeskthumb.phinf.naver.net/20150515_257/joonggo_safe_1431683533156oH4s1_JPEG/%C0%A5_%B8%DE%C0%CE%B9%E8%B3%CA.jpg?type=w740")
                .fit().centerCrop()
                .into(contestJudgeEventImg);
        Picasso.with(this)
                .load("http://cafeptthumb4.phinf.naver.net/20150515_158/joonggo_safe_14316834508065pKXS_JPEG/%C0%A5_%BB%F3%BC%BC.jpg?type=w740")
                .fit().centerCrop()
                .into(eventSummaryImg);
        eventSummaryText.setText("1. 품명 및 모델명 : 노샥 / OKBABY CAP-01\n" +
                "2. KC 인증 필 (품질경영 및 공산품안전관리법 상 안전인증대상 또는 자율안전확인대상 공산품에 한함) : B044T166-0001\n" +
                "3. 크기, 중량 : 10cm(W)*15cm(H) , 40g\n" +
                "4. 색상 : 베이지, 네이비\n" +
                "5. 재질 (섬유의 경우 혼용률) : 안감: 폴리에틸렌70%/폴리에스테르30% 겉감: 플리에스테르100%\n" +
                "6. 사용연령 : 8~20개월\n" +
                "7. 동일모델의 출시년월 : 2007년5월\n" +
                "8. 제조자, 수입품의 경우 수입자를 함께 표기 (병행수입의 경우 병행수입 여부로 대체 가능) : 제조자: OKBABY / 수입자: ㈜샤인엠앤피\n" +
                "9. 제조국 : OKBABY / 이탈리아\n" +
                "10. 취급방법 및 취급시 주의사항, 안전표시 (주의, 경고 등) : 제품 사용시 반드시 보호자를 동반하세요. 아기의 머리에 알맞게 조절하여 사용하시고, 실내용이므로 자전거 및 오토바이 승차시에는 사용하지 마세요\n" +
                "11. 품질보증기준 : 공정거래위원회 고시(소비자분쟁해결기준)에 의거하여 보상해 드립니다.\n" +
                "12. A/S 책임자와 전화번호 : (주)샤인엠앤피 / 02-516-5381");
        eventSummaryText.setScroller(new Scroller(this));
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
        if (v.getId() == joinToContestButton.getId()) {
            flag = contestManager.checkValidContestJoin();
            if (!flag) return;
            chooseImage();
        } else if (v.getId() == judgeEntryButton.getId()) {
            flag = contestManager.checkValidContestJudge();
            if (!flag) return;
            judgeContest();
        }else if(v.getId() == specificInfoButton.getId()){
            flag = contestManager.checkValidContestJudge();
            if (!flag) return;
            openWebPage();
        }
    }

    private void openWebPage() {

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
}
