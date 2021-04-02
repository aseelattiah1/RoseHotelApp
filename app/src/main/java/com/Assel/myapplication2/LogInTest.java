package com.Assel.myapplication2;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LogInTest {

    public static boolean getStatus = false;

    public static void logIn(String email, String password) {

        Task<AuthResult> task = LogInActivity.auth.signInWithEmailAndPassword(email, password);
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getStatus = true;
                }
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getStatus = false;
            }
        });

    }

    public static void logIn2(String email, String password) {
        getStatus = true;

    }
}
