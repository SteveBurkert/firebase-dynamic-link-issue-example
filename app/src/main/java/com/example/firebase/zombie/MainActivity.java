package com.example.firebase.zombie;

import android.os.Bundle;
import android.util.Log;

import com.example.firebase.zombie.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String testMail = "me@example.com";
    private static final String testPW   = "123456789";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. initialize firebase auth
        // 2. check if we have a user, stop here if we have one
        // 3. if not, sign in anonymously

        initializeFirebaseAuth();
    }

    private void initializeFirebaseAuth() {
        // Make sure auth is initialized, or auth state can have 3 states
        // 1. unknown -> FirebaseUser == null
        // 2. no user -> FirebaseUser == null
        // 3. signed in -> -> FirebaseUser != null
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.removeAuthStateListener(this);
                log("FirebaseAuth initialized. Will now check authorization.");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    log("Found a FirebaseUser");
                    // verifyRegistration(user);
                } else {
                    log("No firebase authenticated user found.");
                    signInAnonymously(firebaseAuth);
                }
            }
        });
    }

    private void signInAnonymously(@NonNull final FirebaseAuth firebaseAuth) {

    }

    private void signInTestUser() {

    }

    private void signOut() {

    }

    private void displayUserState(@NonNull FirebaseUser user) {

    }

    private void log(@NonNull final String message) {
        Log.d("MainActivity", message);
    }

}