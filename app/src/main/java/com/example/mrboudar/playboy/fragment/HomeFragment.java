package com.example.mrboudar.playboy.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrboudar.playboy.L;
import com.example.mrboudar.playboy.R;
import com.example.mrboudar.playboy.WebViewActivity;
import com.example.mrboudar.playboy.adapter.MyRecyclerViewAdapter;
import com.example.mrboudar.playboy.common.Common;
import com.example.mrboudar.playboy.model.ImageContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MrBoudar on 16/8/3.
 */
public class HomeFragment extends Fragment {
    private static final String FRAG_ENTRY = "frag_entry";
    private static final String FRAG_SOURCE = "frag_source";
    @Bind(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.id_recyclerview)
    RecyclerView recyclerView;

    List<ImageContent> imageContentList = new ArrayList<>();
    MyRecyclerViewAdapter recyclerViewAdapter;
    int entry;
    String sourcUrl;


    public static HomeFragment newInstance(int entry,String sourceUrl){
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAG_ENTRY,entry);
        bundle.putString(FRAG_SOURCE,sourceUrl);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(null != bundle){
            entry = bundle.getInt(FRAG_ENTRY);
            sourcUrl = bundle.getString(FRAG_SOURCE);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_home,null);
        ButterKnife.bind(this,view);
        initAdapter();
        initData();
        setListener();
        return view;
    }

    private void setListener(){
        int [] colorRes = new int[]{Color.RED,Color.YELLOW,Color.GREEN};
        swipeRefreshLayout.setColorSchemeColors(colorRes);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                new AsyT().execute(sourcUrl,Common.USER_AGENT);
            }
        });
    }

    private void initAdapter(){
        //必须设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(),imageContentList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                gotoWebViewActivity(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
    private void gotoWebViewActivity(int position){
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.WV_URL,imageContentList.get(position).getImageUrl());
        intent.putExtra(WebViewActivity.WV_TITILE,imageContentList.get(position).getImageName());
        startActivity(intent);
    }

    private void initData(){
        AsyT asyT = new AsyT();
        asyT.execute(sourcUrl,Common.USER_AGENT);
    }

    private class AsyT extends AsyncTask<String,Void,Document> {
        @Override
        protected Document doInBackground(String... params) {
            Document document = null;
            try {
                document = Jsoup.connect(params[0]).userAgent(params[1]).get();
            } catch (IOException e) {
                e.printStackTrace();
                L.e(getClass().getSimpleName(),e.getLocalizedMessage());
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            if(null != document){
                imageContentList.clear();
                Elements elements = document.body().select("img");
                for (Element element : elements) {
                    ImageContent content = new ImageContent();
                    content.setImageName(element.attr("alt"));
                    content.setImageUrl(element.attr("data-img"));
                    imageContentList.add(content);
                }
            }
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
