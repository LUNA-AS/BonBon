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

import com.example.bonbon.R;
import com.example.bonbon.ViewObservationActivity;
import com.example.bonbon.data_models.Observation;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DashboardNotesAdapter extends RecyclerView.Adapter<DashboardNotesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Observation> observations;
    private SimpleDateFormat sdf;

    public DashboardNotesAdapter(Context context, ArrayList<Observation> observations) {
        this.context = context;
        this.observations = observations;
        sdf = new SimpleDateFormat("d/MMM/yy hh:mm");
    }

    public void setObservations(ArrayList<Observation> observations) {
        this.observations = observations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.observation_dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String formatted = sdf.format(observations.get(position).getTimeStamp());
        holder.time.setText(formatted);
        holder.body.setText(observations.get(position).getBody());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewObservationActivity.class);
                intent.putExtra("body", observations.get(position).getBody());
                intent.putExtra("tags", observations.get(position).getTags());
                intent.putExtra("image", observations.get(position).getImage());
                intent.putExtra("timestamp", observations.get(position).getTimeStamp());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, body;
        ConstraintLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.observationDateDashboard);
            body = itemView.findViewById(R.id.observationBodyDashboard);
            container = itemView.findViewById(R.id.dashboardNoteContainer);
        }
    }
}
