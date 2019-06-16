package com.xwing.sundae.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xwing.sundae.R;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class CustomeButtonGroupView extends LinearLayout {
    private Context mContext;

    public CustomeButtonGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.customeview_buttongroup_view, this);
        initView2();
    }

    private static String[] testName = {"apple","title","layout","LinearLayout","new","child"};


    public void initView2() {
       LinearLayout parentLL = (LinearLayout) findViewById(R.id.tagcontainerLayout);
       List<String> list = new ArrayList<>();
       for (int i = 0; i < testName.length; i++) {
            list.add(testName[i]);
       }
       TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
       mTagContainerLayout.setTags(list);

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            public void onTagClick(int position, String text) {
                Toast.makeText(mContext, "text=" + text, Toast.LENGTH_SHORT).show();
            }
            public void onTagLongClick(final int position, String text) {
                Toast.makeText(mContext, "text=" + text, Toast.LENGTH_SHORT).show();
            }
        });
    }



}