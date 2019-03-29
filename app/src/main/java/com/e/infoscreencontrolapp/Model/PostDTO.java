package com.e.infoscreencontrolapp.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class PostDTO implements Parcelable {
    public long postId;
    public String headline;
    public String text;
    public boolean featured;
    public Date created;
    public PostTypeDTO postType;
    public String picture;

    public PostDTO(){
    }

    //PostDTO with image
    public PostDTO(String headline, String text, String image, boolean featured, Date created, PostTypeDTO postType){
        this.headline = headline;
        this.text = text;
        this.picture = image;
        this.featured = featured;
        this.created = created;
        this.postType = postType;
    }

    protected PostDTO(Parcel in) {
        postId = in.readLong();
        headline = in.readString();
        text = in.readString();
        featured = in.readByte() != 0;
        picture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(postId);
        dest.writeString(headline);
        dest.writeString(text);
        dest.writeByte((byte) (featured ? 1 : 0));
        dest.writeString(picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostDTO> CREATOR = new Creator<PostDTO>() {
        @Override
        public PostDTO createFromParcel(Parcel in) {
            return new PostDTO(in);
        }

        @Override
        public PostDTO[] newArray(int size) {
            return new PostDTO[size];
        }
    };
}
