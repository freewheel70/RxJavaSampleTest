package com.hong.app.freegank.utils;

/**
 * Created by Freewheel on 2016/4/17.
 */
public class UrlUtil {

    public static String extraSourceFromUrlStr(String urlStr){
        if (urlStr.contains("github")){
            return "Github";
        }else if (urlStr.contains("jianshu")){
            return "简书";
        }else if (urlStr.contains("weixin")){
            return "微信";
        }else {
            return "博客";
        }
    }
}
