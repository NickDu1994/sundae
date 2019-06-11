package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.FollowModel;
import com.xwing.sundae.android.model.MyFollowerModel;

import java.util.List;

/**
 * Author Maggie
 * Date: 2019/6/6
 * Time: 17:34
 */
public class MyFollowerInfoAdapter extends RecyclerView.Adapter<MyFollowerInfoAdapter.ViewHolder>{

    /**
     * 传到adapter内的数据
     */
    private List<MyFollowerModel> mDatas;
    /**
     * 上下文
     */
    private Context mContext;

    /**
     *
     */
    private LayoutInflater mInfalter;

    private MyFollowerInfoAdapter.onSwipeListener mOnSwipeListener;
    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    /**
     * 构造器
     * @param data
     * @param context
     */
    public MyFollowerInfoAdapter(List<MyFollowerModel> data,Context context) {
        this.mDatas = data;
        this.mContext = context;
        mInfalter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_follower_info, viewGroup, false);
        return new MyFollowerInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        MyFollowerModel follow = mDatas.get(i);
        viewHolder.followTime.setText(follow.getFollowTime());
        viewHolder.sentCounts.setText(follow.getSentCounts());
        viewHolder.getPraisedCounts.setText(follow.getGetPraisedCounts());
        RequestOptions options = new RequestOptions().placeholder(R.drawable.defaultpic).circleCropTransform();
        Glide.with(mContext).load(follow.getFollow_avatarUrl()).apply(options).into(viewHolder.follow_avatarUrl);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });

        //验证长按
        viewHolder.follow_field.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "longclig", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onLongClick() called with: v = [" + v + "]");
                return false;
            }
        });

        viewHolder.btnCancelFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onDel(viewHolder.getAdapterPosition());
                }
            }
        });

        (viewHolder.follow_field).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "onClick:"
                        + mDatas.get(viewHolder.getAdapterPosition()).getFollow_avatarUrl(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView follow_avatarUrl;
        TextView followTime,sentCounts,getPraisedCounts,follow_username;
        Button btnCancelFollow;
        LinearLayout follow_field;

        public ViewHolder(View v) {
            super(v);
            follow_username = v.findViewById(R.id.follow_username);
            follow_field = v.findViewById(R.id.follow_field);
            follow_avatarUrl = v.findViewById(R.id.follow_avatarUrl);
            followTime = v.findViewById(R.id.followTime);
            sentCounts = v.findViewById(R.id.sentCounts);
            getPraisedCounts = v.findViewById(R.id.getPraisedCounts);
            btnCancelFollow = v.findViewById(R.id.btnCancelFollow);
        }
    }

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
}
