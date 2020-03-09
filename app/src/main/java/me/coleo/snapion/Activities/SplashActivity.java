package me.coleo.snapion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.server.ServerClass;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (!ServerClass.isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_LONG).show();
                } else {
                    if (Constants.getKey(getApplicationContext()).equals(Constants.NO_KEY))
                        ServerClass.createUser(SplashActivity.this);
                    else
                        ServerClass.enterUser(SplashActivity.this);
                }
            }
        };
        Timer t = new Timer();
        t.schedule(task,1000);

    }

    public void goToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}