package com.e.infoscreencontrolapp.Main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.e.infoscreencontrolapp.Http.HttpAsyncTaskGet;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPost;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPut;
import com.e.infoscreencontrolapp.Http.TaskCompleted;
import com.e.infoscreencontrolapp.Model.ListViewPostViewHolder;
import com.e.infoscreencontrolapp.Model.PostDTO;
import com.e.infoscreencontrolapp.R;

public class MainPageAdapterPost extends ArrayAdapter<PostDTO> implements TaskCompleted {


    private Context context;
    private PostDTO[] posts;

    public MainPageAdapterPost(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public void updatePostData(PostDTO[] posts) {
        this.posts = posts;
    }

    //Need to be override or list is empty
    @Override
    public int getCount() {
        if (posts != null) {
            return posts.length;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListViewPostViewHolder mViewHolder = new ListViewPostViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.post_view, parent, false);
            mViewHolder.headline = (TextView) convertView.findViewById(R.id.postViewText);
            mViewHolder.created = (TextView) convertView.findViewById(R.id.postViewCreatedText);
            mViewHolder.featured = (CheckBox) convertView.findViewById(R.id.postViewCheckBox);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ListViewPostViewHolder) convertView.getTag();
        }
        if (posts != null) {
            mViewHolder.headline.setText(posts[position].headline);

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            mViewHolder.created.setText(df.format("dd-MM-yyyy", posts[position].created).toString());
            mViewHolder.featured.setChecked(posts[position].featured);

            mViewHolder.featured.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    PostDTO post = posts[position];
                    post.featured = checkBox.isChecked();
                    new HttpAsyncTaskPut(getInstance(), post, "post").execute();
                    if (posts[position].featured) {
                        Toast.makeText(context, "Tilføjet " + posts[position].headline + " fra information skærmen", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fjernet " + posts[position].headline + " fra information skærmen", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return convertView;
    }

    private TaskCompleted getInstance() {
        return this;
    }

    //TODO Rewrite HTTP class so post/put/delete get own interface
    @Override
    public void onTaskCompleted(Object obj) {
        if (obj != null) {
            /*if (obj instanceof PostDTO) {
                if (((PostDTO) obj).featured) {
                    Toast.makeText(context, "Tilføjet " + ((PostDTO) obj).featured + " fra information skærmen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fjernet " + ((PostDTO) obj).featured + " fra information skærmen", Toast.LENGTH_SHORT).show();
                }
            }*/
        }
    }
}
