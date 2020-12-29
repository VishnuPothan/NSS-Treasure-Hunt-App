package com.codemaster.nsstreasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codemaster.nsstreasurehunt.SharedPreference.SharedPreference;

public class DevScreen extends AppCompatActivity {

    TextView devby,vishnu,fajar,aswin,subin,sou;
    Animation fadeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_screen);
        devby = findViewById(R.id.devby);
        vishnu = findViewById(R.id.vishnu);
        fajar = findViewById(R.id.fajar);
        sou = findViewById(R.id.sou);
        aswin = findViewById(R.id.aswin);
        subin = findViewById(R.id.subin);

        devby.animate().translationYBy(-250).setDuration(1000).setStartDelay(1500);
        vishnu.animate().translationYBy(-250).setDuration(1000).setStartDelay(1500);
        fajar.animate().translationYBy(-250).setDuration(1000).setStartDelay(1500);
        aswin.animate().translationYBy(-250).setDuration(1000).setStartDelay(1500);
        subin.animate().translationYBy(-250).setDuration(1000).setStartDelay(1500);
        sou.animate().translationYBy(-250).setDuration(1000).setStartDelay(1500);

        devby.animate().alphaBy(1).setDuration(1000).setStartDelay(1500);
        vishnu.animate().alphaBy(1).setDuration(1000).setStartDelay(1500);
        fajar.animate().alphaBy(1).setDuration(1000).setStartDelay(1500);
        aswin.animate().alphaBy(1).setDuration(1000).setStartDelay(1500);
        subin.animate().alphaBy(1).setDuration(1000).setStartDelay(1500);
        sou.animate().alphaBy(1).setDuration(1000).setStartDelay(1500);

        fadeAnimation = AnimationUtils.loadAnimation(DevScreen.this, R.anim.fade);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(mainIntent);
            finish();

        },4500);
    }
}