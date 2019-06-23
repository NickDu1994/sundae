package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.MessageModel;
import com.xwing.sundae.android.util.MessageConstant;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageModel> mDatas;
    private Context mContext;

    private MessageAdapter.onSwipeListener mOnSwipeListener;

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

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
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        MessageModel message = mDatas.get(position);
        String messageTxt = "";
        int drawableId = 0;
        int type = message.getType();

        switch (type) {
            case 0:
                messageTxt = MessageConstant.MESSAGE_TYPE_SYSTEM;
                drawableId = R.drawable.message_system;
                viewHolder.messageImage.setBackgroundResource(R.drawable.message_system_bg);
                break;
            case 1:
                messageTxt = MessageConstant.MESSAGE_TYPE_LIKE;
                drawableId = R.drawable.message_like;
                viewHolder.messageImage.setBackgroundResource(R.drawable.message_like_bg);
                break;
            case 2:
                messageTxt = MessageConstant.MESSAGE_TYPE_FOLLOW;
                drawableId = R.drawable.message_follow;
                viewHolder.messageImage.setBackgroundResource(R.drawable.message_follow_bg);
                break;
            case 3:
                messageTxt = MessageConstant.MESSAGE_TYPE_COLLECT;
                drawableId = R.drawable.message_collection;
                viewHolder.messageImage.setBackgroundResource(R.drawable.message_collect_bg);
                break;
        }

        viewHolder.messageTitle.setText(messageTxt);
        viewHolder.messageContent.setText(message.getContent());
        viewHolder.messageTime.setText("2天前");
        Glide.with(mContext).load(drawableId).into(viewHolder.messageImage);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });

        viewHolder.btnDeleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onDel(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    //② 创建ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTitle;
        TextView messageContent;
        ImageView messageImage;
        TextView messageTime;
        Button btnDeleteMessage;

        public ViewHolder(View v) {
            super(v);
            messageTitle = (TextView) v.findViewById(R.id.message_title);
            messageContent = (TextView) v.findViewById(R.id.message_content);
            messageImage = (ImageView) v.findViewById(R.id.message_image);
            messageTime = (TextView) v.findViewById(R.id.message_time);
            btnDeleteMessage = v.findViewById(R.id.btnDeleteMessage);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
}
