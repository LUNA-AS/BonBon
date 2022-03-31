package com.example.bonbon.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonbon.NewObservation;
import com.example.bonbon.R;
import com.example.bonbon.data_models.Observation;

import java.util.ArrayList;

public class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.ViewHolder> {
    ArrayList<Observation> observations;
    Context context;

    public ObservationsAdapter(ArrayList<Observation> observations, Context context) {
        this.observations = observations;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.observation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // TODO change time to be a formatted string within the class Observation
        holder.timestamp.setText(String.valueOf(observations.get(position).getTimeStamp()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO replace the destination to be a viewing activity
                Intent intent = new Intent(context, NewObservation.class);
                intent.putExtra("body", observations.get(position).getBody());
                intent.putExtra("tags", observations.get(position).getTags());
                intent.putExtra("timestamp", observations.get(position).getTimeStamp());
                intent.putExtra("image", observations.get(position).getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        ConstraintLayout container;

        public ViewHolder(@NonNull View view) {
            super(view);
            timestamp = view.findViewById(R.id.observationDate);
            container = view.findViewById(R.id.observationItemContainer);
        }
    }
}
