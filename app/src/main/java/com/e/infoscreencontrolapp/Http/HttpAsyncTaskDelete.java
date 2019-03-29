package com.e.infoscreencontrolapp.Http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpAsyncTaskDelete extends HttpAsyncTask {

    public HttpAsyncTaskDelete(TaskCompleted listener, Object obj, String url) {
        super(listener, obj, url);
    }

    //Call when task is execute
    @Override
    protected Object doInBackground(String... strings) {
        String json;
        try {
            //Post request
            URL urlCon = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlCon.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod( "DELETE" );
            httpURLConnection.setRequestProperty( "Content-Type", "application/json");
            httpURLConnection.setRequestProperty( "charset", "utf-8");
            httpURLConnection.setRequestProperty("APIKey", "KMzkkAgjh52W-yD6Uqtcnu%YFZjU=t-sLQ6+Y*rrGeUSKUd@U-=Gx&$*@UQ3KNn9");

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            obj = gson.toJson(obj);

            OutputStreamWriter wr= new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(obj.toString());
            wr.flush();
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpURLConnection.getInputStream();
                in.close();
            } else {
                Log.e("Failede creating", responseCode + "");
            }
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
