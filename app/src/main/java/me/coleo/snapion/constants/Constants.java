package me.coleo.snapion.constants;

import android.content.Context;
import android.content.SharedPreferences;

public class Constants {

    private static String NO_TOKEN = "where is token";
    private static String TOKEN_STORAGE = "someWhereInDarkness";
    private static String TOKEN_DATA = "someWhereInDarkness12";

    public static String PARKING_ID = "parkingson";
    public static String SEARCH_MODE = "SEASsdas";

    /**
     * گرفتن کلید ارتباط با سرور
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_DATA, Constants.NO_TOKEN);
    }

    private static String BASE_URL = "http://";

    /**
     * ذخیره کلید ارطباط با سرور در حافظه
     */
    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_DATA, token);
        editor.apply();
        editor.commit();
    }

    public enum SearchMode {
        location,
        search,
    }

    /**
     * تحلیل زمان بازگشتی از سرور
     */
    public static String parseTime(String time) {
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String hour = time.substring(8, 10);
        String minute = time.substring(10, 12);
        String second = time.substring(12, 14);
        return hour + ":" + minute + ":" + second + " - " + year + "/" + month + "/" + day;
    }


}
