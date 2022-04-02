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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.bonbon.R;
import com.example.bonbon.data_models.Child;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class AssessmentPagerAdapter extends PagerAdapter {
    Context context;
    private ArrayList<Child> children;

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

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
        if (children.get(position).getImage() != null) {
            Glide.with(context).load(children.get(position).getImage()).into(profilePic);
        }

        // save assessment
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Float> results = new HashMap<>();

                if (socialRB.getRating() > 0) {
                    results.put("social", socialRB.getRating());
                } else {
                    socialRB.setRating(3F);
                    results.put("social", 3F);
                }
                if (healthRB.getRating() > 0) {
                    results.put("health", healthRB.getRating());
                } else {
                    healthRB.setRating(3F);
                    results.put("health", 3F);
                }
                if (learningRB.getRating() > 0) {
                    results.put("learning", learningRB.getRating());
                } else {
                    learningRB.setRating(3F);
                    results.put("learning", 3F);
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("teachers").document(FirebaseAuth.getInstance().getUid())
                        .collection("class").document(children.get(position).getID())
                        .collection("assessments").add(results)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                children.remove(position);
                                Toast.makeText(context, "saved assessment for " + name.getText().toString(), Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        });

            }
        });
        try {
            container.addView(view, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
