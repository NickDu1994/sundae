package com.xwing.sundae.android.view.my;

import android.annotation.SuppressLint;
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

public class EditLineActivity extends AppCompatActivity implements View.OnClickListener,
        UserInfoEditLineView.OnRootClickListener {
    private static final String EDITTYPE = "EDITTYPE";
    private static final String USER_INFO_TYPE_NICKNAME = "nickname";
    private static final String USER_INFO_TYPE_PROFILE = "profile";
    private static final String USER_INFO_TYPE_GENDER = "gender";
    private static final String USER_INFO_TYPE_REGION = "region";


    LinearLayout user_info_edit_lines;
    TextView edit_line_cancel, edit_text_value;
    Button edit_line_save;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String user_nickname = "Xin.";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_line);

        initView();
        String userInfoEditType = getIntentData();
        Log.v("TAG", userInfoEditType);
        user_info_edit_lines = findViewById(R.id.user_info_edit_lines);
        switch (userInfoEditType) {
            case USER_INFO_TYPE_NICKNAME:
                UserInfoEditLineView name_edit_view = new UserInfoEditLineView(this)
                        .setThinEdit(user_nickname)
                        .setOnRootClickListener(this, USER_INFO_TYPE_NICKNAME);
                user_info_edit_lines.addView(name_edit_view);
                break;
            case USER_INFO_TYPE_PROFILE:
                UserInfoEditLineView profile_edit_view = new UserInfoEditLineView(this)
                        .setProfileEdit("")
                        .setOnRootClickListener(this, USER_INFO_TYPE_PROFILE);
                user_info_edit_lines.addView(profile_edit_view);
                break;
            case USER_INFO_TYPE_GENDER:
                UserInfoEditLineView gender_edit_view = new UserInfoEditLineView(this)
                        .setGenderEdit()
                        .setOnRootClickListener(this, USER_INFO_TYPE_GENDER);
                user_info_edit_lines.addView(gender_edit_view);
                break;
        }

    }

    private void initView() {
        edit_line_cancel = findViewById(R.id.edit_line_cancel);
        edit_line_save = findViewById(R.id.edit_line_save);

        edit_text_value = findViewById(R.id.edit_text_value);
        edit_line_cancel.setOnClickListener(this);
        edit_line_save.setOnClickListener(this);
    }

    private String getIntentData() {
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(EDITTYPE);
        String type_name = data.getString("name");
        return type_name;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_line_cancel:
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case R.id.edit_line_save:
                saveInfo();
                break;
        }

    }

    private void saveInfo() {
        this.finish();
//        Log.v("TAG",edit_text_value.getText().toString());
    }

    @Override
    public void onRootClick(View view) {
        Toast.makeText(this, (String) view.getTag(), Toast.LENGTH_SHORT).show();
        switch ((String) view.getTag()) {
            case "nickname":
                break;
            case "gender":
                break;
            case "profile":
                break;
        }

    }
}
