package com.example.bonbon.ui.assessment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonbon.R;

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
        return inflater.inflate(R.layout.fragment_assessment, container, false);
    }
}