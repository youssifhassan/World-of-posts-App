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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class NewPostActivity extends AppCompatActivity {
    private Button postBtn;
    private EditText titleEdit,contentEdit;


    FirebaseDatabase database;
    DatabaseReference myRef;




    String postContent;
    String postTitle;





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        postTitle = titleEdit.getText().toString();
        postContent = contentEdit.getText().toString();

        outState.putString("title", postTitle);
        outState.putString("content", postContent);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postBtn = findViewById(R.id.postBtn);

        titleEdit = findViewById(R.id.addTitleEdit);
        contentEdit = findViewById(R.id.addContentEdit);




        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("posts");

        if (savedInstanceState != null) {

            titleEdit.setText( savedInstanceState.getString("title"));
            contentEdit.setText( savedInstanceState.getString("content"));



        }




        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(titleEdit.getText().toString().trim())) {
                    titleEdit.setError(getString(R.string.enterTitle));
                    return;
                }
                if (TextUtils.isEmpty(contentEdit.getText().toString().trim())) {
                    contentEdit.setError(getString(R.string.enterContent));
                    return;
                }


                final Item post = new Item();
                post.setTitle(titleEdit.getText().toString().trim());
                post.setContent(contentEdit.getText().toString().trim());



                myRef.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()){

                            Toast.makeText(NewPostActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewPostActivity.this,MainActivity.class));
                        }else {

                            Toast.makeText(NewPostActivity.this, R.string.conerror, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

    }



}