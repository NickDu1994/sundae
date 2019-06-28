package com.xwing.sundae.android.view.my;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.UserInfoOneLineView;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.ImageServerConstant;
import com.xwing.sundae.android.util.SharedPreferencesHelper;
import com.xwing.sundae.android.util.SharedPreferencesUtil;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;

import static android.support.constraint.Constraints.TAG;

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

    RelativeLayout user_info_show;

    SharedPreferencesHelper sharedPreferencesHelper;

    LinearLayout user_field, list_item;
    ImageView user_pic, user_setting;
    TextView user_name, user_id,id_label;

    GetUserInfo getUserInfo;
    UserInfo userInfo;

    SharedPreferencesUtil spUtil;


    private OnFragmentInteractionListener mListener;

    // set the user all pics as circle && placeholder
    RequestOptions options = new RequestOptions().error(R.drawable.defaultpic_white).circleCropTransform();

    public MyFragment() {
        // Required empty public constructor
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
        Log.v(TAG, "success dk");
        getUserInfo = new GetUserInfo(getActivity());
        spUtil = SharedPreferencesUtil.getInstance(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        sharedPreferencesHelper =
                new SharedPreferencesHelper(getActivity(), "user");

        initView(view);
        addChildViews();
        setUserInfo();
        initEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("dandan","explore resume");
        setUserInfo();
    }

    private void initEvent() {
        user_field.setOnClickListener(this);
        user_pic.setOnClickListener(this);
        user_setting.setOnClickListener(this);

    }

    private void initView(View view) {

        image = (ImageView) view.findViewById(R.id.user_pic);
        user_field = (LinearLayout) view.findViewById(R.id.user_field);
        list_item = (LinearLayout) view.findViewById(R.id.user_field_detail);
        user_pic = (ImageView) view.findViewById(R.id.user_pic);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_id = (TextView) view.findViewById(R.id.user_id);
        user_setting = (ImageView) view.findViewById(R.id.user_setting);
        user_info_show = (RelativeLayout) view.findViewById(R.id.user_info_show);
        id_label = (TextView) view.findViewById(R.id.id_label);

    }

    /**
     * 分类
     */
    private void addChildViews() {

        UserInfoOneLineView mySent = new UserInfoOneLineView(getContext())
                .init(R.drawable.sent, this.getString(R.string.string_my_sent), false, true)
                .setOnRootClickListener(this, "mySent");
        list_item.addView(mySent);

//        UserInfoOneLineView myComment = new UserInfoOneLineView(getContext())
//                .init(R.drawable.comment, this.getString(R.string.string_my_comment), false, true)
//                .setOnRootClickListener(this, "myComment");
//        list_item.addView(myComment);

        UserInfoOneLineView myCollect = new UserInfoOneLineView(getContext())
                .init(R.drawable.collect, this.getString(R.string.string_my_collect), false, true)
                .setOnRootClickListener(this, "myCollect");
        list_item.addView(myCollect);

        UserInfoOneLineView myFollow = new UserInfoOneLineView(getContext())
                .init(R.drawable.follow, this.getString(R.string.string_my_follow), false, true)
                .setOnRootClickListener(this, "myFollow");
        list_item.addView(myFollow);

//        UserInfoOneLineView suggFeedback = new UserInfoOneLineView(getContext())
//                .init(R.drawable.feedback, this.getString(R.string.string_suggestion_feedback), false, true)
//                .setOnRootClickListener(this, "suggFeedback");
//        list_item.addView(suggFeedback);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_field:
                toUserInfoPage();
                break;
            case R.id.user_pic:
                toUserInfoPage();
                break;
            case R.id.user_setting:
                logOut();
                break;
        }
    }

    /**
     * 查看用户登录状态，并且返回跳转页面 - 未登录跳转到登录页面
     */
    private void toUserInfoPage() {
        Intent intent;
        if (getUserInfo.ifLogin()) {
            intent = new Intent(getActivity(), UserInfoActivity.class);
        } else {
            intent = new Intent(getActivity(), LoginActivity.class);
        }
        startActivity(intent);
    }

    /**
     * 用户登录之后的显示text
     */
    private void setUserInfo() {
        if (getUserInfo.ifLogin() && null != getUserInfo.getUserInfo() && !"".equals(getUserInfo.getUserInfo())) {
            userInfo = getUserInfo.getUserInfo().getData();
            user_name.setText(userInfo.getNickname());
            user_id.setText(userInfo.getUsername());
            String avatarUrl = userInfo.getAvatarUrl();
            if (null == avatarUrl || "".equals(avatarUrl)) {
                Glide.with(this).load(R.drawable.defaultpic_white).apply(options).into(image);
            } else {
                Glide.with(this).load(ImageServerConstant.IMAGE_SERVER_URL + avatarUrl).apply(options).into(image);
            }
//            Glide.with(this).load("http://localhost:808/images/user/156031376172435/141b54f3-b618-4171-8558-f0d346b5a8af.jpg").apply(options).into(image);

            user_setting.setVisibility(View.VISIBLE);
        } else {
            setUserInfoAsNoLogin();
        }
    }

    /**
     * 用户未登录显示的text
     */
    private void setUserInfoAsNoLogin() {
        //隐藏登出icon
        user_setting.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.defaultpic_white).apply(options).into(image);
        user_name.setText(this.getString(R.string.string_click_login));
        id_label.setVisibility(View.GONE);
        user_id.setText("");
    }

    /**
     * 登出
     */
    private void logOut() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("退出登录")
                .setMessage("退出后不会删除任何历史数据，下次登录依然可以使用本账号")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferencesHelper.remove("user_info");
                                String currentKeywordNote = spUtil.getSP("keyword_note");
                                if(currentKeywordNote!= null){
                                    spUtil.removeSP("keyword_note");
                                }
                                sharedPreferencesHelper.put("auth",false);
                                setUserInfoAsNoLogin();
                                MainActivity.removeMessageBadge();
                            }
                        }).setNegativeButton(this.getString(R.string.string_cancel_btn),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create();

        alertDialog.show();
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
        if (!getUserInfo.ifLogin()) {
            Toast.makeText(getContext(), "请先进行登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        int position = 0;
        switch ((String) view.getTag()) {
            case "mySent":
                position = 1;
                startActivity(new Intent(getActivity(), MyPubishActivity.class));
                break;
//            case "myComment":
//                position = 2;
//                startActivity(new Intent(getActivity(),MyCommentActivity.class));
//                break;
            case "myCollect":
                position = 3;
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case "myFollow":
                position = 4;
                startActivity(new Intent(getActivity(), MyFollowActivity.class));
                break;
//            case "suggFeedback":
//                position = 5;
//                startActivity(new Intent(getActivity(),FeedbackActivity.class));
//                break;
        }
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
