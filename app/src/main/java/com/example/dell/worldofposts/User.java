package com.example.dell.worldofposts;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    String userName;
    String userMail;
    String userImage;

    public User(String userName, String userMail, String userImage) {
        this.userName = userName;
        this.userMail = userMail;
        this.userImage = userImage;
    }

    public User() {
    }

    protected User(Parcel in) {
        userName = in.readString();
        userMail = in.readString();
        userImage = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userMail);
        dest.writeString(userImage);
    }
}
