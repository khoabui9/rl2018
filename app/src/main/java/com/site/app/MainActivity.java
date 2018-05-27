package com.site.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

//    private FirebaseAuth mAuth;
//    private EditText editTextEmail, editTextPassword;
//    private Button buttonLogin;
//    private String emailString, passwordString;
    public static final int MY_PERMISSIONS_CAM = 0;
    public static final int MY_PERMISSIONS_STO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Please accept the request!", Toast.LENGTH_SHORT).show();
            requestCam();
            requestStorage();
        }

        else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
//                requestStorage();
                    Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
                    startActivity(intent);
                }
            }, 100);

        }


        Button buttonStart = (Button) findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
                    startActivity(intent);

                }
                else {
                    requestCam();
                    requestStorage();
                }
            }
        });



//        mAuth = FirebaseAuth.getInstance();
//        editTextEmail = (EditText) findViewById(R.id.email_input);
//        editTextPassword = (EditText) findViewById(R.id.password_input);
//        buttonLogin = (Button) findViewById(R.id.login_btn);
//        requestCam();



//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                requestStorage();
//                emailString = editTextEmail.getText().toString();
//                passwordString = editTextPassword.getText().toString();
//
//                if (emailString.isEmpty() || passwordString.isEmpty()) {
//                    Toast.makeText(MainActivity.this, "The username or password is empty!", Toast.LENGTH_SHORT).show();
//                }
//                //Firebase sign in with Email and Password:
//                else if (!emailString.isEmpty() && !passwordString.isEmpty()) {
//                    mAuth.signInWithEmailAndPassword(emailString, passwordString)
//                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success:
//                                        Log.d("TAG", "signInWithEmail:success");
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        // If sign in fails:
//                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
//                                        Toast.makeText(MainActivity.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }
//            }
//        });
    }

    public  void requestCam() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAM);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAM);
            }
        } else {
            // Permission has already been granted
        }
    }

    public  void requestStorage() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_STO);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_STO);
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_CAM: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "open camera failed.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //Firebase Autologin:
    @Override
    public void onStart() {
        super.onStart();
        requestStorage();


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
            startActivity(intent);

        }

//        if (mAuth.getCurrentUser() != null) {
//            requestStorage();
//            Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

}
