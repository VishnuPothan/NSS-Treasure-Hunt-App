package com.codemaster.nsstreasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class FinishScreen extends AppCompatActivity {


    LottieAnimationView fireworks;
    TextView congo,thanks;
    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_finish_screen);

            fireworks = findViewById(R.id.puravedi);
            congo= findViewById(R.id.congrats);
            thanks=findViewById(R.id.thanks1);

            fireworks.animate().translationY(1000).setDuration(3000).setStartDelay(4000);
            congo.animate().translationY(500).setDuration(3000).setStartDelay(4000);
            thanks.animate().translationY(600).setDuration(2000).setStartDelay(3000);
            congo.animate().alphaBy(1f).setDuration(3000).setStartDelay(4000);
            thanks.animate().alphaBy(1f).setDuration(3000).setStartDelay(4000);
        }

    }
