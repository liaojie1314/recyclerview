package com.example.recyclerapplication.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerapplication.R;
import com.example.recyclerapplication.beans.MoreTypeBean;

import java.util.List;

public class MoreTypeAdapter extends RecyclerView.Adapter {
    //定义三种常量标型
    public static final int TYPE_FULL_IMAGE=0;
    public static final int TYPE_RIGHT_IMAGE=1;
    public static final int TYPE_THREE_IMAGES=2;

    private final List<MoreTypeBean> mData;

    public MoreTypeAdapter(List<MoreTypeBean>data){
        this.mData=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //创建ViewHolder
        if (viewType==TYPE_FULL_IMAGE){
            view=View.inflate(parent.getContext(), R.layout.item_type_full_image,null);
            return new FullImageHolder(view);
        }else if (viewType==TYPE_RIGHT_IMAGE){
            view=View.inflate(parent.getContext(),R.layout.item_type_left_title_right_image,null);
            return new RightImageHolder(view);
        }else{
            view=View.inflate(parent.getContext(),R.layout.item_type_three_image,null);
            return new ThreeImagesHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        MoreTypeBean moreTypeBean=mData.get(position);
        if (moreTypeBean.type==0){
            return TYPE_FULL_IMAGE;
        }else if (moreTypeBean.type==1){
            return TYPE_RIGHT_IMAGE;
        }else{
            return TYPE_THREE_IMAGES;
        }
    }
    private class FullImageHolder extends RecyclerView.ViewHolder{

        public FullImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private class ThreeImagesHolder extends RecyclerView.ViewHolder{
        public ThreeImagesHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private class RightImageHolder extends RecyclerView.ViewHolder{

        public RightImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
