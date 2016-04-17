package com.hong.app.rxjavatest.Blogs.BlogFragments;

import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.NetworkHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class VideoBlogFragment extends BaseBlogFragment {

    private static final String TAG = "AndroidBlogFragment";

    @Override
    protected List<BlogBean> requestBlogList() {
        return NetworkHelper.getBlogList("休息视频", SIZE_OF_IMAGES_PER_REQUEST, currentPage);
    }
}
