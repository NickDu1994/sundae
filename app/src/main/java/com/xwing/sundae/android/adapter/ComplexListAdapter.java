package com.xwing.sundae.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.ComplexListModel;

import java.util.List;

public class ComplexListAdapter extends RecyclerView.Adapter<ComplexListAdapter.ViewHolder> {

    private List<ComplexListModel> mDataList;
    private Context mContext;

    public ComplexListAdapter(List<ComplexListModel> complexListModelList, Context context){
        mDataList = complexListModelList;
        mContext  = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customeview_complex_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ComplexListModel complexListModel = mDataList.get(i);
        viewHolder.mainTextview.setText(complexListModel.getMainTitle());
        viewHolder.mainContentTextview.setText(complexListModel.getMainContent());
        Glide.with(mContext).load(complexListModel.getImagesList()[0]).into(viewHolder.mainImageview1);
        Glide.with(mContext).load(complexListModel.getImagesList()[1]).into(viewHolder.mainImageview2);
        Glide.with(mContext).load(complexListModel.getImagesList()[2]).into(viewHolder.mainImageview3);
        viewHolder.recommendTextview.setVisibility(complexListModel.isAdditionalInformation1() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.viewnumberTextview.setText(complexListModel.getAdditionalInformation2());
        viewHolder.createtimeTextview.setText(complexListModel.getAdditionalInformation3());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mainTextview;
        TextView mainContentTextview;
        ImageView mainImageview1;
        ImageView mainImageview2;
        ImageView mainImageview3;
        TextView recommendTextview;
        TextView viewnumberTextview;
        TextView createtimeTextview;

        public ViewHolder(View view) {
            super(view);
            mainTextview = view.findViewById(R.id.main_title);
            mainContentTextview = view.findViewById(R.id.main_content);
            mainImageview1 = view.findViewById(R.id.main_image_1);
            mainImageview2 = view.findViewById(R.id.main_image_2);
            mainImageview3 = view.findViewById(R.id.main_image_3);
            recommendTextview = view.findViewById(R.id.recommend);
            viewnumberTextview = view.findViewById(R.id.view_number);
            createtimeTextview = view.findViewById(R.id.create_time);
        }
    }

}