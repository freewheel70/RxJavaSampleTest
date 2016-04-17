package com.hong.app.rxjavatest.Blogs;

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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BlogBrowserActivity extends AppCompatActivity {

    private static final String TAG = "BlogBrowserActivity";

    public static final String EXTRA_URL = "blog_url";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.webview)
    WebView webview;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    String urlStr;
    private static final CharSequence LABEL_FreeGank = "freeGank";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_browser);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initWebview();
        urlStr = getIntent().getStringExtra(EXTRA_URL);
        webview.loadUrl(urlStr);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.copy_url:
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(LABEL_FreeGank, urlStr);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this,R.string.has_copy_url,Toast.LENGTH_SHORT).show();

                break;
            case R.id.open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlStr));
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_blog_browser, menu);
        return true;
    }
}
