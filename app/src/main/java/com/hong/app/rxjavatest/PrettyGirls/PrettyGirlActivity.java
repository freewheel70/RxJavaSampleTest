package com.hong.app.rxjavatest.PrettyGirls;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.hong.app.rxjavatest.NetworkHelper;
import com.hong.app.rxjavatest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/16.
 */
public class PrettyGirlActivity extends AppCompatActivity {

    private static final String TAG = "PrettyGirlActivity";

    List<String> imageUrlStrList = new ArrayList<>();

    @Bind(R.id.beauty_list)
    RecyclerView beautyList;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.center_progress_bar)
    ProgressBar centerProgressBar;

    BeautyAdapter adapter;
    LayoutInflater inflater;
    GridLayoutManager gridLayoutManager;

    int SIZE_OF_IMAGES_PER_REQUEST = 10;
    int currentPage = 1;
    boolean hasInitFirstPage = false;
    boolean isRequesting = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_girl);
        ButterKnife.bind(this);
        inflater = LayoutInflater.from(this);
        initRecyclerView();
        initViews();

        requestOneMorePage();
    }

    private void initRecyclerView() {
        beautyList.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        beautyList.setLayoutManager(gridLayoutManager);

        beautyList.setItemAnimator(new DefaultItemAnimator());

        adapter = new BeautyAdapter();
        beautyList.setAdapter(adapter);

        beautyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled() called with: " + "recyclerView = [" + recyclerView + "], dx = [" + dx + "], dy = [" + dy + "]");
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!hasInitFirstPage) {
                    return;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (gridLayoutManager.findLastVisibleItemPosition() == imageUrlStrList.size() - 1) {
                        Log.d(TAG, "onScrollStateChanged: lastvisibleitem " + gridLayoutManager.findLastVisibleItemPosition());
                        if (!isRequesting) {
                            showProgressbar();
                            requestOneMorePage();
                        }
                    }
                }

            }
        });
    }

    private void initViews() {

    }

    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        if (!hasInitFirstPage) {
            Log.d(TAG, "hideProgressbar: hide center progress bar");
            centerProgressBar.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void requestOneMorePage() {

        isRequesting = true;

        Observable
                .create(new Observable.OnSubscribe<List<String>>() {

                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        List<String> beautyList = NetworkHelper.getBeautyList(SIZE_OF_IMAGES_PER_REQUEST, currentPage);
                        subscriber.onNext(beautyList);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        enableRequest();
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        imageUrlStrList.addAll(strings);
                        adapter.notifyDataSetChanged();
                        currentPage++;
                        enableRequest();

                    }
                });
    }

    private void enableRequest() {
        isRequesting = false;
        hideProgressbar();
        hasInitFirstPage = true;
    }

    private View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String imageUrl = (String) v.getTag();
            if (imageUrl != null) {

            }
        }
    };

    public class BeautyAdapter extends RecyclerView.Adapter<BeautyViewholder> {

        @Override
        public BeautyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_beauty, parent, false);
            BeautyViewholder viewholder = new BeautyViewholder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(BeautyViewholder holder, int position) {

            final String imageStr = imageUrlStrList.get(position);

            Glide.with(PrettyGirlActivity.this)
                    .load(imageStr)
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PrettyGirlActivity.this, PrettyGirlDetailActivity.class);
                    intent.putExtra(PrettyGirlDetailActivity.EXTRA_BEAUTY_IMG, imageStr);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageUrlStrList.size();
        }
    }

    public class BeautyViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        ImageView image;

        public BeautyViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
