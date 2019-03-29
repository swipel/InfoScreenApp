package com.e.infoscreencontrolapp.UpdatePost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.infoscreencontrolapp.Fragments;
import com.e.infoscreencontrolapp.Main.MainPage;
import com.e.infoscreencontrolapp.Model.PostDTO;
import com.e.infoscreencontrolapp.R;
import com.e.infoscreencontrolapp.Utils.TextValidator;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class UpdatePost extends Fragments implements UpdatePostPresenter.View {

    private Button createPostButton;
    private Button cancelPostButton;
    private EditText headline;
    private EditText body;
    private ImageView picture;
    private CheckBox checkbox;
    private UpdatePostPresenter presenter = new UpdatePostPresenter(this);
    private PostDTO post;

    final int PICK_CONTACT_REQUEST = 1;
    private boolean headBool = true;
    private boolean bodyBool = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("post")) {
            post = getArguments().getParcelable("post");
        }

        createPostButton = getActivity().findViewById(R.id.createPostUpdateButton);
        cancelPostButton = getActivity().findViewById(R.id.cancelPostUpdateButton);
        headline = getActivity().findViewById(R.id.headlineUpdateText);
        body = getActivity().findViewById(R.id.bodyUpdateText);
        picture = getActivity().findViewById(R.id.imageUpdateChosen);
        checkbox = getActivity().findViewById(R.id.checkBoxUpdateActive);

        headline.setText(post.headline);
        checkbox.setChecked(post.featured);

        if (post.postType.name.equals("Text")) {
            body.setText(post.text);
            body.setVisibility(View.VISIBLE);
            picture.setVisibility(View.INVISIBLE);
        } else {
            updatePicture();
        }

        headline.addTextChangedListener(new TextValidator(headline) {
            @Override
            public void validate(TextView textView, String text, Boolean validated) {
                headBool = validated;
                checkPostValidation();
            }
        });

        body.addTextChangedListener(new TextValidator(body) {
            @Override
            public void validate(TextView textView, String text, Boolean validated) {
                bodyBool = validated;
                checkPostValidation();
            }
        });


        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updatePost(headline.getText().toString(), body.getText().toString(), checkbox.isChecked(), ((BitmapDrawable)picture.getDrawable()).getBitmap(), post);
                changePage();
            }
        });

        cancelPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage();
            }
        });
    }

    /*
    Update imageviwe with image from json
     */
    private void updatePicture(){
        byte[] decodedString = Base64.decode(post.picture,Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(decodedString);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);

        picture.setImageBitmap(bitmap);
        picture.setVisibility(View.VISIBLE);
        body.setVisibility(View.INVISIBLE);

        //When image clickede open libaray
        picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Get picture from library
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_CONTACT_REQUEST);
            }
        });
    }

    /*
    Validate head and body and change button from active or disable
     */
    private void checkPostValidation() {
        if (headBool && bodyBool) {
            createPostButton.setEnabled(true);
            createPostButton.getBackground().setColorFilter(null);
        } else {
            createPostButton.setEnabled(false);
            createPostButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        }
    }

    /*
    Change page
     */
    private void changePage() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainPage fragment = new MainPage();
        Bundle bundle = new Bundle(1);
        bundle.putInt("layout", R.layout.fragment_main_page);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }

/*
Result from intent image library
 */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                picture.setImageBitmap(selectedImage);
                bodyBool = true;
                checkPostValidation();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
