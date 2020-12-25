package com.codemaster.nsstreasurehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.codemaster.nsstreasurehunt.model.StartQuizDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {
    FirebaseAuth mauth;
    DatabaseReference df;
    Button startBtn;
    WaitMessage waitMessage;
    ProgressBar progressBar;
    VideoView video1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Background Video
        video1=findViewById(R.id.bgvid1);

        String path ="android.resource://com.codemaster.nsstreasurehunt/"+R.raw.bgvd1;
        Uri u = Uri.parse(path);
        video1.setVideoURI(u);
        video1.start();

        video1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);


            }
        });


        //firebase initialize
        mauth = FirebaseAuth.getInstance();
        df = FirebaseDatabase.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);

        //initialization
        startBtn = findViewById(R.id.startBtn);
        df.child("ongoing").child(mauth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startBtn.setText("Resume");
                } else {
                    startBtn.setText("Start");
                }
                progressBar.setVisibility(View.GONE);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startBtn.setText("Start");
                progressBar.setVisibility(View.GONE);
            }
        });

        //on start button click
        startBtn.setOnClickListener(v -> {
            df.child("start").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        StartQuizDetails startQuizDetails = snapshot.getValue(StartQuizDetails.class);
                        waitMessage = new WaitMessage(HomeScreen.this, startQuizDetails.getTime());
                        if (startQuizDetails.isStart()) {
                            Intent intent = new Intent(HomeScreen.this, QuizScreen.class);
                            startActivity(intent);
                        } else {
                            waitMessage.show();
                        }
                    } else {
                        waitMessage = new WaitMessage(HomeScreen.this, "Hunt will start soon!!!");
                        waitMessage.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Some error occurred, restart the hunt", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}