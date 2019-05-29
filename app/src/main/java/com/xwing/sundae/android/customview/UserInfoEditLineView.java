package com.xwing.sundae.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwing.sundae.R;


public class UserInfoEditLineView extends LinearLayout implements View.OnClickListener{
    LinearLayout user_info_edit_profile,user_info_edit_gender,edit_root_container;

    RelativeLayout edit_header,user_info_edit_thin;

    TextView gender_male,gender_female;
    EditText edit_text_value, user_info_edit_profile_value;
    ImageView clear_edit_text_icon, gender_male_selected_icon, gender_femaleSelected_icon;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public UserInfoEditLineView(Context context) {
        super(context);
    }

    public UserInfoEditLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    /**
     * 初始化各个控件
     */
    public UserInfoEditLineView init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_line, this, true);
        user_info_edit_profile = (LinearLayout) findViewById(R.id.user_info_edit_profile);
        user_info_edit_gender = (LinearLayout) findViewById(R.id.user_info_edit_gender);
        edit_header = (RelativeLayout) findViewById(R.id.edit_header);
        user_info_edit_thin = (RelativeLayout) findViewById(R.id.user_info_edit_thin);

        edit_root_container = (LinearLayout) findViewById(R.id.edit_root_container);

        gender_male = findViewById(R.id.gender_male);
        gender_female = findViewById(R.id.gender_female);
        edit_text_value = findViewById(R.id.edit_text_value);
        user_info_edit_profile_value = findViewById(R.id.user_info_edit_profile_value);
        clear_edit_text_icon = findViewById(R.id.clear_edit_text_icon);
        gender_male_selected_icon = findViewById(R.id.gender_male_selected_icon);
        gender_femaleSelected_icon = findViewById(R.id.gender_femaleSelected_icon);


        return this;
    }

 @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_edit_text_icon:
                edit_text_value.setText("");
                break;
        }
    }

    public UserInfoEditLineView setOnRootClickListener(final OnRootClickListener OnRootClickListener , final String tag) {
        edit_root_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_root_container.setTag(tag);
                OnRootClickListener.onRootClick(edit_root_container);
            }
        });
        return this;
    }

    /**
     * 瘦编辑框 + 叉叉
     *
     * @param name
     * @return
     */
    public UserInfoEditLineView setThinEdit(String name) {
        init();

        showEditGender(false);
        showEditThin(true);
        edit_text_value.setText(name);
        showEditProfile(false);
        return this;
    }

    /**
     * 性别
     *
     * @param
     * @return
     */
    public UserInfoEditLineView setGenderEdit() {
        init();
        showEditGender(true);
        showEditThin(false);
        showEditProfile(false);
        gender_male.setText("男");
        gender_female.setText("女");
        Glide.with(this).load(R.mipmap.selected).into(gender_male_selected_icon);
        gender_male.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(R.mipmap.selected).into(gender_male_selected_icon);
                Glide.with(getContext()).load("").into(gender_femaleSelected_icon);
            }
        });
        gender_female.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(R.mipmap.selected).into(gender_femaleSelected_icon);
                Glide.with(getContext()).load("").into(gender_male_selected_icon);
            }
        });

        return this;
    }

    /**
     * 个人简介
     *
     * @param
     * @return
     */
    public UserInfoEditLineView setProfileEdit(String text) {
        init();
        showEditGender(false);
        showEditThin(false);
        showEditProfile(true);
        user_info_edit_profile_value.setText(text);
        return this;
    }

    /**
     * 是否显示姓名编辑
     *
     * @param
     * @return
     */
    private UserInfoEditLineView showEditThin(boolean ifShow) {
        if (ifShow) {
            user_info_edit_thin.setVisibility(VISIBLE);
        } else {
            user_info_edit_thin.setVisibility(GONE);
        }
        return this;

    }

    /**
     * 是否显示个人简介编辑
     *
     * @param
     * @return
     */
    private UserInfoEditLineView showEditProfile(boolean ifShow) {
        if (ifShow) {
            user_info_edit_profile.setVisibility(VISIBLE);
        } else {
            user_info_edit_profile.setVisibility(GONE);
        }
        return this;

    }

    /**
     * 是否显示性别编辑
     *
     * @param
     * @return
     */
    private UserInfoEditLineView showEditGender(boolean ifShow) {
        if (ifShow) {
            user_info_edit_gender.setVisibility(VISIBLE);
        } else {
            user_info_edit_gender.setVisibility(GONE);
        }
        return this;

    }



}
