package com.example.bonbon;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bonbon.data_management.Encryption;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class NewObservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_observation);

        final Uri[] selectedImage = {null};
        // Back button
        ImageButton imageButton = findViewById(R.id.observationBackButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        // Adding images
        ImageButton addPicture = findViewById(R.id.addPictureToObservation);
        ImageView imageView = findViewById(R.id.observationImageAttachment);
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned
                        if (uri != null) {
                            Glide.with(NewObservation.this).load(uri).into(imageView);
                            selectedImage[0] = uri;
                        }
                    }
                });
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        // Link user input
        EditText bodyView = findViewById(R.id.observationBody);
        MultiAutoCompleteTextView tagsView = findViewById(R.id.observationTags);
        tagsView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        tagsView.setThreshold(1);

        // set up auto complete
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                .collection("class").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> tags = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewObservation.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tags);
                tagsView.setAdapter(adapter);
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    String s = Encryption.decryptStringData(ds.getString("firstName")) + " " + Encryption.decryptStringData(ds.getString("lastName"));
                    tags.add(s);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        // Save observation
        Button save = findViewById(R.id.saveObservationButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bodyView.getText().toString().equals("")) {
                    if (!tagsView.getText().toString().equals("")) {
                        String tags = Encryption.encryptStringData(tagsView.getText().toString());
                        String body = Encryption.encryptStringData(bodyView.getText().toString());
                        long timestamp = System.currentTimeMillis();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("tags", tags);
                        map.put("body", body);
                        map.put("timestamp", String.valueOf(timestamp));
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                                .collection("notes").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();
                                if (selectedImage != null) {
                                    String imageRef = "images/" + id + ".jpg";
                                    // save image
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    storage.getReference().child("images/" + id + ".jpg")
                                            .putFile(selectedImage[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            map.put("imageRef", imageRef);
                                            db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                                                    .collection("notes").document(id).set(map);
                                        }
                                    });
                                }
                                Toast.makeText(NewObservation.this, "Added observation", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewObservation.this, "Failed to add observation", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        tagsView.setError("Please selected at least one pupil");
                    }
                } else {
                    bodyView.setError("Observation cannot be empty");
                }
            }
        });
    }

    public void finishActivity() {
        this.finish();
    }
}