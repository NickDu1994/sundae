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
import com.xwing.sundae.android.model.MyCollectModel;
import com.xwing.sundae.android.model.MyFollowerModel;

import java.util.List;

/**
 * Author: Maggie
 * Date: 2019/6/8
 * Time: 22:45
 */
public class MyCollectAdapter extends RecyclerView.Adapter<MyCollectAdapter.ViewHolder>{
    /**
     * 传到adapter内的数据
     */
    private List<MyCollectModel> mDatas;
    /**
     * 上下文
     */
    private Context mContext;

    /**
     *
     */
    private LayoutInflater mInfalter;

    private MyCollectAdapter.onSwipeListener mOnSwipeListener;
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
    public MyCollectAdapter(List<MyCollectModel> data,Context context) {
        this.mDatas = data;
        this.mContext = context;
        mInfalter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collect_list, viewGroup, false);
        return new MyCollectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        MyCollectModel collect = mDatas.get(i);

        RequestOptions options = new RequestOptions().placeholder(R.drawable.defaultpic).circleCropTransform();
        viewHolder.item_name.setText(collect.getItem_name());
        viewHolder.item_content.setText(collect.getItem_content());

        if("".equals(collect.getItem_image()) || null == collect.getItem_image()) {
            Glide.with(mContext).load(R.drawable.explore).apply(options).into(viewHolder.item_image);
        } else {
            Glide.with(mContext).load(collect.getItem_image()).apply(options).into(viewHolder.item_image);
        }
        viewHolder.collect_author.setText(collect.getCollect_author());
        viewHolder.collect_time.setText(collect.getCollect_time());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });

        //验证长按
        viewHolder.collect_field.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "longclig", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onLongClick() called with: v = [" + v + "]");
                return false;
            }
        });

//        viewHolder.btnCancelFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mOnSwipeListener) {
//                    mOnSwipeListener.onDel(viewHolder.getAdapterPosition());
//                }
//            }
//        });

        (viewHolder.collect_field).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "onClick:"
                        + mDatas.get(viewHolder.getAdapterPosition()).getItem_id(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image;
        TextView item_name,item_content,collect_author,collect_time;
        Button btnCancelFollow;
        LinearLayout collect_field;

        public ViewHolder(View v) {
            super(v);
            item_image = v.findViewById(R.id.item_image);
            item_name = v.findViewById(R.id.item_name);
            item_content = v.findViewById(R.id.item_content);
            collect_author = v.findViewById(R.id.collect_author);
            collect_time = v.findViewById(R.id.collect_time);
            collect_field = v.findViewById(R.id.collect_field);
//            btnCancelFollow = v.findViewById(R.id.btnCancelFollow);
        }
    }

    public MyCollectAdapter.onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(MyCollectAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
}
