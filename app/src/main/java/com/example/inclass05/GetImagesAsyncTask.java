package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImagesAsyncTask extends AsyncTask<String, Void, Bitmap> {
        IGetImages iData;
        public GetImagesAsyncTask(IGetImages iData){
            this.iData = iData;
        }

        @Override
        protected Bitmap doInBackground(String... params){
            Bitmap bitMap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                bitMap = BitmapFactory.decodeStream(con.getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitMap;
        }

        @Override
        protected void onPostExecute(Bitmap bitMap) {
            iData.getImages(bitMap);
        }

        public static interface IGetImages{
            public void getImages(Bitmap bitmap);
        }
    }

