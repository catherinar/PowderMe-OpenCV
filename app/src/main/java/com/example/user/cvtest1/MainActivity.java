package com.example.user.cvtest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import org.opencv.core.Mat;
import java.io.InputStream;


import org.opencv.android.OpenCVLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgraw = (ImageView) findViewById(R.id.imageView1);
        ImageView edges = (ImageView) findViewById(R.id.imageView2);
        //replacing above with below

        //ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
        //imageView.setImageBitmap(BitmapFactory.decodeFile(pathToPicture));

        //try{
        //    Mat
        //}

        if(!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "OPENCV not working");
        }else{
            Log.d(this.getClass().getSimpleName(), "OPENCV working");
        }

       String inputFileName="simm_01";
        String inputExtension = "jpg";
        String inputDir = getCacheDir().getAbsolutePath();  // use the cache directory for i/o
        String outputDir = getCacheDir().getAbsolutePath();
        String outputExtension = "png";
        String inputFilePath = inputDir + File.separator + inputFileName + "." + inputExtension;



        Log.d (this.getClass().getSimpleName(), "loading " + inputFilePath + "...");
        Mat image = Imgcodecs.imread(inputFilePath);
        Log.d (this.getClass().getSimpleName(), "width of " + inputFileName + ": " + image.width());
// if width is 0 then it did not read your image.


// for the canny edge detection algorithm, play with these to see different results
        int threshold1 = 70;
        int threshold2 = 100;

        Mat im_canny = new Mat();  // you have to initialize output image before giving it to the Canny method
        Imgproc.Canny(image, im_canny, threshold1, threshold2);
        String cannyFilename = outputDir + File.separator + inputFileName + "_canny-" + threshold1 + "-" + threshold2 + "." + outputExtension;
        Log.d (this.getClass().getSimpleName(), "Writing " + cannyFilename);
        Imgcodecs.imwrite(cannyFilename, im_canny);



}}
