package com.codemaster.nsstreasurehunt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText mobileNumberTextInput;
    Button sendOTPBtn;
    TextView signin,blwtxt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    //Background text
        signin=findViewById(R.id.textView1);
        blwtxt=findViewById(R.id.textView2);

        signin.animate().translationY(-100).setDuration(100).setStartDelay(150);
        blwtxt.animate().translationY(-100).setDuration(100).setStartDelay(150);
        signin.animate().alphaBy(1F).setDuration(500).setStartDelay(300);
        blwtxt.animate().alphaBy(1F).setDuration(500).setStartDelay(300);





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


}
