package com.example.bonbon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.bonbon.R;
import com.example.bonbon.data_models.Child;

import java.util.ArrayList;

public class DashboardPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<Child> children;

    public DashboardPagerAdapter(Context context, ArrayList<Child> children) {
        this.context = context;
        this.children = children;
    }

    public void setChildren(ArrayList<Child> children) {
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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.require_attention_item, container, false);
        TextView name = view.findViewById(R.id.pagerPupilName);
        TextView description = view.findViewById(R.id.pagerDescription);
        name.setText(children.get(position).getFirstName() + " " + children.get(position).getLastName());
        description.setText("Score: " + children.get(position).getAvgScore());
        ImageView profilePic = view.findViewById(R.id.dashboardProfilePic);
        if (children.get(position).getImage() != null) {
            Glide.with(context).load(children.get(position).getImage()).into(profilePic);
        }
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
