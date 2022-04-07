package com.example.bonbon;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bonbon.data_management.Encryption;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class NewPupilProfile extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pupil_profile);

        final Uri[] image = {null};
        String id = null;
        try {
            id = (String) getIntent().getExtras().get("id");
        } catch (Exception e) {
            System.out.println("No ID passed in. activity in creation mode.");
        }

        // Link UI components
        Button save = findViewById(R.id.savePupilButton);
        ImageView profileImg = findViewById(R.id.newPupilPicture);
        EditText firstName, lastName, dob, phone, address;
        firstName = findViewById(R.id.newPupilFirstName);
        lastName = findViewById(R.id.newPupilLastName);
        dob = findViewById(R.id.pupilDOB);
        phone = findViewById(R.id.pupilPhone);
        address = findViewById(R.id.pupilHomeAddress);
        ProgressBar progressBar = findViewById(R.id.newPupilProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        if (id != null) {
            firstName.setText((String) getIntent().getExtras().get("firstName"));
            lastName.setText((String) getIntent().getExtras().get("lastName"));
            dob.setText((String) getIntent().getExtras().get("dob"));
            phone.setHint("Update contact number");
            address.setHint("Update contact address");
            Glide.with(this).load((Uri) getIntent().getExtras().get("image"));
        }

        // get profile image
        // set up activity launcher
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned
                        if (uri != null) {
                            image[0] = uri;
                            profileImg.setImageURI(uri);
                        }
                    }
                });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch("image/*");
            }
        });

        String finalId = id;
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                // Get data from input
                if (firstName.getText().toString().equals("")) {
                    firstName.setError("This field cannot be empty");
                } else if (lastName.getText().toString().equals("")) {
                    lastName.setError("This field cannot be empty");
                } else {

                    // get data from inputs
                    String fName, lName, _dob, _phone, _address;
                    fName = Encryption.encryptStringData(firstName.getText().toString());
                    lName = Encryption.encryptStringData(lastName.getText().toString());
                    _dob = Encryption.encryptStringData(dob.getText().toString());
                    _phone = Encryption.encryptStringData(phone.getText().toString());
                    _address = Encryption.encryptStringData(address.getText().toString());

                    // save info to database
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String UID = auth.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a map with data
                    HashMap<String, Object> map = new HashMap<>();
                    if (!fName.equals("")) {
                        map.put("firstName", Encryption.encryptStringData(fName));
                    }
                    if (!lName.equals("")) {
                        map.put("lastName", Encryption.encryptStringData(lName));
                    }
                    if (!_dob.equals("")) {
                        map.put("dob", Encryption.encryptStringData(_dob));
                    }
                    if (!_phone.equals("")) {
                        map.put("phone", Encryption.encryptStringData(_phone));
                    }
                    if (!_address.equals("")) {
                        map.put("address", Encryption.encryptStringData(_address));
                    }

                    if (finalId == null) {
                        db.collection("teachers").document(UID).collection("class")
                                .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if (image[0] != null) {
                                    String _id = documentReference.getId();
                                    storeProfilePicture(_id, image[0]);
                                }
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                        });
                    } else {
                        db.collection("teachers").document(UID).collection("class")
                                .document(finalId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(NewPupilProfile.this, "Updated data", Toast.LENGTH_SHORT).show();
                                if (image[0] != null) {
                                    storeProfilePicture(finalId, image[0]);
                                }
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }

    public void storeProfilePicture(String id, Uri image) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("profile_pictures/" + id + ".jpg").putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("uploaded image");
            }
        });
    }


}