package com.example.mrboudar.playboy.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mrboudar.playboy.R;
import com.example.mrboudar.playboy.model.SeasonDetailBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrBoudar on 16/8/25.
 */
public class VideoHolder extends BaseViewHolder<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean> {
    private TextView tv_title;
    private TextView tv_title2;
    private List<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean> list = new ArrayList<>();

    public VideoHolder(ViewGroup parent, List<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean> list) {
        super(parent, R.layout.item_video_grid);
        tv_title = $(R.id.tv_count);
        tv_title2 = $(R.id.tv_new);
        this.list = list;
    }

    @Override
    public void setData(SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean data) {
        super.setData(data);
        //
        tv_title.setText((list.size() - getLayoutPosition() + 1) + "");
        if (getLayoutPosition() == 1) {
            tv_title2.setVisibility(View.VISIBLE);
        } else {
            tv_title2.setVisibility(View.INVISIBLE);
        }
    }
}
