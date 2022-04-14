package com.example.bonbon.data_management;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Logger {
    public static void log(String message){
        HashMap<String, Object> activity = new HashMap<>();
        activity.put("timestamp", System.currentTimeMillis());
        activity.put("message", message);
        FirebaseFirestore.getInstance().collection("teachers")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("logs")
                .add(activity).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Activity Logged!");
            }
        });
    }
}
