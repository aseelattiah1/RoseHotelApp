package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.Assel.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class RateUsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText commentET;
    Button sendRateBtn;

    float ratingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        ratingBar = findViewById(R.id.ratingBar);
        commentET = findViewById(R.id.commentET);
        sendRateBtn = findViewById(R.id.sendRateBtn);

        ratingResult = ratingBar.getRating();

        sendRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentET.getText().toString().isEmpty() &&
                        commentET.getText().toString() != null) {

                    saveRating(ratingResult, commentET.getText().toString());

                } else {
                    Toast.makeText(RateUsActivity.this, "Enter Your Comment,Please", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveRating(float rating, String commnit) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RateUs");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("rating", rating);
        hashMap.put("commnit", commnit);

        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RateUsActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RateUsActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}