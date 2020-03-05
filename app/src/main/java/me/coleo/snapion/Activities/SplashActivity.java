package me.coleo.snapion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.server.ServerClass;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!ServerClass.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_LONG).show();
        } else {
            if (Constants.getKey(getApplicationContext()).equals(Constants.NO_KEY))
                ServerClass.createUser(this);
            else
                ServerClass.enterUser(this);

        }
    }

    public void goToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}