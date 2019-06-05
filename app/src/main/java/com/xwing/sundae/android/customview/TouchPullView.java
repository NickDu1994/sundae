package com.xwing.sundae.android.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Author: Maggie
 * Date: 2019/6/2
 * Time: 17:37
 */
public class TouchPullView extends View {

    private Paint mCirclePaint;
    private int mCircleRadius = 100;
    private int mCircleRPointX, mCircleRPointY;

    //可拖动的高度
    private int mDragHeight = 800;

    //进度值
    private float mProgress;


    public TouchPullView(Context context) {
        super(context);
        init();
    }

    public TouchPullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchPullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchPullView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        p.setAntiAlias(true);
        //防抖动
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xFF4879FF);
        mCirclePaint = p;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawCircle(mCircleRPointX,
                mCircleRPointY,
                mCircleRadius,
                mCirclePaint);
    }

    /**
     * 当进行测量时触发
     * @param widthMeasureSpec 宽度大小值
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽度的意图，宽度的类型
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth;
        int measureHeight;

        int iWidth = 2*mCircleRadius + getPaddingLeft()+getPaddingRight();
        int iHeight = (int)(mDragHeight*mProgress+0.5f)+getPaddingTop()+getPaddingBottom();

        if(widthMode == MeasureSpec.EXACTLY) {
            measureWidth = width;
        } else if(widthMode == MeasureSpec.AT_MOST) {
            measureWidth = Math.min(iWidth,width);
        } else {
            measureWidth = iWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            measureHeight = height;
        } else if(heightMode == MeasureSpec.AT_MOST) {
            measureHeight = Math.min(iHeight,height);
        } else {
            measureHeight = iHeight;
        }

        //设置测量的高度宽度
        setMeasuredDimension(measureWidth,measureHeight);

    }

    /**
     * 当大小改变时触发
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleRPointX = getWidth() >> 1;
        mCircleRPointY = getHeight() >> 1;

    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        Log.e("MAGGIETAG",progress+"");
        mProgress = progress;
        //请求重新进行测量
        requestLayout();
    }
}
