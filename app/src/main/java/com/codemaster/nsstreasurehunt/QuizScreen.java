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
import android.widget.TextView;
import android.widget.Toast;

import com.codemaster.nsstreasurehunt.model.OnGoingDetails;
import com.codemaster.nsstreasurehunt.model.QuestionDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class QuizScreen extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ImageView hintImg, hintRefreshImg;
    TextInputEditText answerText;
    Button submitBtn;
    TextView qnoText;
    ProgressBar progressBar;
    String uid;
    int currQno;
    QuestionDetails questionDetails;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);

        //initialization
        hintImg = findViewById(R.id.hintImage);
        answerText = findViewById(R.id.answerText);
        submitBtn = findViewById(R.id.submit);
        qnoText = findViewById(R.id.questionNumber);
        hintRefreshImg = findViewById(R.id.hintRefresh);
        progressBar = findViewById(R.id.progressBar);

        //Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // initial question fetch
        initialSetUp();

        // on the press of submit answer check
        submitBtn.setOnClickListener(v -> {
            if (answerText.getText().toString().isEmpty()) {
                answerText.setError("Enter a answer to submit");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            checkAnswer();
        });

        //on hint fetch
        hintRefreshImg.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            hintFetch();
        });

    }

    //initial question fetch based on resume or start
    private void initialSetUp() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser != null) {
            uid = currentFirebaseUser.getUid();
        }

        valueEventListener = mDatabase.child("ongoing").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    OnGoingDetails onGoingDetails = snapshot.getValue(OnGoingDetails.class);
                    currQno = Integer.parseInt(onGoingDetails.getCurrQno()) + 1;
                    if (currQno - 1 == 20) {
                        Intent finishScreenIntent = new Intent(getApplicationContext(), FinishScreen.class);
                        startActivity(finishScreenIntent);
                        finish();
                        return;
                    }
                    questionFetch(currQno);
                } else {
                    OnGoingDetails onGoingDetails = new OnGoingDetails("0", ServerValue.TIMESTAMP);
                    mDatabase.child("ongoing").child(uid).setValue(onGoingDetails).addOnSuccessListener(aVoid -> {
                        questionFetch(1);
                        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Some error occurred, restart the hunt", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Some error occurred, restart the hunt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //question fetch
    private void questionFetch(int currQno) {
        Log.i("here fun", String.valueOf(currQno));
        mDatabase.removeEventListener(valueEventListener);
        mDatabase.child("Questions").child(String.valueOf(currQno)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    questionDetails = snapshot.getValue(QuestionDetails.class);
                    //initialize values
                    qnoText.setText(String.valueOf(currQno));

                    if (questionDetails.isImgAvl()) {
                        // Load the image using Picasso
                        hintImg.setVisibility(View.VISIBLE);
                        Picasso.get().load(questionDetails.getImg()).into(hintImg);
                        Toast.makeText(getApplicationContext(), "New hint Published!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        hintImg.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Hint currently not published for this question, check back later", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Some error occurred, restart the hunt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //hint fetch function
    private void hintFetch() {
        questionFetch(currQno);
    }

    //answer check function
    private void checkAnswer() {
        String answerStr = answerText.getText().toString().toLowerCase().trim();


        if (answerStr.equals(questionDetails.getAnswer())) {
            OnGoingDetails onGoingDetails = new OnGoingDetails(String.valueOf(currQno), ServerValue.TIMESTAMP);
            currQno++;
            mDatabase.child("ongoing").child(uid).setValue(onGoingDetails).addOnSuccessListener(aVoid -> {
                if (currQno == 21) {
                    Intent finishScreenIntent = new Intent(getApplicationContext(), FinishScreen.class);
                    startActivity(finishScreenIntent);
                    finish();
                } else {
                    questionFetch(currQno);
                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Some error occurred, restart the hunt", Toast.LENGTH_SHORT).show());

            Toast.makeText(getApplicationContext(), "Correct answer", Toast.LENGTH_SHORT).show();
            answerText.setText("");
        } else {
            progressBar.setVisibility(View.GONE);
            answerText.setText("");
            Toast.makeText(getApplicationContext(), "That's not the answer!!!", Toast.LENGTH_SHORT).show();
        }
    }
}