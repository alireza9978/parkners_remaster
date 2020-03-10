package me.coleo.snapion.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

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
import me.coleo.snapion.Activities.SupportActivity;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.models.Parking;
import me.coleo.snapion.models.User;

public class ServerClass {

    private static String TAG = "Server_class";

    private static String getErrorMessage(VolleyError error) {
        String temp = new String(error.networkResponse.data);
        try {
            JSONObject json = new JSONObject(temp);
            JSONObject messagesObject = json.getJSONObject("messages");
            JSONArray errorsArray = messagesObject.getJSONArray("error");
            temp = errorsArray.getJSONObject(0).getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
            temp = "خطای نامشخص";
        }
        return temp;
    }

    private static void handleError(Context context, VolleyError error) {
        error.printStackTrace();
        if (error.networkResponse == null) {
            Toast.makeText(context, "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show();
        } else {
            if (error.networkResponse.statusCode == 403) {
                Toast.makeText(context, "کلید شما منقضی شده", Toast.LENGTH_SHORT).show();
                Constants.setToken(context, "");
                Intent intent = new Intent(context, SplashActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                return;
            }
            if (error.networkResponse.statusCode != 417) {
                error.printStackTrace();
                Toast.makeText(context, getErrorMessage(error), Toast.LENGTH_LONG).show();
            } else {
                error.printStackTrace();
                Toast.makeText(context, getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
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

        String url = Constants.CREATE_USER_URL;
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
                }, error -> ServerClass.handleError(context, error));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    /**
     * اعلام ورود کاربر به برنامه
     */
    public static void enterUser(final Context context) {

        String url = Constants.ENTER_USER_URL;

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
                }, error -> ServerClass.handleError(context, error));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    /**
     * گرفتن پارکینگ ها بر اساس لوکیشن
     */
    public static void aroundParking(Context context, double lat, double lng,
                                     Constants.SearchMode mode, String search,
                                     ArrayList<Parking> parkings, int page) {

        String url = Constants.AROUND_PARKING_URL;
        JSONObject temp = new JSONObject();
        try {
            temp.put("latitude", lat);
            temp.put("longitude", lng);
            temp.put("page", page);
            if (mode != Constants.SearchMode.location) {
                temp.put("search", search);
            }
            temp.put("page_size", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.POST, url, temp,
                        response -> {
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
                        }
                        , error -> ServerClass.handleError(context, error));


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void sendComment(Context context, String comment) {

        String url = Constants.COMMENT_URL;

        Log.i(TAG, "around parking");
        JSONObject temp = new JSONObject();
        try {
            JSONObject com = new JSONObject();
            com.put("text", comment);
            temp.put("comment", com);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.POST, url, temp,
                        response -> {
                            saveToken(context, response);
                            ((SupportActivity) context).sent();
                        }
                        , error -> ServerClass.handleError(context, error));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }


}
