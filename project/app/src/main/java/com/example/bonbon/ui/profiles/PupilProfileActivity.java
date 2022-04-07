package com.example.bonbon.ui.profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bonbon.NewObservation;
import com.example.bonbon.NewPupilProfile;
import com.example.bonbon.R;
import com.example.bonbon.adapters.ObservationsAdapter;
import com.example.bonbon.data_management.Encryption;
import com.example.bonbon.data_models.Observation;
import com.example.bonbon.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class PupilProfileActivity extends AppCompatActivity {

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_profile);

        // Get details from intent
        String firstNameString = getIntent().getExtras().getString("firstName");
        String lastNameString = getIntent().getExtras().getString("lastName");
        String id = getIntent().getExtras().getString("id");
        String dob = getIntent().getExtras().getString("dob");
        Uri image = (Uri) getIntent().getExtras().get("image");


        // set up indicators
        int sPoints = (Integer) getIntent().getExtras().get("avgS");
        int hPoints = (Integer) getIntent().getExtras().get("avgH");
        int lPoints = (Integer) getIntent().getExtras().get("avgL");
        TextView sText = findViewById(R.id.sPoints);
        TextView hText = findViewById(R.id.hPoints);
        TextView lText = findViewById(R.id.lPoints);
        setStarIndicator(sPoints, sText);
        setStarIndicator(hPoints, hText);
        setStarIndicator(lPoints, lText);


        // Link UI components
        TextView firstName = findViewById(R.id.pupilProfileFirstName);
        TextView lastName = findViewById(R.id.pupilProfileLastName);
        ImageButton newNote = findViewById(R.id.newObservationButton);
        ImageButton viewContacts = findViewById(R.id.showContactDetails);
        ImageButton editProfile = findViewById(R.id.editDetails);
        ImageView profilePic = findViewById(R.id.pupilProfilePicture);

        // Set up page content
        firstName.setText(firstNameString);
        lastName.setText(lastNameString);
        if (image != null) {
            Glide.with(this).load(image).into(profilePic);
        }

        // Buttons
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PupilProfileActivity.this, NewObservation.class));
            }
        });

        // Load notes
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                .collection("notes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Find notes with related tags
                ArrayList<Observation> observations = new ArrayList<>();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    if (ds.getString("tags").contains(firstNameString + " " + lastNameString)) {
                        long l = Long.parseLong(ds.getString("timestamp").trim());
                        Observation o = new Observation(Encryption.decryptStringData(ds.getString("tags")), Encryption.decryptStringData(ds.getString("body")),
                                l, ds.getString("imageRef"));
                        observations.add(o);
                    }
                }
                // Set up observations list
                RecyclerView observationRecycler = findViewById(R.id.pupilObservationsRecycler);
                ObservationsAdapter adapter = new ObservationsAdapter(observations, PupilProfileActivity.this);
                observationRecycler.setLayoutManager(new LinearLayoutManager(PupilProfileActivity.this));
                observationRecycler.setAdapter(adapter);

            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PupilProfileActivity.this, NewPupilProfile.class);
                intent.putExtra("firstName", firstNameString);
                intent.putExtra("lastName", lastNameString);
                intent.putExtra("id", id);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

        // Show contacts:

        final int[] authFailCount = {0};
        executor = ContextCompat.getMainExecutor(PupilProfileActivity.this);
        biometricPrompt = new androidx.biometric.BiometricPrompt(PupilProfileActivity.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(PupilProfileActivity.this, "Authentication Error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(PupilProfileActivity.this, ViewContacts.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authFailCount[0]++;
                if(authFailCount[0]>2){
                    FirebaseAuth.getInstance().signOut();
                    // TODO log activity
                    finish();
                    startActivity(new Intent(PupilProfileActivity.this, LoginActivity.class));
                }
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Scan your fingerprint to proceed")
                .setNegativeButtonText("")
                .build();

        viewContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Authenticate
                biometricPrompt.authenticate(promptInfo);

            }
        });

    }

    private void setStarIndicator(int score, TextView textView) {
        String text = "★★☆☆☆";
        switch (score) {
            case 0:
                text = "☆☆☆☆☆";
                textView.setText(text);
                break;
            case 1:
                text = "★☆☆☆☆";
                textView.setText(text);
                break;
            case 2:
                text = "★★☆☆☆";
                textView.setText(text);
                break;
            case 4:
                text = "★★★★☆";
                textView.setText(text);
                break;
            case 5:
                text = "★★★★★";
                textView.setText(text);
                break;
            default:
                text = "★★★☆☆";
                textView.setText(text);
                break;
        }
    }

}