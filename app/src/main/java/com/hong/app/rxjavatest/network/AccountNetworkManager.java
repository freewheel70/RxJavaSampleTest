package com.hong.app.rxjavatest.network;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/19.
 */
public class AccountNetworkManager {


    private static final String SIGN_UP_YRL_STR = "http://amoyhouse.com:9999/signup";

    public static String signup(String username, String password) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);


//            StringBuilder builder = new StringBuilder();
//            builder.append("username=");
//            builder.append(username);
//            builder.append("&");
//            builder.append("password=");
//            builder.append(password);

            String response = NetworkHelper.sendNonGetRequest("POST", jsonObject.toString(), SIGN_UP_YRL_STR);

            NetworkLogger.printMessageIfDebug("signup response " + response);

            return response;

        } catch (Exception e) {
            e.printStackTrace();

            return "signup fail : " + e.getMessage();
        }

    }


}
