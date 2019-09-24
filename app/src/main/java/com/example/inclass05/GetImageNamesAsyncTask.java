package com.example.inclass05;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImageNamesAsyncTask extends AsyncTask<String, Void, String> {
    IGetImageNames getImageNames;

    public GetImageNamesAsyncTask(IGetImageNames getImageNames) {
        this.getImageNames = getImageNames;
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null){
            String[] imagenames;
            imagenames = result.split(";");
            getImageNames.getImageNames(imagenames);
        }
    }
    public static  interface IGetImageNames{
        public void getImageNames(String[] imageNames);
    }
}
