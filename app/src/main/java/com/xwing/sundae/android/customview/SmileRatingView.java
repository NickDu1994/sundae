package com.xwing.sundae.android.customview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xwing.sundae.R;
import com.xwing.sundae.android.util.CommonMethod;

public class SmileRatingView extends SimpleRatingView{
    @Override
    public int getStateRes(int posi, int state) {
        int id = R.drawable.star_border_show;
        switch (state) {
            case STATE_NONE:
                id = R.drawable.smile_default;
                break;
            case STATE_HALF:
                id = R.drawable.smile_active;
                break;
            case STATE_FULL:
                id = R.drawable.smile_active;
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
            return STATE_FULL;
        }
        if (dis > 0.5) {
            return STATE_NONE;
        }
        return 0;
    }

    @Override
    public ImageView getRatingView(Context context, int numStars, int posi) {
        ImageView imageView = new ImageView(context);
        int width = CommonMethod.dp2px(context, 22);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        params.setMargins(CommonMethod.dp2px(context, 10),0,0,0);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
