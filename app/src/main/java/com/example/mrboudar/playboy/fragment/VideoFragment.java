package com.example.mrboudar.playboy.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrboudar.playboy.L;
import com.example.mrboudar.playboy.R;
import com.example.mrboudar.playboy.VideoDetailActivity;
import com.example.mrboudar.playboy.VideoHomeActivity;
import com.example.mrboudar.playboy.WebViewActivity;
import com.example.mrboudar.playboy.adapter.MyRecyclerViewAdapter;
import com.example.mrboudar.playboy.adapter.VideoRecyclerViewAdapter;
import com.example.mrboudar.playboy.api.APISTATUS;
import com.example.mrboudar.playboy.api.HttpMethods;
import com.example.mrboudar.playboy.common.Common;
import com.example.mrboudar.playboy.model.ImageContent;
import com.example.mrboudar.playboy.model.ResponseBody;
import com.example.mrboudar.playboy.model.SeasonDetailBean;
import com.example.mrboudar.playboy.model.SeasonQueryBean;
import com.example.mrboudar.playboy.model.TabTitle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrBoudar on 16/8/3.
 */
public class VideoFragment extends Fragment {
    private static final String FRAG_ENTRY = "frag_entry";
    private static final String FRAG_SOURCE = "frag_source";
    @Bind(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.id_recyclerview)
    RecyclerView recyclerView;

    List<SeasonQueryBean.DataBean.ResultsBean> resultsBeanList = new ArrayList<>();
    VideoRecyclerViewAdapter recyclerViewAdapter;
    int entry;
    TabTitle tabTitle;

    private Subscription subscription;

    private SeasonDetailBean mSeasonDetailBean;

    ProgressDialog mProgressDialog;

    public static VideoFragment newInstance(int entry, TabTitle tabTitle) {
        VideoFragment homeFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAG_ENTRY, entry);
        bundle.putSerializable(FRAG_SOURCE, (Serializable) tabTitle);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            entry = bundle.getInt(FRAG_ENTRY);
            tabTitle = (TabTitle) bundle.getSerializable(FRAG_SOURCE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_home, null);
        ButterKnife.bind(this, view);
        initAdapter();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        int[] colorRes = new int[]{Color.RED, Color.YELLOW, Color.GREEN};
        swipeRefreshLayout.setColorSchemeColors(colorRes);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Hint");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("奋力加载中...");
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAdapter = new VideoRecyclerViewAdapter(getActivity(), resultsBeanList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new VideoRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mProgressDialog.show();
                SeasonQueryBean.DataBean.ResultsBean bean = resultsBeanList.get(position);
                getVideoDetail(bean.getId());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                SeasonQueryBean.DataBean.ResultsBean bean = resultsBeanList.get(position);
                getVideoDetail(bean.getId());
            }
        });
    }

    private void gotoVideoDetailActivity(SeasonDetailBean seasonDetailBean) {
        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
        intent.putExtra(VideoDetailActivity.DATA_EXTRA,seasonDetailBean);
        startActivity(intent);
    }

    private void getVideoDetail(int id) {
        mProgressDialog.show();
        subscription = HttpMethods.getInstance().getApiService().getSeasonDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SeasonDetailBean>() {
                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                        gotoVideoDetailActivity(mSeasonDetailBean);
                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SeasonDetailBean seasonDetailBean) {
                            mSeasonDetailBean = seasonDetailBean;
                    }
                });
    }

    private void initData() {
        subscription = HttpMethods.getInstance().getApiService().getSeasonQuery(1, 10, Integer.parseInt(tabTitle.getKey()), "heat", tabTitle.getName(), false).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SeasonQueryBean>() {
                    @Override
                    public void onCompleted() {

                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SeasonQueryBean seasonQueryBean) {
                        if (seasonQueryBean.getCode().equalsIgnoreCase(APISTATUS.SUCESS.toString())) {
                            List<SeasonQueryBean.DataBean.ResultsBean> results = seasonQueryBean.getData().getResults();
                            resultsBeanList.clear();
                            resultsBeanList.addAll(results);
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mProgressDialog){
            mProgressDialog = null;
        }
    }
}
