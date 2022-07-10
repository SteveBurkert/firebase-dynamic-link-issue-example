package com.example.firebase.zombie;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.firebase.zombie.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("SetTextI18n")
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
        binding.buttonSignIn.setOnClickListener(view -> signInTestUser());
        binding.buttonSignInAnon.setOnClickListener(view -> signInAnonymously());
        binding.buttonSignOut.setOnClickListener(view -> signOut());
    }

    private void initializeFirebaseAuth() {
        // Make sure auth is initialized, or auth state can have 3 states
        // 1. unknown -> FirebaseUser == null
        // 2. no user -> FirebaseUser == null
        // 3. signed in -> -> FirebaseUser != null
        log("Initialize FirebaseAuth.state");
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.removeAuthStateListener(this);
                binding.tvAuthInit.setText("true");
                log("FirebaseAuth.state initialized");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    log("Found a FirebaseUser");
                    displayUserState(user);
                } else {
                    log("No firebase authenticated user found.");
                    signInAnonymously();
                }
            }
        });
    }

    private void signInAnonymously() {
        log("Going to sign in anonymously.");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                log("Successful signed in anonymously.");
                FirebaseUser anonUser = firebaseAuth.getCurrentUser();
                if (anonUser != null) {
                    displayUserState(anonUser);
                } else {
                    log("Error: FirebaseUser == null after anon authentication.");
                }
            } else {
                log("Error: Could not sign in anonymously." + task.getException());
            }
        });
    }

    private void signInTestUser() {
        log("Going to sign in test user.");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(testMail, testPW).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                log("Successful signed in with email.");
                FirebaseUser anonUser = firebaseAuth.getCurrentUser();
                if (anonUser != null) {
                    displayUserState(anonUser);
                } else {
                    log("Error: FirebaseUser == null after email authentication.");
                }
            } else {
                log("Error: Could not sign in with email." + task.getException());
            }
        });
    }

    private void signOut() {
        log("Going to sign out.");
        FirebaseAuth.getInstance().signOut();
    }

    private void displayUserState(@NonNull FirebaseUser user) {
        StringBuilder builder = new StringBuilder();
        builder.append("FirebaseUser:\n");
        builder.append("isAnonymous: ").append(user.isAnonymous()).append("\n");
        builder.append("Auth providers {").append("\n");
        for (UserInfo info : user.getProviderData()) {
            builder.append("ProviderId: ").append(info.getProviderId());
        }
        builder.append("}");

        binding.tvUserAvailable.setText(builder.toString());

    }

    private void log(@NonNull final String message) {
        Log.d("MainActivity", message);
        String oldLod = binding.tvLog.getText().toString();
        binding.tvLog.setText(oldLod + "\n" + message);
    }

}