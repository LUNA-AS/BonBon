package com.example.bonbon;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.URI;

public class NewPupilProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pupil_profile);

        // Link UI components
        Button save = findViewById(R.id.savePupilButton);
        ImageView profileImg = findViewById(R.id.newPupilPicture);
        EditText firstName, lastName, dob, phone, address;

        // get profile image
        final Uri[] image = {null};
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
    }
}