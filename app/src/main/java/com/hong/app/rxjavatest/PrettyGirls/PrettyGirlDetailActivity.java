package com.hong.app.rxjavatest.PrettyGirls;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.CustomViews.gesture_imageview.GestureImageView;
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.Utils.FileUtil;
import com.hong.app.rxjavatest.network.OKHttpHelper;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/16.
 */
@SuppressLint("LongLogTag")
public class PrettyGirlDetailActivity extends AppCompatActivity {

    private static final String TAG = "PrettyGirlDetailActivity";

    public static final String EXTRA_PRETTY = "extar_beauty";
    public static final String EXTRA_SOURCE = "extar_source";

    public static final int EXTRA_SOURCE_COLLECTION = 2;


    @Bind(R.id.beauty_image)
    GestureImageView beautyImage;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    BlogBean prettyBean;
    String beautyImageUrlStr;
    private boolean isImageStared = false;
    private boolean fromCollection = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fromCollection = (getIntent().getIntExtra(EXTRA_SOURCE, 0) == EXTRA_SOURCE_COLLECTION);

        if (fromCollection) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            String filePath = getIntent().getStringExtra(EXTRA_PRETTY);
            Glide.with(this).load(new File(filePath)).into(beautyImage);
        } else {
            prettyBean = getIntent().getParcelableExtra(EXTRA_PRETTY);
            beautyImageUrlStr = prettyBean.getUrl();
        }

    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("保存图片")
                .setMessage("保存此图片到您的手机上？")
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Observable.create(new Observable.OnSubscribe<Bitmap>() {
                            @Override
                            public void call(Subscriber<? super Bitmap> subscriber) {
                                Bitmap bitmap = OKHttpHelper.getBitmapfromUrl(beautyImageUrlStr);
                                FileUtil.saveBitmapIntoFile(bitmap, FileUtil.PUBLIC_IMAGE_STORAGE_DIR, prettyBean.getDescription());
                                subscriber.onCompleted();
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Bitmap>() {
                                    @Override
                                    public void onCompleted() {
                                        Toast.makeText(PrettyGirlDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(PrettyGirlDetailActivity.this, "保存失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNext(Bitmap bitmap) {

                                    }
                                });

                    }
                })
                .setNegativeButton("否", null)
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            showSaveDialog();
        } else if (itemId == R.id.action_star) {
            if (isImageStared) {
                deletImageFromCache(item);
            } else {
                saveImageIntoCache(item);
            }
        } else if (itemId == R.id.action_see_all) {
            startActivity(new Intent(PrettyGirlDetailActivity.this, PrettyGirlCollectionActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void deletImageFromCache(final MenuItem item) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                FileUtil.deleteImage(FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getDescription());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(PrettyGirlDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.star_empty);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PrettyGirlDetailActivity.this, "取消收藏失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    private void saveImageIntoCache(final MenuItem item) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = OKHttpHelper.getBitmapfromUrl(beautyImageUrlStr);
                FileUtil.saveBitmapIntoFile(bitmap, FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getDescription());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(PrettyGirlDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.star_full);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PrettyGirlDetailActivity.this, "收藏失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fromCollection) {
            return super.onCreateOptionsMenu(menu);
        } else {
            this.getMenuInflater().inflate(R.menu.menu_pretty, menu);
            final MenuItem item = menu.findItem(R.id.action_star);

            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    boolean isImageFileExist = FileUtil.isImageFileExist(FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getDescription());
                    Log.d(TAG, "isImageFileExist: " + isImageFileExist);
                    isImageStared = isImageFileExist;
                    subscriber.onNext(isImageFileExist);
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

                        }

                        @Override
                        public void onNext(Boolean isImageFileExist) {
                            if (isImageFileExist) {
                                item.setIcon(R.drawable.star_full);
                                File file = FileUtil.getImageFile(FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getDescription());
                                Glide.with(PrettyGirlDetailActivity.this).load(file).into(beautyImage);
                            } else {
                                item.setIcon(R.drawable.star_empty);
                                Glide.with(PrettyGirlDetailActivity.this).load(beautyImageUrlStr).into(beautyImage);
                            }
                        }
                    });

            return true;
        }
    }
}

