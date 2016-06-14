package com.hong.app.rxjavatest.blogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.database.Blog;
import com.hong.app.rxjavatest.services.NetworkSyncService;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Freewheel on 2016/4/17.
 */
public class BlogBrowserActivity extends AppCompatActivity {

    private static final String TAG = "BlogBrowserActivity";

    public static final String EXTRA_BLOG = "blog_extra";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.webview)
    WebView webview;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    BlogBean blogBean;
    String urlStr;
    private static final CharSequence LABEL_FreeGank = "freeGank";
    private Menu menu;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_browser);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initWebview();

        blogBean = getIntent().getParcelableExtra(EXTRA_BLOG);
        urlStr = blogBean.getUrl();
        webview.loadUrl(urlStr);

        getSupportActionBar().setTitle(blogBean.getType());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initWebview() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setSaveFormData(false);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setLoadWithOverviewMode(false);
        webview.getSettings().setUseWideViewPort(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(TAG, "onProgressChanged() called with: " + " newProgress = [" + newProgress + "]");
                if (newProgress > 90) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.copy_url:
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(LABEL_FreeGank, urlStr);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, R.string.has_copy_url, Toast.LENGTH_SHORT).show();

                break;
            case R.id.open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlStr));
                startActivity(intent);
                break;

            case R.id.favourite:
                if (isFavorite) {
                    Observable.create(new Observable.OnSubscribe<Blog>() {
                        @Override
                        public void call(Subscriber<? super Blog> subscriber) {
                            Blog blog = Blog.getBlogById(blogBean.getId());
                            if (blog != null) {
                                blog.isRemoved = true;
                                blog.isSynced = false;
                                blog.save();
                            }
                            subscriber.onCompleted();
                        }
                    })
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Blog>() {
                                @Override
                                public void onCompleted() {
                                    isFavorite = false;
                                    item.setIcon(R.drawable.favourite_empty);
                                    startService(new Intent(BlogBrowserActivity.this, NetworkSyncService.class));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "onError: " + e.getMessage());
                                }

                                @Override
                                public void onNext(Blog blog) {

                                }
                            });
                } else {

                    Observable.create(new Observable.OnSubscribe<Blog>() {
                        @Override
                        public void call(Subscriber<? super Blog> subscriber) {
                            Log.d(TAG, "call: before save blog ");
                            Blog blog = Blog.getBlogById(blogBean.getId());
                            if (blog == null) {
                                blog = new Blog(blogBean);
                            } else {
                                blog.isRemoved = false;
                                blog.isSynced = false;
                            }
                            blog.save();
                            Log.d(TAG, "call: after save blog ");
                            subscriber.onCompleted();
                        }
                    })
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Blog>() {
                                @Override
                                public void onCompleted() {
                                    Log.d(TAG, "call: before set favourite icon ");
                                    isFavorite = true;
                                    final MenuItem favouriteItem = menu.findItem(R.id.favourite);
                                    favouriteItem.setIcon(R.drawable.favourite);
                                    startService(new Intent(BlogBrowserActivity.this, NetworkSyncService.class));
                                    Log.d(TAG, "call: after set favourite icon ");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "onError: " + e.getMessage());
                                }

                                @Override
                                public void onNext(Blog blog) {

                                }
                            });
                }
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_blog_browser, menu);
        this.menu = menu;

        final MenuItem favouriteItem = menu.findItem(R.id.favourite);

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean isBlogCollected = Blog.isBlogCollected(blogBean.getId());
                subscriber.onNext(isBlogCollected);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        favouriteItem.setIcon(aBoolean ? R.drawable.favourite : R.drawable.favourite_empty);
                        isFavorite = aBoolean;
                        Log.d(TAG, "call: isFavorite " + isFavorite);
                    }
                });


        return true;
    }
}
