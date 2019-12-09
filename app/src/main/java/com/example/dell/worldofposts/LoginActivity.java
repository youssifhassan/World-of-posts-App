package com.example.dell.worldofposts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText mailET,passET;
    Button loginBtn;
    FirebaseAuth mAuth;
    TextView goRegister;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mailET = findViewById(R.id.login_mail);
        passET = findViewById(R.id.login_password);


        loginBtn = findViewById(R.id.login_button);
        goRegister = findViewById(R.id.login_register_text);

        mAuth = FirebaseAuth.getInstance();

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mailET.getText().toString();
                final String password = passET.getText().toString();

                if (!RegisterActivity.isValidMail(email)) {
                    mailET.setError(getString(R.string.validmail));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passET.setError(getString(R.string.validPass));
                    return;
                }

                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.show();


                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (!task.isSuccessful()) {
                                    // there was an error

                                    dialog.dismiss();

                                    Toast.makeText(LoginActivity.this, R.string.conerror, Toast.LENGTH_SHORT).show();



                                } else {
                                    dialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });

            }
        });
    }
}
