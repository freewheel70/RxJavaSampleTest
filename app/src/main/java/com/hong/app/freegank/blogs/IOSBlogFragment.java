package com.hong.app.freegank.blogs;

import com.hong.app.freegank.network.GankNetworkManager;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Freewheel on 2016/4/17.
 */
public class IOSBlogFragment extends BaseBlogFragment {

    private static final String TAG = "AndroidBlogFragment";

    @Override
    protected List<BlogBean> requestBlogList() throws JSONException, ParseException {
        return GankNetworkManager.getBlogList(GankNetworkManager.TYPE_IOS, getSIZE_OF_IMAGES_PER_REQUEST(), getCurrentPage());
    }
}
