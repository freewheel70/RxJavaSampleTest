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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.hong.app.rxjavatest.Events.ServerSyncBlogEvent;
import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlFragment;
import com.hong.app.rxjavatest.database.User;
import com.hong.app.rxjavatest.profile.LoginActivity;
import com.hong.app.rxjavatest.profile.ProfilePageActivity;
import com.hong.app.rxjavatest.services.NetworkSyncService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        com.blunderer.materialdesignlibrary.interfaces.ViewPager {

    private static final String TAG = "MainActivity";
    private static final int LOGIN_REQUEST_CODE = 11;

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

        EventBus.getDefault().register(this);

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
                if (User.getUser().isAnonymous()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MainActivity.this, ProfilePageActivity.class);
                    startActivity(intent);
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_sync) {
            startService(new Intent(MainActivity.this, NetworkSyncService.class));
        }

        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerServerSyncResult(ServerSyncBlogEvent event) {
        if (event.success) {
            Toast.makeText(MainActivity.this, "同步成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "同步失败:" + event.message, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "]");
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                startService(new Intent(MainActivity.this, NetworkSyncService.class));
            }
        }
    }
}
