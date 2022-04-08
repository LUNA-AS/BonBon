package com.example.bonbon.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.bonbon.NewObservation;
import com.example.bonbon.R;
import com.example.bonbon.adapters.DashboardNotesAdapter;
import com.example.bonbon.adapters.DashboardPagerAdapter;
import com.example.bonbon.data_management.Encryption;
import com.example.bonbon.data_models.Child;
import com.example.bonbon.data_models.Observation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

public class DashboardFragment extends Fragment {


    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Linking UI components
        FloatingActionButton newObservation = view.findViewById(R.id.newObservationFab);
        newObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewObservation.class));
            }
        });

        ViewPager pager = view.findViewById(R.id.dashboardPager);
        pager.setPageMargin(-400);
        RecyclerView recyclerView = view.findViewById(R.id.dashboardRecycler);
        ArrayList<Child> children = new ArrayList<>();
        ArrayList<Observation> observations = new ArrayList<>();
        DashboardNotesAdapter notesAdapter = new DashboardNotesAdapter(getContext(), observations);
        DashboardPagerAdapter pagerAdapter = new DashboardPagerAdapter(getContext(), children);

        pager.setAdapter(pagerAdapter);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Task<QuerySnapshot> db = FirebaseFirestore.getInstance().collection("teachers")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("notes")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            long l = Long.parseLong(ds.getString("timestamp").trim());
                            Observation o = new Observation(Encryption.decryptStringData(ds.getString("tags")),
                                    Encryption.decryptStringData(ds.getString("body")),
                                    l, ds.getString("imageRef"));
                            observations.add(o);
                        }
                        notesAdapter.setObservations(observations);
                        notesAdapter.notifyDataSetChanged();
                    }
                });

        Task<QuerySnapshot> db1 = FirebaseFirestore.getInstance().collection("teachers")
                .document(FirebaseAuth.getInstance().getUid()).collection("class")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        children.clear();
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            Task<Uri> storage = FirebaseStorage.getInstance()
                                    .getReference().child("profile_pictures/" + ds.getId() + ".jpg")
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Child c = new Child(Encryption.decryptStringData(ds.getString("firstName")),
                                                    Encryption.decryptStringData(ds.getString("lastName")));
                                            c.setID(ds.getId());
                                            c.setImage(uri);
                                            c.setAddress(Encryption.decryptStringData(ds.getString("address")));
                                            c.setDateOfBirth(Encryption.decryptStringData(ds.getString("dob")));
                                            setAvgScores(c, pagerAdapter, children);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                            Child c = new Child(Encryption.decryptStringData(ds.getString("firstName")),
                                                    Encryption.decryptStringData(ds.getString("lastName")));
                                            c.setID(ds.getId());
                                            c.setImage(null);
                                            c.setAddress(Encryption.decryptStringData(ds.getString("address")));
                                            c.setDateOfBirth(Encryption.decryptStringData(ds.getString("dob")));
                                            setAvgScores(c, pagerAdapter, children);
                                        }
                                    });
                        }
                    }
                });

        return view;
    }

    public void setAvgScores(Child c, DashboardPagerAdapter adapter, ArrayList<Child> children) {
        System.out.println("get avg scores called");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String id = c.getID();
        db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                .collection("class").document(id).collection("assessments")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int sTotal = 0;
                int hTotal = 0;
                int lTotal = 0;
                int count = queryDocumentSnapshots.getDocuments().size();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    sTotal += ds.get("social", Integer.class);
                    hTotal += ds.get("health", Integer.class);
                    lTotal += ds.get("learning", Integer.class);
                }
                int avgScore = 3;
                int avgS = 3;
                int avgL = 3;
                int avgH = 3;
                if (count > 0) {
                    avgS = sTotal / count;
                    avgH = hTotal / count;
                    avgL = lTotal / count;
                    avgScore = sTotal / count + hTotal / count + lTotal / count;
                    avgScore = avgScore / 3;
                }
                c.setAvgScore(avgScore);
                c.setAvgS(avgS);
                c.setAvgL(avgL);
                c.setAvgH(avgH);
                children.add(c);
                Collections.sort(children);
                adapter.setChildren(children);
                adapter.notifyDataSetChanged();
            }
        });

    }

}