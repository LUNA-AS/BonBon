package com.example.bonbon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class NewPupilProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pupil_profile);

        // Link UI components
        Button save = findViewById(R.id.savePupilButton);
        ImageView profileImg = findViewById(R.id.pupilProfilePicture);

    }
}