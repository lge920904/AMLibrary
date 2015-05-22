package com.giveangel.amlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.giveangel.amlibrary.imagecontest.InformationActivity;
import com.giveangel.amlibrary.snapshotmms.MessageSender;
import com.giveangel.sender.AMLCostants;

import java.lang.reflect.Field;
import java.util.HashMap;
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
                timer.schedule(timerTask, 10000);
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
        try {
            BackStack stack = new BackStack(getApplication());
            Activity activty = stack.getTopActivity();
            MessageSender sender;
            if (activty != null)
                sender = new MessageSender(activty, "test");
            else sender = new MessageSender(this, "test");
            sender.sendMessage("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Activity getActivity() throws Exception {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
        if (Build.VERSION.SDK_INT > 19) {
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        }
        return null;
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

    public class BackStack {

        private Application application;

        public BackStack(Application application) {
            this.application = application;
        }

        public Activity getTopActivity() {
            if (application == null) {
                throw new IllegalStateException("Application is null");
            }
            Object obj = null;
            Field f;

            try {
                f = Application.class.getDeclaredField("mLoadedApk");
                f.setAccessible(true);
                obj = f.get(application); // obj => LoadedApk
                f = obj.getClass().getDeclaredField("mActivityThread");
                f.setAccessible(true);
                obj = f.get(obj); // obj => ActivityThread
                f = obj.getClass().getDeclaredField("mActivities");
                f.setAccessible(true);
                HashMap map = (HashMap) f.get(obj); //  obj => HashMap=<IBinder, ActivityClientRecord>
                if (map.values().size() == 0) {
                    return null;
                }
                obj = map.values().toArray()[0]; // obj => ActivityClientRecord
                f = obj.getClass().getDeclaredField("activity");
                f.setAccessible(true);
                obj = f.get(obj); // obj => Activity
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                if (!(obj instanceof Activity)) {
                    Log.i("return", "null");
                    return null;
                }
            }
            Log.i("return", "Activity");
            return (Activity) obj;
        }
    }
}
