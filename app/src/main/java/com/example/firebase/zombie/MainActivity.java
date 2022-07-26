package com.example.firebase.zombie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.firebase.zombie.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    // Test dynamic link: https://repoexmaple.page.link/test

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkDynamicLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkDynamicLink(intent);
    }

    private void checkDynamicLink(final Intent intent) {
        if (intent != null) {
            // Open and consume
            FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    final PendingDynamicLinkData data = task.getResult();
                    if (data != null && data.getLink() != null) {
                        binding.textView.setText("App opened by dynamic: " + data.getLink());
                    } else {
                        binding.textView.setText("URI was null. No link in intent. Not opened by dynamic.");
                    }
                } else {
                    binding.textView.setText("Could not load dynamic link: " + task.getException());
                }
            });
        } else {
            binding.textView.setText("No intent.");
        }
    }
}