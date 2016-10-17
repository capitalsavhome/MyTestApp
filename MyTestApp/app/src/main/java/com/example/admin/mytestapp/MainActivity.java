package com.example.admin.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
