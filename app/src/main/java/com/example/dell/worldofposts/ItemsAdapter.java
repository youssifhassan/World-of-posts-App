package com.example.dell.worldofposts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsAdapter  extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {



    ArrayList<Item> posts;
    Context context;
    ItemsAdapter(Context context,ArrayList<Item> posts){
        this.context=context;
        this.posts = posts;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item post=new Item();
        post=posts.get(position);
        holder.title.setText(post.getTitle());


    }

    @Override
    public int getItemCount() {

        if (posts==null)
            return 0;

        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.listTitle);
        }
    }
}
