package com.example.mrboudar.playboy.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.mrboudar.playboy.model.SeasonDetailBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


/**
 * Created by MrBoudar on 16/8/25.
 */
public class VideoDetailRecycleAdapter extends RecyclerArrayAdapter<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean> {
    /**
     * Constructor
     *
     * @param context The current context.
     */
    public VideoDetailRecycleAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(parent, getAllData());
    }
}
