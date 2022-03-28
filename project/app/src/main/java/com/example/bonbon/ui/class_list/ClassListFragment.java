package com.example.bonbon.ui.class_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonbon.R;
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

        return view;
    }
}