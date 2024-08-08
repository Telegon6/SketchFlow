package com.chessytrooper.sketchflow;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chessytrooper.sketchflow.welcome.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            // User is signed in, go directly to Sketchpad activity
            startActivity(new Intent(SplashActivity.this, SketchActivity.class));
        } else {
            // No user is signed in, go to Welcome activity
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        }

        finish(); // Close the splash activity so it's not in the back stack
    }
}