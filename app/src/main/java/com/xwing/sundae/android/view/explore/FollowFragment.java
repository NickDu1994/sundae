package com.xwing.sundae.android.view.explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.FollowAdapter;
import com.xwing.sundae.android.model.FollowModel;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<FollowModel> followList = new ArrayList<>();

    XRefreshView xRefreshView;
    RecyclerView recyclerView;
    FollowAdapter followAdapter;
    private Handler handler = new Handler();
    UserInfo userInfo;
    GetUserInfo getUserInfo;
    Long currentUserId;

    public FollowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowFragment newInstance(String param1, String param2) {
        FollowFragment fragment = new FollowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        xRefreshView = getActivity().findViewById(R.id.follow_list_wrapper);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.follow_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        followAdapter = new FollowAdapter(followList, getContext());
        recyclerView.setAdapter(followAdapter);

        getUserInfo = new GetUserInfo(getActivity());
        if(null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            currentUserId = userInfo.getId();
            getFollowList();
        }

        setPullandRefresh();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("dandan","explore resume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.w("dandan", "message");
        if (hidden) {

        } else {
            if (null != getUserInfo) {
                userInfo = getUserInfo.getUserInfo().getData();
                Long newId = userInfo.getId();
                    followList.clear();
                    currentUserId = newId;
                    getFollowList();
            }
        }
    }

    private void getFollowList() {
        String url = Constant.REQUEST_URL_MY + "/user/getExploreList";
        Long user_id = userInfo.getId();

        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());

        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(), "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if(!Constant.LOG_LEVEL.equals("PRD")) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getFollowList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    String tmp = gson.toJson(data);
                    FollowModel[] myFollowModels = gson.fromJson(tmp, FollowModel[].class);
                    followList.addAll(Arrays.asList(myFollowModels));

//                    afterResponse(followList);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            followAdapter.notifyDataSetChanged();
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

//    private void getFollowList() {
//        initMockData();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                followAdapter.notifyDataSetChanged();
//                xRefreshView.stopRefresh();
//                xRefreshView.stopLoadMore();
//
//            }
//        });
//    }

    public void initMockData() {
//        for(int i = 0; i < 3; i++){
//            FollowModel follow = new FollowModel(
//                    "蛋蛋评价了一个词条",
//                    "2天前",
//                    "WTF",
//                    "This is a content for sundae app usingThis is a content for sundae app usingThis is a content for sundae app using",
//                    "https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/0e4bef5f02adf70.jpg"
//            );
//            followList.add(follow);
//        }
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
                followList.clear();
                getFollowList();
//                xRefreshView.stopRefresh();
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
}
