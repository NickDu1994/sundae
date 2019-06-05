package com.xwing.sundae.android.view.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.ComplexListForMyAdapter;
import com.xwing.sundae.android.model.SwiperBean;

import java.util.ArrayList;
import java.util.List;

public class MyFollowActivity extends AppCompatActivity {

    private static final String TAG = "MAGGIE";
    private RecyclerView mRv;
    private ComplexListForMyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<SwiperBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);
        mRv = (RecyclerView) findViewById(R.id.rv);
        initDatas();

        mAdapter = new ComplexListForMyAdapter(this, mDatas);
        mAdapter.setOnDelListener(new ComplexListForMyAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < mDatas.size()) {
                    Toast.makeText(MyFollowActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    mDatas.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < mDatas.size()) {
                    SwiperBean swiperBean = mDatas.get(pos);
                    mDatas.remove(swiperBean);
                    mAdapter.notifyItemInserted(0);
                    mDatas.add(0, swiperBean);
                    mAdapter.notifyItemRemoved(pos + 1);
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mRv.scrollToPosition(0);
                    }
                }
            }
        });
//        mRv.setLayoutManager(mLayoutManager = new GridLayoutManager(this, 1));
        mRv.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mRv.setHasFixedSize(true);
        mRv.setAdapter(mAdapter);

        mRv.setOnTouchListener(new View.OnTouchListener() {
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

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new SwiperBean("test" + i));
        }
    }
}
