package com.e.infoscreencontrolapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class PostTypeDTO {
    public String name;
    public Date created;

    public PostTypeDTO(String name, Date created){
        this.name = name;
        this.created = created;
    }

    @Override
    public String toString() {
        return name;
    }
}
