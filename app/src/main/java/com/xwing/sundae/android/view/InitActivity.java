package com.xwing.sundae.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xwing.sundae.R;

public class InitActivity extends AppCompatActivity {

    TextView skip;

    private int countSeconds = 3;//倒计时秒数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        skip = findViewById(R.id.skip);

        mCountHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private void toLoginPage() {
        Intent intent = new Intent(InitActivity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private Handler mCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                skip.setText(countSeconds + "秒");
                mCountHandler.sendEmptyMessageDelayed(0, 1000);
                if(countSeconds == 0) {
                    toLoginPage();
                }
            }
        }
    };
}
