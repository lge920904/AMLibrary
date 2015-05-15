package com.giveangel.amlibrary.imagecontest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class GalleryJudgeActivity extends ActionBarActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_judge);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = (String) view.getTag();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery_judge, menu);
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

    private class GridViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 43;
        }

        @Override
        public Object getItem(int i) {
            return "WALL " + String.valueOf(i + 1);
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

            final String imageUrl = "http://tbremer.pf-control.de/walls/" + String.valueOf(i + 1) + ".png";
//            convertView.setTag(imageUrl);

            viewHolder.text.setText(getItem(i).toString());


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
