package com.hong.app.rxjavatest.network;

import com.hong.app.rxjavatest.Constant;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/19.
 */
public class AccountNetworkManager {

    public static NetworkResponseResult signup(String username, String password) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.JSON_KEY_USER_NAME, username);
            jsonObject.put(Constant.JSON_KEY_PASSWORD, password);

            String response = OKHttpHelper.postJson(NetworkURLConstant.SIGN_UP_URL_STR, jsonObject.toString());

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
            jsonObject.put(Constant.JSON_KEY_USER_NAME, username);
            jsonObject.put(Constant.JSON_KEY_PASSWORD, password);

            String response = OKHttpHelper.postJson(NetworkURLConstant.LOG_IN_URL_STR, jsonObject.toString());

            NetworkLogger.printMessageIfDebug("login response " + response);

            return OKHttpHelper.deserializeResponse(response);

        } catch (Exception e) {
            e.printStackTrace();

            return new NetworkResponseResult(e.getMessage(), false);
        }

    }


}
