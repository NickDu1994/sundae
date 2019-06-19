package com.xwing.sundae.android.view.explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.FollowAdapter;
import com.xwing.sundae.android.model.FollowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements View.OnClickListener {
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
    FollowAdapter followAdapter;
    private Handler handler = new Handler();
    private FollowFragment followFragment;
    private RankTabFragment rankTabFragment;

    FragmentManager fm;
    FragmentTransaction transaction;

    private TextView followBtn, rankBtn;

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

    private void setDefaultFragment() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        followFragment = FollowFragment.newInstance("","");
        transaction.add(R.id.list_container, followFragment).commit();
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

//        xRefreshView = getActivity().findViewById(R.id.follow_list_wrapper);
//        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.follow_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
//            @Override
//            public boolean canScrollVertically() {
//                return true;
//            }
//        });
//        recyclerView.setHasFixedSize(true);
//        followAdapter = new FollowAdapter(followList, getContext());
//        recyclerView.setAdapter(followAdapter);
//
//        getFollowList();
//        setPullandRefresh();
        followBtn = (TextView) getActivity().findViewById(R.id.follow_btn);
        rankBtn = (TextView) getActivity().findViewById(R.id.rank_btn);
        //控件颜色
        followBtn.setTextColor(getResources().getColor(R.color.colorMainTheme));
        rankBtn.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        followBtn.setOnClickListener(this);
        rankBtn.setOnClickListener(this);
        setDefaultFragment();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (followFragment != null){
            transaction.hide(followFragment);
        }

        if(rankTabFragment != null){
            transaction.hide(rankTabFragment);
        }
    }

    @Override
    public void onClick(View v) {
        // 开启Fragment事务
        transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch (v.getId())
        {
            case R.id.follow_btn:
                if (followFragment == null)
                {
                    followFragment = FollowFragment.newInstance("","");
                    transaction.add(R.id.list_container, followFragment);
                } else {
                    transaction.show(followFragment);
                }

                followBtn.setTextColor(getResources().getColor(R.color.colorMainTheme));
                rankBtn.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                break;
            case R.id.rank_btn:
                if (rankTabFragment == null)
                {
                    rankTabFragment = RankTabFragment.newInstance("","");
                    transaction.add(R.id.list_container, rankTabFragment);
                } else {
                    transaction.show(rankTabFragment);
                }
                //控件颜色
                followBtn.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                rankBtn.setTextColor(getResources().getColor(R.color.colorMainTheme));
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
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

    private void getFollowList() {
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
//        for(int i = 0; i < 10; i++){
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
        xRefreshView.setPullLoadEnable(true);
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
