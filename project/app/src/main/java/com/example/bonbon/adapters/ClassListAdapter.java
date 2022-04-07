package com.example.bonbon.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bonbon.NewObservation;
import com.example.bonbon.R;
import com.example.bonbon.data_models.Child;
import com.example.bonbon.ui.profiles.PupilProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {
    ArrayList<Child> children;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    public ClassListAdapter() {
    }

    public ClassListAdapter(ArrayList<Child> children, Context context) {
        this.children = children;
        this.context = context;
    }

    @NonNull
    @Override
    public ClassListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClassListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(children.get(position).getFirstName() + " " + children.get(position).getLastName());

        getAvgScores();

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PupilProfileActivity.class);
                intent.putExtra("firstName", children.get(position).getFirstName());
                intent.putExtra("lastName", children.get(position).getLastName());
                intent.putExtra("dateOfBirth", children.get(position).getFirstName());
                intent.putExtra("id", children.get(position).getID());
                intent.putExtra("address", children.get(position).getAddress());
                intent.putExtra("image", children.get(position).getImage());
                intent.putExtra("avgScore", children.get(position).getAvgScore());
                intent.putExtra("avgL", children.get(position).getAvgL());
                intent.putExtra("avgS", children.get(position).getAvgS());
                intent.putExtra("avgH", children.get(position).getAvgH());

                context.startActivity(intent);
                Toast.makeText(context, "" + children.get(position).getAvgScore(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NewObservation.class));
            }
        });

        System.out.println(children.get(position).getImage());
        if (children.get(position).getImage() == null) {
            holder.profilePic.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            Glide.with(context).load(children.get(position).getImage()).into(holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        try {
            size = children.size();
        } catch (Exception e) {
            size = 0;
        }
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton addNote;
        ImageView profilePic;
        ConstraintLayout container;

        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.classListName);
            addNote = view.findViewById(R.id.classListAddQuickNote);
            profilePic = view.findViewById(R.id.classListProfileImage);
            container = view.findViewById(R.id.classListItemContainer);
        }
    }

    public void filter(ArrayList<Child> filteredList) {
        children = filteredList;
        notifyDataSetChanged();
    }

    public void getAvgScores() {
        System.out.println("get avg scores called");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (Child c : children) {
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
                }
            });

        }

    }
}
