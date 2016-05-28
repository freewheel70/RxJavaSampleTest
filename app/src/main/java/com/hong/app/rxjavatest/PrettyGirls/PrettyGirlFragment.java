package com.hong.app.rxjavatest.PrettyGirls;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hong.app.rxjavatest.BasePageFragment;
import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.network.GankNetworkManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/16.
 */
public class PrettyGirlFragment extends BasePageFragment {

    private static final String TAG = "PrettyGirlFragment";

    List<BlogBean> prettyList;

    public PrettyGirlFragment() {
        super();
        prettyList = new ArrayList<>();
    }

    @Override
    protected int getContainerViewId() {
        return R.layout.fragment_base_list;
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
        if (prettyList.size() == 0) {
            noContentWarning.setVisibility(View.VISIBLE);
            sendRequest();
        }
    }

    @Override
    protected void initLayoutManager() {
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initAdapter() {
        adapter = new PrettyAdapter(getActivity(),inflater,prettyList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected boolean sendRequest() {

        if(!super.sendRequest()){
            return false;
        }

        Observable
                .create(new Observable.OnSubscribe<List<BlogBean>>() {
                    @Override
                    public void call(Subscriber<? super List<BlogBean>> subscriber) {
                        List<BlogBean> girlList = null;
                        try {
                            girlList = GankNetworkManager.getBlogList(GankNetworkManager.TYPE_WELFARE, SIZE_OF_IMAGES_PER_REQUEST, currentPage);
                            subscriber.onNext(girlList);
                            subscriber.onCompleted();
                        }  catch (Exception e) {
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
                        if (prettyList.size() > 0) {
                            noContentWarning.setVisibility(View.INVISIBLE);
                        }
                        onItemsLoadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorMsg(e.getMessage());
                        enableRequest();
                        if (prettyList.size() == 0) {
                            noContentWarning.setVisibility(View.VISIBLE);
                        }
                        onItemsLoadComplete();
                    }

                    @Override
                    public void onNext(List<BlogBean> blogBeen) {
                        prettyList.addAll(blogBeen);
                        refreshRecyclerView();
                    }


                });

        return true;
    }




    @Override
    protected int getDataListSize() {
        return prettyList.size();
    }


}
