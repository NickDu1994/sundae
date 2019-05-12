package com.xwing.sundae.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.xwing.sundae.R;

public class SearchRoundCTACardView extends LinearLayout {

    public SearchRoundCTACardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.customeview_search_round_cta_card_view, this);
    }


}
