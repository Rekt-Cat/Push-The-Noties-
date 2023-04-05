package com.example.pushthenoties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ReceivedNotiActivity extends AppCompatActivity {
    TextView title;
    TextView body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_noti);

        title=findViewById(R.id.head);
        body=findViewById(R.id.body);
        if(SplashScreen.title != null && SplashScreen.body!=null){
            title.setText(SplashScreen.title);
            body.setText(SplashScreen.body);
        }
        else{
            title.setText(getIntent().getStringExtra("title"));
            body.setText(getIntent().getStringExtra("body"));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }
}