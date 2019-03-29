package com.e.infoscreencontrolapp.UpdatePost;

import android.graphics.Bitmap;
import android.util.Base64;

import com.e.infoscreencontrolapp.Http.HttpAsyncTaskGet;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPost;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPut;
import com.e.infoscreencontrolapp.Http.TaskCompleted;
import com.e.infoscreencontrolapp.Model.PostDTO;
import com.e.infoscreencontrolapp.Model.PostTypeDTO;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class UpdatePostPresenter implements TaskCompleted {

    public interface View {
    }

    private View v;

    public UpdatePostPresenter(View v) {
        this.v = v;
    }


    @Override
    public void onTaskCompleted(Object obj) {
        if (obj != null) {

        }
    }

    public void updatePost(String headline, String body, boolean checked, Bitmap picture, PostDTO post) {
        post.headline = headline;
        post.featured = checked;
        if (post.postType.name.equals("Text")) {
            post.text = body;
        } else {
            post.picture = BitmapToBase64(picture);
        }
        new HttpAsyncTaskPut(this, post, "post").execute();
    }

    /*
Convert bitmap to base64
 */
    private String BitmapToBase64(Bitmap picture) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] pictureByte = stream.toByteArray();
        picture.recycle();
        return Base64.encodeToString(pictureByte, Base64.DEFAULT);
    }
}
