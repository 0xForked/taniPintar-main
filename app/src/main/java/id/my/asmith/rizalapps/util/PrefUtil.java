package id.my.asmith.rizalapps.util;

/**
 * Created by Asmith on 9/8/2017.
 */

public class PrefUtil {

    /**public static final String USER_SESSION = "user_session";

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putUser(Context context, String key, Users user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        putString(context, key, json);
    }

    public static Users getUser(Context context, String key) {
        Gson gson = new Gson();
        String json = getString(context, key);
        Users user = gson.fromJson(json, Users.class);
        return user;
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreference(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getSharedPreference(context).getString(key, null);
    }

    public static void clear(Context context) {
        getSharedPreference(context).edit().clear().apply();
    }**/
}
