package com.example.bonbon.ui.class_list;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bonbon.NewPupilProfile;
import com.example.bonbon.R;
import com.example.bonbon.adapters.ClassListAdapter;
import com.example.bonbon.data_models.Child;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Locale;

public class ClassListFragment extends Fragment {
    ArrayList<Child> children;

    public ClassListFragment() {
        // Required empty public constructor
        children = new ArrayList<>();
    }


    public static ClassListFragment newInstance() {
        ClassListFragment fragment = new ClassListFragment();
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
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);

        // Linking UI components
        RecyclerView classListRecycler = view.findViewById(R.id.classListRecycler);
        EditText searchBar = view.findViewById(R.id.classSearchBar);


        // create adapter
        ClassListAdapter adapter = new ClassListAdapter();


        // Populate class list
        getPupils(classListRecycler, adapter);


        // Set up the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String filter = editable.toString();
                ArrayList<Child> filteredList = new ArrayList<>();
                for (Child c : children) {
                    if (c.getFirstName().toLowerCase(Locale.ROOT)
                            .contains(filter.toLowerCase(Locale.ROOT)) ||
                            c.getLastName().toLowerCase(Locale.ROOT)
                                    .contains(filter.toLowerCase(Locale.ROOT))) {
                        filteredList.add(c);
                    }
                }
                adapter.filter(filteredList);
            }
        });

        // Floating action button
        FloatingActionButton addPupilButton = view.findViewById(R.id.newPupil);
        addPupilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewPupilProfile.class));
            }
        });
        return view;
    }

    public void getPupils(RecyclerView classListRecycler, ClassListAdapter adapter) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String teacherID = auth.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        db.collection("teachers").document(teacherID).collection("class")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Child> pupils = new ArrayList<>();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    String firstName = ds.getString("firstName");
                    String lastName = ds.getString("lastName");
                    String dob = ds.getString("dob");
                    String phone = ds.getString("phone");
                    String address = ds.getString("address");
                    Child c = new Child(firstName, lastName);
                    c.setAddress(address);
                    c.setDateOfBirth(dob);
                    pupils.add(c);
                }

                // Set up the list view
                children = pupils;
                adapter.setContext(getContext());
                adapter.setChildren(pupils);
                classListRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                classListRecycler.setAdapter(adapter);
            }
        });
        DocumentReference reference = db.collection("teachers").document(teacherID);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                db.collection("teachers").document(teacherID).collection("class")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Child> pupils = new ArrayList<>();
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            String firstName = ds.getString("firstName");
                            String lastName = ds.getString("lastName");
                            String dob = ds.getString("dob");
                            String phone = ds.getString("phone");
                            String address = ds.getString("address");
                            Child c = new Child(firstName, lastName);
                            c.setAddress(address);
                            c.setDateOfBirth(dob);
                            pupils.add(c);
                        }

                        // Set up the list view
                        children = pupils;
                        adapter.setChildren(pupils);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }
}