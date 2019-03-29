package com.e.infoscreencontrolapp.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.e.infoscreencontrolapp.CreatePost.CreatePost;
import com.e.infoscreencontrolapp.Fragments;
import com.e.infoscreencontrolapp.Model.IntervalDTO;
import com.e.infoscreencontrolapp.Model.PostDTO;
import com.e.infoscreencontrolapp.R;
import com.e.infoscreencontrolapp.UpdatePost.UpdatePost;
import com.e.infoscreencontrolapp.Utils.TextValidator;


public class MainPage extends Fragments implements MainPagePresenter.View {
    private Button createPostButton;
    private ListView postListView;
    private MainPageAdapterPost adapter;
    private MainPagePresenter presenter = new MainPagePresenter(this);
    private PostDTO[] posts;
    private EditText intervalText;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intervalText = getActivity().findViewById(R.id.mainPageInterval);
        presenter.getIntervalFromHttp();
        createPost();
        postListView();
        intervalUpdate();
    }

    /*
        Interval editText
        add listener for textWatcher
        Call presenter when a value change and pass validator
     */
    private void intervalUpdate(){
        intervalText.addTextChangedListener(new TextValidator(intervalText) {
            @Override
            public void validate(TextView textView, String text, Boolean validated) {
                if (validated){
                    try {
                        presenter.updateIntervalToHttp(Integer.parseInt(text));
                    } catch (NumberFormatException e) {
                        intervalText.setError("Må kun være hele tal");
                    }
                }
            }
        });
    }

    /*
     CreatePost button
     add listener on click
     Change side when click
     */
    private void createPost(){
        createPostButton = getActivity().findViewById(R.id.changeViewCreatePost);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CreatePost fragment = new CreatePost();
                Bundle bundle = new Bundle(1);
                bundle.putInt("layout", R.layout.fragment_create_post);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    /*
    Post listView
    Custom Post arrayAdaptor
    Add listener on click
    Call alertDialog when click with "Change" or "Delete"
     */
    private void postListView(){
        adapter = new MainPageAdapterPost(getActivity(),
                android.R.layout.simple_spinner_item);

        postListView = (ListView) getActivity().findViewById(R.id.listPost);
        postListView.setAdapter(adapter);
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String[] Types = {"Redigere", "Slet"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Vælg en handling");
                builder.setItems(Types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (Types[item] == "Redigere") {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            UpdatePost fragment = new UpdatePost();
                            Bundle bundle = new Bundle(1);
                            bundle.putInt("layout", R.layout.fragment_update_post);
                            bundle.putParcelable("post", posts[position]);
                            fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_frame, fragment);
                            fragmentTransaction.commit();
                        } else if (Types[item] == "Slet") {
                            deleteAlert(position);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    /*
    Handler in presenter call this method every x seconds with updatede post data for listView of posts
     */
    @Override
    public void onPostUpdate(PostDTO[] posts) {
        this.posts = posts;
        adapter.updatePostData(posts);
        adapter.notifyDataSetChanged();
    }

    /*
    Presenter call this with interval value from db
     */
    @Override
    public void onIntervalUpdate(IntervalDTO interval) {
        Log.e("Interval", "Update");
        intervalText.setText(Integer.toString(interval.interval));
    }

    /*
    Second alertDialog
    When click true delete
     */
    private void deleteAlert(final int position){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        presenter.deletePostToHttp(posts[position]);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Er du sikker?").setPositiveButton("Ja", dialogClickListener)
                .setNegativeButton("Nej", dialogClickListener).show();
    }
}
