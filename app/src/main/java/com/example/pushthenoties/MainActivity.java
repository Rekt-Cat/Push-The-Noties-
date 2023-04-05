package com.example.pushthenoties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Button send;
    TextInputLayout title;
    TextInputLayout description;
    FirebaseAuth firebaseAuth;
    FloatingActionButton sent,receive,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        send=findViewById(R.id.send);
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        sent = findViewById(R.id.sent);
        receive = findViewById(R.id.received);
        logout=findViewById(R.id.logout);

        firebaseAuth=FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleText=title.getEditText().getText().toString();
                String descriptionText = description.getEditText().getText().toString();
                if(titleText.equals("")){
                    title.setError("Please enter a title");
                }
                else{
                    title.setError(null);
                }
                if(descriptionText.equals("")){
                    description.setError("Please enter a description");
                }
                else{
                    description.setError(null);
                }
                if(!titleText.equals("")&&!descriptionText.equals("")){
                    Log.d("ran","ran");

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",titleText,descriptionText,FirebaseAuth.getInstance().getUid(),
                            getApplicationContext(),MainActivity.this);
                    notificationsSender.SendNotifications();

                }
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,receiveNoty.class));
            }
        });

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SentNotie.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this,SignIn.class));
                finish();
            }
        });






    }
}