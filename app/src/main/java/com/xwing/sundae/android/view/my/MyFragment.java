package com.xwing.sundae.android.view.my;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.UserInfoOneLineView;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.SharedPreferencesHelper;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static com.xwing.sundae.android.util.CommonMethod.getUserInfo;
import static com.xwing.sundae.android.util.CommonMethod.ifLogin;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment implements View.OnClickListener, UserInfoOneLineView.OnRootClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView image;

    LinearLayout user_field, list_item;
    ImageView user_pic;
    TextView user_name, user_id;

    SharedPreferencesHelper sharedPreferencesHelper;


    private OnFragmentInteractionListener mListener;


    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * 获取存在preference内信息
     *
     * @param key
     */
    private String getPreferences(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String value = preferences.getString(key, "");
        return value;
    }

    /**
     * 插入preference内值
     *
     * @param key,value
     */
    private boolean setPreferences(String key, String value) {
        Boolean isSetSucc = false;
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
            isSetSucc = true;
        } catch (Exception e) {
        } finally {
            return isSetSucc;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        SharedPreferencesHelper sharedPreferencesHelper1 = new SharedPreferencesHelper(getActivity(),"clear")
//                .clear();
        Log.v(TAG, "test");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "MyFragment");

        initView(view);
        loadPortrait();
        addChildViews();
        initEvent();


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {

        super.onResume();
//        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "MyFragment");
        Log.d("authDebug2","auth"+sharedPreferencesHelper.get("auth","").toString());


//        Log.d("authDebug",sharedPreferencesHelper.get("auth","").toString());
//        Log.d("authDebug",sharedPreferencesHelper.get("auth","").toString());
//        Log.d("maggieTest",sharedPreferencesHelper.get("user_info","").toString());
        String commonResponse = sharedPreferencesHelper.get("user_info","").toString();
        if(null!=commonResponse && !"".equals(commonResponse)) {
            CommonMethod.ifLogin(commonResponse);
        }
//        if (loginFlag()) {
//            setUserInfo();
//        } else {
//
//        }
        Log.v(TAG, "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.v(TAG, "onStart");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(TAG, "onViewStateRestored");
    }

//    private boolean loginFlag() {
//        if (sharedPreferencesHelper.get("user_info", "") == null) {
//            return false;
//        }
//
//        Log.v(TAG,sharedPreferencesHelper.get("user_info", "user_info").toString());
//
//        return CommonMethod.ifLogin(sharedPreferencesHelper.get("user_info","").toString());
//    }

    private void initEvent() {
        user_field.setOnClickListener(this);
        user_pic.setOnClickListener(this);

    }

    private void initView(View view) {
        image = (ImageView) view.findViewById(R.id.user_pic);
        user_field = (LinearLayout) view.findViewById(R.id.user_field);
        list_item = (LinearLayout) view.findViewById(R.id.user_field_detail);
        user_pic = (ImageView) view.findViewById(R.id.user_pic);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_id = (TextView) view.findViewById(R.id.user_id);

    }

//    private void setUserInfo() {
//        if (ifLogin(getPreferences("user_info"))) {
//            CommonResponse<UserInfo> userInfoCommonResponse = getUserInfo(getPreferences("user_info"));
//
//            if (null != userInfoCommonResponse.getData() && null != userInfoCommonResponse.getData().getInfo()) {
//                UserInfo info = userInfoCommonResponse.getData();
//                user_id.setText(userInfoCommonResponse.getData().getInfo().getUsername());
//                user_name.setText(userInfoCommonResponse.getData().getInfo().getNickname());
//                Glide.with(this).load(userInfoCommonResponse.getData());
//            }
//        }
//    }

    private void addChildViews() {

        UserInfoOneLineView mySent = new UserInfoOneLineView(getContext())
                .init(R.mipmap.sent, this.getString(R.string.string_my_sent), false, true)
                .setOnRootClickListener(this, "mySent");
        list_item.addView(mySent);

        UserInfoOneLineView myComment = new UserInfoOneLineView(getContext())
                .init(R.mipmap.comment, this.getString(R.string.string_my_comment), false, true)
                .setOnRootClickListener(this, "myComment");
        list_item.addView(myComment);

        UserInfoOneLineView myCollect = new UserInfoOneLineView(getContext())
                .init(R.mipmap.collect, this.getString(R.string.string_my_collect), false, true)
                .setOnRootClickListener(this, "myCollect");
        list_item.addView(myCollect);

        UserInfoOneLineView myFollow = new UserInfoOneLineView(getContext())
                .init(R.mipmap.follow, this.getString(R.string.string_my_follow), false, true)
                .setOnRootClickListener(this, "myFollow");
        list_item.addView(myFollow);

        UserInfoOneLineView suggFeedback = new UserInfoOneLineView(getContext())
                .init(R.mipmap.feedback, this.getString(R.string.string_suggestion_feedback), false, true)
                .setOnRootClickListener(this, "suggFeedback");
        list_item.addView(suggFeedback);

    }

    private void loadPortrait() {
        RequestOptions options = new RequestOptions().
                circleCropTransform();
        Glide.with(this)
                .load(R.mipmap.girl)
                .apply(options)
                .into(image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_field:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.user_pic:
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                break;
        }
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

    @Override
    public void onRootClick(View view) {
        int position = 0;
        switch ((String) view.getTag()) {
            case "mySent":
                position = 1;
                break;
            case "myComment":
                position = 2;
                break;
            case "myCollect":
                position = 3;
                break;
            case "myFollow":
                position = 4;
                break;
            case "suggFeedback":
                position = 5;
                break;
        }
        Toast.makeText(getContext(), "点击了第" + position + "行", Toast.LENGTH_SHORT).show();
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
