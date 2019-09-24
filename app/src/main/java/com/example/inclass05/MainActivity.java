/*Group 1 5
 Ravina Gaikawad
 Sameer Shanbhag*/

package com.example.inclass05;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GetImageNamesAsyncTask.IGetImageNames, GetImagesAsyncTask.IGetImages, GetImagesUrlAsyncTask.IGetImagesURL {
    String[] imageNames;
    String[] imageUrls;
    int count = 0;
    String keyword = "keyword";
    ImageView iv_main;
    ProgressBar pb_main;
    LinearLayout ll1;
    TextView tv_enterKeyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll1 = findViewById(R.id.ll1);
        ll1.setVisibility(View.VISIBLE);

        iv_main = findViewById(R.id.iv_main);
        iv_main.setVisibility(View.INVISIBLE);

        pb_main = findViewById(R.id.pb_main);
        pb_main.setVisibility(View.INVISIBLE);

        tv_enterKeyword = findViewById(R.id.tv_enterKeyword);

        findViewById(R.id.bt_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()){
                    pb_main.setVisibility(View.VISIBLE);
                    new GetImageNamesAsyncTask(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.iv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_main.setVisibility(View.VISIBLE);
                if(imageUrls.length > 0){
                    if(count == imageUrls.length - 1){
                        count = 0;
                        new GetImagesAsyncTask(MainActivity.this).execute(imageUrls[count]);
                    } else {
                        count = count + 1;
                        new GetImagesAsyncTask(MainActivity.this).execute(imageUrls[count]);
                    }
                }
            }
        });

        findViewById(R.id.iv_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_main.setVisibility(View.VISIBLE);
                if(imageUrls.length > 0){
                    if(count == 0){
                        count = imageUrls.length - 1;
                        new GetImagesAsyncTask(MainActivity.this).execute(imageUrls[count]);
                    } else {
                        count = count - 1;
                        new GetImagesAsyncTask(MainActivity.this).execute(imageUrls[count]);
                    }
                }

            }
        });
    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if(networkCapabilities != null)
        {
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getImageNames(final String[] imageNames) {
        this.imageNames = imageNames;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Keyword")
                .setItems(imageNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pb_main.setVisibility(View.VISIBLE);
                        RequestParams requestParams = new RequestParams();
                        requestParams.addParams(keyword, imageNames[i]);
                        tv_enterKeyword.setText(imageNames[i]);
                        new GetImagesUrlAsyncTask(requestParams, MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/index.php");
                    }
                });

        final AlertDialog alertDialog = builder.create();
        pb_main.setVisibility(View.GONE);
        alertDialog.show();
    }

    public void getImagesURL(final String[] imageUrls) {
        this.imageUrls = imageUrls;
        if(this.imageUrls[0] != "" && this.imageUrls.length > 0){
            new GetImagesAsyncTask(MainActivity.this).execute(imageUrls[0]);
        }
        else
        {
            Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
            pb_main.setVisibility(View.GONE);
            iv_main.setVisibility(View.GONE);

        }
    }

    @Override
    public void getImages(Bitmap bitmap) {
        if (bitmap != null) {
            iv_main.setVisibility(View.VISIBLE);
            iv_main.setImageBitmap(bitmap);
            pb_main.setVisibility(View.GONE);
        } else {
            Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT);
        }
    }
}
