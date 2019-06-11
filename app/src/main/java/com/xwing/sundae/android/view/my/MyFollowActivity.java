package com.xwing.sundae.android.view.my;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.MyFollowerInfoAdapter;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.MyFollowerModel;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
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
    private static final String REQUEST_URL_MY = "http://192.168.31.17:8080/user";

    private static final String TAG = "MAGGIE";
    /**
     * list view
     */
    private RecyclerView recyclerView;
//    private ComplexListForMyAdapter mAdapter;
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

    TextView header_title;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);

        header_title = findViewById(R.id.header_title);
        header_title.setText("我的关注");

        xRefreshView = findViewById(R.id.follower_list_wrapper);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        //获得页面数据
//        getFollowList();
        getMyFollowList();
        setPullandRefresh();

        myFollowerInfoAdapter = new MyFollowerInfoAdapter(followList, this);

        // 添加删除(取消关注)监听器
        myFollowerInfoAdapter.setOnDelListener(new MyFollowerInfoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < followList.size()) {
                    Long remove_userid = followList.get(pos).getFollow_user_id();
                    // call remove api
//                    removeFollower(remove_userid);
                    Toast.makeText(MyFollowActivity.this, "取消关注:" + pos, Toast.LENGTH_SHORT).show();
                    followList.remove(pos);
                    myFollowerInfoAdapter.notifyItemRemoved(pos);//推荐用这个
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myFollowerInfoAdapter);

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

    private void getFollowList() {
        initMockData();

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
                    "http://pic26.nipic.com/20130116/1773545_152734135000_2.jpg",
                    "关注于  2018年11月13日",
                    "发布数 20",
                    "获赞数 400"
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
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(false);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                //super.onRefresh(isPullDown);
                xRefreshView.setLoadComplete(false);
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
        String url = REQUEST_URL_MY + "/follow";
        GetUserInfo userInfo = new GetUserInfo(this);
        Long user_id = userInfo.getUserInfo().getData().getId();

        HashMap paramsMap = new HashMap<>();
        paramsMap.put("userId",user_id);

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
                    Map<String,Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    MyFollowerModel[] myFollowerModels = gson.fromJson(data.toString(), MyFollowerModel[].class);
                    List<MyFollowerModel> myFollowerModels1 = Arrays.asList(myFollowerModels);

//                    CommonResponse<MyFollowerModel> myFollowerModel =
//                            (CommonResponse<MyFollowerModel>) gson.fromJson(map_res.get("data").toString(), new TypeToken<CommonResponse<MyFollowerModel>>() {
//                            }.getType());
//                    followList.add(myFollowerModel.getData());
//                    List<MyFollowerModel> list = gson.fromJson(map_res.get("data"), List.class);
                    Log.e("loginPostRequest","getFollowList");
                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void removeFollower(String remove_userid) {
        String url = "http://10.0.2.2:3001/getMyFollowList";
        OkhttpUtil.okHttpPostJson(url, "", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyFollowActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(MyFollowActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
