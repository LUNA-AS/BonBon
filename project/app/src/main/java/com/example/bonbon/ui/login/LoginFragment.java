package com.example.bonbon.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonbon.MainActivity;
import com.example.bonbon.R;
import com.example.bonbon.data_management.Encryption;
import com.example.bonbon.ui.profiles.PupilProfileActivity;
import com.example.bonbon.ui.profiles.ViewContacts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        Button loginBtn = view.findViewById(R.id.loginButton);
        EditText emailIn = view.findViewById(R.id.loginEmailInput);
        EditText passIn = view.findViewById(R.id.loginPasswordInput);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailIn.getText().toString().trim();
                String password = passIn.getText().toString();

                // Sign in with encrypted password
                if (!email.equals("")) {
                    if (!password.equals("")) {
                        mAuth.signInWithEmailAndPassword(email, Encryption.oneWayEncrypt(password))
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                                Toast.makeText(getContext(), "Login successful. Fetching data.", Toast.LENGTH_SHORT).show();
                                                FirebaseFirestore.getInstance().collection("keys")
                                                        .document(Encryption.oneWayEncrypt(mAuth.getUid()))
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        // Get encryption key
                                                        String key = documentSnapshot.getString("key");
                                                        Encryption.setKey(key);
                                                        System.out.println("KEY: " + key);
                                                        startActivity(new Intent(getContext(), MainActivity.class));
                                                        getActivity().finish();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Login Error: " + task.getException()
                                                    .getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        passIn.setError("Password cannot be empty");
                    }
                } else {
                    emailIn.setError("Email cannot be empty");
                }
            }
        });

        // In page navigation
        TextView register = view.findViewById(R.id.createAccountLoginText);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, RegistrationFragment.class, null) // gets the first animations
                        .commit();
            }
        });


        return view;
    }
}