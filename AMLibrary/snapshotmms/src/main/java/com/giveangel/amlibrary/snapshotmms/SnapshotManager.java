package com.giveangel.amlibrary.snapshotmms;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Kyungman on 2015-05-06.
 */
class SnapshotManager {
    static final String DEFAULT_FILE_NAME = "giveangel_snapshot.png";

    public static String shootingSnapshot(Activity activity) {
        try {
            View snapshotView = activity.getWindow().getDecorView();
            snapshotView.setDrawingCacheEnabled(true);
            Bitmap screenshot = snapshotView.getDrawingCache();
            String filename = DEFAULT_FILE_NAME;
            File f = new File(Environment.getExternalStorageDirectory(), filename);
            f.createNewFile();
            OutputStream outStream = new FileOutputStream(f);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            snapshotView.setDrawingCacheEnabled(false);
            outStream.close();
            return f.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void deleteSnapshot(String path){
        File file = new File(path);
        file.delete();
    }
}
