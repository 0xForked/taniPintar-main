package id.my.asmith.rizalapps.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 6/27/2018.
 */

public class AppPrefs {

    private static AppPrefs appPrefs;
    private SharedPreferences sharedPreferences;

    private AppPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("taniPintarPrefs",Context.MODE_PRIVATE);
    }

    public static AppPrefs getInstance(Context context) {
        if (appPrefs == null) {
            appPrefs = new AppPrefs(context);
        }
        return appPrefs;
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void clearPrefs() {
        sharedPreferences.edit().clear().apply();
    }
}
