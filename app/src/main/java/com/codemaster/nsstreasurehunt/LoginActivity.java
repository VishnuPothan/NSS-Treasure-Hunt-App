package com.codemaster.nsstreasurehunt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText mobileNumberTextInput;
    Button sendOTPBtn;
    VideoView video1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Background Video
        video1 = findViewById(R.id.bgvid1);

        String path = "android.resource://com.codemaster.nsstreasurehunt/" + R.raw.bgvd1;
        Uri u = Uri.parse(path);
        video1.setVideoURI(u);
        video1.start();

        video1.setOnPreparedListener(mediaPlayer -> {
            float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
            float screenRatio = video1.getWidth() / (float)
                    video1.getHeight();
            float scaleX = videoRatio / screenRatio;
            if (scaleX >= 1f) {
                video1.setScaleX(scaleX);
            } else {
                video1.setScaleY(1f / scaleX);
            }
            mediaPlayer.setLooping(true);
        });


        //initialization
        mobileNumberTextInput = findViewById(R.id.mobileNumberTextInput);
        sendOTPBtn = findViewById(R.id.sentOTPBtn);


        //sendOtp button clicked listener
        sendOTPBtn.setOnClickListener(v -> {
            String phoneNo = mobileNumberTextInput.getText().toString();
            if (phoneNo.isEmpty() || phoneNo.length() < 10) {
                mobileNumberTextInput.setError("Enter valid mobile number");
                mobileNumberTextInput.requestFocus();
                return;
            }
            Intent OTPScreenIntent = new Intent(LoginActivity.this, OTPScreen.class);
            OTPScreenIntent.putExtra("PhoneNumber", "+91" + mobileNumberTextInput.getText().toString());
            startActivity(OTPScreenIntent);
            finish();
        });

    }

    protected void OnResume() {
        video1.resume();
        super.onResume();

    }

    protected void OnPause() {
        video1.suspend();
        super.onPause();
    }

    protected void OnTesting() {
        video1.stopPlayback();
        super.onDestroy();
    }
}
