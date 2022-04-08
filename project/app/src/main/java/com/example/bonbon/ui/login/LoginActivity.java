package com.example.bonbon.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bonbon.MainActivity;
import com.example.bonbon.R;
import com.example.bonbon.data_management.Encryption;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is logged in
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {

            executor = ContextCompat.getMainExecutor(LoginActivity.this);
            biometricPrompt = new androidx.biometric.BiometricPrompt(LoginActivity.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(LoginActivity.this, "Authentication Error: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    FirebaseFirestore.getInstance().collection("keys")
                            .document(Encryption.oneWayEncrypt(mAuth.getUid()))
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // Get encryption key
                            String key = documentSnapshot.getString("key");
                            Encryption.setKey(key);
                            System.out.println("KEY: " + key);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    // TODO log activity
                }
            });
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Scan your fingerprint to proceed")
                    .setNegativeButtonText("Return")
                    .build();
            biometricPrompt.authenticate(promptInfo);

        }
    }
}