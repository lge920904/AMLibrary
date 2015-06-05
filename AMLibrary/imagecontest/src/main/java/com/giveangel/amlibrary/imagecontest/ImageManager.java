package com.giveangel.amlibrary.imagecontest;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Kyungman on 2015-05-06.
 */
class ImageManager {
    static final String DEFAULT_FILE_NAME = "giveangel_snapshot.png";

    /* 갤러리에서 각 뷰를 선택해 사진을 전송,
    뷰의 사진을 찍고 파일로 저장하는 함수 */
    public static String getImage(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap screenshot = view.getDrawingCache();
            String filename = DEFAULT_FILE_NAME;
            File f = new File(Environment.getExternalStorageDirectory(), filename);
            f.createNewFile();
            OutputStream outStream = new FileOutputStream(f);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            view.setDrawingCacheEnabled(false);
            outStream.close();
            return f.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 전송 후 사진을 지우는 함수 */
    public static void deleteImage(String path) {
        File file = new File(path);
        file.delete();
        Log.i("ImageManager", "Delete file");
    }
}
