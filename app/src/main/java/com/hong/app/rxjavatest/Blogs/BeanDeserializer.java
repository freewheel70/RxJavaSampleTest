package com.hong.app.rxjavatest.Blogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BeanDeserializer {

    public static List<BlogBean> deserialize(String beanString) throws JSONException {

        List<BlogBean> blogBeanList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(beanString);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            BlogBean bean = new BlogBean();
            bean.setUrl(object.optString("url"));
            bean.setDescription(object.optString("desc"));
            bean.setWho(object.optString("who"));
            bean.setPublishedAt(object.optString("publishedAt"));

            blogBeanList.add(bean);
        }

        return blogBeanList;
    }

}
