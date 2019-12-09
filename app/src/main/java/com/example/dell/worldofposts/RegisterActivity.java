package com.example.dell.worldofposts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText mailET,passET,repassET;
    Button registerBtn;
    FirebaseAuth mAuth;
    TextView goSignin;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mailET = findViewById(R.id.register_mail);
        passET = findViewById(R.id.register_password);
        repassET = findViewById(R.id.register_repass);
        registerBtn = findViewById(R.id.register_button);
        goSignin = findViewById(R.id.register_sign_in_text);


        mAuth = FirebaseAuth.getInstance();


        goSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mailET.getText().toString().trim();
                final String password = passET.getText().toString().trim();
                final String repass = repassET.getText().toString().trim();





                if (!isValidMail(email)) {
                    mailET.setError(getString(R.string.regvalidmail));
                    return;
                }

                if (TextUtils.isEmpty(password)|| password.length() < 6) {
                    passET.setError(getString(R.string.minimum6));
                    return;
                }


                if (!repass.equals(password)) {
                    repassET.setError(getString(R.string.passmatch));
                    return;
                }


                dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setMessage("loading");
                dialog.show();


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, R.string.conerror, Toast.LENGTH_SHORT).show();
                                } else {


                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference().child("users");
                                    User user = new User(null,email,null);
                                    myRef.child(mAuth.getUid()).setValue(user);
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();

                                }
                            }
                        });

            }
        });



    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
