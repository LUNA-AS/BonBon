package com.example.bonbon;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bonbon.data_management.Encryption;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;

public class NewPupilProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pupil_profile);

        final Uri[] image = {null};

        // Link UI components
        Button save = findViewById(R.id.savePupilButton);
        ImageView profileImg = findViewById(R.id.newPupilPicture);
        EditText firstName, lastName, dob, phone, address;
        firstName = findViewById(R.id.newPupilFirstName);
        lastName = findViewById(R.id.newPupilLastName);
        dob = findViewById(R.id.pupilDOB);
        phone = findViewById(R.id.pupilPhone);
        address = findViewById(R.id.pupilHomeAddress);

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    HashMap<String, String> map = new HashMap<>();
                    map.put("firstName", fName);
                    map.put("lastName", lName);
                    map.put("dob", _dob);
                    map.put("phone", _phone);
                    map.put("address", _address);

                    db.collection("teachers").document(UID).collection("class")
                            .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String id = documentReference.getId();
                            storeProfilePicture(id, image[0]);
                        }
                    });
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
                finish();
            }
        });
    }
}