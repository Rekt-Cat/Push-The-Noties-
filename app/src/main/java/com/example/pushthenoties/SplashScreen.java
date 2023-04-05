package com.example.pushthenoties;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    FirebaseAuth auth;
    String name;
    NetworkInfo info = null;
    boolean connected;
    String val;
    public static String body,title;
    FirebaseFirestore dbRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance();
        dbRoot=FirebaseFirestore.getInstance();
        val=getIntent().getStringExtra("userId");
        title=getIntent().getStringExtra("title");
        body=getIntent().getStringExtra("body");
        if(val!=null){
            DocumentReference documentReference = dbRoot.collection("NotiReceived").document(FirebaseAuth.getInstance().getUid());
            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("description", body);
            documentReference.collection("allReceivedNoties").document().set(map);

            Intent i = new Intent(this, ReceivedNotiActivity.class);
            i.putExtra("Title",title);
            i.putExtra("body",body);
            Log.d("polo", ""+title);
            Log.d("polo", ""+body);
            startActivity(new Intent(this, ReceivedNotiActivity.class));
            finish();
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;

            }
        }
        return  false;
    }

    @Override
    protected void onStart() {
        super.onStart();


            Log.d("lol", "doesnt has extra");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connected = isNetworkAvailable();
                    FirebaseAuth.getInstance().addAuthStateListener(SplashScreen.this);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();


    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d("lolt", "internet spl : " + connected);
        if(connected) {
            if (auth.getCurrentUser() != null) {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreen.this, SignIn.class));
                finish();
            }
        }
        else{
            Toast.makeText(SplashScreen.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }
}