package com.e.infoscreencontrolapp.CreatePost;

import android.graphics.Bitmap;
import android.util.Base64;

import com.e.infoscreencontrolapp.Http.HttpAsyncTaskGet;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPost;
import com.e.infoscreencontrolapp.Http.TaskCompleted;
import com.e.infoscreencontrolapp.Model.PostDTO;
import com.e.infoscreencontrolapp.Model.PostTypeDTO;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class CreatePostPresenter implements TaskCompleted {

    public interface View {
        void onPostTypeUpdate(PostTypeDTO[] postTypes);
    }
    private View v;

    public CreatePostPresenter(View v) {
        this.v = v;
        getPostType();
    }


    @Override
    public void onTaskCompleted(Object obj) {
        if (obj != null) {
            if (obj instanceof PostTypeDTO[]) {
                v.onPostTypeUpdate((PostTypeDTO[]) obj);
            }
        }
    }

    private void getPostType(){
        new HttpAsyncTaskGet(this, new PostTypeDTO[0], "http://www.skole.pietras.dk/api/postType").execute();

    }

    public void createPost(String headline, String body, Object spinner, boolean checked, Bitmap picture){
        if (spinner == null){
            spinner = "Text";
        }

        String base64Picture = null;

        if(spinner == "Text") {
            picture = null;
        } else {
            body = null;
            base64Picture = BitmapToBase64(picture);
        }
        new HttpAsyncTaskPost(this, new PostDTO(headline, body, base64Picture, checked, new Date(), new PostTypeDTO(spinner.toString(), new Date())), "http://www.skole.pietras.dk/api/post").execute();
    }

    /*
    Convert bitmap to base64
     */
    private String BitmapToBase64(Bitmap picture){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] pictureByte = stream.toByteArray();
        picture.recycle();
        return Base64.encodeToString(pictureByte, Base64.DEFAULT);
    }
}
