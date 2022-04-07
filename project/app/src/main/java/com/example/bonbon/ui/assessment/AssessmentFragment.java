package com.example.bonbon.ui.assessment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonbon.R;
import com.example.bonbon.adapters.AssessmentPagerAdapter;
import com.example.bonbon.data_management.Encryption;
import com.example.bonbon.data_models.Child;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AssessmentFragment extends Fragment {
    public AssessmentFragment() {
        // Required empty public constructor
    }

    public static AssessmentFragment newInstance() {
        AssessmentFragment fragment = new AssessmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_assessment, container, false);

        ViewPager pager = view.findViewById(R.id.assessmentViewPager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                .collection("class").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Child> children = new ArrayList<>();
                AssessmentPagerAdapter adapter = new AssessmentPagerAdapter(getContext(), children);
                pager.setAdapter(adapter);
                pager.setPadding(10, 0, 10, 0);

                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    String id = ds.getId();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    storage.getReference().child("profile_pictures").child(id + ".jpg")
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Uri uri) {
                            Child c = new Child(Encryption.decryptStringData(ds.getString("firstName")),
                                    Encryption.decryptStringData(ds.getString("lastName")));
                            c.setID(id);
                            c.setImage(uri);
                            c.setAddress(Encryption.decryptStringData(ds.getString("address")));
                            c.setDateOfBirth(Encryption.decryptStringData(ds.getString("dob")));
                            children.add(c);
                            adapter.setChildren(children);
                            adapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Child c = new Child(Encryption.decryptStringData(ds.getString("firstName")),
                                    Encryption.decryptStringData(ds.getString("lastName")));
                            c.setID(id);
                            c.setAddress(Encryption.decryptStringData(ds.getString("address")));
                            c.setDateOfBirth(Encryption.decryptStringData(ds.getString("dob")));
                            children.add(c);
                            adapter.setChildren(children);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        return view;
    }
}