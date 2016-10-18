package com.example.admin.mytestapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static String BUNDLE_KEY_COUNT = "CountKey";
    public final static String BUNDLE_KEY_TIME = "TimeKey";
    public final static String BROADCAST_ACTION = "com.example.admin.mytestapp";
    public final static String SECOND_BROADCAST_ACTION = "com.example.admin.mytestappSecond";
    public final static String EXTRA_COUNT = "Extra_count";
    public final static String EXTRA_TIME = "Extra_count";
    public final static String MAIN_SHARED_PREF = "MsharedPref";
    public final static String MAIN_SAVE_COUNTER = "MainSaveCounter";
    public final static String MAIN_SAVE_TIME = "MainSaveTime";

    /**
     * BroadcastReceiver to take messages from Service Thread
     */
    private BroadcastReceiver mBroadcastReceiver;

    /**
     * BroadcastReceiver to take messages from Service (Not from thread)
     */
    private BroadcastReceiver mSecondBroadcastReceiver;

    /**
     * value of counter
     */
    private int mCount;

    /**
     * last time of launch service
     */
    private String mTime;

    /**
     * textView for counter
     */
    private TextView mTextViewCount;

    /**
     * textView for Time
     */
    private TextView mTextViewTime;

    /**
     * shared preferences to save data count and time in MainActivity
     */
    private SharedPreferences mSharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization of SharedPreferences
        mSharedPreferences = getSharedPreferences(MAIN_SHARED_PREF, MODE_PRIVATE);

        //get data from SharedPreferences
        mCount = mSharedPreferences.getInt(MAIN_SAVE_COUNTER, 0);
        mTime = mSharedPreferences.getString(MAIN_SAVE_TIME, "FirstLaunch");

        //initialization of textViews
        mTextViewCount = (TextView) findViewById(R.id.tvCounter);
        mTextViewTime = (TextView) findViewById(R.id.tvTime);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //get incoming message Count
                mCount = intent.getIntExtra(EXTRA_COUNT, 0);
                //show counter
                mTextViewCount.setText(Integer.toString(mCount));
            }
        };

        mSecondBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ////get incoming message Time
                mTime = intent.getStringExtra(EXTRA_TIME);
                //show time
                mTextViewTime.setText(mTime);
            }
        };
        //set and register intentFilters
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(SECOND_BROADCAST_ACTION);
        registerReceiver(mSecondBroadcastReceiver, intentFilter1);

        //show time and counter
        mTextViewTime.setText(mTime);
        mTextViewCount.setText(Integer.toString(mCount));
    }

    public void btnOnClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnStart :
                startService(new Intent(this, MyService.class));
                break;
            case R.id.btnStop :
                stopService(new Intent(this, MyService.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop service
        stopService(new Intent(this, MyService.class));

        //save counter and Time to SharedPreferences in MainActivity
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(MAIN_SAVE_COUNTER, mCount);
        editor.putString(MAIN_SAVE_TIME, mTime);
        editor.commit();

        //unregister all receivers
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mSecondBroadcastReceiver);
    }
}
