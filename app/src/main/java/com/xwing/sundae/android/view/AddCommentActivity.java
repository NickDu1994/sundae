package com.xwing.sundae.android.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.BigRatingView;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.RatingConstant;

import java.util.HashMap;

import okhttp3.Call;

public class AddCommentActivity extends AppCompatActivity {

    public int mainRate = 0;
//    public int tasteRate = 0;
//    public int envRate = 0;
//    public int serviceRate = 0;
    public String content = "";
    public String idString;
    private Handler handler = new Handler();

    UserInfo userInfo;

    GetUserInfo getUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        getUserInfo = new GetUserInfo(this);
        if (null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
        }

        idString = getIntent().getStringExtra("id");

        //声明四个打分条
        XLHRatingBar xlhRatingBar = (XLHRatingBar) findViewById(R.id.ratingBar_main);
        xlhRatingBar.setRatingView(new BigRatingView());

//        XLHRatingBar xlhRatingBarTaste = (XLHRatingBar) findViewById(R.id.ratingBar_taste);
//        xlhRatingBarTaste.setRatingView(new SmileRatingView());
//
//        XLHRatingBar xlhRatingBarEnv = (XLHRatingBar) findViewById(R.id.ratingBar_env);
//        xlhRatingBarEnv.setRatingView(new SmileRatingView());
//
//        XLHRatingBar xlhRatingBarService = (XLHRatingBar) findViewById(R.id.ratingBar_service);
//        xlhRatingBarService.setRatingView(new SmileRatingView());

        //声明4个打分描述
        final TextView mainRateDescView = findViewById(R.id.total_rate_desc);
//        final TextView tasteRateDescView = findViewById(R.id.taste_rate_desc);
//        final TextView envRateDescView = findViewById(R.id.env_rate_desc);
//        final TextView serviceRateDescView = findViewById(R.id.service_rate_desc);
        //四个打分的监听
        xlhRatingBar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(float rating, int numStars) {
                mainRateDescView.setText(getRateDesc(rating));
                mainRate = (int) Math.ceil(rating);
            }
        });

//        xlhRatingBarTaste.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
//            @Override
//            public void onChange(float rating, int numStars) {
//                tasteRateDescView.setText(getRateDesc(rating));
//                tasteRate = (int) Math.ceil(rating);
//            }
//        });
//
//        xlhRatingBarEnv.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
//            @Override
//            public void onChange(float rating, int numStars) {
//                envRateDescView.setText(getRateDesc(rating));
//                envRate = (int) Math.ceil(rating);
//            }
//        });
//
//        xlhRatingBarService.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
//            @Override
//            public void onChange(float rating, int numStars) {
//                serviceRateDescView.setText(getRateDesc(rating));
//                serviceRate = (int) Math.ceil(rating);
//            }
//        });

        //监听评论输入
        final EditText commentContent = findViewById(R.id.comment_content);
        final TextView wordCount = findViewById(R.id.word_count);
        commentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String content = commentContent.getText().toString();
                wordCount.setText(content.length() + "/"
                        + 100);
            }
        });

        TextView submitComment = findViewById(R.id.submit_comment);
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WriteCommentActivity.this, avg_price.getText().toString()+"-"+commentContent.getText()+"-"+mainRate+"-"+envRate+"-"+tasteRate+"-"+serviceRate, Toast.LENGTH_LONG).show();
                HashMap<String, String> paramsMap = new HashMap<>();

                Long user_id = userInfo.getId();
                paramsMap.put("userId", user_id.toString());
                paramsMap.put("rate", String.valueOf(mainRate));
                paramsMap.put("content", commentContent.getText().toString());
//                paramsMap.put("tasteRate", String.valueOf(tasteRate));
//                paramsMap.put("envRate", String.valueOf(envRate));
//                paramsMap.put("serviceRate", String.valueOf(serviceRate));
                paramsMap.put("abbrId", idString);
                submit(paramsMap);
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submit(HashMap<String, String> paramsMap) {
        String url = Constant.REQUEST_URL_MY + "/comment/post";

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(AddCommentActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(AddCommentActivity.this, "发表成功", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public String getRateDesc(float rate) {
        String result = "";
        switch ((int) Math.ceil(rate)) {
            case 1:
                result = RatingConstant.rateDesc[0];
                break;
            case 2:
                result = RatingConstant.rateDesc[1];
                break;
            case 3:
                result = RatingConstant.rateDesc[2];
                break;
            case 4:
                result = RatingConstant.rateDesc[3];
                break;
            case 5:
                result = RatingConstant.rateDesc[4];
                break;
            default:
        }
        return result;
    }
}
