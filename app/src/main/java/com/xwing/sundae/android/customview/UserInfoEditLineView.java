package com.xwing.sundae.android.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.UserInfo;


/**
 *
 */
public class UserInfoEditLineView extends LinearLayout implements View.OnClickListener {

    /**
     * 总container
     */
    public static LinearLayout edit_root_container;

    /**
     * 编辑页面的header(cancel/save)
     */
    public static RelativeLayout edit_header;

    /**
     * 姓名编辑区域
     */
    public static RelativeLayout user_info_edit_thin;
    /**
     * 姓名输入label
     */
    public static EditText edit_text_value;
    /**
     * 清除btn
     */
    public static ImageView clear_edit_text_icon;
    /**
     * 个人简介
     */
    public static LinearLayout user_info_edit_profile;
    /**
     * 个人简介编辑区域
     */
    public static EditText user_info_edit_profile_value;
    /**
     * 性别
     */
    public static LinearLayout user_info_edit_gender;

    /**
     * 性别(男、女)
     */
    public static TextView gender_male, gender_female;

    /**
     * 性别选择icon
     */
    public static ImageView gender_male_selected_icon, gender_femaleSelected_icon;

    /**
     * 获取edit的类型
     */
    public static String editType;

    /**
     * user的姓名
     */
    public static String name;

    /**
     * user的性别
     */
    public static String gender;

    /**
     * user的个人简介
     */
    public static String profile;

    /**
     * user的地区
     */
    public static String region;

    public static String getEditType() {
        return editType;
    }

    public static void setEditType(String editType) {
        UserInfoEditLineView.editType = editType;
    }

    public static String getName() {
        return edit_text_value.getText().toString();
    }

    public static void setName(String name) {
        UserInfoEditLineView.name = name;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        UserInfoEditLineView.gender = gender;
    }

    public static String getProfile() {
        return user_info_edit_profile_value.getText().toString();
    }

    public static void setProfile(String profile) {
        UserInfoEditLineView.profile = profile;
    }

    public static String getRegion() {
        return region;
    }

    public static void setRegion(String region) {
        UserInfoEditLineView.region = region;
    }

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

    public static String getGenderByOper() {
        if (null != gender_male_selected_icon.getDrawable()) {
            return "男";
        }
        return "女";
    }

    /**
     * 初始化各个控件
     *
     * @return
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

        clear_edit_text_icon.setOnClickListener(this);

        //默认值
        setName(edit_text_value.getText().toString());
        setProfile(user_info_edit_profile_value.getText().toString());

        //默认性别为男
        selectMale();
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_edit_text_icon:
                edit_text_value.setText("");
                setName("");
                break;
        }
    }

    public UserInfoEditLineView setOnRootClickListener(final OnRootClickListener OnRootClickListener, final String tag) {
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
     * 姓名编辑框 + 叉叉
     *
     * @param name
     * @return
     */
    public UserInfoEditLineView setNameEdit(String name) {
        init();
        setEditType("name");
        setName(name);
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
    public UserInfoEditLineView setGenderEdit(String gender) {
        init();
        setEditType("gender");
        setGender(gender);
        if (null == gender || "".equals(gender) || ("男").equals(gender)) {
            selectMale();
        } else {
            selectFamale();
        }

        showEditGender(true);
        showEditThin(false);
        showEditProfile(false);

        gender_male.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMale();
            }
        });
        gender_female.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFamale();
            }
        });

        return this;
    }

    /**
     * 选中男(图标)
     */
    private void selectMale() {
        Glide.with(getContext()).load(R.drawable.selected).into(gender_male_selected_icon);
        Glide.with(getContext()).load("").into(gender_femaleSelected_icon);
    }

    /**
     * 选中女(图标)
     */
    private void selectFamale() {
        Glide.with(getContext()).load(R.drawable.selected).into(gender_femaleSelected_icon);
        Glide.with(getContext()).load("").into(gender_male_selected_icon);
    }

    /**
     * 个人简介
     *
     * @param
     * @return
     */
    public UserInfoEditLineView setProfileEdit(String text) {
        init();
        setEditType("profile");
        setProfile(text);
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
