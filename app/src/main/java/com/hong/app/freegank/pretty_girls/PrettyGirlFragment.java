package com.hong.app.freegank.pretty_girls;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hong.app.freegank.BasePageFragment;
import com.hong.app.freegank.blogs.BlogBean;
import com.hong.app.freegank.R;
import com.hong.app.freegank.network.GankNetworkManager;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Freewheel on 2016/4/16.
 */
public class PrettyGirlFragment extends BasePageFragment {

    private static final String TAG = "PrettyGirlFragment";

    public PrettyGirlFragment() {
        super();
    }

    @Override
    protected int getContainerViewId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void onRecyclerViewItemClick(int pos) {
        BlogBean blogBean = getBlogBeanList().get(pos);
        Intent intent = new Intent(getActivity(), PrettyGirlDetailActivity.class);
        intent.putExtra(PrettyGirlDetailActivity.EXTRA_PRETTY, blogBean);
        startActivity(intent);
    }

    @Override
    protected void addItemDecoration() {
        //do nothing temporarily
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getBlogBeanList().size() == 0) {
            noContentWarning.setVisibility(View.VISIBLE);
            sendRequest(true);
        }
    }

    @Override
    protected void initLayoutManager() {
        setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getLayoutManager().setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(getLayoutManager());
    }

    @Override
    protected void initAdapter() {
       setAdapter( new PrettyAdapter(getActivity(), getInflater(), getBlogBeanList()));
        recyclerView.setAdapter(getAdapter());
    }

    @Override
    protected boolean sendRequest(final boolean isRefreshing) {

        if (!super.sendRequest(isRefreshing)) {
            return false;
        }

        Observable
                .create(new Observable.OnSubscribe<List<BlogBean>>() {
                    @Override
                    public void call(Subscriber<? super List<BlogBean>> subscriber) {
                        List<BlogBean> girlList = null;
                        try {
                            girlList = GankNetworkManager.getBlogList(GankNetworkManager.TYPE_WELFARE, getSIZE_OF_IMAGES_PER_REQUEST(), getCurrentPage());
                            subscriber.onNext(girlList);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BlogBean>>() {
                    @Override
                    public void onCompleted() {
                        if (getBlogBeanList().size() > 0) {
                            noContentWarning.setVisibility(View.INVISIBLE);
                        }
                        onItemsLoadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorMsg(e.getMessage());
                        enableRequest();
                        if (getBlogBeanList().size() == 0) {
                            noContentWarning.setVisibility(View.VISIBLE);
                        }
                        onItemsLoadComplete();
                    }

                    @Override
                    public void onNext(List<BlogBean> blogBeen) {
                        refreshDataList(blogBeen, isRefreshing);
                        refreshRecyclerView();
                        increasePageNum();
                        enableRequest();
                    }


                });

        return true;
    }


    @Override
    protected int getDataListSize() {
        return getBlogBeanList().size();
    }


}
