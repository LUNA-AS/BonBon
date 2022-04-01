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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bonbon.NewObservation;
import com.example.bonbon.R;
import com.example.bonbon.data_models.Child;
import com.example.bonbon.ui.profiles.PupilProfileActivity;

import java.util.ArrayList;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {
    ArrayList<Child> children;
    Context context;

    public ArrayList<Child> getChildren() {
        return children;
    }

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
        // TODO add profile images from database

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PupilProfileActivity.class);
                intent.putExtra("firstName", children.get(position).getFirstName());
                intent.putExtra("lastName", children.get(position).getFirstName());
                intent.putExtra("dateOfBirth", children.get(position).getFirstName());
                intent.putExtra("address", children.get(position).getAddress());
                // TODO add metrics
                context.startActivity(intent);
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
}
