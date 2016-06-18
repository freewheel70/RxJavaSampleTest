package com.hong.app.freegank.pretty_girls;

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
import com.hong.app.freegank.R;
import com.hong.app.freegank.blogs.BlogBean;
import com.hong.app.freegank.custom_views.gesture_imageview.GestureImageView;
import com.hong.app.freegank.network.OKHttpHelper;
import com.hong.app.freegank.utils.FileUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Freewheel on 2016/4/16.
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

    private BlogBean prettyBean;
    private String beautyImageUrlStr;
    private String filePath;
    private boolean isImageStared = false;
    private boolean fromCollection = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fromCollection = (getIntent().getIntExtra(EXTRA_SOURCE, 0) == EXTRA_SOURCE_COLLECTION);

        if (fromCollection) {
            filePath = getIntent().getStringExtra(EXTRA_PRETTY);
            Glide.with(this).load(new File(filePath)).into(beautyImage);
            getSupportActionBar().setTitle(R.string.favourite);

            isImageStared = true;

        } else {
            prettyBean = getIntent().getParcelableExtra(EXTRA_PRETTY);
            beautyImageUrlStr = prettyBean.getUrl();
            getSupportActionBar().setTitle(prettyBean.getDescription());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            showSaveDialog();
        } else if (itemId == R.id.action_star) {
            if (isImageStared) {
                deleteImage(item);
            } else {
                saveImageIntoCache(item);
            }
        } else if (itemId == R.id.action_see_all) {
            startActivity(new Intent(PrettyGirlDetailActivity.this, PrettyGirlCollectionActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_image_title)
                .setMessage(R.string.save_image_hint)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (fromCollection) {
                            savePictureFromCache();
                        } else {
                            savePictureFromNetwork();
                        }

                    }
                })
                .setNegativeButton("否", null)
                .show();
    }

    private void savePictureFromCache() {
        savePicture(new PictureSaveAction() {
            @Override
            public boolean save() {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//                File file = new File(filePath);
//                FileUtil.saveBitmapIntoFile(bitmap, FileUtil.PUBLIC_IMAGE_STORAGE_DIR, file.getName());

                File file = new File(filePath);
                return FileUtil.copyFile(file, new File(FileUtil.PUBLIC_IMAGE_STORAGE_DIR, file.getName()));

            }
        });
    }

    private void savePictureFromNetwork() {

        savePicture(new PictureSaveAction() {
            @Override
            public boolean save() {
                Bitmap bitmap = OKHttpHelper.getBitmapfromUrl(beautyImageUrlStr);
                return FileUtil.saveBitmapIntoFile(bitmap, FileUtil.PUBLIC_IMAGE_STORAGE_DIR, prettyBean.getImageName());
            }
        });
    }

    private void savePicture(final PictureSaveAction saveAction) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                if (saveAction.save()) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("save error"));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(PrettyGirlDetailActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PrettyGirlDetailActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {

                    }
                });
    }

    private interface PictureSaveAction {
        boolean save();
    }

    private void deleteImage(MenuItem item) {
        if (fromCollection) {
            exitWithDeletionPath();
        } else {
            deletImageFromCache(item);
        }
    }

    private void exitWithDeletionPath() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRETTY, filePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void deletImageFromCache(final MenuItem item) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                FileUtil.deleteImage(FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getImageName());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(PrettyGirlDetailActivity.this, R.string.remove_favourite_success, Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.star_empty);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PrettyGirlDetailActivity.this, R.string.remove_favourite_fail, Toast.LENGTH_SHORT).show();
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
                if (FileUtil.saveBitmapIntoFile(bitmap, FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getImageName())) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("save error"));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(PrettyGirlDetailActivity.this, R.string.add_favourite_success, Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.star_full);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PrettyGirlDetailActivity.this, R.string.add_favourite_fail, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fromCollection) {
            this.getMenuInflater().inflate(R.menu.menu_pretty2, menu);
            return true;
        } else {
            this.getMenuInflater().inflate(R.menu.menu_pretty, menu);
            final MenuItem item = menu.findItem(R.id.action_star);

            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    boolean isImageFileExist = FileUtil.isImageFileExist(FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getImageName());
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
                                File file = FileUtil.getImageFile(FileUtil.PRIVATE_IMAGE_STORAGE_DIR, prettyBean.getImageName());
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

