package com.xwing.sundae.android.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.CommentAdapter;
import com.xwing.sundae.android.model.CommentModel;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CommentListActivity extends AppCompatActivity {
    XRefreshView xRefreshView;
    private List<CommentModel> commentList = new ArrayList<CommentModel>();
    private CommentAdapter adapter;
    private RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    private Handler handler = new Handler();
    String idString;
    Boolean isLast = false;
    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        idString = getIntent().getStringExtra("id");
        xRefreshView = findViewById(R.id.xrefreshview_poetryFragment);
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

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getComment();
        setPullandRefresh();
    }

    private void getComment() {
        String url = Constant.REQUEST_URL_MY + "/comment/getCommentList";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("abbrId", idString);
        paramsMap.put("page", Integer.toString(currentPage));
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(CommentListActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if(!Constant.LOG_LEVEL.equals("PRD")) {
                    Toast.makeText(CommentListActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getCommentList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    LinkedTreeMap data = (LinkedTreeMap) map_res.get("data");
                    Object content = data.get("content");
                    isLast = (Boolean) data.get("last");
                    String tmp = gson.toJson(content);
                    CommentModel[] commentModels = gson.fromJson(tmp, CommentModel[].class);
                    commentList.addAll(Arrays.asList(commentModels));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commentAdapter.notifyDataSetChanged();
                            xRefreshView.stopRefresh();
                            xRefreshView.stopLoadMore();
                        }
                    });

                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void setPullandRefresh() {
        xRefreshView.setPinnedTime(1000);
        //如果刷新时不想让里面的列表滑动，可以这么设置
        xRefreshView.setPinnedContent(false);
        xRefreshView.setMoveForHorizontal(true);
        //允许下拉刷新
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
//        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xRefreshView.enableReleaseToLoadMore(false);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(false);
//        xRefreshView.setEmptyView(R.layout.layout_empty_view);//添加empty_view

        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                //super.onRefresh(isPullDown);
                xRefreshView.setLoadComplete(false);
                commentList.clear();
                currentPage = 0;
                isLast = false;
                getComment();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLast) {
                            currentPage++;
                            getComment();
                        } else {
                            xRefreshView.stopLoadMore();
                        }
                    }
                }, 2000);
            }
        });
    }
}
