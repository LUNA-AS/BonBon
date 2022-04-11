package com.example.bonbon.ui.account;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonbon.R;
import com.example.bonbon.data_management.Encryption;
import com.example.bonbon.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button logout = view.findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        final String[] firstName = {""};
        final String[] lastName = {""};

        // get data
        FirebaseFirestore.getInstance().collection("teachers").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firstName[0] = documentSnapshot.getString("firstName");
                lastName[0] = documentSnapshot.getString("lastName");
            }
        });

        EditText fName = view.findViewById(R.id.editFirstName);
        EditText lName = view.findViewById(R.id.editLastName);
        fName.setText(firstName[0]);
        lName.setText(lastName[0]);

        Button saveChanges = view.findViewById(R.id.updateAccount);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                if (!fName.getText().toString().equals("")) {
                    map.put("firstName", fName.getText().toString());
                }
                if (!lName.getText().toString().equals("")) {
                    map.put("lastName", fName.getText().toString());
                }
                FirebaseFirestore.getInstance().collection("teachers")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Updated data!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}