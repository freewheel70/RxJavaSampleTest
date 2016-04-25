package com.hong.app.rxjavatest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlCollectionActivity;
import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlFragment;
import com.hong.app.rxjavatest.Utils.Constants;
import com.hong.app.rxjavatest.database.User;
import com.hong.app.rxjavatest.profile.LoginActivity;
import com.hong.app.rxjavatest.services.NetworkSyncService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
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


    public static class FragmentItem {
        public BasePageFragment fragment;
        public String name;

        public FragmentItem(BasePageFragment fragment, String name) {
            this.fragment = fragment;
            this.name = name;
        }
    }

    List<FragmentItem> fragmentItemList = new ArrayList<>();

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

        fragmentItemList.add(new FragmentItem(favouriteBlogFragment, "收藏"));
        fragmentItemList.add(new FragmentItem(androidBlogFragment, "Android"));
        fragmentItemList.add(new FragmentItem(prettyGirlFragment, "福利"));
        fragmentItemList.add(new FragmentItem(iosBlogFragment, "IOS"));
        fragmentItemList.add(new FragmentItem(frontBlogFragment, "前端"));
        fragmentItemList.add(new FragmentItem(resourceBlogFragment, "拓展资源"));
        fragmentItemList.add(new FragmentItem(appBlogFragment, "App"));
        fragmentItemList.add(new FragmentItem(videoBlogFragment, "视频"));
        fragmentItemList.add(new FragmentItem(coolBlogFragment, "瞎推荐"));

        ViewPagerHandler viewPagerHandler = new ViewPagerHandler(this);
        for (int i = 0; i < fragmentItemList.size(); i++) {
            FragmentItem fragmentItem = fragmentItemList.get(i);
            viewPagerHandler.addPage(fragmentItem.name, fragmentItem.fragment);
        }

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

        Glide.with(this).load(new File(Constants.AVATAR_PATH))
                .error(R.mipmap.ic_launcher)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(avatar);
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


    @OnClick(R.id.fab)
    public void clickFab(View view) {
        int currentItem = viewPager.getCurrentItem();
        FragmentItem fragmentItem = fragmentItemList.get(currentItem);
        fragmentItem.fragment.sendRequest();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_pic_collection) {
            startActivity(new Intent(MainActivity.this, PrettyGirlCollectionActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void sendSyncRequest() {
        startService(new Intent(MainActivity.this, NetworkSyncService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }

        return false;
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
                sendSyncRequest();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("确定退出FreeGank吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}
