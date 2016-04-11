package com.hong.app.rxjavatest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.image)
    ImageView imageView;

    ProgressDialog progressDialog;

    Subscriber subscriber = new Subscriber<Bitmap>() {

        @Override
        public void onStart() {
            super.onStart();
            showProgressDialog();
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        @Override
        public void onCompleted() {

        }


        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: " + e.getLocalizedMessage());
            dismissProgressDialog();
        }

        @Override
        public void onNext(Bitmap bitmap) {
            dismissProgressDialog();
            imageView.setImageBitmap(bitmap);
        }
    };


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initViews() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imageUrl = "http://amoyhouse.com:7788/one_hit.jpg";
                ImageLoader.getInstance().loadImage(imageUrl, subscriber);

            }
        });
    }


}
