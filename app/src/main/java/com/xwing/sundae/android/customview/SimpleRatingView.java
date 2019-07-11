package com.xwing.sundae.android.customview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.xlhratingbar_lib.IRatingView;
import com.xwing.sundae.R;
import com.xwing.sundae.android.util.CommonMethod;

public class SimpleRatingView implements IRatingView {

    @Override
    public int getStateRes(int posi, int state) {
        int id = R.drawable.star_border_show;
        switch (state) {
            case STATE_NONE:
                id = R.drawable.star_border_hide;
                break;
            case STATE_HALF:
                id = R.drawable.star_border_half;
                break;
            case STATE_FULL:
                id = R.drawable.star_border_show;
                break;
            default:
                break;
        }
        return id;
    }

    @Override
    public int getCurrentState(float rating, int numStars, int position) {
        position++;
        float dis = position - rating;
        if (dis <= 0) {
            return STATE_FULL;
        }
        if (dis == 0.5) {
            return STATE_HALF;
        }
        if (dis > 0.5) {
            return STATE_NONE;
        }
        return 0;
    }


    @Override
    public ImageView getRatingView(Context context, int numStars, int posi) {
        ImageView imageView = new ImageView(context);
        int width = CommonMethod.dp2px(context, 18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        params.setMargins(2,0,0,0);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
