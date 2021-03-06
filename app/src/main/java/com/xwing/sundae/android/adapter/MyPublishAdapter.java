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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.MyFollowerModel;
import com.xwing.sundae.android.model.MyPublishModel;
import com.xwing.sundae.android.util.ImageServerConstant;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

/**
 * Author: Maggie
 * Date: 2019/6/13
 * Time: 10:33
 */
public class MyPublishAdapter extends RecyclerView.Adapter<MyPublishAdapter.ViewHolder>{

    /**
     * 传到adapter内的数据
     */
    private List<MyPublishModel> mDatas;

    /**
     * 上下文
     */
    private Context mContext;
    /**
     *
     */
    private LayoutInflater mInfalter;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    /**
     * 构造器
     * @param data
     * @param context
     */
    public MyPublishAdapter(List<MyPublishModel> data, Context context) {
        this.mDatas = data;
        this.mContext = context;
        mInfalter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publish, viewGroup, false);
        return new MyPublishAdapter.ViewHolder(view, onRecyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder v, int i) {
        MyPublishModel publish = mDatas.get(i);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.explore)
                .error(R.drawable.explore);
        if("".equals(publish.getItem_image()) || null == publish.getItem_image()) {
            Glide.with(mContext).load(R.drawable.pic).apply(options).into(v.item_image);
        } else {
            Glide.with(mContext).load(ImageServerConstant.IMAGE_SERVER_URL + publish.getItem_image()).apply(options).into(v.item_image);
        }
        if(null != publish.getItem_name()) {
            v.item_name.setText(publish.getItem_name());
        }

        v.htmlTextView.setHtml(publish.getItem_content(),
                new HtmlHttpImageGetter(v.htmlTextView));

//        v.item_content.setText(publish.getItem_content());
        String abbr_type = publish.getAbb_type();
        String abbr_type_name = "词条";
        if(null == abbr_type || "".equals(abbr_type)) {
            abbr_type_name = "";
        }
        abbr_type = abbr_type.substring(0,1);
        switch (abbr_type){
            case "0":
                abbr_type_name = "词条";
                break;
            case "1":
                abbr_type_name = "";
                break;
        }
        v.abb_type.setText(abbr_type_name);
        String liked_count = publish.getAbb_likedCount();
        int point = liked_count.indexOf(".");
        v.abb_likedCount.setText(liked_count.substring(0,point));
        v.create_time.setText(publish.getCreate_time());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        ImageView item_image;
        TextView create_time,abb_likedCount,abb_type,item_name,item_content;
        View mView;
        OnRecyclerItemClickListener mListener;
        HtmlTextView htmlTextView;

        public ViewHolder(@NonNull View v, OnRecyclerItemClickListener listener) {
            super(v);
            v.setOnClickListener(this);
            item_image = v.findViewById(R.id.item_image);
            create_time = v.findViewById(R.id.abbr_create_time);
            abb_likedCount = v.findViewById(R.id.abb_likedCount);
            abb_type = v.findViewById(R.id.abb_type);
            item_name = v.findViewById(R.id.item_name);
//            item_content = v.findViewById(R.id.item_content);
            htmlTextView = (HtmlTextView) v.findViewById(R.id.item_content);
            this.mListener = listener;
        }


        @Override
        public void onClick(View v) {
            Log.d("dkdebug", "click inside mypublishAdapter");
            mListener.OnItemClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickEvent(OnRecyclerItemClickListener listener) {
        this.onRecyclerItemClickListener = listener;
    }

    public interface OnRecyclerItemClickListener {
        void OnItemClick(View view, int postion);
    }

}
