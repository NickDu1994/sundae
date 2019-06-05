package com.xwing.sundae.android.view.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.TouchPullView;

public class TestPullActivity extends AppCompatActivity {

    private static final float TOUCH_MOVE_MAX_Y = 600;

    private LinearLayout pull_it;

    private float mTouchMoveStartY = 0;

    private TouchPullView touchPullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pull);

        touchPullView = findViewById(R.id.touch_pull);

        findViewById(R.id.pull_it).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //得到意图
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchMoveStartY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float y = event.getY();
                        //判断是否向下拉动
                        if (y >= mTouchMoveStartY) {
                            float moveSize = y - mTouchMoveStartY;
                            float progress = moveSize >= TOUCH_MOVE_MAX_Y ?
                                    1 : moveSize / TOUCH_MOVE_MAX_Y;
                            touchPullView.setProgress(progress);
                        }
                        return true;
                    default:
                        break;


                }
                return false;
            }
        });
    }
}
