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
import com.xwing.sundae.android.model.RankModel;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<RankModel> mDatas;
    private Context mContext;

    public RankAdapter(List<RankModel> data, Context context) {
        this.mDatas = data;
        this.mContext = context;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //LayoutInflater.from指定写法
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rank, viewGroup, false);
        return new RankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RankAdapter.ViewHolder viewHolder, int position) {

        RankModel rank = mDatas.get(position);
        if (position > 2) {
            viewHolder.itemRank.setText(String.valueOf(rank.getItemRank()));
        }
        switch (position) {
            case 0:
                viewHolder.itemRankImage.setImageResource(R.drawable.gold);
                break;
            case 1:
                viewHolder.itemRankImage.setImageResource(R.drawable.silver);
                break;
            case 2:
                viewHolder.itemRankImage.setImageResource(R.drawable.brozen);
                break;
            default:
                break;
        }

        viewHolder.itemName.setText(rank.getItemName());
        viewHolder.itemContent.setText(rank.getItemContent());
        Glide.with(mContext).load(rank.getItemImage()).into(viewHolder.itemImage);
        Glide.with(mContext).load(rank.getItemAvatar()).into(viewHolder.itemAvatar);
        viewHolder.authorName.setText(rank.getAuthorName());
        viewHolder.likeNumber.setText(rank.getLikeNumber());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });
    }

    //② 创建ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemRank;
        ImageView itemRankImage;
        TextView itemName;
        TextView itemContent;
        ImageView itemImage;
        ImageView itemAvatar;
        TextView authorName;
        TextView likeNumber;

        public ViewHolder(View v) {
            super(v);
            itemRank = (TextView) v.findViewById(R.id.item_rank);
            itemRankImage = (ImageView) v.findViewById(R.id.item_rank_image);
            itemName = (TextView) v.findViewById(R.id.item_name);
            itemContent = (TextView) v.findViewById(R.id.item_content);
            itemImage = (ImageView) v.findViewById(R.id.item_image);
            itemAvatar = (ImageView) v.findViewById(R.id.item_avatar);
            authorName = (TextView) v.findViewById(R.id.author_name);
            likeNumber = (TextView) v.findViewById(R.id.like_number);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
