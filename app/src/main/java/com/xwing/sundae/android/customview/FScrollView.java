package com.xwing.sundae.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
public class FScrollView extends ScrollView {
    private OnScrollListener listener;

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public FScrollView(Context context) {
        super(context);
    }

    public FScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnScrollListener{
        void onScroll(int scrollY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener != null){
            listener.onScroll(t);
        }
    }
}
