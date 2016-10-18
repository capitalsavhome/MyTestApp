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
    private final static String SHARED_PREF = "SharedPref";

    /**
     * Thread for counter
     */
    private MyThread mThread = new MyThread();

    /**
     * flag to start/stop thread
     */
    private boolean mFlag = false;

    /**
     * SharedPreferences to save time
     */
    private SharedPreferences mSharedPreferences;




    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startId == 1) {

            //initialization of sharedPreferences
            mSharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);


            //get current launch time
            Calendar calendar = new GregorianCalendar();
            calendar.getInstance();
            String string = new SimpleDateFormat("dd-MM-yyyy--HH:mm:ss").format(calendar.getTime());

            //save current launch time
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(SAVED_TIME, string);
            editor.commit();

            someTask();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();

        //if thread not interrupt
        if (!mThread.isInterrupted()) {
            //interrupt thread
            mThread.interrupt();
        }
        //set flag false - to stop thread
        mFlag = false;

        //get time of last Start
        String time = mSharedPreferences.getString(SAVED_TIME, "FirstLaunch");

        //send last launch time to MainActivity
        Intent newIntent = new Intent(MainActivity.SECOND_BROADCAST_ACTION);
        newIntent.putExtra(MainActivity.EXTRA_TIME, time);
        sendBroadcast(newIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.LOG_TAG, "onBind");
        return null;
    }

    /**
     * method start thread
     */
    void someTask() {
        //set flag true
        mFlag = true;
        //start thread
        mThread.start();
    }

    class MyThread extends Thread{

        private final static String SHARED_PREF_THREAD = "SharedPrefThread";

        /**
         * SharedPreferences in Thread to save count
         */
        private SharedPreferences mSharedPreferences;

        /**
         * intent to send Broadcast messages
         */
        private Intent mIntent = new Intent(MainActivity.BROADCAST_ACTION);

        /**
         * show true if method saveValues was called in Thread
         */
        private boolean isCalledSave = false;

        @Override
        public void run() {
            //initialization of sharedPreferences
            mSharedPreferences = getSharedPreferences(SHARED_PREF_THREAD, MODE_PRIVATE);

            //get last value of counter
            int count = mSharedPreferences.getInt(SAVED_COUNTER, 0);

                while (true) {
                    //if thread not stop
                    if (mFlag) {

                        count++;

                        //send value of counter to mainActivity
                        mIntent.putExtra(MainActivity.EXTRA_COUNT, count);
                        sendBroadcast(mIntent);
                        Log.d(Constants.LOG_TAG, Integer.toString(count));
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //if thread stop
                    else {
                        //make one call method saveValues
                        if (!isCalledSave) {
                            saveValues(count);
                            isCalledSave = true;
                        }
                    }
                }
        }

        /**
         * save values from counter to SharedPreferences
         * @param count - int counter
         */
        private void saveValues(int count) {
            mSharedPreferences = getSharedPreferences(SHARED_PREF_THREAD, MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(SAVED_COUNTER, count);
            editor.commit();
        }
    }
}


