package com.hong.app.rxjavatest.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Freewheel on 2016/4/20.
 */
public class OKHttpHelper {

    private static final String TAG = "OKHttpHelper";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static String sendGetRequest(String urlStr) {

        Request request = new Request.Builder()
                .url(urlStr)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postJson(String url, String json) throws IOException {

        Log.d(TAG, "postJson() called with: " + "url = [" + url + "], json = [" + json + "]");

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static Bitmap getBitmapfromUrl(String urlString) {
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try {
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NetworkResponseResult deserializeResponse(String response) throws JSONException {
        Log.d(TAG, "deserializeResponse() called with: " + "response = [" + response + "]");

        JSONObject jsonObject = new JSONObject(response);

        int status = jsonObject.getInt("status");
        String message = jsonObject.getString("message");

        NetworkResponseResult responseResult = new NetworkResponseResult(message, status == 200);

        return responseResult;
    }
}
