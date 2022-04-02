package com.example.bonbon.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.bonbon.R;
import com.example.bonbon.data_models.Child;

import java.util.ArrayList;

public class AssessmentPagerAdapter extends PagerAdapter {
    Context context;
    private ArrayList<Child> children;

    public AssessmentPagerAdapter(Context context, ArrayList<Child> children) {
        this.context = context;
        this.children = children;
    }

    @Override
    public int getCount() {
        return children.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.assessment_item, container, false);
        // Link UI
        ImageView profilePic = view.findViewById(R.id.assessmentProfilePic);
        TextView name = view.findViewById(R.id.assessmentName);
        RatingBar socialRB = view.findViewById(R.id.socialSkillsRatingBar);
        RatingBar healthRB = view.findViewById(R.id.healthRatingBar);
        RatingBar learningRB = view.findViewById(R.id.learningRatingBar);
        Button save = view.findViewById(R.id.saveAssessment);

        // Set values
        name.setText(children.get(position).getFirstName() + " " + children.get(position).getLastName());
        Glide.with(context).load(children.get(position).getImage());
        

        return super.instantiateItem(container, position);
    }
}
