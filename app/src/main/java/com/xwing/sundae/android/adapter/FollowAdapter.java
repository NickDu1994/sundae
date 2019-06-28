package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.FollowModel;
import com.xwing.sundae.android.util.ImageServerConstant;
import com.xwing.sundae.android.view.IndexDetailActivity;

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

        final FollowModel follow = mDatas.get(position);
        viewHolder.eventDesc.setText(follow.getEvent_desc());
        viewHolder.eventTime.setText(follow.getEvent_time());
        viewHolder.itemName.setText(follow.getItem_name());
        viewHolder.itemContent.setText(follow.getItem_content());
        String avatarUrl = ImageServerConstant.IMAGE_SERVER_URL + follow.getItem_avatar();
        String itemUrl = ImageServerConstant.IMAGE_SERVER_URL + follow.getItem_image();
        RequestOptions options = new RequestOptions().circleCropTransform();
        if(null == itemUrl || "".equals(itemUrl)) {
            Glide.with(mContext).load(R.drawable.defaultpic_theme).into(viewHolder.itemImage);
        } else {
            Glide.with(mContext).load(itemUrl).into(viewHolder.itemImage);
        }

        viewHolder.itemUsername.setText(follow.getItem_username());
        Glide.with(mContext).load(avatarUrl).apply(options).into(viewHolder.itemAvatar);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
                Intent intent = new Intent(mContext, IndexDetailActivity.class);
                intent.putExtra("entryId", follow.getId().toString());
                mContext.startActivity(intent);
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
        TextView itemUsername;
        ImageView itemAvatar;

        public ViewHolder(View v) {
            super(v);
            eventDesc = (TextView) v.findViewById(R.id.event_desc);
            eventTime = (TextView) v.findViewById(R.id.event_time);
            itemName = (TextView) v.findViewById(R.id.item_name);
            itemContent = (TextView) v.findViewById(R.id.item_content);
            itemImage = (ImageView) v.findViewById(R.id.item_image);
            itemUsername= (TextView) v.findViewById(R.id.item_username);
            itemAvatar = (ImageView) v.findViewById(R.id.item_avatar);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
