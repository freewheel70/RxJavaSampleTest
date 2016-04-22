package com.hong.app.rxjavatest.Blogs.BlogFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.Events.ServerSyncBlogEvent;
import com.hong.app.rxjavatest.database.Blog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class FavouriteBlogFragment extends BaseBlogFragment {

    private static final String TAG = "FavouriteBlogFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    protected List<BlogBean> requestBlogList() {
        return BlogBean.convertBlogList(Blog.getAllNonRemovedFavouriteBlogs());
    }

    protected void refreshDataList(List<BlogBean> beanList) {
        blogBeanList.clear();
        blogBeanList.addAll(beanList);
    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest();
    }

    @Subscribe
    public void refreshListDueToNetworkSync(ServerSyncBlogEvent event){
        sendRequest();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
