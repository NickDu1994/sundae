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
import com.xwing.sundae.android.model.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageModel> mDatas;
    private Context mContext;

    public MessageAdapter(List<MessageModel> data, Context context) {
        this.mDatas = data;
        this.mContext = context;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //LayoutInflater.from指定写法
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MessageModel message = mDatas.get(position);
        viewHolder.messageTitle.setText("收到关注");
        viewHolder.messageContent.setText(message.getContent());
        viewHolder.messageTime.setText("2天前");
        Glide.with(mContext).load(R.drawable.explore).into(viewHolder.messageImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });
    }

    //② 创建ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTitle;
        TextView messageContent;
        ImageView messageImage;
        TextView messageTime;

        public ViewHolder(View v) {
            super(v);
            messageTitle = (TextView) v.findViewById(R.id.message_title);
            messageContent = (TextView) v.findViewById(R.id.message_content);
            messageImage = (ImageView) v.findViewById(R.id.message_image);
            messageTime = (TextView) v.findViewById(R.id.message_time);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
