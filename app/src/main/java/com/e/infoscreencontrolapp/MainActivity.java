package com.e.infoscreencontrolapp;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.e.infoscreencontrolapp.CreatePost.CreatePost;
import com.e.infoscreencontrolapp.Main.MainPage;

public class MainActivity extends AppCompatActivity implements CreatePost.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainPage fragment = new MainPage();
        Bundle bundle = new Bundle(1);
        bundle.putInt("layout", R.layout.fragment_main_page);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
