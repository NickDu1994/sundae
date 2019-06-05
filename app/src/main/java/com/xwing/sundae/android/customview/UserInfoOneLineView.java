package com.xwing.sundae.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xwing.sundae.R;
import com.xwing.sundae.android.util.DensityUtils;

public class UserInfoOneLineView extends LinearLayout {

    /**
     * 上下分割线，默认隐藏上面分割线
     */
    private View dividerTop, dividerBottom;

    /**
     * 最外层容器
     */
    private LinearLayout llRoot;
    /**
     * 最左边的Icon
     */
    private ImageView ivLeftIcon;
    /**
     * 中间的文字内容
     */
    private TextView tvTextContent;

    /**
     * 中间的输入框
     */
    private EditText editContent;

    /**
     * 右边的文字
     */
    private TextView tvRightText;

    /**
     * 右边的icon 通常是箭头
     */
    private ImageView ivRightIcon;

    private TextView tx;


    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }


    /**
     * 右边箭头的点击事件
     */
    public static interface OnArrowClickListener {
        void onArrowClick(View view);
    }

    public UserInfoOneLineView(Context context) {
        super(context);
    }

    public UserInfoOneLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public UserInfoOneLineView init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_item, this, true);
        llRoot = (LinearLayout) findViewById(R.id.ll_root);
        dividerTop = findViewById(R.id.divider_top);
        dividerBottom = findViewById(R.id.divider_bottom);
        ivLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
        tvTextContent = (TextView) findViewById(R.id.tv_text_content);
        editContent = (EditText) findViewById(R.id.edit_content);
        tvRightText = (TextView) findViewById(R.id.tv_right_text);
        ivRightIcon = (ImageView) findViewById(R.id.iv_right_icon);
        return this;
    }

    /**
     * 文字 + 箭头
     *
     * @param textContent
     * @return
     */
    public UserInfoOneLineView init(String textContent) {
        init();
        setTextContent(textContent);
        showEdit(false);
        setRightText("");
        showLeftIcon(false);
        return this;
    }

    /**
     * 默认情况下的样子  icon + 文字 + 右箭头 + 下分割线
     *
     * @param iconRes     icon图片
     * @param textContent 文字内容
     */
    public UserInfoOneLineView init(int iconRes, String textContent, Boolean ifShowDividerTop, Boolean ifShowDividerBottom) {
        init();
        showDivider(ifShowDividerTop, ifShowDividerBottom);
        setLeftIcon(iconRes);
        setTextContent(textContent);
        showEdit(false);
        tvRightText.setVisibility(INVISIBLE);
        showArrow(true);
        return this;
    }


    /**
     * 我的页面每一行  icon + 文字 + 右箭头（显示/不显示） + 右箭头左边的文字（显示/不显示）+ 下分割线
     *
     * @param iconRes     icon图片
     * @param textContent 文字内容
     */
    public UserInfoOneLineView initMine(int iconRes, String textContent, String textRight, boolean showArrow) {
        init();
        setLeftIcon(iconRes);
        setTextContent(textContent);
        showDivider(false, true);
        setRightText(textRight);
        setEditable(false);
        showArrow(showArrow);
        return this;
    }


    /**
     * icon + 文字 + edit + 下分割线
     *
     * @return
     */
