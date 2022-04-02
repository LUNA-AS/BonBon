package com.example.bonbon;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NewObservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_observation);

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
                    String s = ds.getString("firstName") + " " + ds.getString("lastName");
                    tags.add(s);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void finishActivity() {
        this.finish();
    }
}