package com.example.user.cvtest1;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.opencv.core.Mat;
import java.io.IOException;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public final static  String PICTURE_INTENT_EXTRA = "PICTURE_TO_USE";
    private Uri imageUri;

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

        final Button colorButton = (Button) findViewById(R.id.colorButton);

        colorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startColorApp();
            }
        });

        if(!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for Initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mOpenCVCallBack);
        }else{
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        Bitmap bitmap = null;

        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            imageUri = Uri.parse(extras.getString(FirstActivity.PICTURE_INTENT_EXTRA));
            String filePath = extras.getString("FILE");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            InputStream stream = getResources().openRawResource(R.raw.image1);
            bitmap = BitmapFactory.decodeStream(stream);
        }


        ImageView imgOrigIV = (ImageView) findViewById(R.id.imageOriginal);
        imgOrigIV.setImageBitmap(bitmap);

        Mat imgOrigMat = new Mat();
        Utils.bitmapToMat(bitmap, imgOrigMat);

        Mat edgesMat = new Mat(imgOrigMat.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(imgOrigMat, edgesMat, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edgesMat, edgesMat, 50, 100);
        Bitmap bitmapEdges = Bitmap.createBitmap(imgOrigMat.cols(), imgOrigMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edgesMat, bitmapEdges);

        ImageView imgEdgesIV = (ImageView) findViewById(R.id.imageEdges);
        imgEdgesIV.setImageBitmap(bitmapEdges);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(MainActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    protected void startColorApp(){
        Intent intent = new Intent(MainActivity.this, ColorActivity.class);
        intent.putExtra(PICTURE_INTENT_EXTRA, imageUri.toString());
        intent.putExtra("IMAGE", getRealPathFromURI(imageUri));
        startActivity(intent);
    }
}

