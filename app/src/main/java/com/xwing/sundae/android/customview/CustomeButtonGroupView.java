package com.xwing.sundae.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwing.sundae.R;
import com.xwing.sundae.android.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class CustomeButtonGroupView extends LinearLayout {
    private Context mContext;
    private String[] mTagList;
    TagContainerLayout mTagContainerLayout;
    ImageView cleanIV;

    public CustomeButtonGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.customeview_buttongroup_view, this);
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        cleanIV = findViewById(R.id.buttonGroupCleanButton);
    }

    public void setTitle(String title){
        TextView textView = (TextView) findViewById(R.id.buttonGroupTitle);
        textView.setText(title);
    }

    public void setTagList(String[] tagList){
        mTagList = tagList;
        initView2();
    }

    public void initView2() {
       LinearLayout parentLL = (LinearLayout) findViewById(R.id.tagcontainerLayout);
       List<String> list = new ArrayList<>();
       for (int i = 0; i < mTagList.length; i++) {
            list.add(mTagList[i]);
       }

       if(mTagList.length == 1 && mTagList[0] == ""){
           //do nothing
       }else {
           mTagContainerLayout.setTags(list);
       }


        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            public void onTagClick(int position, String text) {
                Toast.makeText(mContext, "text=" + text, Toast.LENGTH_SHORT).show();
            }
            public void onTagLongClick(final int position, String text) {
                Toast.makeText(mContext, "text=" + text, Toast.LENGTH_SHORT).show();
            }
        });


        cleanIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(getContext());
                if(spUtil.getSP("keyword_note")!= null){
                    spUtil.removeSP("keyword_note");
                }
                removeAllTag();
            }
        });
    }

    public void removeAllTag(){
        mTagContainerLayout.removeAllTags();
    }

}
