package com.e.infoscreencontrolapp.Main;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.e.infoscreencontrolapp.Http.HttpAsyncTaskDelete;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskGet;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPost;
import com.e.infoscreencontrolapp.Http.HttpAsyncTaskPut;
import com.e.infoscreencontrolapp.Http.TaskCompleted;
import com.e.infoscreencontrolapp.Model.IntervalDTO;
import com.e.infoscreencontrolapp.Model.PostDTO;
import com.e.infoscreencontrolapp.Model.PostTypeDTO;

public class MainPagePresenter implements TaskCompleted {
    public interface View {
        void onPostUpdate(PostDTO[] posts);
        void onIntervalUpdate(IntervalDTO interval);
    }

    @Override
    public void onTaskCompleted(Object obj) {
        if (obj != null){
            if (obj instanceof PostDTO[]) {
                v.onPostUpdate((PostDTO[])obj);
            } else if (obj instanceof PostDTO) {
                Log.e("delte", "Delete");
            } else if (obj instanceof IntervalDTO){
                v.onIntervalUpdate((IntervalDTO)obj);
            }
        }
    }

    private View v;
    private int interval = 5000;
    private AsyncTask pageTask;
    private final Handler handler = new Handler();

    public MainPagePresenter(View v) {
        this.v = v;
        requestDataFromHttp();
    }

    public void deletePostToHttp(PostDTO post){
        new HttpAsyncTaskDelete(getInstance(), post, "post").execute();
    }

    public void getIntervalFromHttp(){
        new HttpAsyncTaskGet(getInstance(), new IntervalDTO(), "interval").execute();
    }

    public void updateIntervalToHttp(int interval){
        new HttpAsyncTaskPut(getInstance(), new IntervalDTO(interval), "interval").execute();
    }

    //Create handle and start runnable start async task and check if they running
    public void requestDataFromHttp() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pageTask == null || pageTask.getStatus() == AsyncTask.Status.FINISHED) {
                    pageTask = new HttpAsyncTaskGet(getInstance(), new PostDTO[0], "post").execute();
                }
                handler.postDelayed(this, interval);
            }
        }, 0);
    }

    private TaskCompleted getInstance() {
        return this;
    }
}
