package com.xwing.sundae.android.view.my;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.MyFollowerInfoAdapter;
import com.xwing.sundae.android.adapter.MyPublishAdapter;
import com.xwing.sundae.android.model.MyCollectModel;
import com.xwing.sundae.android.model.MyFollowerModel;
import com.xwing.sundae.android.model.MyPublishModel;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.IndexDetailActivity;
import com.xwing.sundae.android.view.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MyPubishActivity extends AppCompatActivity {
    private static final String TAG = "MAGGIEMyPubishActivity";
    /**
     * list view
     */
    private RecyclerView recyclerView;
    /**
     * refresh包裹的layout
     */
    XRefreshView xRefreshView;
    /**
     * 关注界面用到的adapter
     */
    private MyPublishAdapter myPublishAdapter;
    /**
     * 数据源
     */
    private List<MyPublishModel> publishList = new ArrayList<>();
    ;
    /**
     * header标题
     */
    TextView header_title, no_text;
    /**
     * handler
     */
    private Handler handler = new Handler();

    UserInfo userInfo;

    GetUserInfo getUserInfo;

    private String no_text_value = "oh ho! 你还没有任何发布哦~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pubish);

        getUserInfo = new GetUserInfo(this);
        if (null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            getCollectList();
        }

        header_title = findViewById(R.id.header_title);
        header_title.setText("我的发布");

        no_text = findViewById(R.id.no_text);
        no_text.setVisibility(View.GONE);
        xRefreshView = findViewById(R.id.collect_list_wrapper);
        recyclerView = (RecyclerView) findViewById(R.id.publish_rv);
        setPullandRefresh();

    }

    private void getCollectList() {
        getMyPublishList();

        handler.post(new Runnable() {
            @Override
            public void run() {
//                myPublishAdapter.notifyDataSetChanged();
                xRefreshView.stopRefresh();
                xRefreshView.stopLoadMore();

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
                publishList.clear();
                getMyPublishList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMyPublishList();
                    }
                }, 2000);
            }
        });
    }

    private void getMyPublishList() {
        String url = Constant.REQUEST_URL_MY + "/userPublish/getAllPublish";
        Long user_id = userInfo.getId();

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());

        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyPubishActivity.this, "getMyPublishList Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(MyPubishActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getMyPublishList" + response);

                xRefreshView.stopRefresh();

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    String json_data = gson.toJson(data);

                    MyPublishModel[] myPublishModels = gson.fromJson(json_data, MyPublishModel[].class);
                    publishList.addAll(Arrays.asList(myPublishModels));
                    if(null == publishList || publishList.size() ==0) {
                        no_text.setText(no_text_value);
                        recyclerView.setVisibility(View.GONE);
                        xRefreshView.setLoadComplete(true);
                    } else {
                        no_text.setVisibility(View.GONE);
                    }

                    afterResponse(publishList);


                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void afterResponse(final List<MyPublishModel> publishList) {
        myPublishAdapter = new MyPublishAdapter(publishList, MyPubishActivity.this);
        myPublishAdapter.setOnItemClickEvent(new MyPublishAdapter.OnRecyclerItemClickListener() {
            @Override
            public void OnItemClick(View view, int postion) {
                Toast.makeText(MyPubishActivity.this, "we got click from afterresponse" + postion, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyPubishActivity.this, IndexDetailActivity.class);
                String entryId = String.valueOf(publishList.get(postion).getItem_id());
                intent.putExtra("entryId", entryId);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(MyPubishActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myPublishAdapter);
    }
}
