/*
 *
 *  *
 *  *  *
 *  *  *  * ===================================
 *  *  *  * Copyright (c) 2016.
 *  *  *  * 作者：安卓猴
 *  *  *  * 微博：@安卓猴
 *  *  *  * 博客：http://sunjiajia.com
 *  *  *  * Github：https://github.com/opengit
 *  *  *  *
 *  *  *  * 注意**：如果您使用或者修改该代码，请务必保留此版权信息。
 *  *  *  * ===================================
 *  *  *
 *  *  *
 *  *
 *
 */

package com.example.mrboudar.playboy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.mrboudar.playboy.R;
import com.example.mrboudar.playboy.model.ImageContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int mItemWidth;
    int mItemHeight;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    List<ImageContent> dataList = new ArrayList<>();

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public LayoutInflater mLayoutInflater;

    public MyRecyclerViewAdapter(Context mContext, List<ImageContent> list) {
        this.mContext = mContext;
        this.dataList = list;
        mLayoutInflater = LayoutInflater.from(mContext);
        mItemWidth = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth();
        mItemHeight = ((Activity)mContext).getResources().getDimensionPixelSize(R.dimen.dimens_200_dp);
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home, null));
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyRecyclerViewHolder viewHolder = (MyRecyclerViewHolder) holder;
        ImageContent t = dataList.get(position);
        viewHolder.mTextView.setText(t.getImageName());
        ViewGroup.LayoutParams imageParams = viewHolder.imageView.getLayoutParams();
        imageParams.height = mItemHeight;
        imageParams.width = mItemWidth;
        viewHolder.imageView.setLayoutParams(imageParams);
        ViewGroup.LayoutParams textParams = viewHolder.mTextView.getLayoutParams();
        textParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        textParams.width = mItemWidth;
        viewHolder.mTextView.setLayoutParams(textParams);
        Glide.with(mContext).load(t.getImageUrl()).centerCrop().into(viewHolder.imageView);
        if(null != mOnItemClickListener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.itemView,position);
                    return false;
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return dataList.size() == 0 ? 0 : dataList.size();
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_textview)
        TextView mTextView;
        @Bind(R.id.id_imageview)
        ImageView imageView;

        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
