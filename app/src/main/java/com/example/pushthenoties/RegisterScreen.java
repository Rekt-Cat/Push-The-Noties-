package com.example.pushthenoties;

import static android.content.ContentValues.TAG;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {
    Button sign, register;
    TextInputLayout fullName, email, password;
    FirebaseAuth auth;
    ProgressDialog pd;
    FirebaseFirestore dbroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        dbroot=FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        sign = findViewById(R.id.sign);
        register = findViewById(R.id.register);
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);

        //Log.d("lol", "the user is : " + auth.getCurrentUser().toString());


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full = fullName.getEditText().getText().toString();
                String mail = email.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString();

                if (full.isEmpty()) {
                    fullName.setError("Please enter your name");
                } else {
                    fullName.setError(null);
                    fullName.setErrorEnabled(false);
                }
                if (mail.isEmpty()) {
                    email.setError("Please enter an Email");
                } else {
                    email.setError(null);
                    email.setErrorEnabled(false);
                }
                if (pass.isEmpty()) {
                    password.setError("Please enter a password");
                } else if (pass.length() < 6) {
                    password.setError("Please must have at least 6 character");
                } else {
                    password.setError(null);
                    password.setErrorEnabled(false);
                }
                if (!full.isEmpty() && !mail.isEmpty() && !pass.isEmpty() && pass.length() >= 6) {
                    pd = new ProgressDialog(RegisterScreen.this);
                    pd.setMessage("Please Wait...");
                    pd.show();
                    registerUser(full, mail, pass);
                }
            }
        });



        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterScreen.this, SignIn.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(findViewById(R.id.email1), "email");
                pairs[1] = new Pair<View, String>(findViewById(R.id.password1), "pass");
                pairs[2] = new Pair<View, String>(findViewById(R.id.fullname), "fullname");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterScreen.this, pairs);
                startActivity(i, activityOptions.toBundle());
                finish();
            }
        });

    }

    private void registerUser(String fullName1, String email1, String password1) {
        auth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(RegisterScreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    //FirebaseUser firebaseUser = auth.getCurrentUser();
                    DocumentReference documentReference = dbroot.collection("Users").document(FirebaseAuth.getInstance().getUid());
                    Map<String,Object> user = new HashMap<>();
                    user.put("fullname",fullName1);
                    user.put("email",email1);
                    user.put("password",password1);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("lol", "onSuccess: created!");
                            Toast.makeText(RegisterScreen.this, "User Registered successfully!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterScreen.this,SignIn.class);
                            Pair[] pairs = new Pair[3];
                            pairs[0] = new Pair<View, String>(findViewById(R.id.email1), "email");
                            pairs[1] = new Pair<View, String>(findViewById(R.id.password1), "pass");
                            pairs[2] = new Pair<View, String>(findViewById(R.id.fullname), "fullname");
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterScreen.this, pairs);
                            startActivity(i, activityOptions.toBundle());
                            finish();
                        }
                    });

                }
                else{
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        pd.dismiss();
                        Toast.makeText(RegisterScreen.this, "User already registered!", Toast.LENGTH_LONG).show();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        pd.dismiss();
                        Toast.makeText(RegisterScreen.this, "Please enter a valid email!", Toast.LENGTH_LONG).show();
                    } catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
