package com.xwing.sundae.android.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.AbbreviationDetailModel;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.ImageServerConstant;
import com.xwing.sundae.android.util.OkhttpUtil;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;

import okhttp3.Call;

public class IndexDetailActivity extends AppCompatActivity {

    private ImageView backIV;
    private String currentEntryId;
    private String storageAuthorId;
    private boolean isLike = false;
    private boolean isSave = false;
    private boolean isFollow = false;
    ImageView likeIV;
    ImageView saveIV;
    TextView followTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_detail);
        likeIV = findViewById(R.id.detailLike);
        saveIV = findViewById(R.id.detailSave);
        followTV = findViewById(R.id.user_follow);
        backIV = (ImageView) findViewById(R.id.my_info_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        showDetail(bd.getString("entryId"));

        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSave){
                    handleSaveAndLike(currentEntryId, true, "save");

                }else {
                    handleSaveAndLike(currentEntryId, false, "save");

                }
            }
        });

        likeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLike){
                    handleSaveAndLike(currentEntryId, true, "like");

                }else {
                    handleSaveAndLike(currentEntryId, false, "like");

                }
            }
        });

        followTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFollow){
                    handleFollow(true);
                    followTV.setText("已关注");
                    isFollow = true;
                }else {
                    handleFollow(false);
                    followTV.setText("关注");
                    isFollow = false;
                }
            }
        });

    }

    public void showDetail(String entryId) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);

        currentEntryId = entryId;

        String url = Constant.globalServerUrl + "/abbreviation/getOneEntryDetail";
        HashMap<String, String> paramsMap = new HashMap<>();
        GetUserInfo getUserInfo = new GetUserInfo(this);
        try{
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        }catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId","");
        }
        paramsMap.put("entryId",entryId);

        Log.d("dkdebug", "request " + "paramsMap=" + paramsMap.toString());
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(IndexDetailActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(IndexDetailActivity.this,"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<AbbreviationDetailModel> responseAbbreviation =
                            (CommonResponse<AbbreviationDetailModel>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<AbbreviationDetailModel>>() {}.getType());
                    final AbbreviationDetailModel data = responseAbbreviation.getData();
                    TextView detailTitleTV = findViewById(R.id.detailTitle);
                    detailTitleTV.setText(data.getAbbreviation().getAbbrName());
                    TextView detailFullnameTV = findViewById(R.id.detailFullname);
                    detailFullnameTV.setText(data.getAbbreviation().getFullName());

                    HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.html_text);
                    htmlTextView.setHtml(data.getAbbreviation().getContent(),
                            new HtmlHttpImageGetter(htmlTextView));

                    TextView createTimeTV =  findViewById(R.id.create_time);
                    createTimeTV.setText(CommonMethod.CalculateTimeUntilNow(data.getAbbreviation().getCreateTime()));
                    ImageView userPicIV =  findViewById(R.id.user_pic);
                    Glide.with( IndexDetailActivity.this).load(ImageServerConstant.IMAGE_SERVER_URL + data.getAvatar()).into(userPicIV);
                    TextView authorTV =  findViewById(R.id.author);
                    authorTV.setText(data.getAuthor());
                    storageAuthorId = data.getAbbreviation().getCreateBy();
                    TextView likeTV =  findViewById(R.id.like_number);
                    likeTV.setText(data.getAbbreviation().getLikedCount() + "获赞");


                    if(data.isLike()){
                        likeIV.setImageResource(R.drawable.like);
                        isLike = true;
                    }else {
                        likeIV.setImageResource(R.drawable.dislike);
                        isLike = false;
                    }

                    if(data.isCollect()){
                        saveIV.setImageResource(R.drawable.heart_fill);
                        isSave = true;
                    }else {
                        saveIV.setImageResource(R.drawable.heart);
                        isSave = false;
                    }

                    if(data.isFollow()){
                        followTV.setText("已关注");
                    }else {
                        followTV.setText("关注");
                    }

                    if(data.getAbbreviation().getImageList().size() >= 1){
                        ImageView detailMainImage =  findViewById(R.id.detailMainImage);
                        Glide.with( IndexDetailActivity.this).load(ImageServerConstant.IMAGE_SERVER_URL + data.getAbbreviation().getImageList().get(0).getPath()).into(detailMainImage);
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });

    }

    public void handleSaveAndLike(final String entryId, boolean isEnroll, String type) {
        String url= "";
        final boolean finalIsEnroll = isEnroll;
        final String finalType = type;
        if("like".equals(type)){
            if(isEnroll){
                url = Constant.globalServerUrl + "/abbreviation/like";
            }else {
                url = Constant.globalServerUrl + "/abbreviation/removeLike";
            }
        }else {
            if(isEnroll){
                url = Constant.globalServerUrl + "/collection/abbreviation";
            }else {
                url = Constant.globalServerUrl + "/collection/removeCollect";
            }
        }

        final String tempLogUrl = url;

        GetUserInfo getUserInfo = new GetUserInfo( IndexDetailActivity.this);
        if(!getUserInfo.isUserLogined())   {
            Toast.makeText( IndexDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent( IndexDetailActivity.this, LoginActivity.class));
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        try{
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        }catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId","");
        }
        paramsMap.put("entryId",entryId);
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText( IndexDetailActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", tempLogUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText( IndexDetailActivity.this,"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", tempLogUrl + "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {}.getType());
                    if(saveResult.getStatus() == 200){
                        Log.d("dkdebug","save success");
                        if("like".equals(finalType)){
                            if(finalIsEnroll){
                                likeIV.setImageResource(R.drawable.like);
                                isLike = false;
                            }else {
                                likeIV.setImageResource(R.drawable.dislike);
                                isLike = true;
                            }
                        }else {
                            if(finalIsEnroll){
                                saveIV.setImageResource(R.drawable.heart_fill);
                                isSave = true;
                            }else {
                                saveIV.setImageResource(R.drawable.heart);
                                isSave = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void handleFollow(boolean isEnroll) {
        String url= "";
        if(isEnroll){
            url = Constant.globalServerUrl + "/follow/follow";
        }else {
            url = Constant.globalServerUrl + "/follow/remove";
        }
        final String logUrl = url;

        GetUserInfo getUserInfo = new GetUserInfo( IndexDetailActivity.this);
        if(!getUserInfo.isUserLogined())   {
            Toast.makeText( IndexDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent( IndexDetailActivity.this, LoginActivity.class));
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        try{
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        }catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId","");
        }
        paramsMap.put("followedUserId", storageAuthorId);
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText( IndexDetailActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", logUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText( IndexDetailActivity.this,"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", logUrl + "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {}.getType());
                    if(saveResult.getStatus() == 200){
                        Log.d("dkdebug","save success");
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }
}
