package com.hong.app.freegank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hong.app.freegank.blogs.AndroidBlogFragment;
import com.hong.app.freegank.blogs.AppBlogFragment;
import com.hong.app.freegank.blogs.CoolBlogFragment;
import com.hong.app.freegank.blogs.FavouriteBlogFragment;
import com.hong.app.freegank.blogs.FrontBlogFragment;
import com.hong.app.freegank.blogs.IOSBlogFragment;
import com.hong.app.freegank.blogs.ResourceBlogFragment;
import com.hong.app.freegank.blogs.VideoBlogFragment;
import com.hong.app.freegank.events.ServerSyncBlogEvent;
import com.hong.app.freegank.pretty_girls.PrettyGirlCollectionActivity;
import com.hong.app.freegank.pretty_girls.PrettyGirlFragment;
import com.hong.app.freegank.utils.Constants;
import com.hong.app.freegank.database.Blog;
import com.hong.app.freegank.database.User;
import com.hong.app.freegank.profile.LoginActivity;
import com.hong.app.freegank.services.NetworkSyncService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int LOGIN_REQUEST_CODE = 11;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

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

    private LayoutInflater inflater;

    private List<String> tabTitles = new ArrayList<>();
    private FreeFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        inflater = LayoutInflater.from(this);

        initTabTitles();
        initViewPager();
        initNavHeaderView();
        initViews();
    }

    private void initTabTitles() {

        tabTitles.add(getString(R.string.favourite_blogs_title));
        tabTitles.add(getString(R.string.android_blogs_title));
        tabTitles.add(getString(R.string.pretty_blogs_title));
        tabTitles.add(getString(R.string.IOS_blogs_title));
        tabTitles.add(getString(R.string.front_blogs_title));
        tabTitles.add(getString(R.string.resource_blogs_title));
        tabTitles.add(getString(R.string.app_blogs_title));
        tabTitles.add(getString(R.string.video_blogs_title));
        tabTitles.add(getString(R.string.cool_blogs_title));
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void initViewPager() {

        adapter = new FreeFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
        } else if (itemId == R.id.action_sync_all) {
            syncAllBlogs();
        }

        return false;
    }

    private void syncAllBlogs() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Log.d(TAG, "call: sync_all");
                Blog.resetBlogSyncStatus();
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        sendSyncRequest();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerServerSyncResult(ServerSyncBlogEvent event) {
        if (event.isSuccess()) {
            Toast.makeText(MainActivity.this, getString(R.string.sync_server_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.sync_server_fail) + " : " + event.getMessage(), Toast.LENGTH_SHORT).show();
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
                .setTitle(getString(R.string.exit_title))
                .setMessage(R.string.exit_warning)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    class FreeFragmentPagerAdapter extends FragmentPagerAdapter {

        public FreeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public BasePageFragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("pos", position);
            BasePageFragment basePageFragment;
            switch (position) {
                case 0:
                    basePageFragment = new FavouriteBlogFragment();
                    break;
                case 1:
                    basePageFragment = new AndroidBlogFragment();
                    break;
                case 2:
                    basePageFragment = new PrettyGirlFragment();
                    break;
                case 3:
                    basePageFragment = new IOSBlogFragment();
                    break;
                case 4:
                    basePageFragment = new FrontBlogFragment();
                    break;
                case 5:
                    basePageFragment = new ResourceBlogFragment();

                    break;
                case 6:
                    basePageFragment = new AppBlogFragment();
                    break;
                case 7:
                    basePageFragment = new VideoBlogFragment();
                    break;
                case 8:
                    basePageFragment = new CoolBlogFragment();
                    break;
                default:
                    basePageFragment = new FavouriteBlogFragment();
            }
            basePageFragment.setArguments(bundle);

            return basePageFragment;
        }

        @Override
        public int getCount() {

            return tabTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }
}
