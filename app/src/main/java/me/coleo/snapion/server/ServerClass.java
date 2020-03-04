package me.coleo.snapion.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.coleo.snapion.Activities.SearchActivity;
import me.coleo.snapion.Activities.SplashActivity;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.models.Parking;
import me.coleo.snapion.models.User;

public class ServerClass {

    private static String TAG = "Server_class";

    private static void printError(String from, VolleyError error) {
        if (error != null) {
            Log.i(TAG, "printError: " + from + " message :" + error.getMessage() + " code :" + error.networkResponse.statusCode);
        } else {
            Log.i(TAG, "printError: " + from + " null ");
        }
    }


    /**
     * بررسی اتصال به شبکه
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * ذخیره کردن کلید ارتباط
     */
    private static void saveToken(Context context, JSONObject response) {
        String token = null;
        try {
            token = response.getString("token");
        } catch (Exception e) {
            Log.i("SERVER_CONNECTION", "Oh_RIDIM_TOKEN");
        }
        Constants.setToken(context, token);
    }

    /**
     * ساختن کاربر جدید
     */
    public static void createUser(final Context context) {

        String url = Constants.CREATE_USER;
        Log.i(TAG, "createUser: start");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response.getJSONObject("user").toString(), User.class);
                        Constants.setKey(context, user.getKey());
                        saveToken(context, response);
                        ServerClass.enterUser(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> ServerClass.printError("createUser", error));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    /**
     * اعلام ورود کاربر به برنامه
     */
    public static void enterUser(final Context context) {

        String url = Constants.ENTER_USER;

        String key = Constants.getKey(context);
        Log.i(TAG, "enterUser: key: " + key);
        JSONObject temp = new JSONObject();
        try {
            temp.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, temp, response -> {
                    saveToken(context, response);
                    ((SplashActivity) context).goToMain();
                }, error -> ServerClass.printError("enterUser", error));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    /**
     * گرفتن پارکینگ ها بر اساس لوکیشن
     */
    public static void aroundParking(Context context, double lat, double lng, ArrayList<Parking> parkings, int page) {

        String url = Constants.ENTER_USER;
        url += "?page=" + page;

        Log.i(TAG, "around parking");
        JSONObject temp = new JSONObject();
        try {
            temp.put("latitude", lat);
            temp.put("longitude", lng);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, temp, response -> {
                    saveToken(context, response);
                    try {
                        JSONArray parkingArray = response.getJSONArray("parkings");
                        Gson gson = new Gson();
                        for (int i = 0; i < parkingArray.length(); i++) {
                            JSONObject parking = parkingArray.getJSONObject(i);
                            parkings.add(gson.fromJson(parking.toString(), Parking.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((SearchActivity) context).loadParkingFromServer();
                }, error -> ServerClass.printError("enterUser", error));


        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }
}
