package com.example.dell.worldofposts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private Button pick,save;
    private ImageView imageView;
    private EditText nameET;
    private Uri filePath;

    private final int PICK_REQUEST = 11;


    FirebaseStorage storage;
    StorageReference storageReference;

    StorageReference deletedImgReference;


    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth mAuth;

    User current;
    DatabaseReference userRef;

    ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        pick = findViewById(R.id.selectImage);
        save = findViewById(R.id.saveProfile);
        nameET=findViewById(R.id.profileName);
        imageView=findViewById(R.id.profileImg);



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("images");

        mAuth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");


        userRef = database.getReference().child("users").child(mAuth.getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                current= dataSnapshot.getValue(User.class);

                if (current.userName!=null)
                    nameET.setText(current.userName);
                if (current.userImage!=null)
                    Glide.with(ProfileActivity.this).load(current.userImage).into(imageView);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameET.getText().toString().trim())) {
                    nameET.setError(getString(R.string.enter_prof_name));
                    return;
                }

                if(filePath == null&&current.userImage==null){
                    Toast.makeText(ProfileActivity.this, R.string.selectimage, Toast.LENGTH_SHORT).show();
                    return;
                }

                    upload();

            }
        });

    }


    private void upload() {


        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.show();

        final User user = new User();

        if (nameET.getText().toString().isEmpty())
            user.userName=null;
        else
            user.userName=nameET.getText().toString();

        user.userMail=mAuth.getCurrentUser().getEmail();




        if(filePath != null)
        {

            StorageReference ref = storageReference.child(filePath.getLastPathSegment());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {



                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            dialog.dismiss();

                            user.userImage=taskSnapshot.getDownloadUrl().toString();
                            userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                                }
                            });




                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();

                            Toast.makeText(ProfileActivity.this, R.string.errorhappened, Toast.LENGTH_SHORT).show();

                        }
                    });
        }else{


            user.userImage=current.userImage;
            userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                }
            });;
            dialog.dismiss();

        }

    }



    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose)), PICK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
