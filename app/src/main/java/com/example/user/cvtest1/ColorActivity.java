package com.example.user.cvtest1;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;
import java.io.InputStream;

public class ColorActivity extends AppCompatActivity {

    public static int color = android.graphics.Color.GREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        final Button redButton = (Button) findViewById(R.id.redButton);
        final Button greenButton = (Button) findViewById(R.id.greenButton);
        final Button blueButton = (Button) findViewById(R.id.blueButton);

        redButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color = android.graphics.Color.RED;
                finish();
                startActivity(getIntent());
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color = android.graphics.Color.GREEN;
                finish();
                startActivity(getIntent());
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color = android.graphics.Color.BLUE;
                finish();
                startActivity(getIntent());
            }
        });


        Bitmap bitmap = null;

        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            Uri imageUri = Uri.parse(extras.getString(MainActivity.PICTURE_INTENT_EXTRA));
            String filePath = extras.getString("IMAGE");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream stream = getResources().openRawResource(R.raw.image1);
            bitmap = BitmapFactory.decodeStream(stream);
        }

        //ImageView imgOrigIV = (ImageView) findViewById(R.id.withoutColor);
        //imgOrigIV.setImageBitmap(bitmap);

        //Bitmap bitmap = BitmapFactory.decodeStream(stream);

        //TextView info = (TextView) findViewById(R.id.infobox);

        // A new face detector is created for detecting the face and its landmarks.
        //
        // Setting "tracking enabled" to false is recommended for detection with unrelated
        // individual images (as opposed to video or a series of consecutively captured still
        // images).  For detection on unrelated individual images, this will give a more accurate
        // result.  For detection on consecutive images (e.g., live video), tracking gives a more
        // accurate (and faster) result.
        //
        // By default, landmark detection is not enabled since it increases detection time.  We
        // enable it here in order to visualize detected landmarks.
        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        // This is a temporary workaround for a bug in the face detector with respect to operating
        // on very small images.  This will be fixed in a future release.  But in the near term, use
        // of the SafeFaceDetector class will patch the issue.
        Detector<Face> safeDetector = new SafeFaceDetector(detector);

        // Create a frame from the bitmap and run face detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = safeDetector.detect(frame);

        if (!safeDetector.isOperational()) {
            // info.setText("Dependency not installed");
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            //Log.w(TAG, "Face detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
               // Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                //Log.w(TAG, getString(R.string.low_storage_error));
            }
        } else {
            //info.setText("should be working");
        }


        FaceView overlay = (FaceView) findViewById(R.id.withColor);
        overlay.setContent(bitmap, faces, color);

        // Although detector may be used multiple times for different images, it should be released
        // when it is no longer needed in order to free native resources.
        safeDetector.release();
    }
}
