package com.hong.app.rxjavatest.network;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/19.
 */
public class AccountNetworkManager {


    private static final String SIGN_UP_URL_STR = "http://amoyhouse.com:9999/signup";
    private static final String LOG_IN_URL_STR =  "http://amoyhouse.com:9999/login";

    public static NetworkResponseResult signup(String username, String password) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            String response = OKHttpHelper.postJson(SIGN_UP_URL_STR, jsonObject.toString());

            NetworkLogger.printMessageIfDebug("signup response " + response);

            return OKHttpHelper.deserializeResponse(response);

        } catch (Exception e) {
            e.printStackTrace();

            return new NetworkResponseResult(e.getMessage(), false);
        }

    }


    public static NetworkResponseResult login(String username, String password) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            String response = OKHttpHelper.postJson(LOG_IN_URL_STR, jsonObject.toString());

            NetworkLogger.printMessageIfDebug("login response " + response);

            return OKHttpHelper.deserializeResponse(response);

        } catch (Exception e) {
            e.printStackTrace();

            return new NetworkResponseResult(e.getMessage(), false);
        }

    }


}
