package com.example.user.cvtest1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import org.opencv.core.Mat;
import java.io.InputStream;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {


        @Override
        public void onManagerConnected(int status){
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d(this.getClass().getSimpleName(), "OpenCV Loaded Sucessfully");
                }break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for Initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mOpenCVCallBack);
        }else{
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        InputStream stream = getResources().openRawResource(R.raw.image1);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);

        ImageView imgOrigIV = (ImageView) findViewById(R.id.imageOriginal);
        imgOrigIV.setImageBitmap(bitmap);

        Mat imgOrigMat = new Mat();
        Utils.bitmapToMat(bitmap, imgOrigMat);

        Mat edgesMat = new Mat(imgOrigMat.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(imgOrigMat, edgesMat, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edgesMat, edgesMat, 50, 100);
        Bitmap bitmapEdges = Bitmap.createBitmap(imgOrigMat.cols(), imgOrigMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edgesMat, bitmapEdges);

        // find the imageview and draw it!
        ImageView imgEdgesIV = (ImageView) findViewById(R.id.imageEdges);
        imgEdgesIV.setImageBitmap(bitmapEdges);
    }
}

