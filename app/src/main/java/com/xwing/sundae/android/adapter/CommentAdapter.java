package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.SimpleRatingView;
import com.xwing.sundae.android.model.CommentModel;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.ImageServerConstant;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> mDatas;
    private Context mContext;

    public CommentAdapter(List<CommentModel> data, Context context) {
        this.mDatas = data;
        this.mContext = context;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //LayoutInflater.from指定写法
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder viewHolder, int position) {

        CommentModel comment = mDatas.get(position);
        String timeString = comment.getItem_date();

        viewHolder.username.setText(comment.getUsername());
        viewHolder.content.setText(comment.getItem_content());

        RequestOptions options = new RequestOptions()
                .error(R.drawable.defaultpic_theme).circleCropTransform();
        String avatarUrl = comment.getItem_avatar();
        if(null == avatarUrl || "".equals(avatarUrl)) {
            Glide.with(mContext).load(R.drawable.defaultpic_theme).apply(options).into(viewHolder.avatar);
        } else {
            Glide.with(mContext).load(ImageServerConstant.IMAGE_SERVER_URL + avatarUrl).apply(options).into(viewHolder.avatar);
        }

        viewHolder.date.setText(CommonMethod.CalculateTimeUntilNow(timeString));

        viewHolder.rate.setRatingView(new SimpleRatingView());
        viewHolder.rate.setRating(comment.getItem_rate());
        viewHolder.rate.setEnabled(false);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });
    }

    //② 创建ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView content;
        ImageView avatar;
        TextView date;
        XLHRatingBar rate;

        public ViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.comment_username);
            content = (TextView) v.findViewById(R.id.comment_content);
            avatar = (ImageView) v.findViewById(R.id.item_avatar);
            date = (TextView) v.findViewById(R.id.comment_date);
            rate = (XLHRatingBar) v.findViewById(R.id.ratingBar);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
