package me.coleo.snapion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.server.ServerClass;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView splashTV;
    SpinKitView splashLoading;
    AlphaAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash0);
        TranslateAnimation animation = new TranslateAnimation(0,0,0,-150);
        Fade f = new Fade();

        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());


        logo = findViewById(R.id.splashLogoIV);
        splashTV = findViewById(R.id.splashTV);
        splashLoading = findViewById(R.id.spinkit);



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


        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashTV.setVisibility(View.VISIBLE);
                t.schedule(task,1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.startAnimation(animation);


    }

    public void goToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }











    public class MyAnimationListener implements Animation.AnimationListener {


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            logo.clearAnimation();
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) logo.getLayoutParams();

            lp.setMargins(0, 0, 0, 300);
            logo.setLayoutParams(lp);

            splashTV.startAnimation(anim);

            splashLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}


