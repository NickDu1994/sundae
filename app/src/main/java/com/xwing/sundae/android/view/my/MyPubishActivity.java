package com.xwing.sundae.android.view.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
     * 关注界面用到的adapter
     */
    private MyPublishAdapter myPublishAdapter;
    /**
     * 数据源
     */
    private List<MyPublishModel> publishList = new ArrayList<>();;
    /**
     * header标题
     */
    TextView header_title;

    UserInfo userInfo;

    GetUserInfo getUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pubish);

        getUserInfo = new GetUserInfo(this);
        if(null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            getMyPublishList();
        }

        header_title = findViewById(R.id.header_title);
        header_title.setText("我的发布");

        recyclerView = (RecyclerView) findViewById(R.id.publish_rv);

    }

    private void getMyPublishListMock() {
//        for (int i = 0; i < 20; i++) {
//            MyPublishModel myPublishModel = new MyPublishModel(
//                    "2016-06-16",
//                    "111",
//                    "词条",
//                    "2019-1-1",
//                    "bbbb",
//                    "ccc",
//                    "10"
//            );
//            publishList.add(myPublishModel);
//        }
    }

    private void getMyPublishList() {
        String url = Constant.REQUEST_URL_MY + "/userPublish/getAllPublish";
        Long user_id = userInfo.getId();

        HashMap<String,String> paramsMap = new HashMap<>();
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

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    MyPublishModel[] myPublishModels = gson.fromJson(data.toString(), MyPublishModel[].class);
                    publishList.addAll(Arrays.asList(myPublishModels));

                    afterResponse(publishList);

                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void afterResponse(List<MyPublishModel> publishList) {
        myPublishAdapter = new MyPublishAdapter(publishList, MyPubishActivity.this);
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
