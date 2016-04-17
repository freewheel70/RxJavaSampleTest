package com.hong.app.rxjavatest;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

import com.blunderer.materialdesignlibrary.adapters.ViewPagerAdapter;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;
import com.blunderer.materialdesignlibrary.models.ViewPagerItem;
import com.hong.app.rxjavatest.Blogs.BlogFragments.AndroidBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.AppBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.CoolBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.FrontBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.IOSBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.ResourceBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.VideoBlogFragment;
import com.hong.app.rxjavatest.CustomViews.CustomPagerSlidingTabStrip;
import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements com.blunderer.materialdesignlibrary.interfaces.ViewPager {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabs)
    CustomPagerSlidingTabStrip tabStrip;

    @Bind(R.id.view_pager)
    ViewPager viewPager;


    private ViewPager.OnPageChangeListener onPageChangeListener;
    private List<ViewPagerItem> pagerItems = new ArrayList<>();
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initPageItems();
        initViewPager();
        initViews();
    }

    private void initViews() {

    }

    private void initPageItems() {


        AndroidBlogFragment androidBlogFragment = new AndroidBlogFragment();
        PrettyGirlFragment prettyGirlFragment = new PrettyGirlFragment();
        IOSBlogFragment iosBlogFragment = new IOSBlogFragment();
        FrontBlogFragment frontBlogFragment = new FrontBlogFragment();
        VideoBlogFragment videoBlogFragment = new VideoBlogFragment();
        ResourceBlogFragment resourceBlogFragment = new ResourceBlogFragment();
        AppBlogFragment appBlogFragment = new AppBlogFragment();
        CoolBlogFragment coolBlogFragment = new CoolBlogFragment();

        ViewPagerHandler viewPagerHandler = new ViewPagerHandler(this)
                .addPage("Android", androidBlogFragment)
                .addPage("福利", prettyGirlFragment)
                .addPage("IOS",iosBlogFragment)
                .addPage("前端",frontBlogFragment)
                .addPage("拓展资源",resourceBlogFragment)
                .addPage("App",appBlogFragment)
                .addPage("视频",videoBlogFragment)
                .addPage("瞎推荐",coolBlogFragment)
                ;

        pagerItems = viewPagerHandler.getViewPagerItems();

    }


    private void initViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), pagerItems);
        viewPager.setAdapter(pagerAdapter);

        int defaultViewPagerPageSelectedPosition = defaultViewPagerPageSelectedPosition();
        selectPage(defaultViewPagerPageSelectedPosition);
        showTabs();

    }

    @Override
    public ViewPagerHandler getViewPagerHandler() {
        return null;
    }

    @Override
    public void selectPage(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void updateNavigationDrawerTopHandler(ViewPagerHandler viewPagerHandler, int defaultViewPagerPageSelectedPosition) {
        if (viewPagerHandler == null) {
            viewPagerHandler = new ViewPagerHandler(this);
        }
        pagerItems.clear();
        pagerItems.addAll(viewPagerHandler.getViewPagerItems());
        pagerAdapter.notifyDataSetChanged();

        selectPage(defaultViewPagerPageSelectedPosition);
        showTabs();
    }

    @Override
    public int defaultViewPagerPageSelectedPosition() {
        return 0;
    }

    private void showTabs() {
        tabStrip.setTextColor(getResources().getColor(android.R.color.white));
        tabStrip.setTextSize(getTextSize());
        tabStrip.setDividerColor(getResources().getColor(android.R.color.transparent));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.tab_bg_color));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.tab_indicator_color));
        tabStrip.setUnderlineColor(getResources().getColor(android.R.color.white));
        tabStrip.setUnderlineHeight(tabStrip.getIndicatorHeight());
        tabStrip.setShouldExpand(true);
        tabStrip.setOnPageChangeListener(onPageChangeListener);
        tabStrip.setViewPager(viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tabStrip.setTabBackground(android.R.attr.selectableItemBackground);
        }
    }


    private int getTextSize() {

        int textsizeDp = 16;

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi;

        return textsizeDp * densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }


}
