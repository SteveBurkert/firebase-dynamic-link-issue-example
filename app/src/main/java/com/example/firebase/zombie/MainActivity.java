package com.example.firebase.zombie;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import com.example.firebase.zombie.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        initializeFirebaseAuth();
        setupButtons();
        binding.tvLog.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setupButtons() {
        binding.buttonSignIn.setOnClickListener(view -> signInTestUser());
        binding.buttonSignInAnon.setOnClickListener(view -> signInAnonymously());
        binding.buttonSignOut.setOnClickListener(view -> signOut());
        binding.buttonReset.setOnClickListener(view -> resetExample());
        binding.buttonClearLog.setOnClickListener(view -> clearLog());
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
                binding.tvAuthInit.setText("FirebaseAuth initialized: true");
                log("FirebaseAuth.state initialized");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    log("Found a FirebaseUser.");
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
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && !user.isAnonymous()) {
            log("Error: Can't sign in, user is already authenticated");
            return;
        } else if (user == null) {
            log("Error: For test purposes you can not sign in, without an anon account.");
            return;
        }

        final AuthCredential credential = EmailAuthProvider.getCredential(testMail, testPW);

        user.linkWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                log("Successful signed in with email and linked to anon account.");
                FirebaseUser anonUser = firebaseAuth.getCurrentUser();
                if (anonUser != null) {
                    displayUserState(anonUser);
                } else {
                    log("Error: FirebaseUser == null after email authentication.");
                }
            } else {
                final Exception e = task.getException();
                log("Error: Could not link anon account with with email sign in." + e);
                if (e instanceof FirebaseAuthUserCollisionException) {
                    log("Use RESET (unlink) to restart the sample.");
                }

            }
        });
    }

    /**
     * Since I only want to use a dedicated test account, we have to delete the email
     * account again or the next account linking will fail due to
     * an FirebaseAuthUserCollisionException.
     */
    private void resetExample() {
        log("Going to reset the example");
        log("Going to delete email account.");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        firebaseAuth.signInWithEmailAndPassword(testMail, testPW).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user.delete().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            log("User is deleted. Restart.");
                            clearLog();
                            initializeFirebaseAuth();
                        } else {
                            log("Error. Reset failed. " + task1.getException());
                        }
                    });
                } else {
                    log("Error: Can't delete account. User == null.");
                }
            } else {
                log("Error: Can't delete account. " + task.getException());
            }
        });
    }

    private void signOut() {
        log("Going to sign out.");
        FirebaseAuth.getInstance().signOut();
        displayUserState(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void displayUserState(@Nullable FirebaseUser user) {
        if (user != null) {
            final String userId = user.getUid();
            StringBuilder builder = new StringBuilder();
            builder.append("FirebaseUser:\n\n");
            builder.append("id: ").append(userId.substring(0, 5)).append("...").append(userId.substring(userId.length() - 5)).append("\n");
            builder.append("isAnonymous: ").append(user.isAnonymous()).append("\n");
            builder.append("Auth providers {").append("\n");
            for (UserInfo info : user.getProviderData()) {
                builder.append("  ProviderId: ").append(info.getProviderId()).append("\n");
            }
            builder.append("}").append("\n");
            boolean isInInvalidState = !user.isAnonymous() && user.getProviderData().size() == 1 && "firebase".equals(user.getProviderId());
            builder.append("Has zombie state: ").append(isInInvalidState);
            if (isInInvalidState) {
                builder.append("\n").append("(The value of 'isAnonymous' should be true')");
            }
            binding.tvUserAvailable.setText(builder.toString());
        } else {
            binding.tvUserAvailable.setText("FirebaseUser: null");
        }

    }

    private void log(@NonNull final String message) {
        Log.d("MainActivity", message);
        String oldLod = binding.tvLog.getText().toString();
        binding.tvLog.setText(oldLod + "\n" + message);
    }

    private void clearLog() {
        binding.tvLog.setText("Log:");
    }
}