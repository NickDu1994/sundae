package com.xwing.sundae.android.view.my;

import android.content.Intent;
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
import com.xwing.sundae.android.adapter.MyCollectAdapter;
import com.xwing.sundae.android.model.MyCollectModel;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.IndexDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MyCollectActivity extends AppCompatActivity {
    private static final String TAG = "MAGGIE_COLLECT";
    /**
     * list view
     */
    private RecyclerView recyclerView;
    /**
     * refresh包裹的layout
     */
    XRefreshView xRefreshView;
    /**
     * 数据源
     */
    private List<MyCollectModel> collectList = new ArrayList<>();
    /**
     * handler
     */
    private Handler handler = new Handler();
    /**
     * 收藏界面用到的adapter
     */
    private MyCollectAdapter myCollectAdapter;
    /**
     * header标题
     */
    TextView header_title;

    UserInfo userInfo;

    GetUserInfo getUserInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);

        getUserInfo = new GetUserInfo(this);
        if(null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            getMyCollectList();
        }

        header_title = findViewById(R.id.header_title);
        header_title.setText("我的收藏");

        xRefreshView = findViewById(R.id.collect_list_wrapper);
        recyclerView = findViewById(R.id.collect_rv);

        setPullandRefresh();
    }

    private void getCollectList() {
//        initMockData();

        handler.post(new Runnable() {
            @Override
            public void run() {
                myCollectAdapter.notifyDataSetChanged();
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();

            }
        });
    }

    private void initMockData() {
//        for(int i = 0; i < 20; i++){
//            MyCollectModel collect = new MyCollectModel(
//                    (Long) 150_304_252_018,
//                    "",
//                    "EXCUSE ME!",
//                    "dudusuaodaodaondandsoansdpaodaopdapodaonanpasda",
//                    "Dong Sheng",
//                    "2018-09-09"
//            );
//            collectList.add(collect);
//        }
//        Gson gson = new Gson();
//        Log.e("collectList",gson.toJson(collectList));
    }

    private void getMyCollectList() {
        String url = Constant.REQUEST_URL_MY + "/collection/abbreviation";
        Long user_id = userInfo.getId();

        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());

        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyCollectActivity.this, "getMyFollowList Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(MyCollectActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getFollowList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    String json_data = gson.toJson(data);
                    MyCollectModel[] myCollectModels = gson.fromJson(json_data, MyCollectModel[].class);
                    collectList.addAll(Arrays.asList(myCollectModels));

                    afterResponse(collectList);

                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void afterResponse(final List<MyCollectModel> collectList) {
        myCollectAdapter = new MyCollectAdapter(collectList,this);
        // 添加删除(取消关注)监听器
        myCollectAdapter.setOnDelListener(new MyCollectAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < collectList.size()) {
                    Long remove_entryid = collectList.get(pos).getItem_id();
                    Long user_id = userInfo.getId();
                    // call remove api
                    removeCollect(user_id,remove_entryid,pos);
                    Toast.makeText(MyCollectActivity.this, "取消收藏:" + pos, Toast.LENGTH_SHORT).show();
                    collectList.remove(pos);

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myCollectAdapter);

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

    private void removeCollect(Long user_id, Long remove_userid, final int pos) {
        String url = Constant.REQUEST_URL_MY + "/collection/removeCollect";

        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());
        paramsMap.put("entryId", remove_userid.toString());

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyCollectActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                myCollectAdapter.notifyItemRemoved(pos);//推荐用这个
                Toast.makeText(MyCollectActivity.this, "Success", Toast.LENGTH_SHORT).show();

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
        xRefreshView.enableReleaseToLoadMore(false);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                //super.onRefresh(isPullDown);
                xRefreshView.setLoadComplete(false);
                getCollectList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCollectList();
                    }
                }, 2000);
            }
        });
    }
}
