package com.xul.utils;

import com.xul.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name){
        String address = bundle.getString("test.url");
        String url = "";

        switch (name){
            case GETUSERLIST:
                url = address + bundle.getString("getUserList.uri");
                break;
            case LOGIN:
                url = address + bundle.getString("login.uri");
                break;
            case UPDATEUSERINFO:
                url = address + bundle.getString("updateUserInfo.uri");
                break;
            case GETUSERINFO:
                url = address + bundle.getString("getUserInfo.uri");
                break;
            case ADDUSERINFO:
                url = address + bundle.getString("addUser.uri");
                break;
        }
        return url;
    }

}
