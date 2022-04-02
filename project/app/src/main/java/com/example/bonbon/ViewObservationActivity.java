package com.example.bonbon;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;

public class ViewObservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observation);

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
        storage.getReference().child(imageRef).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ViewObservationActivity.this).load(uri).into(imageAttachment);
            }
        });

        // Load text
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yy hh:mm");
        String body = (String) getIntent().getExtras().get("body");
        String tags = (String) getIntent().getExtras().get("tags");
        long longTimestamp = (long) getIntent().getExtras().get("timestamp");

        TextView timestampView = findViewById(R.id.viewObservationTimestamp);
        TextView bodyView = findViewById(R.id.viewObservationBody);
        TextView tagsView = findViewById(R.id.viewObservationTags);

        timestampView.setText(sdf.format(longTimestamp));
        bodyView.setText(body);
        tagsView.setText(tags);

    }
}