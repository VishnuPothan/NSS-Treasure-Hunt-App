package com.codemaster.nsstreasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.codemaster.nsstreasurehunt.SharedPreference.SharedPreference;

public class SplashScreen extends AppCompatActivity {


    ImageView dlogo,splashbg,treshunt;
    LottieAnimationView lottieAnimationView;

    Animation fadeAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //initialization
        dlogo = findViewById(R.id.deekshalogo);
        splashbg = findViewById(R.id.logo);
        treshunt = findViewById(R.id.thunt);
        lottieAnimationView = findViewById(R.id.lottie);

        splashbg.animate().translationY(1200).setDuration(2000).setStartDelay(3000);
        dlogo.animate().translationXBy(10).setDuration(2000).setStartDelay(100);
        treshunt.animate().translationXBy(10).setDuration(2000).setStartDelay(100);
        lottieAnimationView.animate().translationY(500).setDuration(2000).setStartDelay(3000);

        fadeAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fade);

        new Handler().postDelayed(() -> {
            if (SharedPreference.getUserVerified(getApplicationContext())){
                Intent mainIntent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(mainIntent);
            }else {
                Intent verifyIntent = new Intent(SplashScreen.this, DevScreen.class);
                startActivity(verifyIntent);
            }
            finish();
        }, 1500);
    }
}
