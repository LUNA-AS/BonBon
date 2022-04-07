package com.example.bonbon.ui.profiles;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonbon.R;
import com.example.bonbon.data_management.Encryption;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewContacts extends AppCompatActivity {
    String phone , address ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        String id = getIntent().getExtras().getString("id");
        if (id.equals("") || id == null) {
            Toast.makeText(this, "null id", Toast.LENGTH_SHORT).show();
        }
        System.out.println("activity started with id: " + id);

        ProgressBar progressBar = findViewById(R.id.viewContactProgressBar);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                .collection("class").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                phone = Encryption.decryptStringData(documentSnapshot.getString("phone"));
                address = Encryption.decryptStringData(documentSnapshot.getString("address"));
                if (!phone.equals("")) {
                    phoneTextView.setText(phone);
                }
                if (!address.equals("")) {
                    addressTextView.setText(address);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed due to exception: " + e.getMessage());
            }
        });

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ImageButton copyPhone = findViewById(R.id.copyPhone);
        ImageButton copyAddress = findViewById(R.id.copyAddress);
        copyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone != null) {
                    ClipData clip = ClipData.newPlainText("phone", phone);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ViewContacts.this, "Copied phone number to clipboard.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewContacts.this, "No data available to copy.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        copyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address != null) {
                    ClipData clip = ClipData.newPlainText("address", address);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ViewContacts.this, "Copied home address to clipboard.", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(ViewContacts.this, "No data available to copy.", Toast.LENGTH_SHORT);
                }
            }
        });

        ImageButton backBtn = findViewById(R.id.viewContactsBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}