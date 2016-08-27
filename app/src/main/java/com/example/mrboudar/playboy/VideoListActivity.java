package com.example.mrboudar.playboy;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrboudar.playboy.DataResponse.VideoCategoryResponse;
import com.example.mrboudar.playboy.api.APISTATUS;
import com.example.mrboudar.playboy.api.ApiService;
import com.example.mrboudar.playboy.api.HttpMethods;
import com.example.mrboudar.playboy.common.SharepreferenceUtils;
import com.example.mrboudar.playboy.common.Utils;
import com.example.mrboudar.playboy.core.BaseCoreActivity;
import com.example.mrboudar.playboy.fragment.VideoFragment;
import com.example.mrboudar.playboy.model.ResponseBody;
import com.example.mrboudar.playboy.model.TabTitle;
import com.example.mrboudar.playboy.model.VideoCategory;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrBoudar on 16/8/3.
 */
public class VideoListActivity extends BaseCoreActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.id_viewpager)
    ViewPager viewPager;

    @Bind(R.id.id_tablayout)
    TabLayout tableLayout;

    @Bind(R.id.id_toolbar)
    Toolbar toolbar;

    @Bind(R.id.draw_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.id_navigationview)
    NavigationView navigationView;

    List<Fragment> fragmentList = new ArrayList<>();

    List<TabTitle> tabTitles = new ArrayList<>();

    Subscription subscription;

    @Override
    protected void onCreateOverride(Bundle savedInstanceState) {
        initNavigationView();
        initListener();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initToolbars() {
        setSupportActionBar(toolbar);
        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.appbar_scrolling_view_behavior);
        mActionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void closeLoading() {

    }

    void initNavigationView() {
        View navView = LayoutInflater.from(this).inflate(R.layout.header_nav,null);
        ImageView headIv = (ImageView) navView.findViewById(R.id.id_header_face);
        TextView headName = (TextView) navView.findViewById(R.id.id_header_authorname);
        SharepreferenceUtils sharepreferenceUtils = new SharepreferenceUtils(this);
        if(!TextUtils.isEmpty(sharepreferenceUtils.getUserAvatar())){
            Glide.with(this).load(sharepreferenceUtils.getUserAvatar()).centerCrop().into(headIv);
        }
        if(!TextUtils.isEmpty(sharepreferenceUtils.getUserNick())){
            headName.setText(sharepreferenceUtils.getUserNick());
        }
        navigationView.addHeaderView(navView);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        navigationView.inflateMenu(R.menu.menu_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_categories:
                        navigationView.setSelected(true);
                        break;
                    case R.id.nav_menu_feedback:
                        navigationView.setSelected(true);
                        break;
                    case R.id.nav_menu_home:
                        navigationView.setSelected(true);

                        break;
                    case R.id.nav_menu_setting:
                        navigationView.setSelected(true);
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

    }

    void initViewPagerAdapter() {
        HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragmentList, tabTitles);
        viewPager.setAdapter(homePageAdapter);
        tableLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.setTabsFromPagerAdapter(homePageAdapter);
    }

    void initListener() {
        // viewPager.addOnPageChangeListener(this);
    }

    void initData() {

        ApiService apiService = HttpMethods.getInstance().getApiService();

        subscription = apiService.getHomeCateGory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResponseBody<VideoCategoryResponse>>() {
                    @Override
                    public void onCompleted() {

                        subscription.unsubscribe();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody<VideoCategoryResponse> objectResponseBody) {
                        if (objectResponseBody.getCode().equalsIgnoreCase(APISTATUS.SUCESS.toString())) {
                            List<VideoCategory> list = objectResponseBody.getData().getCategory();
                            if(!Utils.isCollectionEmpty(list)){
                                for (int i = 0;i < list.size();i++){
                                    VideoCategory videoCategory = list.get(i);
                                    TabTitle tabTitle = new TabTitle();
                                    tabTitle.setKey(videoCategory.getId());
                                    tabTitle.setName(videoCategory.getName());
                                    tabTitles.add(tabTitle);
                                    VideoFragment videoFragment = VideoFragment.newInstance(i, tabTitle);
                                    fragmentList.add(videoFragment);
                                }
                                initViewPagerAdapter();
                            }
                        }
                    }
                });
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        toolbar.setTitle(tabTitles.get(position).getName());
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class HomePageAdapter extends FragmentStatePagerAdapter {
        private List<TabTitle> tabTitles;
        private List<Fragment> fragmentList;

        public HomePageAdapter(FragmentManager fm, List<Fragment> list, List<TabTitle> tabTitles) {
            super(fm);
            this.fragmentList = list;
            this.tabTitles = tabTitles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position).getName();
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
