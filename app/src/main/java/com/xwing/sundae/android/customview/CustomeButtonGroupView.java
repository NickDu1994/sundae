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
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.next.tagview.TagCloudView;


public class CustomeButtonGroupView extends LinearLayout {
    private Context mContext;
    private String[] mTagList;
    TagCloudView tagCloudView1;
    ImageView cleanIV;
    OnTagClickListener myClickItemListener;


    public CustomeButtonGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.customeview_buttongroup_view, this);
        tagCloudView1 = (TagCloudView) findViewById(R.id.tag_cloud_view_1);
        cleanIV = findViewById(R.id.buttonGroupCleanButton);
    }

    public void setTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.buttonGroupTitle);
        textView.setText(title);
    }

    public void setTagList(String[] tagList) {
        mTagList = tagList;
        initView2();
    }

    public void initView2() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mTagList.length; i++) {
            list.add(mTagList[i]);
        }

        if (mTagList.length == 1 && mTagList[0] == "") {
            //do nothing
        } else {
            tagCloudView1.setTags(CommonMethod.removeDuplicate(list));
        }


        final List<String> finalList = list;
        tagCloudView1.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                if (position == -1) {
                    if(Constant.LOG_LEVEL == "DEV"){
                        Toast.makeText(mContext, "点击末尾文字",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    myClickItemListener.onTagClick(finalList.get(position));
                }
            }
        });
//        tagCloudView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "TagView onClick",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });



        cleanIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(getContext());
                if (spUtil.getSP("keyword_note") != null) {
                    spUtil.removeSP("keyword_note");
                }
                removeAllTag();
            }
        });
    }

    public void removeAllTag() {
        tagCloudView1.removeAllViews();
    }

    public void hideCleanButton() {
        cleanIV.setVisibility(View.GONE);
    }

    public void setOnItemClickListener(CustomeButtonGroupView.OnTagClickListener listener) {
        this.myClickItemListener = listener;
    }

    public interface OnTagClickListener {
        public void onTagClick(String keyword);
    }
}
