package com.xwing.sundae.android.view.my;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.MyCollectAdapter;
import com.xwing.sundae.android.adapter.MyFollowerInfoAdapter;
import com.xwing.sundae.android.model.MyCollectModel;
import com.xwing.sundae.android.model.MyFollowerModel;

import java.util.ArrayList;
import java.util.List;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);

        xRefreshView = findViewById(R.id.collect_list_wrapper);
        recyclerView = findViewById(R.id.collect_rv);

        getCollectList();
        setPullandRefresh();

        myCollectAdapter = new MyCollectAdapter(collectList,this);
        // 添加删除(取消关注)监听器
        myCollectAdapter.setOnDelListener(new MyCollectAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < collectList.size()) {
                    String remove_userid = collectList.get(pos).getItem_id();
                    // call remove api
                    Toast.makeText(MyCollectActivity.this, "取消收藏:" + pos, Toast.LENGTH_SHORT).show();
                    collectList.remove(pos);
                    myCollectAdapter.notifyItemRemoved(pos);//推荐用这个
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

    private void getCollectList() {
        initMockData();

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
        for(int i = 0; i < 20; i++){
            MyCollectModel collect = new MyCollectModel(
                    "15604030425_2018",
                    "",
                    "EXCUSE ME!",
                    "dudusuaodaodaondandsoansdpaodaopdapodaonanpasda",
                    "Dong Sheng",
                    "2018-09-09"
            );
            collectList.add(collect);
        }
        Gson gson = new Gson();
        Log.e("maggieTestFollow",gson.toJson(collectList));
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
