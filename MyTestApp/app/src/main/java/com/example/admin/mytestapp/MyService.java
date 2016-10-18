package com.example.admin.mytestapp;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeUnit;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 17.10.2016.
 */

public class MyService extends Service {

    private final static String SAVED_COUNTER = "savedCounter";
    private final static String SAVED_TIME = "savedTime";
    private MyThread mThread = new MyThread();
    private boolean mFlag = false;
    private SharedPreferences mSharedPreferences;




    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startId == 1) {

            mSharedPreferences = getSharedPreferences("MP", MODE_PRIVATE);


            //get current launch time
            Calendar calendar = new GregorianCalendar();
            calendar.getInstance();
            String string = new SimpleDateFormat("dd-MM-yyyy--HH:mm:ss").format(calendar.getTime());

            //save current launch time
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(SAVED_TIME, string);
            editor.commit();

            Log.d(Constants.LOG_TAG, string);

            Log.d(Constants.LOG_TAG, "onStartCommand");
            someTask();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        if (!mThread.isInterrupted()) {
            mThread.interrupt();
        }
        mFlag = false;

        //get time of last Start
        String time = mSharedPreferences.getString(SAVED_TIME, "FirstLaunch");

        //send last launch time to MainActivity
        Intent newIntent = new Intent(MainActivity.SECOND_BROADCAST_ACTION);
        newIntent.putExtra(MainActivity.EXTRA_TIME, time);
        sendBroadcast(newIntent);
        Log.d(Constants.LOG_TAG, "Thread Interrupt. YEEEES!!!!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.LOG_TAG, "onBind");
        return null;
    }

    void someTask() {
        mFlag = true;
        mThread.start();
    }

    class MyThread extends Thread{

        private SharedPreferences mSharedPreferences;
        private Intent mIntent = new Intent(MainActivity.BROADCAST_ACTION);
        private boolean isCalledSave = false;

        @Override
        public void run() {
            mSharedPreferences = getSharedPreferences("MP", MODE_PRIVATE);
            int count = mSharedPreferences.getInt(SAVED_COUNTER, 0);

                while (true) {
                    if (mFlag) {
                        count++;
                        mIntent.putExtra(MainActivity.EXTRA_COUNT, count);
                        sendBroadcast(mIntent);
                        Log.d(Constants.LOG_TAG, Integer.toString(count));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        if (!isCalledSave) {
                            saveValues(count);
                            isCalledSave = true;
                        }
                    }
                }
        }

        private void saveValues(int count) {
            mSharedPreferences = getSharedPreferences("MP", MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(SAVED_COUNTER, count);
            editor.commit();
        }
    }
}


