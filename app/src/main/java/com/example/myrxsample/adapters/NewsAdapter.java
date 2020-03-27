package com.example.myrxsample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.myrxsample.R;
import com.example.myrxsample.entity.NewsResultEntity;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MainViewHolder> {

    private List<NewsResultEntity> mEntities = new ArrayList<>();

    public NewsAdapter(List<NewsResultEntity> titles) {
        mEntities = titles;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_retrofit_adapter_item, parent, false);
        return new MainViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        final NewsResultEntity entity = mEntities.get(position);
        holder.mTvType.setText(entity.getType());
        holder.mTvTitle.setText(entity.getDesc());
    }

    @Override
    public int getItemCount() {
        return mEntities.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvTitle;
        public TextView mTvType;

        public MainViewHolder(View rootView) {
            super(rootView);
            mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            mTvType = (TextView) rootView.findViewById(R.id.tv_type);
        }
    }
}
