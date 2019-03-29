package com.e.infoscreencontrolapp.CreatePost;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.e.infoscreencontrolapp.Model.PostTypeDTO;
import com.e.infoscreencontrolapp.R;


public class CreatePostAdapterSpinner extends ArrayAdapter<PostTypeDTO> {


    private PostTypeDTO[] postType;
    LayoutInflater flater;


    public CreatePostAdapterSpinner(Activity context, int resouceId, int textviewId, PostTypeDTO[] postType) {
        super(context, resouceId, textviewId, postType);
        if (context != null) {
            flater = context.getLayoutInflater();
            this.postType = postType;
        }
    }

    //Need to be override or list is empty
    @Override
    public int getCount(){
        if (postType != null) {
            return postType.length;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PostTypeDTO rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.posttype_spinner_view,null,true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.postTypeSpinnerText);
        txtTitle.setText(rowItem.name);

        return rowview;
    }
}