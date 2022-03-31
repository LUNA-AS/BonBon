package com.example.bonbon.ui.profiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bonbon.NewObservation;
import com.example.bonbon.R;
import com.example.bonbon.adapters.ObservationsAdapter;
import com.example.bonbon.data_models.Observation;

import java.util.ArrayList;

public class PupilProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_profile);

        // Get details from intent
        String firstNameString = getIntent().getExtras().getString("firstName");
        String lastNameString = getIntent().getExtras().getString("lastName");

        // Link UI components
        TextView firstName = findViewById(R.id.pupilProfileFirstName);
        TextView lastName = findViewById(R.id.pupilProfileLastName);
        ImageButton newNote = findViewById(R.id.newObservationButton);
        ImageButton viewContacts = findViewById(R.id.showContactDetails);
        ImageButton editProfile = findViewById(R.id.editDetails);

        // Set up page content
        firstName.setText(firstNameString);
        lastName.setText(lastNameString);

        // Buttons
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PupilProfileActivity.this, NewObservation.class));
            }
        });

        // Set up observations list
        RecyclerView observationRecycler = findViewById(R.id.pupilObservationsRecycler);
        ArrayList<Observation> observations = new ArrayList<>();
        observations.add(new Observation(new ArrayList<String>(), "", System.currentTimeMillis(), ""));
        ObservationsAdapter adapter = new ObservationsAdapter(observations, PupilProfileActivity.this);
        observationRecycler.setLayoutManager(new LinearLayoutManager(PupilProfileActivity.this));
        observationRecycler.setAdapter(adapter);

    }
}