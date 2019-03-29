package com.e.infoscreencontrolapp.Http;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class HttpAsyncTask extends AsyncTask<String, Void, Object> {
    protected TaskCompleted listener;
    protected String url;
    protected Object obj;

    public HttpAsyncTask(TaskCompleted listener, Object obj, String url) {
        this.obj = obj;
        this.url = url;
        this.listener = listener;
    }

    //Call when task is execute
    @Override
    protected abstract Object doInBackground(String... strings);

    @Override
    protected void onPostExecute(Object type) {
        super.onPostExecute(type);
        listener.onTaskCompleted(type);
    }

    protected String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}