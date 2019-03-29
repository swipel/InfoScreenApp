package com.e.infoscreencontrolapp.CreatePost;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e.infoscreencontrolapp.Fragments;
import com.e.infoscreencontrolapp.Main.MainPage;
import com.e.infoscreencontrolapp.Model.PostTypeDTO;
import com.e.infoscreencontrolapp.R;
import com.e.infoscreencontrolapp.Utils.TextValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class CreatePost extends Fragments implements CreatePostPresenter.View {

    private Button createPostButton;
    private Button cancelPostButton;
    private EditText headline;
    private EditText body;
    private CheckBox checkbox;
    private CreatePostPresenter presenter = new CreatePostPresenter(this);
    private CreatePostAdapterSpinner adapter;
    private Spinner postTypeSpin;
    private ImageView imageChosen;

    private boolean headBool = false;
    private boolean bodyBool = false;
    final int PICK_CONTACT_REQUEST = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageChosen = getActivity().findViewById(R.id.imageChosen);
        createPostButton = getActivity().findViewById(R.id.createPostButton);
        createPostButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        cancelPostButton = getActivity().findViewById(R.id.cancelPostButton);
        headline = getActivity().findViewById(R.id.headlineText);
        body = getActivity().findViewById(R.id.bodyText);
        checkbox = getActivity().findViewById(R.id.checkBoxActive);

        //Validate headline field
        headline.addTextChangedListener(new TextValidator(headline) {
            @Override
            public void validate(TextView textView, String text, Boolean validated) {
                headBool = validated;
                checkPostValidation();
            }
        });

        //Validate body field
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
                presenter.createPost(headline.getText().toString(), body.getText().toString(), postTypeSpin.getSelectedItem(), checkbox.isChecked(), ((BitmapDrawable)imageChosen.getDrawable()).getBitmap());
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
    Enable or disable create button
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
    Change to main page
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
    Change post type
     */
    @Override
    public void onPostTypeUpdate(final PostTypeDTO[] postTypes) {
        adapter = new CreatePostAdapterSpinner(getActivity(),
                R.layout.posttype_spinner_view, R.id.postTypeSpinnerText, postTypes);
        postTypeSpin = (Spinner) getActivity().findViewById(R.id.spinnerPostType);
        postTypeSpin.setAdapter(adapter);

        postTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (postTypes[position].name.equals("Text")){
                    body.setVisibility(View.VISIBLE);
                    imageChosen.setVisibility(View.INVISIBLE);

                } else if (postTypes[position].name.equals("Picture")) {
                    imageChosen.setVisibility(View.VISIBLE);
                    body.setVisibility(View.INVISIBLE);

                    //Get picture from library
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_CONTACT_REQUEST);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Needs to be override
            }
        });
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
                imageChosen.setImageBitmap(selectedImage);
                bodyBool = true;
                checkPostValidation();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            body.setVisibility(View.VISIBLE);
            imageChosen.setVisibility(View.INVISIBLE);
            postTypeSpin.setSelection(0);
        }
    }
}
