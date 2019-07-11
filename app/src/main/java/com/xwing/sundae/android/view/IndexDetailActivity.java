package com.xwing.sundae.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.CommentAdapter;
import com.xwing.sundae.android.customview.FScrollView;
import com.xwing.sundae.android.model.AbbreviationDetailModel;
import com.xwing.sundae.android.model.CommentModel;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.ImageServerConstant;
import com.xwing.sundae.android.util.OkhttpUtil;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class IndexDetailActivity extends AppCompatActivity {

    private ImageView backIV;
    private String currentEntryId;
    private String storageAuthorId;
    private int totalCommentNumber;
    private boolean isLike = false;
    private boolean isSave = false;
    private boolean isFollow = false;
    private boolean firstStart = true;
    ImageView likeIV;
    ImageView saveIV;
    TextView followTV;
    TextView collectNumber;
    TextView addComment;
    TextView totalCount;
    GetUserInfo getUserInfo;
    FScrollView scrollView;
    LinearLayout viewMore;
    LinearLayout commentEmpty;

    private List<CommentModel> commentList = new ArrayList<>();
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    private Handler handler = new Handler();

    String storageUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_detail);

        getUserInfo = new GetUserInfo(IndexDetailActivity.this);
        if (null != getUserInfo.getUserInfo() && !"".equals(getUserInfo.getUserInfo())) {
            storageUserId = getUserInfo.getUserInfo().getData().getId().toString();
        } else {
            storageUserId = "";
        }

        likeIV = findViewById(R.id.detailLike);
        saveIV = findViewById(R.id.detailSave);
        followTV = findViewById(R.id.user_follow);
        backIV = (ImageView) findViewById(R.id.my_info_back);
        addComment = (TextView) findViewById(R.id.add_comment);
        totalCount = (TextView) findViewById(R.id.comment_count_title);
        viewMore = (LinearLayout) findViewById(R.id.view_more_comment);
        commentEmpty = (LinearLayout) findViewById(R.id.comment_empty);

        final RelativeLayout rl = findViewById(R.id.info_header);
        rl.getBackground().setAlpha(100);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        showDetail(bd.getString("entryId"));
        getComments(bd.getString("entryId"));

        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSave) {
                    handleSaveAndLike(currentEntryId, true, "save");

                } else {
                    handleSaveAndLike(currentEntryId, false, "save");

                }
            }
        });

        likeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLike) {
                    handleSaveAndLike(currentEntryId, true, "like");

                } else {
                    handleSaveAndLike(currentEntryId, false, "like");

                }
            }
        });

        followTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFollow) {
                    handleFollow(true);
                } else {
                    handleFollow(false);
                }
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IndexDetailActivity.this, AddCommentActivity.class);
                intent.putExtra("id", currentEntryId);
                startActivity(intent);
            }
        });

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IndexDetailActivity.this, CommentListActivity.class);
                intent.putExtra("id", currentEntryId);
                startActivity(intent);
            }
        });


        scrollView = findViewById(R.id.detail_scroll);
        scrollView.setOnScrollListener(new FScrollView.OnScrollListener() {
            @Override
            public void onScroll(int t) {
                float percent = (float) (t) / 300f;
                if (t < 300) {
                    rl.getBackground().setAlpha((int) (155 * percent + 100));
                    Log.d("dandan", "onScroll" + (int) (255 * percent));
                } else {
                    rl.getBackground().setAlpha(255);
                }
                Log.d("dandan", String.valueOf(t));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.comment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        commentAdapter = new CommentAdapter(commentList, this);
        recyclerView.setAdapter(commentAdapter);
    }

    public void showDetail(String entryId) {
        currentEntryId = entryId;

        String url = Constant.globalServerUrl + "/abbreviation/getOneEntryDetail";
        HashMap<String, String> paramsMap = new HashMap<>();

        try {
            if (getUserInfo.isUserLogined()) {
                paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
            } else {
                paramsMap.put("userId", "");
                followTV.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId", "");
            followTV.setVisibility(View.INVISIBLE);
        }
        paramsMap.put("entryId", entryId);

        Log.d("dkdebug", "request " + "paramsMap=" + paramsMap.toString());
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
//                if (Constant.LOG_LEVEL == "DEV") {
                Toast.makeText(IndexDetailActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
//                }
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if (Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(IndexDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try {
                    CommonResponse<AbbreviationDetailModel> responseAbbreviation =
                            (CommonResponse<AbbreviationDetailModel>) gson.fromJson(response,
                                    new TypeToken<CommonResponse<AbbreviationDetailModel>>() {
                                    }.getType());
                    final AbbreviationDetailModel data = responseAbbreviation.getData();
                    TextView detailTitleTV = findViewById(R.id.detailTitle);
                    detailTitleTV.setText(data.getAbbreviation().getAbbrName());
                    TextView detailFullnameTV = findViewById(R.id.detailFullname);
                    detailFullnameTV.setText(data.getAbbreviation().getFullName());

                    HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.html_text);
                    htmlTextView.setHtml(data.getAbbreviation().getContent(),
                            new HtmlHttpImageGetter(htmlTextView));

                    TextView createTimeTV = findViewById(R.id.create_time);
                    createTimeTV.setText(CommonMethod.CalculateTimeUntilNow(data.getAbbreviation().getCreateTime()));
                    ImageView userPicIV = findViewById(R.id.user_pic);
                    RequestOptions options = new RequestOptions().
                            circleCropTransform();
                    if (null == data.getAvatar() || "".equals(data.getAvatar())) {
                        Glide.with(IndexDetailActivity.this).load(R.drawable.defaultpic_theme)
                                .apply(options).into(userPicIV);
                    } else {
                        Glide.with(IndexDetailActivity.this).load(ImageServerConstant.IMAGE_SERVER_URL + data.getAvatar())
                                .apply(options).into(userPicIV);
                    }
                    TextView authorTV = findViewById(R.id.author);
                    authorTV.setText(data.getAuthor());
                    storageAuthorId = data.getAbbreviation().getCreateBy();
                    TextView likeTV = findViewById(R.id.like_number);
                    likeTV.setText(data.getAbbreviation().getLikedCount());
                    collectNumber = findViewById(R.id.collect_number);
                    collectNumber.setText(data.getCollectNumber());
                    if (data.isLike()) {
                        likeIV.setImageResource(R.drawable.like);
                        isLike = true;
                    } else {
                        likeIV.setImageResource(R.drawable.dislike);
                        isLike = false;
                    }

                    if (data.isCollect()) {
                        saveIV.setImageResource(R.drawable.heart_fill);
                        isSave = true;
                    } else {
                        saveIV.setImageResource(R.drawable.heart);
                        isSave = false;
                    }

                    if (data.getAbbreviation().getCreateBy().equals(storageUserId)) {
                        followTV.setVisibility(View.INVISIBLE);
                    } else {
                        if (data.isFollow()) {
                            followTV.setText("已关注");
                            followTV.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                            followTV.setBackgroundResource(R.drawable.custom_round_button_dark);
                            isFollow = true;
                        } else {
                            followTV.setText("+关注");
                            followTV.setTextColor(getResources().getColor(R.color.colorMainTheme));
                            followTV.setBackgroundResource(R.drawable.custom_round_button);
                            isFollow = false;
                        }
                    }

                    if (data.getAbbreviation().getImageList().size() >= 1) {
                        ImageView detailMainImage = findViewById(R.id.detailMainImage);
                        Glide.with(IndexDetailActivity.this).load(ImageServerConstant.IMAGE_SERVER_URL + data.getAbbreviation().getImageList().get(0).getPath()).into(detailMainImage);
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });

    }

    public void getComments(String entryId) {
        String url = Constant.REQUEST_URL_MY + "/comment/getCommentListTwo";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("abbrId", entryId);
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(IndexDetailActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if(!Constant.LOG_LEVEL.equals("PRD")) {
                    Toast.makeText(IndexDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getCommentList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    LinkedTreeMap data = (LinkedTreeMap) map_res.get("data");
                    Object content = data.get("content");
                    double total = (double) data.get("totalElements");
                    totalCommentNumber = (int) total;
                    totalCount.setText("网友点评（" + String.valueOf(totalCommentNumber) + "）");
                    if (totalCommentNumber == 0) {
                        viewMore.setVisibility(View.GONE);
                        commentEmpty.setVisibility(View.VISIBLE);
                    } else {
                        viewMore.setVisibility(View.VISIBLE);
                        commentEmpty.setVisibility(View.GONE);
                    }
                    String tmp = gson.toJson(content);
                    CommentModel[] commentModels = gson.fromJson(tmp, CommentModel[].class);
                    commentList.addAll(Arrays.asList(commentModels));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commentAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }
    public void handleSaveAndLike(final String entryId, boolean isEnroll, String type) {
        String url = "";
        final boolean finalIsEnroll = isEnroll;
        final String finalType = type;
        if ("like".equals(type)) {
            if (isEnroll) {
                url = Constant.globalServerUrl + "/abbreviation/like";
            } else {
                url = Constant.globalServerUrl + "/abbreviation/removeLike";
            }
        } else {
            if (isEnroll) {
                url = Constant.globalServerUrl + "/collection/abbreviation";
            } else {
                url = Constant.globalServerUrl + "/collection/removeCollect";
            }
        }

        final String tempLogUrl = url;


        if (!getUserInfo.isUserLogined()) {
            Toast.makeText(IndexDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(IndexDetailActivity.this, LoginActivity.class));
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        try {
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        } catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId", "");
        }
        paramsMap.put("entryId", entryId);
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
//                if (Constant.LOG_LEVEL == "DEV") {
                Toast.makeText(IndexDetailActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
//                }
                Log.d("dkdebug onFailure", tempLogUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if (Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(IndexDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", tempLogUrl + "response" + response);
                Gson gson = new Gson();
                try {
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>) gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {
                                    }.getType());
                    if (saveResult.getStatus() == 200) {
                        Log.d("dkdebug", "save success");
                        TextView likeTV = findViewById(R.id.like_number);
                        if ("like".equals(finalType)) {
                            String currentNum = likeTV.getText().toString();
                            int resultNum;
                            if (finalIsEnroll) {
                                likeIV.setImageResource(R.drawable.like);
                                resultNum = Integer.valueOf(currentNum) + 1;
                                isLike = true;
                            } else {
                                likeIV.setImageResource(R.drawable.dislike);
                                resultNum = Integer.valueOf(currentNum) - 1;
                                isLike = false;
                            }
                            likeTV.setText(String.valueOf(resultNum));
                        } else {
                            String currentNum = collectNumber.getText().toString();
                            int resultNum;
                            if (finalIsEnroll) {
                                saveIV.setImageResource(R.drawable.heart_fill);
                                resultNum = Integer.valueOf(currentNum) + 1;
                                isSave = true;
                            } else {
                                saveIV.setImageResource(R.drawable.heart);
                                resultNum = Integer.valueOf(currentNum) - 1;
                                isSave = false;
                            }
                            collectNumber.setText(String.valueOf(resultNum));
                        }
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void handleFollow(boolean isEnroll) {
        String url = "";
        if (isEnroll) {
            url = Constant.globalServerUrl + "/follow/follow";
        } else {
            url = Constant.globalServerUrl + "/follow/remove";
        }
        final String logUrl = url;
        final boolean finalIsEnroll = isEnroll;

        GetUserInfo getUserInfo = new GetUserInfo(IndexDetailActivity.this);
        if (!getUserInfo.isUserLogined()) {
            Toast.makeText(IndexDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(IndexDetailActivity.this, LoginActivity.class));
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        try {
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        } catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId", "");
        }
        paramsMap.put("followedUserId", storageAuthorId);
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
//                if (Constant.LOG_LEVEL == "DEV") {
                Toast.makeText(IndexDetailActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
//                }
                Log.d("dkdebug onFailure", logUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if (Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(IndexDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", logUrl + "response" + response);
                Gson gson = new Gson();
                try {
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>) gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {
                                    }.getType());
                    if (saveResult.getStatus() == 200) {
                        Log.d("dkdebug", "save success");

                        if (finalIsEnroll) {
                            followTV.setText("已关注");
                            followTV.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                            followTV.setBackgroundResource(R.drawable.custom_round_button_dark);
                            isFollow = true;
                        } else {
                            followTV.setText("+关注");
                            followTV.setTextColor(getResources().getColor(R.color.colorMainTheme));
                            followTV.setBackgroundResource(R.drawable.custom_round_button);
                            isFollow = false;
                        }

                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstStart == true) {
            firstStart = false;
        } else {
            Intent intent = getIntent();
            Bundle bd = intent.getExtras();
//            Toast.makeText(FoodDetailsActivity.this, "我回来啦",Toast.LENGTH_LONG).show();
            commentList.clear();
            getComments(bd.getString("entryId"));
        }
    }
}
