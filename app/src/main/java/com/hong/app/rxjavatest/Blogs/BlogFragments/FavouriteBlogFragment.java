package com.hong.app.rxjavatest.Blogs.BlogFragments;

import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.database.Blog;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class FavouriteBlogFragment extends BaseBlogFragment {

    private static final String TAG = "FavouriteBlogFragment";

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
}
