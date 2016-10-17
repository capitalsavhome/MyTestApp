package com.example.admin.mytestapp;

import android.app.Service;
import android.content.Intent;
import android.icu.util.TimeUnit;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Admin on 17.10.2016.
 */

public class MyService extends Service {

    private MyThread mThread = new MyThread();
    private boolean mFlag = false;
    int i = 0;

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG_TAG, "onStartCommand");
        someTask();
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
        @Override
        public void run() {
            while (true) {
                if (mFlag) {
                    Log.d(Constants.LOG_TAG, Integer.toString(i));
                    i++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


