package com.example.admin.mytestapp;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeUnit;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 17.10.2016.
 */

public class MyService extends Service {

    private final static String SAVED_COUNTER = "savedCounter";
    private MyThread mThread = new MyThread();
    private boolean mFlag = false;


    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startId == 1) {
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

        private boolean isCalledSave = false;

        @Override
        public void run() {
            mSharedPreferences = getSharedPreferences("MP", MODE_PRIVATE);
            int count = mSharedPreferences.getInt(SAVED_COUNTER, 0);

                while (true) {
                    if (mFlag) {
                        count++;
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


