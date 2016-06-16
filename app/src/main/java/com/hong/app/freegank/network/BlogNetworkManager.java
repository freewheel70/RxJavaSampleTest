package com.hong.app.freegank.network;

import com.hong.app.freegank.Constant;
import com.hong.app.freegank.database.Blog;
import com.hong.app.freegank.database.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Freewheel on 2016/4/20.
 */
public class BlogNetworkManager {


    public static NetworkResponseResult uploadNonSyncedBlogs() {

        List<Blog> blogList = Blog.getAllNonSyncedFavouriteBlogs();

        try {

            JSONObject jsonObject = new JSONObject();
            User user = User.getUser();
            jsonObject.put(Constant.JSON_KEY_USER_NAME, user.username);
            jsonObject.put(Constant.JSON_KEY_PASSWORD, user.password);

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < blogList.size(); i++) {
                JSONObject object = blogList.get(i).toJSONObject();
                jsonArray.put(object);
            }

            jsonObject.put("blogs", jsonArray);

            String response = OKHttpHelper.postJson(NetworkURLConstant.UPLOAD_BLOGS_URL, jsonObject.toString());

            NetworkResponseResult responseResult = OKHttpHelper.deserializeResponse(response);

            if (responseResult.isSuccess()) {
                for (int i = 0; i < blogList.size(); i++) {
                    Blog blog = blogList.get(i);
                    blog.isSynced = true;
                    blog.save();
                }
            }

            return responseResult;

        } catch (Exception e) {
            e.printStackTrace();
            return new NetworkResponseResult(e.getMessage(), false);
        }
    }

    public static NetworkResponseResult getMyBlogs() {

        try {
            JSONObject jsonObject = new JSONObject();
            User user = User.getUser();
            jsonObject.put(Constant.JSON_KEY_USER_NAME, user.username);
            jsonObject.put(Constant.JSON_KEY_PASSWORD, user.password);

            String response = OKHttpHelper.postJson(NetworkURLConstant.GET_MY_BLOGS_URL, jsonObject.toString());

            NetworkLogger.printMessageIfDebug("getMyBlogs response :" + response);

            JSONObject responseObject = new JSONObject(response);
            if (responseObject.getInt("status") != 200) {
                throw new Exception(responseObject.getString("message"));
            } else {
                JSONArray jsonArray = responseObject.getJSONArray("blogs");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Blog.createBlogFromJsonObject(object);
                }
            }

            NetworkResponseResult responseResult = new NetworkResponseResult("OK",true);

            return responseResult;

        } catch (Exception e) {
            e.printStackTrace();
            return new NetworkResponseResult(e.getMessage(),false);
        }

    }

}
