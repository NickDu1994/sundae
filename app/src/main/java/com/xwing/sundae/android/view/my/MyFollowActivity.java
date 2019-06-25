package com.xwing.sundae.android.view.my;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.MyFollowerInfoAdapter;
import com.xwing.sundae.android.model.MyFollowerModel;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.view.GetUserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 *
 */
public class MyFollowActivity extends AppCompatActivity {

    private static final String TAG = "MAGGIEMyFollow";
    /**
     * list view
     */
    private RecyclerView recyclerView;
    /**
     * 关注界面用到的adapter
     */
    private MyFollowerInfoAdapter myFollowerInfoAdapter;
    private LinearLayoutManager mLayoutManager;
    /**
     * 数据源
     */
    private List<MyFollowerModel> followList = new ArrayList<>();
    /**
     * handler
     */
    private Handler handler = new Handler();
    /**
     * refresh包裹的layout
     */
    XRefreshView xRefreshView;

    /**
     * header标题
     */
    TextView header_title, no_text;

    UserInfo userInfo;

    GetUserInfo getUserInfo;

    private String no_text_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);

        getUserInfo = new GetUserInfo(this);
        if (null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            getMyFollowList();
        }

        header_title = findViewById(R.id.header_title);
        header_title.setText("我的关注");
        no_text = findViewById(R.id.no_text);
        no_text.setVisibility(View.GONE);
        no_text_value = "oh ho! 你还没有任何关注哦~";

        xRefreshView = findViewById(R.id.follower_list_wrapper);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        setPullandRefresh();
    }

    private void getFollowList() {
//        initMockData();
        getMyFollowList();

        handler.post(new Runnable() {
            @Override
            public void run() {
                myFollowerInfoAdapter.notifyDataSetChanged();
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();

            }
        });
    }

    private void initMockData() {
        for (int i = 0; i < 20; i++) {
            MyFollowerModel follow = new MyFollowerModel(
                    (long) 100,
                    "Maggie",
                    null,
                    null,
                    null,
                    null
            );
            followList.add(follow);
        }
        Gson gson = new Gson();

        Log.e("maggieTestFollow", gson.toJson(followList));

    }

    private void setPullandRefresh() {
        xRefreshView.setPinnedTime(1000);
        //如果刷新时不想让里面的列表滑动，可以这么设置
        xRefreshView.setPinnedContent(false);
        xRefreshView.setMoveForHorizontal(true);
        //允许下拉刷新
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(false);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                //super.onRefresh(isPullDown);
                xRefreshView.setLoadComplete(false);
                followList.clear();
                getFollowList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFollowList();
                    }
                }, 2000);
            }
        });
    }

    private void getMyFollowList() {
        String url = Constant.REQUEST_URL_MY + "/follow/follow";
        Long user_id = userInfo.getId();

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());

        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyFollowActivity.this, "getMyFollowList Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(MyFollowActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getFollowList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    String json_data = gson.toJson(data);

                    MyFollowerModel[] myFollowerModels = gson.fromJson(json_data, MyFollowerModel[].class);
                    followList.addAll(Arrays.asList(myFollowerModels));
                    if (null == followList || followList.size() == 0) {
                        no_text.setText(no_text_value);
                        recyclerView.setVisibility(View.GONE);
                        xRefreshView.setLoadComplete(true);
                    } else {
                        no_text.setVisibility(View.GONE);
                    }

                    afterResponse(followList);


                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void afterResponse(final List<MyFollowerModel> followList) {
        myFollowerInfoAdapter = new MyFollowerInfoAdapter(followList, MyFollowActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(MyFollowActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myFollowerInfoAdapter);

        // 添加删除(取消关注)监听器
        myFollowerInfoAdapter.setOnDelListener(new MyFollowerInfoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < followList.size()) {
                    Long remove_userid = followList.get(pos).getFollow_user_id();
                    Long user_id = userInfo.getId();
                    // call remove api
                    removeFollower(user_id, remove_userid, pos);
                    followList.remove(pos);

                    Toast.makeText(MyFollowActivity.this, "取消关注:" + pos, Toast.LENGTH_SHORT).show();

                }
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });
    }


    private void removeFollower(Long user_id, Long remove_userid, final int pos) {
        String url = Constant.REQUEST_URL_MY + "/follow/remove";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());
        paramsMap.put("followedUserId", remove_userid.toString());

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyFollowActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                myFollowerInfoAdapter.notifyItemRemoved(pos);//推荐用这个
                Toast.makeText(MyFollowActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
