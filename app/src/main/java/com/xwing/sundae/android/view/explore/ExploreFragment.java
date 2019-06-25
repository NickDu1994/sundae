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
import android.widget.TextView;
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
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
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

    TextView no_text;

    private String no_text_value = "oh ho! 你没有任何发现哦~ 快去关注吧";

//    private TextView followBtn, rankBtn;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        no_text = getActivity().findViewById(R.id.no_text);
        no_text.setVisibility(View.GONE);
        xRefreshView = getActivity().findViewById(R.id.follow_list_wrapper);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.follow_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        followAdapter = new FollowAdapter(followList, getContext());
        recyclerView.setAdapter(followAdapter);

        getUserInfo = new GetUserInfo(getActivity());
        if (null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            currentUserId = userInfo.getId();
            getFollowList();
        }

        setPullandRefresh();
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
//                if (!currentUserId.equals(newId)) {
                    followList.clear();
                    currentUserId = newId;
                    getFollowList();
//                }
            }
        }
    }

//    @Override
//    public void onClick(View v) {
//        // 开启Fragment事务
//        transaction = fm.beginTransaction();
//        hideFragment(transaction);
//
//        switch (v.getId())
//        {
//            case R.id.follow_btn:
//                if (followFragment == null)
//                {
//                    followFragment = FollowFragment.newInstance("","");
//                    transaction.add(R.id.list_container, followFragment);
//                } else {
//                    transaction.show(followFragment);
//                }
//
//                followBtn.setTextColor(getResources().getColor(R.color.colorMainTheme));
//                rankBtn.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
//                break;
//            case R.id.rank_btn:
//                if (rankTabFragment == null)
//                {
//                    rankTabFragment = RankTabFragment.newInstance("","");
//                    transaction.add(R.id.list_container, rankTabFragment);
//                } else {
//                    transaction.show(rankTabFragment);
//                }
//                //控件颜色
//                followBtn.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
//                rankBtn.setTextColor(getResources().getColor(R.color.colorMainTheme));
//                break;
//        }
//        // transaction.addToBackStack();
//        // 事务提交
//        transaction.commit();
//    }

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

    private void getFollowList() {
        String url = Constant.REQUEST_URL_MY + "/user/getExploreList";
        Long user_id = userInfo.getId();

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());

        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(), "getMyFollowList Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getFollowList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Object data = map_res.get("data");
                    String tmp = gson.toJson(data);
                    FollowModel[] myFollowModels = gson.fromJson(tmp, FollowModel[].class);
                    followList.addAll(Arrays.asList(myFollowModels));
                    if(null==followList || followList.size() == 0) {
                        no_text.setText(no_text_value);
                        xRefreshView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        xRefreshView.setLoadComplete(true);
                    } else {
                        no_text.setVisibility(View.GONE);
                    }

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
