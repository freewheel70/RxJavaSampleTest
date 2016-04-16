package com.hong.app.rxjavatest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hong.app.rxjavatest.PrettyGirls.PrettyGirlDataDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class NetworkHelper {

    private static final String TAG = "NetworkHelper";

    public static Bitmap getBitmapfromUrl(String urlString) {
        Log.d(TAG, "getBitmapfromUrl() called with: " + "urlString = [" + urlString + "]");
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<String> requestCityList() {
        try {
            URL url = new URL("http://www.amoyhouse.com:7777/city");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuilder resultBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }

            String result = resultBuilder.toString();

            List<String> cityList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");
                cityList.add(name);
            }

            return cityList;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<String> getBeautyList(int size,int page){

        try {
            URL url = new URL("https://gank.io/api/data/%E7%A6%8F%E5%88%A9/"+size+"/"+page);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line=reader.readLine())!=null){
                builder.append(line);
            }

            String result = builder.toString();

            return PrettyGirlDataDeserializer.deserializer(result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
