package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.FollowModel;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private List<FollowModel> mDatas;
    private Context mContext;

    public FollowAdapter(List<FollowModel> data, Context context) {
        this.mDatas = data;
        this.mContext = context;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //LayoutInflater.from指定写法
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_follow, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        FollowModel follow = mDatas.get(position);
        viewHolder.eventDesc.setText(follow.getEventDesc());
        viewHolder.eventTime.setText(follow.getEventTime());
        viewHolder.itemName.setText(follow.getItemName());
        viewHolder.itemContent.setText(follow.getItemContent());
        Glide.with(mContext).load(follow.getItemImage()).into(viewHolder.itemImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });
    }

    //② 创建ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventDesc;
        TextView eventTime;
        TextView itemName;
        TextView itemContent;
        ImageView itemImage;

        public ViewHolder(View v) {
            super(v);
            eventDesc = (TextView) v.findViewById(R.id.event_desc);
            eventTime = (TextView) v.findViewById(R.id.event_time);
            itemName = (TextView) v.findViewById(R.id.item_name);
            itemContent = (TextView) v.findViewById(R.id.item_content);
            itemImage = (ImageView) v.findViewById(R.id.item_image);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
