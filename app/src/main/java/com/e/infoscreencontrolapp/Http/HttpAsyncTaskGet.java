package com.e.infoscreencontrolapp.Http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpAsyncTaskGet extends HttpAsyncTask {
    public HttpAsyncTaskGet(TaskCompleted listener, Object obj, String url) {
        super(listener, obj, url);
    }

    //Call when task is execute
    @Override
    protected Object doInBackground(String... strings) {
        String json;
        try {
            //Get request
            URL urlCon = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlCon.openConnection();
            httpURLConnection.setRequestProperty("APIKey", "KMzkkAgjh52W-yD6Uqtcnu%YFZjU=t-sLQ6+Y*rrGeUSKUd@U-=Gx&$*@UQ3KNn9");
            InputStream inputStream = httpURLConnection.getInputStream();
            json = convertInputStreamToString(inputStream);

            //Modify dateformat for json to work with net core standard datetime
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            obj = gson.fromJson(json, obj.getClass());
            return obj;
        } catch (MalformedURLException e) {
            Log.e("Malformed", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IOE", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
