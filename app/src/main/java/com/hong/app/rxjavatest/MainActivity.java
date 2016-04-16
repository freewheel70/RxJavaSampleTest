package com.hong.app.rxjavatest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.image)
    ImageView imageView;

    @Bind(R.id.city_label)
    TextView cityLabel;

    ProgressDialog progressDialog;

    Subscriber imageSubscriber = new Subscriber<Bitmap>() {

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

    Subscriber citySubscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            cityLabel.append(s+" ");
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

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(new CharSequence[]{"Requetest Image", "Request List","Go to beauty page"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String imageUrl = "http://amoyhouse.com:7788/one_hit.jpg";
                                ImageLoader.getInstance().loadImage(imageUrl, imageSubscriber);
                                break;
                            case 1:
                                CityRequester.loadCityList(citySubscriber);
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, PrettyGirlActivity.class));
                            default:
                                break;

                        }
                    }
                }).show();


            }
        });
    }


}
