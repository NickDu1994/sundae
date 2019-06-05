package com.xwing.sundae.android.view.my;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.FollowAdapter;
import com.xwing.sundae.android.model.FollowModel;

import java.util.ArrayList;
import java.util.List;

public class MyCommentActivity extends AppCompatActivity {
    /**
     * comment 列表信息
     */
    private List<FollowModel> commentList = new ArrayList<>();
    /**
     * refresh包裹的layout
     */
    XRefreshView xRefreshView;
    /**
     * 与我关注的 使用同一个Adapter
     */
    FollowAdapter followAdapter;
    /**
     * handler
     */
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);

        xRefreshView = findViewById(R.id.common_list_wrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.common_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        followAdapter = new FollowAdapter(commentList, this);
        recyclerView.setAdapter(followAdapter);

//        loadList();
        getCommentList();
        setPullandRefresh();
    }


    private void getCommentList() {
        initMockData();

        handler.post(new Runnable() {
            @Override
            public void run() {
                followAdapter.notifyDataSetChanged();
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();

            }
        });
    }

    public void initMockData() {
        for(int i = 0; i < 10; i++){
            FollowModel follow = new FollowModel(
                    "蛋蛋评价了一个词条",
                    "2天前",
                    "WTF",
                    "This is a content for sundae app usingThis is a content for sundae app usingThis is a content for sundae app using",
                    "https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/0e4bef5f02adf70.jpg"
            );
            commentList.add(follow);
        }
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
                getCommentList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCommentList();
                    }
                }, 2000);
            }
        });
    }
}
