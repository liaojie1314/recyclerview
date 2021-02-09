package com.example.recyclerapplication.adapters;

import android.view.View;
import android.view.ViewGroup;
import com.example.recyclerapplication.R;
import com.example.recyclerapplication.beans.ItemBean;
import java.util.List;
public class GridViewAdapter extends RecyclerViewBaseAdapter {
    public GridViewAdapter(List<ItemBean> data){
        super(data);
    }
    @Override
    protected View getSubView(ViewGroup parent, int viewType) {
        //在这里面创建条目的UI
        View view=View.inflate(parent.getContext(), R.layout.item_grid_view,null);
        return view;
    }
}
