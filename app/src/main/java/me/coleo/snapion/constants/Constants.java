package me.coleo.snapion.constants;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import me.coleo.snapion.Activities.MainActivity;

public class Constants {

    public static String SUPPORT_PHONE_NUMBER = "+989126653006";

    public static String NO_TOKEN = "where is token";
    private static String TOKEN_STORAGE = "someWhereInDarkness";
    private static String TOKEN_DATA = "someWhereInDarkness12";
    public static String NO_KEY = "where is key";
    private static String HAVE_USER = "asdzxcwdwdwd";
    private static String USER_KEY = "neverTryThis";

    public static String SEARCH_MODE = "SEASsdas";
    public static String SEARCH_LAT = "SEASsdxzcs";
    public static String SEARCH_LNG = "SEASsda111wxzs";
    public static String HAVE_lATLNG = "SE321rcd11wxzs";

    public static String PARKING_ID = "parkingson";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 10810;

    private static String BASE_URL = "http://dev2.parkners.com/api/av1/";
    public static String CREATE_USER_URL = BASE_URL + "users/create";
    public static String ENTER_USER_URL = BASE_URL + "users/enter";
    public static String AROUND_PARKING_URL = BASE_URL + "parkings/around_point";

    /**
     * گرفتن کلید ارتباط با سرور
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_DATA, Constants.NO_TOKEN);
    }

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

    /**
     * گرفتن کلید اولین ورود
     */
    public static String getKey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_KEY, Constants.NO_KEY);
    }

    /**
     * ذخیره کلید کاربر
     */
    public static void setKey(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_KEY, token);
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

    /**
     * گرفتن اجازه ی دسترسی به مکان کاربر
     */
    public static void getLocationPermission(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (!((MainActivity) activity).isPermissionToastShown()) {
                    ((MainActivity) activity).setPermissionToastShown(true);
                }
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // if never show again is selected and we can open setting
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(context, "از بخش تنظیمات ، دسترسی برنامه به موقعیت مکانی را فراهم سازید.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            } else {
                // if never show again is selected and we can't open setting
                Toast.makeText(context, "از بخش تنظیمات ، دسترسی برنامه به موقعیت مکانی را فراهم سازید.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
