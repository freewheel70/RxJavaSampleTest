package com.hong.app.rxjavatest.network;

import com.hong.app.rxjavatest.database.Blog;
import com.hong.app.rxjavatest.database.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class BlogNetworkManager {

    private static final String UPLOAD_BLOGS_URL = "http://amoyhouse.com:9999/upload";
    private static final String GET_MY_BLOGS_URL = "http://amoyhouse.com:9999/mymarks";

    public static NetworkResponseResult uploadNonSyncedBlogs() {

        List<Blog> blogList = Blog.getAllNonSyncedFavouriteBlogs();


        try {

            JSONObject jsonObject = new JSONObject();
            User user = User.getUser();
            jsonObject.put("username", user.username);
            jsonObject.put("password", user.password);

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < blogList.size(); i++) {
                JSONObject object = blogList.get(i).toJSONObject();
                jsonArray.put(object);
            }

            jsonObject.put("marks", jsonArray);

            String response = OKHttpHelper.postJson(UPLOAD_BLOGS_URL, jsonObject.toString());

            return OKHttpHelper.deserializeResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
            return new NetworkResponseResult(e.getMessage(), false);
        }
    }

    public static void getMyBlogs() {

        try {
            JSONObject jsonObject = new JSONObject();
            User user = User.getUser();
            jsonObject.put("username", user.username);
            jsonObject.put("password", user.password);

            String response = OKHttpHelper.postJson(GET_MY_BLOGS_URL, jsonObject.toString());

            NetworkLogger.printMessageIfDebug("getMyBlogs response :" + response);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
