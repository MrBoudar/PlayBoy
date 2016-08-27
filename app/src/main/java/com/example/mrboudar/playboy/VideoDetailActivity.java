package com.example.mrboudar.playboy;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mrboudar.playboy.Enums.VideoQuality;
import com.example.mrboudar.playboy.adapter.VideoDetailRecycleAdapter;
import com.example.mrboudar.playboy.api.HttpMethods;
import com.example.mrboudar.playboy.common.Utils;
import com.example.mrboudar.playboy.core.BaseCoreActivity;
import com.example.mrboudar.playboy.model.SeasonDetailBean;
import com.example.mrboudar.playboy.model.VideoUrlBean;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MrBoudar on 16/8/19.
 */
public class VideoDetailActivity extends BaseCoreActivity {
    public static final String DATA_EXTRA = "detail_extra";
    @Bind(R.id.recycler_view)
    EasyRecyclerView recyclerView;
    @Bind(R.id.detail_image)
    ImageView imageView;
    @Bind(R.id.coll_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.toolbar)
    Toolbar mtoolbar;

    SeasonDetailBean mSeasonDetail;

    List<SeasonDetailBean.DataBean.SeasonBean.PlayUrlListBean> playList = new ArrayList<>();

    VideoDetailRecycleAdapter mAdapter;

    private Subscription subscription;
    /**
     * 解码方式 0软解 1硬解
     */
    private int mIsHwCodecEnabled = 0;

    @Override
    protected void onCreateOverride(Bundle savedInstanceState) {
        inittoolbar();
        initRecycleView();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initToolbars() {

    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void closeLoading() {

    }

    /**
     * 初始化toolbar
     */
    private void inittoolbar() {
        // 初始化ToolBar
        setSupportActionBar(mtoolbar);
        //返回键
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mSeasonDetail = getIntent().getParcelableExtra(DATA_EXTRA);
        collapsingToolbarLayout.setTitle(mSeasonDetail.getData().getSeason().getTitle());
        Glide.with(this).load(mSeasonDetail.getData().getSeason().getHorizontalUrl()).into(imageView);

        if (null != mSeasonDetail && null != mSeasonDetail.getData()) {
            if (!Utils.isCollectionEmpty(mSeasonDetail.getData().getSeason().getPlayUrlList())) {
                playList = mSeasonDetail.getData().getSeason().getPlayUrlList();
            }
        }
        Comparator<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean> comparable = new Comparator<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean>() {

            @Override
            public int compare(SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean data1, SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean data2) {
                return -(Integer.parseInt(data1.getEpisode()) - Integer.parseInt(data2.getEpisode()));
            }
        };
        List<SeasonDetailBean.DataBean.SeasonBean.EpisodeBriefBean> playUrlList = mSeasonDetail.getData().getSeason().getEpisode_brief();
        Collections.sort(playUrlList, comparable);
        mAdapter.addAll(playUrlList);
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getVideoCanPlayUrl(mSeasonDetail.getData().getSeason().getId() + "", playList.get(position).getEpisodeSid());
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initRecycleView() {
        mAdapter = new VideoDetailRecycleAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(mAdapter.obtainGridSpanSizeLookUp(5));
        recyclerView.setLayoutManager(manager);
    }


    private void getVideoCanPlayUrl(String seasonId, String episodeSid) {
        //获取播放地址
        subscription = HttpMethods.getInstance().getApiService()
                .getVideoUrl(VideoQuality.HIGH.toString(), seasonId, episodeSid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<VideoUrlBean, VideoUrlBean.DataBean.M3u8Bean>() {
                    @Override
                    public VideoUrlBean.DataBean.M3u8Bean call(VideoUrlBean videoUrlBean) {
                        return videoUrlBean.getData().getM3u8();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Snackbar.make(collapsingToolbarLayout, "奋力加载中...", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Action1<VideoUrlBean.DataBean.M3u8Bean>() {
                    @Override
                    public void call(VideoUrlBean.DataBean.M3u8Bean data) {
                        //启动播放器
                        Intent intent = new Intent(VideoDetailActivity.this, VideoHomeActivity.class);
                        intent.putExtra("videoPath", data.getUrl());
                        intent.putExtra("mediaCodec", mIsHwCodecEnabled);
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Snackbar.make(recyclerView, "获取播放链接失败", Snackbar.LENGTH_SHORT).show();
                    }
                });

    }
}
