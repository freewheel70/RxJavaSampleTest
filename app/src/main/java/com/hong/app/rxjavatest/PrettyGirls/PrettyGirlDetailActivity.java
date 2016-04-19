package com.hong.app.rxjavatest.PrettyGirls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hong.app.rxjavatest.CustomViews.gesture_imageview.GestureImageView;
import com.hong.app.rxjavatest.network.NetworkHelper;
import com.hong.app.rxjavatest.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/16.
 */
public class PrettyGirlDetailActivity extends AppCompatActivity {

    public static final String EXTRA_BEAUTY_IMG = "beauty_image";

    @Bind(R.id.beauty_image)
    GestureImageView beautyImage;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    String beautyImageUrlStr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        beautyImageUrlStr = getIntent().getStringExtra(EXTRA_BEAUTY_IMG);
        Glide.with(this).load(beautyImageUrlStr).into(beautyImage);

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
                                Bitmap bitmap = NetworkHelper.getBitmapfromUrl(beautyImageUrlStr);
                                saveBitmapIntoFile(bitmap);
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

    private void saveBitmapIntoFile(Bitmap bitmap) {

        String dirStr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pretty";
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //create a file to write bitmap data
        File file = new File(dir, new Date().getTime() + ".png");
        FileOutputStream fos = null;
        try {
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showSaveDialog();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_pretty, menu);
        return true;
    }
}