//    public OneLineView initItemWidthEdit(int iconRes, String textContent, String editHint) {
//        init(iconRes, textContent);
//        showEdit(true);
//        setEditHint(editHint);
//        showArrow(false);
//        return this;
//    }

    //---------------------下面是对每个部分的一些设置     上面是应用中常用场景封装-----------------------//

    /**
     * 设置root的paddingTop 与 PaddingBottom 从而控制整体的行高
     * paddingLeft 与 paddingRight 保持默认 20dp
     */
    public UserInfoOneLineView setRootPaddingTopBottom(int paddintTop, int paddintBottom) {
        llRoot.setPadding(DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), paddintTop),
                DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), paddintBottom));
        return this;
    }

    /**
     * 设置root的paddingLeft 与 PaddingRight 从而控制整体的行高
     * <p>
     * paddingTop 与 paddingBottom 保持默认 15dp
     */
    public UserInfoOneLineView setRootPaddingLeftRight(int paddintTop, int paddintRight) {
        llRoot.setPadding(DensityUtils.dp2px(getContext(), paddintTop),
                DensityUtils.dp2px(getContext(), 15),
                DensityUtils.dp2px(getContext(), paddintRight),
                DensityUtils.dp2px(getContext(), 15));
        return this;
    }

    /**
     * 设置上下分割线的显示情况
     *
     * @return
     */
    public UserInfoOneLineView showDivider(Boolean showDividerTop, Boolean showDivderBottom) {
        if (showDividerTop) {
            dividerTop.setVisibility(VISIBLE);
        } else {
            dividerTop.setVisibility(GONE);
        }
        if (showDivderBottom) {
            dividerBottom.setVisibility(VISIBLE);
        } else {
            dividerBottom.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置上分割线的颜色
     *
     * @return
     */
    public UserInfoOneLineView setDividerTopColor(int dividerTopColorRes) {
        dividerTop.setBackgroundColor(getResources().getColor(dividerTopColorRes));
        return this;
    }

    /**
     * 设置上分割线的高度
     *
     * @return
     */
    public UserInfoOneLineView setDividerTopHigiht(int dividerTopHigihtDp) {
        ViewGroup.LayoutParams layoutParams = dividerTop.getLayoutParams();
        layoutParams.height = DensityUtils.dp2px(getContext(), dividerTopHigihtDp);
        dividerTop.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 设置下分割线的颜色
     *
     * @return
     */
    public UserInfoOneLineView setDividerBottomColor(int dividerBottomColorRes) {
        dividerBottom.setBackgroundColor(getResources().getColor(dividerBottomColorRes));
        return this;
    }

    /**
     * 设置上分割线的高度
     *
     * @return
     */
    public UserInfoOneLineView setDividerBottomHigiht(int dividerBottomHigihtDp) {
        ViewGroup.LayoutParams layoutParams = dividerBottom.getLayoutParams();
        layoutParams.height = DensityUtils.dp2px(getContext(), dividerBottomHigihtDp);
        dividerBottom.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 设置左边Icon
     *
     * @param iconRes
     */
    public UserInfoOneLineView setLeftIcon(int iconRes) {
        ivLeftIcon.setImageResource(iconRes);
        return this;
    }

    /**
     * 设置左边Icon显示与否
     *
     * @param showLeftIcon
     */
    public UserInfoOneLineView showLeftIcon(boolean showLeftIcon) {
        if (showLeftIcon) {
            ivLeftIcon.setVisibility(VISIBLE);
        } else {
            ivLeftIcon.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置右边Icon 以及Icon的宽高
     */
    public UserInfoOneLineView setLeftIconSize(int widthDp, int heightDp) {
        ViewGroup.LayoutParams layoutParams = ivLeftIcon.getLayoutParams();
        layoutParams.width = DensityUtils.dp2px(getContext(), widthDp);
        layoutParams.height = DensityUtils.dp2px(getContext(), heightDp);
        ivLeftIcon.setLayoutParams(layoutParams);
        return this;
    }


    /**
     * 设置中间的文字内容
     *
     * @param textContent
     * @return
     */
    public UserInfoOneLineView setTextContent(String textContent) {
        tvTextContent.setText(textContent);
        return this;
    }

    /**
     * 设置中间的文字颜色
     *
     * @return
     */
    public UserInfoOneLineView setTextContentColor(int colorRes) {
        tvTextContent.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置中间的文字大小
     *
     * @return
     */
    public UserInfoOneLineView setTextContentSize(int textSizeSp) {
        tvTextContent.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置右边文字内容
     *
     * @return
     */
    public UserInfoOneLineView setRightText(String rightText) {
        if("".equals(rightText) || null == rightText) {
            tvRightText.setText("未填写");
        } else {
            tvRightText.setText(rightText);
        }
        return this;
    }


    /**
     * 设置右边文字颜色
     *
     * @return
     */
    public UserInfoOneLineView setRightTextColor(int colorRes) {
        tvRightText.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置右边文字大小
     *
     * @return
     */
    public UserInfoOneLineView setRightTextSize(int textSize) {
        tvRightText.setTextSize(textSize);
        return this;
    }

    /**
     * 设置右箭头的显示与不显示
     *
     * @param showArrow
     */
    public UserInfoOneLineView showArrow(boolean showArrow) {
        if (showArrow) {
            ivRightIcon.setVisibility(VISIBLE);
        } else {
            ivRightIcon.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 获取右边icon
     */
    public UserInfoOneLineView setIvRightIcon(int iconRes) {

        ivRightIcon.setImageResource(iconRes);

        return this;
    }

    /**
     * 获取右边icon
     */
    public ImageView getIvRightIcon() {

        return ivRightIcon;
    }


    /**
     * 设置中间的输入框显示与否
     *
     * @param showEdit
     * @return
     */
    public UserInfoOneLineView showEdit(boolean showEdit) {
        if (showEdit) {
            editContent.setVisibility(VISIBLE);
        } else {
            editContent.setVisibility(GONE);
        }
        return this;
    }


    /**
     * 设置中间的输入框 是否可输入
     *
     * @param editable
     * @return
     */
    public UserInfoOneLineView setEditable(boolean editable) {
        editContent.setFocusable(editable);
        editContent.setVisibility(INVISIBLE);
        return this;
    }


    /**
     * 设置中间的输入框hint内容
     *
     * @param showEditHint
     * @return
     */
    public UserInfoOneLineView setEditHint(String showEditHint) {
        editContent.setHint(showEditHint);
        return this;
    }

    /**
     * 设置中间的输入框内容
     *
     * @param s
     * @return
     */
    public UserInfoOneLineView setEditContent(String s) {
        editContent.setText(s);
        return this;
    }

    /**
     * 获取Edit输入的内容
     *
     * @param s
     * @return
     */
    public String getEditContent(String s) {
        return String.valueOf(editContent.getText());

    }


    /**
     * 设置 edit 颜色
     *
     * @param colorRes
     * @return
     */
    public UserInfoOneLineView setEditColor(int colorRes) {
        editContent.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置 edit 字体大小
     *
     * @param editSizeSp
     * @return
     */
    public UserInfoOneLineView setEditSize(int editSizeSp) {
        editContent.setTextSize(editSizeSp);
        return this;
    }
    //----------------------下面是一些点击事件

    public UserInfoOneLineView setOnRootClickListener(final OnRootClickListener onRootClickListener, final String tag) {
        llRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llRoot.setTag(tag);
                onRootClickListener.onRootClick(llRoot);
            }
        });
        return this;
    }

    public UserInfoOneLineView setOnArrowClickListener(final OnArrowClickListener onArrowClickListener, final int tag) {

        ivRightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ivRightIcon.setTag(tag);
                onArrowClickListener.onArrowClick(ivRightIcon);
            }
        });
        return this;
    }



}
