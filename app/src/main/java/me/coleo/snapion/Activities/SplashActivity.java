package me.coleo.snapion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.Timer;
import java.util.TimerTask;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.server.ServerClass;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView splashTV;
    SpinKitView splashLoading;
    AlphaAnimation anim;
    ConstraintLayout retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash0);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -150);

        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());


        logo = findViewById(R.id.splashLogoIV);
        splashTV = findViewById(R.id.splashTV);
        splashLoading = findViewById(R.id.spinkit);
        retry = findViewById(R.id.retry);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                nextPage();
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
                splashLoading.setVisibility(View.VISIBLE);
                t.schedule(task, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.startAnimation(animation);

        retry.setOnClickListener(v -> nextPage());

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

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public void showRetry() {
        runOnUiThread(() -> {
            splashLoading.setVisibility(View.INVISIBLE);
            retry.setVisibility(View.VISIBLE);
        });
    }

    public void hideRetry() {
        runOnUiThread(() -> {
            splashLoading.setVisibility(View.VISIBLE);
            retry.setVisibility(View.INVISIBLE);
        });

    }

    private void nextPage() {
        if (!ServerClass.isNetworkConnected(getApplicationContext())) {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_LONG).show());
            showRetry();
        } else {
            hideRetry();
            if (Constants.getKey(getApplicationContext()).equals(Constants.NO_KEY))
                ServerClass.createUser(SplashActivity.this);
            else
                ServerClass.enterUser(SplashActivity.this);
        }
    }

}


