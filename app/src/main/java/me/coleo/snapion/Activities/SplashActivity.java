package me.coleo.snapion.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import me.coleo.snapion.R;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {


    private CardView[] lights = new CardView[3];

    private int lightsMode = 1; //between 0 to 5

    Timer timer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        lights[0] = findViewById(R.id.lightCV1);
        lights[1] = findViewById(R.id.lightCV2);
        lights[2] = findViewById(R.id.lightCV3);

        TimerTask loadingTimerTask = new TimerTask() {
            @Override
            public void run() {
                lightsMode = (lightsMode==5)? 0 : lightsMode+1;
                applyLightsMode();
            }
        };

        timer2 = new Timer();
        timer2.scheduleAtFixedRate(loadingTimerTask,0,500);
    }


    private void applyLightsMode(){
        switch (lightsMode) {
            case 0:
                lights[2].setCardBackgroundColor(getResources().getColor(R.color.colorLightOff));
                break;

            case 1:
                lights[0].setCardBackgroundColor(getResources().getColor(R.color.colorLightOn));
                break;

            case 2:
                lights[1].setCardBackgroundColor(getResources().getColor(R.color.colorLightOn));
                break;

            case 3:
                lights[2].setCardBackgroundColor(getResources().getColor(R.color.colorLightOn));
                break;

            case 4:
                lights[0].setCardBackgroundColor(getResources().getColor(R.color.colorLightOff));

                break;

            case 5:
                lights[1].setCardBackgroundColor(getResources().getColor(R.color.colorLightOff));
                break;
        }
    }

    private void goToMain(){

        timer2.cancel();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    private void sendReq(){

    }

}