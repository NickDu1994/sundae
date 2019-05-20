package com.xwing.sundae.android.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xwing.sundae.R;

import static com.xwing.sundae.android.util.CommonMethod.isMobileNO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";

    private int countSeconds = 60;//倒计时秒数
    private EditText user_mobile_number, verification_code_text;
    private Button get_verification_code_btn, login_btn;
    private Context mContext;
    private String usersuccess;
    private String userinfomsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();


    }

    private void initView() {
        user_mobile_number = findViewById(R.id.user_mobile_number);
        verification_code_text = findViewById(R.id.verification_code_text);
        get_verification_code_btn = findViewById(R.id.get_verification_code_btn);
        login_btn = findViewById(R.id.login_btn);
    }

    private void initEvent() {
        get_verification_code_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    private Handler mCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                get_verification_code_btn.setText("(" + countSeconds + ")后获取验证码");
                mCountHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                countSeconds = 60;
                get_verification_code_btn.setText("请重新获取验证码");
            }
        }
    };

    private void getMobile(String mobile) {
        if ("".equals(mobile)) {
            Log.e(TAG, "mobile=" + mobile);
            Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        } else if (isMobileNO(mobile) == false) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "输入了正确的手机号");
            requestVerifyCode(mobile);
        }
    }

    private void requestVerifyCode(String mobile) {
        if (countSeconds > 0) {
            --countSeconds;
            get_verification_code_btn.setText("(" + countSeconds + ")后获取验证码");
            mCountHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            countSeconds = 60;
            get_verification_code_btn.setText("请重新获取验证码");
        }
    }

    private void startCountBack() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                get_verification_code_btn.setText(countSeconds + "");
                mCountHandler.sendEmptyMessage(0);
            }
        });
    }

    private void login() {
        String user_num = user_mobile_number.getText().toString().trim();
        String verifyCode = verification_code_text.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_verification_code_btn:
                if (countSeconds == 60) {
                    String mobile = user_mobile_number.getText().toString();
                    Log.e("tag", "mobile==" + mobile);
                    getMobile(mobile);
                } else {
                    Toast.makeText(LoginActivity.this, "不能重复发送验证码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_btn:
                login();
                break;
            default:
                break;
        }
    }
}
