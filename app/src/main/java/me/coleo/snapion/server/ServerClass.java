package me.coleo.snapion.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import me.coleo.snapion.Activities.SplashActivity;
import me.coleo.snapion.constants.Constants;
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
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            User user = gson.fromJson(response.getJSONObject("user").toString(), User.class);
                            Constants.setKey(context, user.getKey());
                            saveToken(context, response);
                            ServerClass.enterUser(context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ServerClass.printError("createUser", error);
                    }
                });

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
                (Request.Method.POST, url, temp, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveToken(context, response);
                        ((SplashActivity) context).goToMain();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ServerClass.printError("enterUser", error);
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

}
