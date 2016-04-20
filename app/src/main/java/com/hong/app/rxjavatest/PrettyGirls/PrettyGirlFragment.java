package com.hong.app.rxjavatest.PrettyGirls;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hong.app.rxjavatest.BasePageFragment;
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.network.GankNetworkManager;

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
public class PrettyGirlFragment extends BasePageFragment {

    private static final String TAG = "PrettyGirlFragment";

    List<String> imageUrlStrList = new ArrayList<>();

    @Override
    protected int getContainerViewId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initLayoutManager() {
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initAdapter() {
        adapter = new BeautyAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void sendRequest() {
        Observable
                .create(new Observable.OnSubscribe<List<String>>() {

                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        List<String> beautyList = GankNetworkManager.getBeautyList(SIZE_OF_IMAGES_PER_REQUEST, currentPage);
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
                        refreshRecyclerView();

                    }
                });
    }


    @Override
    protected int getDataListSize() {
        return imageUrlStrList.size();
    }


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

            Glide.with(PrettyGirlFragment.this)
                    .load(imageStr)
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PrettyGirlDetailActivity.class);
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
