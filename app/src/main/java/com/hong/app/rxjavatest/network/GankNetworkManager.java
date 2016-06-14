package com.hong.app.rxjavatest.network;

import android.support.annotation.StringDef;
import android.util.Log;

import com.hong.app.rxjavatest.blogs.BeanDeserializer;
import com.hong.app.rxjavatest.blogs.BlogBean;

import org.json.JSONException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Freewheel on 2016/4/20.
 */
public class GankNetworkManager {

    private static final String TAG = "GankNetworkManager";

    public static final String TYPE_ANDROID = "Android";
    public static final String TYPE_IOS = "iOS";
    public static final String TYPE_APP = "App";
    public static final String TYPE_COOL = "瞎推荐";
    public static final String TYPE_FRONT = "前端";
    public static final String TYPE_WELFARE = "福利";
    public static final String TYPE_RESOURCE = "拓展资源";
    public static final String TYPE_VIDEO = "休息视频";

    @StringDef({TYPE_ANDROID, TYPE_APP, TYPE_COOL, TYPE_FRONT, TYPE_IOS, TYPE_RESOURCE, TYPE_VIDEO, TYPE_WELFARE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GankType {
    }

    public static List<BlogBean> getBlogList(@GankType String type, int size, int page) throws JSONException, ParseException {

        String urlStr = generateUrlString(type, size, page);
        String response = OKHttpHelper.sendGetRequest(urlStr);

        Log.d(TAG, "getBlogList: response "+response);

        return BeanDeserializer.deserialize(response);
    }

    private static String generateUrlString(@GankType String type, int size, int page) {
        return "http://gank.io/api/data/" + type + "/" + size + "/" + page;
    }


}
