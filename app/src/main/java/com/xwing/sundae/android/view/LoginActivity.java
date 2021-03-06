package com.xwing.sundae.android.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.model.VerifyCode;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.SharedPreferencesHelper;
import com.xwing.sundae.android.util.SharedPreferencesUtil;
import com.xwing.sundae.android.util.interpolator.JellyInterpolator;
import com.xwing.sundae.android.view.my.MyFragment;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.xwing.sundae.android.util.CommonMethod.isMobileNO;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginBeautyActivityTag";

    private TextView mBtnLogin, get_verify_code_btn;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName;
    private RelativeLayout mPsw;
    private EditText user_mobile_no, user_mobile_vercode;
    private FragmentTransaction fragmentTransaction;

    private FragmentManager fragmentManager;
    private int countSeconds = 60;//倒计时秒数

    String verify_code = "";
    SharedPreferencesHelper sharedPreferencesHelper;
    SharedPreferencesUtil spUtil;

    private MyFragment mMyFragment;

    GetUserInfo getUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary) );
        getUserInfo = new GetUserInfo(this);
        spUtil = SharedPreferencesUtil.getInstance(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(LoginActivity.this, "user");
        initView();
        initEvent();
    }


    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (RelativeLayout) findViewById(R.id.input_layout_psw);
        get_verify_code_btn = (TextView) findViewById(R.id.get_verify_code_btn);
        user_mobile_no = (EditText) findViewById(R.id.user_mobile_no);
        user_mobile_vercode = (EditText) findViewById(R.id.user_mobile_vercode);


    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_text = mBtnLogin.getText().toString();
                switch (btn_text) {
                    case "登录":
                        login();
                        break;
                    case "取消":
                        recovery();
                        break;
                }
            }
        });
        get_verify_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countSeconds == 60) {
                    String mobile = user_mobile_no.getText().toString();
                    Log.e("tag", "mobile==" + mobile);
                    getMobile(mobile);
                } else {
                    Toast.makeText(LoginActivity.this, "不能重复发送验证码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 验证码计时
     *
     * @param mobile
     */
    private Handler mCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                get_verify_code_btn.setText("(" + countSeconds + ")后获取验证码");
                mCountHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                countSeconds = 60;
                get_verify_code_btn.setText("重新获取");
            }
        }
    };

    /**
     * 手机号 判断
     *
     * @param mobile
     */
    private void getMobile(String mobile) {
        if ("".equals(mobile)) {
            Log.e(TAG, "mobile=" + mobile);
            Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        } else if (isMobileNO(mobile) == false) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.e(TAG, "输入了正确的手机号");
            requestVerifyCode(mobile);
        }
    }

    /**
     * login 判断
     *
     * @param mobile
     */
    private void requestVerifyCode(String mobile) {
        if (countSeconds > 0) {
            --countSeconds;
            get_verify_code_btn.setText("(" + countSeconds + ")后获取验证码");
            mCountHandler.sendEmptyMessageDelayed(0, 1000);
            getVerifyCodeRequest(mobile);
        } else {
            countSeconds = 60;
            get_verify_code_btn.setText("重新获取验证码");
        }
    }

    /**
     * login 判断
     */
    private void login() {
        String mobile = user_mobile_no.getText().toString();
        String verify_code = user_mobile_vercode.getText().toString();
        if ("".equals(mobile)) {
            Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!("".equals(mobile)) && ("".equals(verify_code))) {

            Toast.makeText(LoginActivity.this, "请获取验证码", Toast.LENGTH_SHORT).show();

            return;
        }
        if (!("".equals(mobile)) && !("".equals(verify_code))) {
            // 计算出控件的高与宽
            mWidth = mBtnLogin.getMeasuredWidth();
            mHeight = mBtnLogin.getMeasuredHeight();
            // 隐藏输入框
            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);
            inputAnimator(mInputLayout, mWidth, mHeight);
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("mobile", mobile);
            paramsMap.put("verify_code", verify_code);

            loginPostRequest();
        }
    }

    /**
     * call 验证码api
     *
     * @param mobile_id
     */
    private void getVerifyCodeRequest(String mobile_id) {
//        String url = REQUEST_URL + "/getPhoneMessage/" + mobile_id;
        String url = Constant.REQUEST_URL_MY + "/user/getPhoneMessage";
        String mock_url = Constant.REQUEST_URL_MY + "/getPhoneMessage";
        HashMap<String, String> paramsMap = new HashMap<>();


        paramsMap.put("phone", mobile_id);
        String request_json = CommonMethod.mapToJson(paramsMap);

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(LoginActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if(!Constant.LOG_LEVEL.equals("PRD")) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }

                Gson gson = new Gson();
                try {
                    CommonResponse<VerifyCode> verifyCodeBean =
                            (CommonResponse<VerifyCode>) gson.fromJson(response,
                                    new TypeToken<CommonResponse<VerifyCode>>() {
                                    }.getType());
                    String code = verifyCodeBean.getData().getCode();
                    sharedPreferencesHelper.put("verify_code", code);
                    Log.v("verify_code", "verify_code" + code);
                } catch (Exception e) {
                    Log.v("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    /**
     * call 登录api
     */
    private void loginPostRequest() {
        verify_code = user_mobile_vercode.getText().toString();
        String url = Constant.REQUEST_URL_MY + "/user/login";
        String mobile = user_mobile_no.getText().toString();
        String verificationCode = user_mobile_vercode.getText().toString();

        String mock_url = Constant.REQUEST_URL_MY + "/getUserInfo";


        HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put("verificationCode", verificationCode);
        paramsMap.put("phone", mobile);

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            //        OkhttpUtil.okHttpPostJson(mock_url, "", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(LoginActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Map<String, Object> map_res = gson.fromJson(response, Map.class);

                if (null != map_res.get("status") && "200.0".equals(map_res.get("status").toString())) {
                    if(!Constant.LOG_LEVEL.equals("PRD")) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                    sharedPreferencesHelper.put("user_info", response);
                    sharedPreferencesHelper.put("auth", true);
                    spUtil.putSP("keyword_note","");
                    spUtil.removeSP("keyword_note");
                    CommonResponse<UserInfo> userInfoBean = getUserInfo.getUserInfo();
                    if (null != userInfoBean.getData()) {
                        Long userId = userInfoBean.getData().getId();
                        checkUnreadMessage(userId);
                        finish();
                    } else {
                        recovery();
                        Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.show(mMyFragment);
                } else {
                    recovery();
                    Toast.makeText(LoginActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean checkUnreadMessage(Long user_id) {
        String url = Constant.REQUEST_URL_MY + "/message/checkHasUnread";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(LoginActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if(!Constant.LOG_LEVEL.equals("PRD")) {
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                Log.e("loginPostRequest", "checkunread" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Boolean data = (Boolean) map_res.get("data");
                    if (data) {
                        MainActivity.addMessageBadge();
                    } else {
                        MainActivity.removeMessageBadge();
                    }
                } catch (Exception e) {
                    Log.e("checkunreadmessage", "error" + e);
                }
            }
        });
        return true;
    }

    /**
     * 输入框的动画效果
     *
     * @param view 控件
     * @param w    宽
     * @param h    高
     */
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 1f, 0.2f);
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
                mBtnLogin.setText("取消");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }

    /**
     * 恢复初始状态
     */
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f, 1f);
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
        mBtnLogin.setText("登录");
    }
}
