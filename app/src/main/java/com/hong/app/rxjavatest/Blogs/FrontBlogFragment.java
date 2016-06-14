package com.hong.app.rxjavatest.blogs;

import com.hong.app.rxjavatest.network.GankNetworkManager;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Freewheel on 2016/4/17.
 */
public class FrontBlogFragment extends BaseBlogFragment {

    private static final String TAG = "AndroidBlogFragment";

    @Override
    protected List<BlogBean> requestBlogList() throws JSONException, ParseException {
        return GankNetworkManager.getBlogList(GankNetworkManager.TYPE_FRONT,  getSIZE_OF_IMAGES_PER_REQUEST(), getCurrentPage());
    }
}
