package com.site.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.*;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.site.app.models.Site;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private String emailString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.email_input);
        editTextPassword = (EditText) findViewById(R.id.password_input);



        buttonLogin = (Button) findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                emailString = editTextEmail.getText().toString();
                passwordString = editTextPassword.getText().toString();

                if(emailString.isEmpty() || passwordString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "The username or password is empty!", Toast.LENGTH_SHORT).show();
                }
                //Firebase sign in with Email and Password:
                else if(!emailString.isEmpty() && !passwordString.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(emailString, passwordString)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success:
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(MainActivity.this, SiteList.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails:
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
            }
        });
    }

    //Firebase Autologin:
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(MainActivity.this, SiteList.class);
            startActivity(i);
        }
    }

}
