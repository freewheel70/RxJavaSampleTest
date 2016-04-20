package com.hong.app.rxjavatest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.adapters.ViewPagerAdapter;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;
import com.blunderer.materialdesignlibrary.models.ViewPagerItem;
import com.hong.app.rxjavatest.Blogs.BlogFragments.AndroidBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.AppBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.CoolBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.FavouriteBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.FrontBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.IOSBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.ResourceBlogFragment;
import com.hong.app.rxjavatest.Blogs.BlogFragments.VideoBlogFragment;
import com.hong.app.rxjavatest.CustomViews.CustomPagerSlidingTabStrip;
import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlFragment;
import com.hong.app.rxjavatest.database.User;
import com.hong.app.rxjavatest.network.BlogNetworkManager;
import com.hong.app.rxjavatest.network.NetworkResponseResult;
import com.hong.app.rxjavatest.profile.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        com.blunderer.materialdesignlibrary.interfaces.ViewPager {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabs)
    CustomPagerSlidingTabStrip tabStrip;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.nav_view)
    NavigationView navView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    private ImageView avatar;
    private TextView userName;

    private ViewPager.OnPageChangeListener onPageChangeListener;
    private List<ViewPagerItem> pagerItems = new ArrayList<>();
    private ViewPagerAdapter pagerAdapter;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        inflater = LayoutInflater.from(this);

        initPageItems();
        initViewPager();
        initNavHeaderView();
        initViews();
    }

    private void initNavHeaderView() {

        View header = inflater.inflate(R.layout.nav_header_main, null, false);
        navView.addHeaderView(header);

        avatar = (ImageView) header.findViewById(R.id.avatar);
        userName = (TextView) header.findViewById(R.id.user_name);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  uploadNonSyncedBlogs();
                downloadAllBlogs();
            }
        });
    }

    private void downloadAllBlogs() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BlogNetworkManager.getMyBlogs();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    private void uploadNonSyncedBlogs() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                NetworkResponseResult responseResult = BlogNetworkManager.uploadNonSyncedBlogs();
                if (responseResult.success) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(responseResult.message));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "上传失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    private void initViews() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        FavouriteBlogFragment favouriteBlogFragment = new FavouriteBlogFragment();

        ViewPagerHandler viewPagerHandler = new ViewPagerHandler(this)
                .addPage("收藏", favouriteBlogFragment)
                .addPage("Android", androidBlogFragment)
                .addPage("福利", prettyGirlFragment)
                .addPage("IOS", iosBlogFragment)
                .addPage("前端", frontBlogFragment)
                .addPage("拓展资源", resourceBlogFragment)
                .addPage("App", appBlogFragment)
                .addPage("视频", videoBlogFragment)
                .addPage("瞎推荐", coolBlogFragment);

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
    protected void onResume() {
        super.onResume();
        User user = User.getUser();
        userName.setText(user.username);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
