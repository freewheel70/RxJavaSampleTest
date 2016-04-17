package com.hong.app.rxjavatest.PrettyGirls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/16.
 */
public class PrettyGirlDataDeserializer {

    public static List<String> deserialize(String jsonString) throws JSONException {
        List<String> imageStringList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            imageStringList.add(jsonArray.getJSONObject(i).getString("url"));
        }

        return imageStringList;
    }
}
