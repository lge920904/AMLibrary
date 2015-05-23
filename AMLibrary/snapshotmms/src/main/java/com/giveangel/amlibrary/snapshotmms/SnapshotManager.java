package com.giveangel.amlibrary.snapshotmms;

import android.app.Activity;
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
class SnapshotManager {
    static final String DEFAULT_FILE_NAME = "giveangel_snapshot.png";

    public static String shootingSnapshot(Activity activity) {
        try {
//            View snapshotView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            View snapshotView = activity.getWindow().getDecorView();
            snapshotView.setDrawingCacheEnabled(true);
            Log.i(SnapshotManager.class.getSimpleName(), snapshotView.getWidth() + " x " + snapshotView.getHeight());
            Bitmap screenshot = snapshotView.getDrawingCache();
            if (screenshot == null) {
                Log.i(SnapshotManager.class.getSimpleName(), "screenshot = null");
                setSnapshotView(snapshotView);
                screenshot = Bitmap.createBitmap(snapshotView.getDrawingCache());
//                screenshot = snapshotView.getDrawingCache();
            }
            Log.i("screenshot", " screenshot = " + screenshot.getWidth() + " x " + screenshot.getHeight());
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

    public static void setSnapshotView(View snapshotView) {
        snapshotView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        snapshotView.layout(0, 0, snapshotView.getMeasuredWidth(), snapshotView.getMeasuredHeight());
        snapshotView.buildDrawingCache(true);
    }

    public static void deleteSnapshot(String path) {
        File file = new File(path);
        file.delete();
    }
}
