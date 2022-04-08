package com.example.bonbon.data_models;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URI;

public class Child implements Comparable<Child>{
    private String ID, firstName, lastName, address, dateOfBirth;
    int avgScore, avgH, avgL, avgS;
    Uri image;

    public int getAvgH() {
        return avgH;
    }

    public void setAvgH(int avgH) {
        this.avgH = avgH;
    }

    public int getAvgL() {
        return avgL;
    }

    public void setAvgL(int avgL) {
        this.avgL = avgL;
    }

    public int getAvgS() {
        return avgS;
    }

    public void setAvgS(int avgS) {
        this.avgS = avgS;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Child(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    @Override
    public int compareTo(Child child) {
        return Integer.compare(getAvgScore(), child.getAvgScore());
    }
}
