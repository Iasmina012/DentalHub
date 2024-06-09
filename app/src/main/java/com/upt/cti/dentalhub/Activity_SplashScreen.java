package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; //ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.app_logo);
        TextView appName = findViewById(R.id.app_name);

        Animation logoAppear = AnimationUtils.loadAnimation(this, R.anim.logo_appear);
        Animation nameAppear = AnimationUtils.loadAnimation(this, R.anim.name_appear);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        logo.startAnimation(logoAppear);
        appName.startAnimation(nameAppear);

        logoAppear.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                logo.startAnimation(bounce);
                appName.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

        });

        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(Activity_SplashScreen.this, Activity_Login.class);
            Activity_SplashScreen.this.startActivity(mainIntent);
            Activity_SplashScreen.this.finish();

        }, SPLASH_DISPLAY_LENGTH);

    }

}