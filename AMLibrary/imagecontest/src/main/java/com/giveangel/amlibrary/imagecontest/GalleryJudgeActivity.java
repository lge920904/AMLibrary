package com.giveangel.amlibrary.imagecontest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giveangel.amlibrary.imagecontest.utils.ContestController;
import com.giveangel.sender.AMLCostants;
import com.giveangel.sender.Helper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class GalleryJudgeActivity extends ActionBarActivity {
    private static final String CONTEST_MSG_THANK = "응모 감사합니다 다음 등수 사진을 뽑아주세요.";
    private static final String CONTEST_MSG_PICKING_FRONT = "위사진을 ";
    private static final String CONTEST_MSG_PICKING_BACK = "등으로 뽑으시겠습니까? \n" +
            "확인 버튼을 누르시면 mms가 한건 발송되며 추가비용은 발생하지 않습니다. \n심사를 완료하시면 로또응모권이 지급됩니다.";
    private static final String BUTTON_POSITIVE = "확인";
    private static final String BUTTON_NEGATIVE = "취소";

    private String appName;
    private DrawerLayout drawer;
    private GridView gridView;
    private GetImageUrlTask task;
    protected ArrayList<String> resultList;
    private int rankCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_judge);
        appName = getIntent().getExtras().getString(AMLCostants.KEY_APP_NAME);
        rankCount = 1;
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        gridView = (GridView) findViewById(R.id.gridView);
        Log.e(this.getClass().getSimpleName(), "create logcat");
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                MessageSender sender = new MessageSender(GalleryJudgeActivity.this, "contest");
//                sender.sendMessage(resultList.get(i));
                sendContestRankDialog(resultList.get(i)).show();
            }
        });
    }

    private AlertDialog sendContestRankDialog(final String urlPath) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(CONTEST_MSG_PICKING_FRONT + rankCount + CONTEST_MSG_PICKING_BACK)
                .setCancelable(false).setPositiveButton(BUTTON_POSITIVE,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        MessageSender sender = new MessageSender(GalleryJudgeActivity.this, appName);
                        sender.sendMessage(rankCount + "등:" + urlPath);
                        rankCount++;
                        Toast.makeText(GalleryJudgeActivity.this,
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
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        if (task == null) {
            task = new GetImageUrlTask();
            task.execute();
        } else {

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

    class GetImageUrlTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ContestController controller = new ContestController(GalleryJudgeActivity.this);
                HashMap<Object, Object> params = new HashMap<>();
                params.put(AMLCostants.KEY_APP_NAME, "test");
                params.put(AMLCostants.KEY_AGENCY_NAME, Helper.getAgencyName(getApplicationContext()));
                params.put(AMLCostants.KEY_CALLINGNUM, Helper.getPhoneNumber(getApplicationContext()));
                resultList = controller.getImageList(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gridView.setAdapter(new GridViewAdapter());
        }
    }

    private class GridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int i) {
            return resultList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.grid_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) convertView.findViewById(R.id.grid_image);
                viewHolder.view = convertView.findViewById(R.id.view);
                viewHolder.text = (TextView) convertView.findViewById(R.id.textpalette);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String imageUrl = resultList.get(i);
//            convertView.setTag(imageUrl);
//            viewHolder.text.setText(getItem(i).toString());
            Picasso.with(convertView.getContext())
                    .load(imageUrl)
                    .fit().centerCrop()
                    .into(viewHolder.image, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            final Bitmap bitmap = ((BitmapDrawable) viewHolder.image.getDrawable()).getBitmap();// Ew!
                            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    if (palette != null) {
                                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                                        if (vibrantSwatch != null) {
                                            viewHolder.view.setBackgroundColor(vibrantSwatch.getRgb());
                                            viewHolder.text.setTextColor(vibrantSwatch.getTitleTextColor());
                                        }
                                    }
                                }
                            });
                        }
                    });
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView image;
        TextView text;
        View view;
    }
}
