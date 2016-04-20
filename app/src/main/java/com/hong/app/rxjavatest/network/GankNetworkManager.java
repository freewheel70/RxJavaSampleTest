package com.hong.app.rxjavatest.network;

import com.hong.app.rxjavatest.Blogs.BeanDeserializer;
import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlDataDeserializer;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class GankNetworkManager {


    public static List<BlogBean> getBlogList(String type, int size, int page) {

        String urlStr = "https://gank.io/api/data/" + type + "/" + size + "/" + page;
        String response = OKHttpHelper.sendGetRequest(urlStr);

        try {
            return BeanDeserializer.deserialize(response);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();

    }

    public static List<String> getBeautyList(int size, int page) {

        String urlStr = "https://gank.io/api/data/%E7%A6%8F%E5%88%A9/" + size + "/" + page;
        String response = OKHttpHelper.sendGetRequest(urlStr);

        try {
            return PrettyGirlDataDeserializer.deserialize(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
