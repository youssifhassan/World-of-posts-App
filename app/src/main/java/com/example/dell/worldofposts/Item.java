package com.example.dell.worldofposts;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{

    String title;
    String content;

    protected Item(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Item(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Item() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }
}
