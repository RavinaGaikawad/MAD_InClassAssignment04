package com.example.inclass05;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImagesUrlAsyncTask extends AsyncTask<String, Void, String[]> {
    Bitmap bitmap;
    RequestParams mparams;
    IGetImagesURL getImagesURL;

    public GetImagesUrlAsyncTask(RequestParams params, IGetImagesURL getImagesURL) {
        mparams = params;
        this.getImagesURL = getImagesURL;
    }

    @Override
    protected String[] doInBackground(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        bitmap = null;
        String[] imageLinks = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mparams.GetEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                stringBuilder.append(line+"\n");
            }
            imageLinks = stringBuilder.toString().split("\n");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageLinks;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        if(strings != null && strings.length > 0)
            getImagesURL.getImagesURL(strings);

    }

    public static  interface IGetImagesURL{
        public void getImagesURL(String[] imageUrls);
    }
}
