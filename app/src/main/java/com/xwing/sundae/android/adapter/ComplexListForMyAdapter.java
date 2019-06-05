package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.SwiperBean;

import java.util.List;

/**
 * Author: Maggie
 * Date: 2019/6/5
 * Time: 15:36
 */
public class ComplexListForMyAdapter extends RecyclerView.Adapter<ComplexListForMyAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInfalter;
    private List<SwiperBean> mDatas;

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    public ComplexListForMyAdapter(Context context, List<SwiperBean> mDatas) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mInfalter.inflate(R.layout.item_swiper, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.content.setText(mDatas.get(position).name);

        //验证长按
        holder.content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "longclig", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onLongClick() called with: v = [" + v + "]");
                return false;
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });

        (holder.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "onClick:" + mDatas.get(holder.getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
