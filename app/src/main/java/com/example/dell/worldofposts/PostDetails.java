package com.example.dell.worldofposts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PostDetails extends AppCompatActivity {

    TextView title,content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        final Item item = getIntent().getExtras().getParcelable("item");


        title = findViewById(R.id.title_details);
        content = findViewById(R.id.content_details);

        title.setText(item.getTitle());
        content.setText(item.getContent());

    }
}
