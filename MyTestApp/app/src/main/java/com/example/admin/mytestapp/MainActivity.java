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

    public final static String BROADCAST_ACTION = "com.example.admin.mytestapp";
    public final static String SECOND_BROADCAST_ACTION = "com.example.admin.mytestappSecond";
    public final static String EXTRA_COUNT = "Extra_count";
    public final static String EXTRA_TIME = "Extra_count";

    private BroadcastReceiver mBroadcastReceiver;

    private BroadcastReceiver mSecondBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getIntExtra(EXTRA_COUNT, 0);
//
//                Log.d(Constants.LOG_TAG, time);
                int time = intent.getIntExtra(EXTRA_TIME, 0);
                String stringTime = Integer.toString(time);
                TextView countTextView = (TextView) findViewById(R.id.tvCounter);

                countTextView.setText(Integer.toString(count));


            }
        };
        mSecondBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra(EXTRA_TIME);
                TextView timeTextView = (TextView) findViewById(R.id.tvTime);
                timeTextView.setText(time);
            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(SECOND_BROADCAST_ACTION);
        registerReceiver(mSecondBroadcastReceiver, intentFilter1);


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
        unregisterReceiver(mBroadcastReceiver);
    }
}
