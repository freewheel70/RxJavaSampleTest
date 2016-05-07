package com.hong.app.rxjavatest.Blogs;

import com.hong.app.rxjavatest.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BeanDeserializer {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.ENGLISH);

    public static List<BlogBean> deserialize(String beanString) throws JSONException, ParseException {

        List<BlogBean> blogBeanList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(beanString);
        JSONArray jsonArray = jsonObject.getJSONArray(Constant.JSON_KEY_GANK_BLOG_ARRAY_RESULT);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            BlogBean bean = new BlogBean();
            bean.setId(object.optString(Constant.JSON_KEY_GANK_BLOG_ID));
            bean.setUrl(object.optString(Constant.JSON_KEY_GANK_BLOG_URL));
            bean.setDescription(object.optString(Constant.JSON_KEY_GANK_BLOG_DESC));
            bean.setWho(object.optString(Constant.JSON_KEY_GANK_BLOG_AUTHOR));
            bean.setType(object.optString(Constant.JSON_KEY_GANK_BLOG_TYPE));

            String dateStr = object.optString(Constant.JSON_KEY_GANK_BLOG_PUBLISH_TIME);
            Date date = dateFormat.parse(dateStr);
            bean.setPublishedAt(date);

            blogBeanList.add(bean);
        }

        return blogBeanList;
    }

}
