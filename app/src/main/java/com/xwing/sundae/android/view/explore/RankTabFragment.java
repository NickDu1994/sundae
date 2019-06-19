package com.xwing.sundae.android.view.explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xwing.sundae.R;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RankTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankTabFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RankFragment weekRankFragment;
    private RankFragment monthRankFragment;
    private RankFragment totalRankFragment;

    private TextView weekBtn, monthBtn, totalBtn;
    private View weekLine, monthLine, totalLine;

    public RankTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankTabFragment newInstance(String param1, String param2) {
        RankTabFragment fragment = new RankTabFragment();
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
        Log.v("danlog", "this is oncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("danlog", "this is onCreateView");
        return inflater.inflate(R.layout.fragment_rank_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        weekBtn = (TextView) getActivity().findViewById(R.id.week_btn);
        monthBtn = (TextView) getActivity().findViewById(R.id.month_btn);
        totalBtn = (TextView) getActivity().findViewById(R.id.total_btn);

        weekLine = (View) getActivity().findViewById(R.id.week_line);
        monthLine = (View) getActivity().findViewById(R.id.month_line);
        totalLine = (View) getActivity().findViewById(R.id.total_line);

        weekBtn.setOnClickListener(this);
        monthBtn.setOnClickListener(this);
        totalBtn.setOnClickListener(this);
        setDefaultFragment();
        Log.v("danlog", "this is onActivityCreated");
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        weekRankFragment = RankFragment.newInstance("week","");
        transaction.add(R.id.rank_container, weekRankFragment).commit();
    }

    public void activeTab(TextView textView, View view) {
        textView.setTextColor(getResources().getColor(R.color.colorMainTheme));
        textView.setTextSize(20);
        view.setVisibility(VISIBLE);
    }

    public void deactiveTab(TextView textView, View view) {
        textView.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        textView.setTextSize(16);
        view.setVisibility(INVISIBLE);
    }

    private void hideFragment(FragmentTransaction transaction){
        if (weekRankFragment != null){
            transaction.hide(weekRankFragment);
        }

        if(monthRankFragment != null){
            transaction.hide(monthRankFragment);
        }

        if(totalRankFragment != null){
            transaction.hide(totalRankFragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch (v.getId())
        {
            case R.id.week_btn:
                if (weekRankFragment == null)
                {
                    weekRankFragment = RankFragment.newInstance("week","");
                    transaction.add(R.id.rank_container, weekRankFragment);
                } else {
                    transaction.show(weekRankFragment);
                }
                //控件颜色
                activeTab(weekBtn, weekLine);
                deactiveTab(monthBtn, monthLine);
                deactiveTab(totalBtn, totalLine);
                break;
            case R.id.month_btn:
                if (monthRankFragment == null)
                {
                    monthRankFragment = RankFragment.newInstance("month","");
                    transaction.add(R.id.rank_container, monthRankFragment);
                } else {
                    transaction.show(monthRankFragment);
                }
                //控件颜色
                deactiveTab(weekBtn, weekLine);
                activeTab(monthBtn, monthLine);
                deactiveTab(totalBtn, totalLine);
                break;
            case R.id.total_btn:
                if (totalRankFragment == null)
                {
                    totalRankFragment = RankFragment.newInstance("total","");
                    transaction.add(R.id.rank_container, totalRankFragment);
                } else {
                    transaction.show(totalRankFragment);
                }
                //控件颜色
                deactiveTab(weekBtn, weekLine);
                deactiveTab(monthBtn, monthLine);
                activeTab(totalBtn, totalLine);
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
        Log.v("danlog", "this is onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.v("danlog", "this is onDetach");
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
}
