package com.codemaster.nsstreasurehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.codemaster.nsstreasurehunt.SharedPreference.SharedPreference;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    String verificationCodeBySystem;
    String phoneNumberStr;
    ImageView treshunt,nsslogo;
    Button verifyBtn;
    PinView OTPCodePinView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_screen);

        //initialization
        verifyBtn = findViewById(R.id.verifyBtn);
        OTPCodePinView = findViewById(R.id.pinView);
        progressBar = findViewById(R.id.progressBar);
        treshunt = findViewById(R.id.thunt);
        nsslogo = findViewById(R.id.nsslogo);

        //Animation
        treshunt.animate().translationYBy(-100).setDuration(2000).setStartDelay(2000);
        nsslogo.animate().translationYBy(-100).setDuration(2000).setStartDelay(2000);
        nsslogo.animate().alphaBy(1f).setDuration(2000).setStartDelay(2000);
        //get intent values
        phoneNumberStr = getIntent().getStringExtra("PhoneNumber");

        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        sendVerificationCode(phoneNumberStr);

        //verifyBtn click listener
        verifyBtn.setOnClickListener(v -> {
            String code = OTPCodePinView.getText().toString();
            if (code.isEmpty() || code.length() < 6) {
                OTPCodePinView.setError("Wrong OTP..");
                OTPCodePinView.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        });
    }

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredential(credential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(OTPScreen.this, task -> {
            if (task.isSuccessful()) {
                checkExistingUser();
            } else {
                Toast.makeText(OTPScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExistingUser() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.orderByChild("phone").equalTo(phoneNumberStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("here", "Already a user..");
                    Intent mainIntent = new Intent(getApplicationContext(), HomeScreen.class);
                    SharedPreference.setUserVerified(getApplicationContext(), true);

                    //getting existing user details and store in shared preference
                    mDatabase.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userName = snapshot.child("userName").getValue().toString();
                                SharedPreference.setUserName(getApplicationContext(), userName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    startActivity(mainIntent);
                } else {
                    Intent createAccountIntent = new Intent(getApplicationContext(), CreateUser.class);
                    createAccountIntent.putExtra("PhoneNumber", phoneNumberStr);
                    startActivity(createAccountIntent);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendVerificationCode(String phoneNo) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNo)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Initialize phone auth callbacks
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code = credential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("here",e.getMessage());
        }
    };
}