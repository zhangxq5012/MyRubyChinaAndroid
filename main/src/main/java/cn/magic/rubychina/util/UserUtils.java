package cn.magic.rubychina.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import cn.magic.rubychina.app.RubyApplication;

/**
 * Created by magic on 2014/8/25.
 */
public class UserUtils {
    private static final String USERTAG = "user";
    private static final String PRIVATE_TOKEN = "private_token";
    private static final String LOGIN="login";
    private static final String EMAIL="email";
    public static final String TOKEN="token";

    public static Context getAppContext() {
        return RubyApplication.getContext();
    }

    public static void saveUserLogin(String login) {
        SharedPreferences.Editor editor = getUserPreference().edit();
        editor.putString(LOGIN,login);
        editor.commit();
    }

    public static void saveUserToken(String private_token) {
        SharedPreferences.Editor editor = getUserPreference().edit();
        editor.putString(PRIVATE_TOKEN,private_token);
        editor.commit();
    }

    public static void saveUserEmail(String email) {
        SharedPreferences.Editor editor = getUserPreference().edit();
        editor.putString(EMAIL,email);
        editor.commit();
    }

    public static String getUserLogin() {
        return getUserPreference().getString(LOGIN, "");
    }

    public static String getUserToken() {
        return getUserPreference().getString(PRIVATE_TOKEN, "");
    }

    public static String getUserEmail() {
        return getUserPreference().getString(EMAIL, "");
    }


    public static SharedPreferences getUserPreference() {
        return getAppContext().getSharedPreferences(USERTAG, Context.MODE_PRIVATE);
    }

    public static boolean clearUser() {
        return getUserPreference().edit().clear().commit();
    }

    public static boolean isLogined() {
        return getUserToken().length() > 0;
    }

    public static Map putToken(Map param){
        param.put(TOKEN,getUserToken());
        return param;
    }
}
