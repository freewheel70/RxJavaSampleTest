package com.hong.app.rxjavatest.Blogs;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.hong.app.rxjavatest.BasePageFragment;
import com.hong.app.rxjavatest.Blogs.BlogAdapter;
import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.Views.SimpleRecyclerViewItemDecoration;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/17.
 */
public abstract class BaseBlogFragment extends BasePageFragment {

    private static final String TAG = "BaseBlogFragment";

    protected List<BlogBean> blogBeanList = new ArrayList<>();

    @Override
    protected int getContainerViewId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void addItemDecoration() {
        recyclerView.addItemDecoration(new SimpleRecyclerViewItemDecoration());
    }

    @Override
    protected int getDataListSize() {
        return blogBeanList.size();
    }

    @Override
    protected void initLayoutManager() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initAdapter() {

        adapter = new BlogAdapter(getActivity(), inflater, blogBeanList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged() called with: " + "hidden = [" + hidden + "]");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (blogBeanList.size() == 0) {
            noContentWarning.setVisibility(View.VISIBLE);
            sendRequest();
        }
    }

    @Override
    protected boolean sendRequest() {

        if (!super.sendRequest()){
            return false;
        }

        Observable.create(new rx.Observable.OnSubscribe<List<BlogBean>>() {
            @Override
            public void call(Subscriber<? super List<BlogBean>> subscriber) {
                List<BlogBean> blogList = null;
                try {
                    blogList = requestBlogList();
                    subscriber.onNext(blogList);
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
                        if (blogBeanList.size() > 0) {
                            noContentWarning.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorMsg(e.getMessage());
                        enableRequest();
                        if (blogBeanList.size() == 0) {
                            noContentWarning.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNext(List<BlogBean> beanList) {
                        refreshDataList(beanList);
                        refreshRecyclerView();
                    }
                });

        return true;
    }

    protected void refreshDataList(List<BlogBean> beanList) {
        blogBeanList.addAll(beanList);
    }

    protected abstract List<BlogBean> requestBlogList() throws JSONException, ParseException;


}