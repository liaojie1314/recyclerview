package com.example.recyclerapplication.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.example.recyclerapplication.R;
import com.example.recyclerapplication.beans.ItemBean;

import java.util.List;

public class StaggerAdapter extends RecyclerViewBaseAdapter{
    public StaggerAdapter(List<ItemBean> data) {
        super(data);
    }

    @Override
    protected View getSubView(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_stagger,null);
        return view;
    }
}
