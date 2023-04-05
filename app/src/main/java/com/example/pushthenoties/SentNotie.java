package com.example.pushthenoties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SentNotie extends AppCompatActivity {
    RecyclerView sentRecyclerView;
    ArrayList<receivedModel> arr;
    receivedNotiAdapter notiAdapter;

    FirebaseFirestore dbRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_notie);

        sentRecyclerView = findViewById(R.id.sentRecycler);
        arr = new ArrayList<>();
        getData();
        sentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        sentRecyclerView.setLayoutManager(linearLayoutManager);
        notiAdapter = new receivedNotiAdapter(arr, getApplicationContext());
        sentRecyclerView.setAdapter(notiAdapter);


    }
    private void getData() {
        dbRoot = FirebaseFirestore.getInstance();
        dbRoot.collection("NotiSent").document(FirebaseAuth.getInstance().getUid())
                .collection("allSentNoties").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds : list){
                            receivedModel rm = ds.toObject(receivedModel.class);
                            arr.add(rm);
                        }
                        Collections.reverse(arr);
                        for(int i =0;i<arr.size();i++){
                            Log.d("size", "onSuccess: "+arr.get(i));
                        }
                        notiAdapter.notifyDataSetChanged();
                        Log.d("size", "o "+arr.size());

                    }
                });
    }
}