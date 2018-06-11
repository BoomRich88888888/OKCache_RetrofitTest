package com.example.okcachhetest.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.okcachhetest.R;

public class DaoJiShiActivity extends AppCompatActivity {

    private TextView mTime;
    int time = 4;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int i = (int) msg.obj;
                    if (i >= 0) {
                        handler.postDelayed(runnable, 1000);
                        mTime.setText(i + "");
                    } else {
                        startActivity(new Intent(DaoJiShiActivity.this,RetrofitActivity.class));
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handler.post(runnable);
    }

    private void initView() {
        mTime = (TextView) findViewById(R.id.mTime);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            --time;
            Message message = handler.obtainMessage(1, time);
            handler.sendMessage(message);
        }
    };
}
