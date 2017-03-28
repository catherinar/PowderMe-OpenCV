package com.example.user.cvtest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgraw = (ImageView) findViewById(R.id.imageView1);
        ImageView edges = (ImageView) findViewById(R.id.imageView2);

        //try{
        //    Mat
        //}

        if(!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "OPENCV not working");
        }else{
            Log.d(this.getClass().getSimpleName(), "OPENCV working");
        }
    }
}
