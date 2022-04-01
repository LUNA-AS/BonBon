package com.example.bonbon.ui.class_list;

import android.content.Intent;
import android.os.Bundle;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ClassListFragment extends Fragment {
    public ClassListFragment() {
        // Required empty public constructor
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

        // TODO get data from database
        ArrayList<Child> children = new ArrayList<>();
        children.add(new Child("test 1", "test1"));
        children.add(new Child("test 2", "test2"));
        children.add(new Child("test 3", "test3"));

        // Set up the list view
        ClassListAdapter adapter = new ClassListAdapter(children, getContext());
        classListRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        classListRecycler.setAdapter(adapter);

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
                    if (c.getFirstName().contains(filter) || c.getLastName().contains(filter)) {
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
}