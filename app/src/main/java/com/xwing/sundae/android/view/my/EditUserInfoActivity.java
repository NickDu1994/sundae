package com.xwing.sundae.android.view.my;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.UserInfoEditLineView;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.view.GetUserInfo;

public class EditUserInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UserInfoEditLineView.OnRootClickListener {
    private static final String EDITTYPE = "EDITTYPE";
    private static final String USER_INFO_TYPE_NICKNAME = "nickname";
    private static final String USER_INFO_TYPE_PROFILE = "profile";
    private static final String USER_INFO_TYPE_GENDER = "gender";
    private static final String USER_INFO_TYPE_REGION = "region";


    LinearLayout user_info_edit_lines;
    TextView edit_line_cancel, edit_text_value;
    Button edit_line_save;

    UserInfoEditLineView userInfoEditLineView;
    UserInfo userInfo = new UserInfo();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userInfo = new GetUserInfo(this).getUserInfo().getData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_line);

        initView();
        String userInfoEditType = getIntentData();

        user_info_edit_lines = findViewById(R.id.user_info_edit_lines);

        userInfoEditLineView = new UserInfoEditLineView(this);

        switch (userInfoEditType) {
            // 点击编辑姓名
            case USER_INFO_TYPE_NICKNAME:
                UserInfoEditLineView name_edit_view = userInfoEditLineView.setNameEdit(userInfo.getInfo().getNickname())
                        .setOnRootClickListener(this, USER_INFO_TYPE_NICKNAME);
                user_info_edit_lines.addView(name_edit_view);
                break;
            // 点击编辑个人简介
            case USER_INFO_TYPE_PROFILE:
                UserInfoEditLineView profile_edit_view = userInfoEditLineView.setProfileEdit(userInfo.getInfo().getProfile())
                        .setOnRootClickListener(this, USER_INFO_TYPE_PROFILE);
                user_info_edit_lines.addView(profile_edit_view);
                break;
            // 点击编辑性别
            case USER_INFO_TYPE_GENDER:
                UserInfoEditLineView gender_edit_view = userInfoEditLineView.setGenderEdit(userInfo.getInfo().getGender())
                        .setOnRootClickListener(this, USER_INFO_TYPE_GENDER);
                user_info_edit_lines.addView(gender_edit_view);
                break;
        }

    }

    private void initView() {
        edit_line_cancel = findViewById(R.id.edit_line_cancel);
        edit_line_save = findViewById(R.id.edit_line_save);
        edit_text_value = findViewById(R.id.edit_text_value);
        edit_line_save.setOnClickListener(this);
        edit_line_cancel.setOnClickListener(this);
    }

    /**
     * 获取从userinfo界面传过来的值
     *
     * @return type_name
     */
    private String getIntentData() {
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(EDITTYPE);
        String type_name = data.getString("name");
        return type_name;
    }

    /**
     * 界面click事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_line_save:
                saveInfo();
                break;
            case R.id.edit_line_cancel:
                finish();
                break;
        }
    }

    /**
     * 保存用户编辑的信息，并且关闭当前activity
     */
    private void saveInfo() {

        String edit_type = UserInfoEditLineView.getEditType();

        switch (edit_type){
            case USER_INFO_TYPE_NICKNAME:
                checkName();
                break;
            case USER_INFO_TYPE_GENDER:
                String gender = UserInfoEditLineView.getGenderByOper();
                userInfo.getInfo().setGender(gender);
                break;
            case USER_INFO_TYPE_PROFILE:
                String profile = UserInfoEditLineView.getProfile();
                userInfo.getInfo().setProfile(profile);
                break;
        }
        Log.e("MAGGIE==>", ""+userInfo);

        //call save info api
        UserInfoActivity.setUserInfo(userInfo);
        this.finish();
    }

    /**
     * 检查姓名编辑是否为空，不为空时进行修改后的赋值
     */
    private void checkName(){
        String name = UserInfoEditLineView.getName();
        if(null == name || "".equals(name)) {
            noNameAlert();
            return;
        }
        userInfo.getInfo().setNickname(name);

    }

    /**
     * 提示用户没有输入姓名
     */
    private void noNameAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("没有输入名字，请重新填写")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public void onRootClick(View view) {

    }
}
