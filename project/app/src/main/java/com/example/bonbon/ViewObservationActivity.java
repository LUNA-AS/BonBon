package com.example.bonbon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bonbon.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;

public class ViewObservationActivity extends AppCompatActivity {

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observation);

        final int[] counter = {0};

        ImageButton backButton = findViewById(R.id.viewObservationButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
            }
        });

        // Load image
        ImageView imageAttachment = findViewById(R.id.viewObservationImageAttachment);
        String imageRef = (String) getIntent().getExtras().get("image");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        try {
            storage.getReference().child(imageRef).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ViewObservationActivity.this).load(uri).into(imageAttachment);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            imageAttachment.setVisibility(View.INVISIBLE);
        }

        // Load text
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yy hh:mm");
        String body = (String) getIntent().getExtras().get("body");
        String tags = (String) getIntent().getExtras().get("tags");
        String id = (String) getIntent().getExtras().get("id");
        long longTimestamp = (long) getIntent().getExtras().get("timestamp");

        TextView timestampView = findViewById(R.id.viewObservationTimestamp);
        TextView bodyView = findViewById(R.id.viewObservationBody);
        TextView tagsView = findViewById(R.id.viewObservationTags);

        timestampView.setText(sdf.format(longTimestamp));
        bodyView.setText(body);
        tagsView.setText(tags);

        ImageButton delete = findViewById(R.id.deleteObservationButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewObservationActivity.this);
                builder.setMessage("Deleting this observation will result in permanently removing it from the database. Do you want to proceed?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                executor = ContextCompat.getMainExecutor(ViewObservationActivity.this);
                                biometricPrompt = new BiometricPrompt(ViewObservationActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                                    @Override
                                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                        super.onAuthenticationError(errorCode, errString);
                                        counter[0]++;
                                        if (counter[0] > 2) {
                                            FirebaseAuth.getInstance().signOut();
                                            Toast.makeText(ViewObservationActivity.this, "Authentication error. User logged out.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ViewObservationActivity.this, LoginActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                        super.onAuthenticationSucceeded(result);
                                        FirebaseFirestore.getInstance().collection("teachers")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("notes").document(id)
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ViewObservationActivity.this, "Successfully deleted observation", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onAuthenticationFailed() {
                                        super.onAuthenticationFailed();
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(ViewObservationActivity.this, "Authentication error. User logged out.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ViewObservationActivity.this, LoginActivity.class));
                                    }
                                });
                                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                        .setTitle("Biometric Authentication")
                                        .setSubtitle("Scan your fingerprint to proceed")
                                        .setNegativeButtonText("Return")
                                        .build();
                                biometricPrompt.authenticate(promptInfo);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }

                });
                builder.show();

            }
        });
    }
}