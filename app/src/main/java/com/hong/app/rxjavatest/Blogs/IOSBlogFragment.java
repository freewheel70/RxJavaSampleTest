package com.hong.app.rxjavatest.Blogs;

import com.hong.app.rxjavatest.network.GankNetworkManager;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class IOSBlogFragment extends BaseBlogFragment {

    private static final String TAG = "AndroidBlogFragment";

    @Override
    protected List<BlogBean> requestBlogList() throws JSONException, ParseException {
        return GankNetworkManager.getBlogList(GankNetworkManager.TYPE_IOS, getSIZE_OF_IMAGES_PER_REQUEST(), getCurrentPage());
    }
}
