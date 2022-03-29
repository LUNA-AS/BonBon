package com.example.bonbon.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.bonbon.data_models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationFragment extends Fragment {

    FirebaseAuth mAuth;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
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
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        TextView signInInstead = view.findViewById(R.id.logInInsteadText);
        signInInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, LoginFragment.class, null) // gets the first animations
                        .commit();

            }
        });

        // Link UI Components
        EditText emailIn = view.findViewById(R.id.registerEmailInput);
        EditText passIn = view.findViewById(R.id.registerPasswordInput);
        EditText passConIn = view.findViewById(R.id.registerConfirmPasswordInput);
        EditText firstNameIn = view.findViewById(R.id.registerFirstNameInput);
        EditText lastNameIn = view.findViewById(R.id.registerLastNameInput);

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user details
                String email, password, passwordCon, firstName, lastName;
                email = emailIn.getText().toString().trim();
                password = passIn.getText().toString();
                passwordCon = passConIn.getText().toString();
                firstName = firstNameIn.getText().toString();
                lastName = lastNameIn.getText().toString();
                User user;
                if (email.contains("@")) {
                    if (!password.equals("")) {
                        if (password.equals(passwordCon)) {
                            if (firstName.equals("")) {
                                firstNameIn.requestFocus();
                                firstNameIn.setError("This field cannot be empty");
                            } else if (lastName.equals("")) {
                                lastNameIn.requestFocus();
                                lastNameIn.setError("This field cannot be empty");
                            } else {
// =========================================== VALID INPUT =========================================
                                user = new User(firstName, lastName, email, Encryption.encryptPassword(password));
                                // Initialize auth
                                mAuth = FirebaseAuth.getInstance();
                                mAuth.createUserWithEmailAndPassword(email, Encryption.encryptPassword(password))
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getContext(),
                                                                        "User Registered! Check your inbox to confirm your email.", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(getContext(),
                                                                        "Failed to send email verification. Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getContext(), "Registration Error: " + task.getException().getMessage()
                                                            , Toast.LENGTH_SHORT).show();
                                                    System.out.println(email);
                                                    System.out.println(password);
                                                }
                                            }
                                        });
                            }
                        } else {
                            passIn.requestFocus();
                            passConIn.requestFocus();
                            passConIn.setError("Passwords do not match!");
                        }
                    } else {
                        passIn.setError("Password cannot be empty");
                    }
                } else {
                    emailIn.requestFocus();
                    emailIn.setError("Invalid email address");
                }
            }
        });


        return view;
    }
}