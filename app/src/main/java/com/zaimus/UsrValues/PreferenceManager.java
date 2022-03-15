package com.zaimus.UsrValues;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static void saveUpgrade(String upgrade, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);

        preferences.edit().putString("app.upgrade", upgrade).commit();
    }

    public static void saveUpdate(String update, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);

        preferences.edit().putString("app.update", update).commit();
    }

    public static String getUpdate(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);
        String update = preferences.getString("app.update", "");
        return update;
    }

    public static void saveCustUpdate(String custupdate, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);

        preferences.edit().putString("app.custupdate", custupdate).commit();
    }

    public static String getCustUpdate(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);
        String update = preferences.getString("app.custupdate", "");
        return update;
    }

    public static String getUpgrade(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);
        String upgrade = preferences.getString("app.upgrade", "");
        return upgrade;
    }

    public static void incrUnique(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);
        int uid = preferences.getInt("app.uid", 0);
        preferences.edit().putInt("app.uid", ++uid).commit();
    }

    public static String getUnid(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);
        int uid = preferences.getInt("app.uid", 0);
        return "T_" + uid;
    }

    public static int getUnInt(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settingsvkc",
                Context.MODE_PRIVATE);
        int uid = preferences.getInt("app.uid", 0);
        return uid;
    }
}
