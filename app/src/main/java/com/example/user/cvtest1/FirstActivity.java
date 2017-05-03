package com.example.user.cvtest1;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FirstActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;

    public final static  String PICTURE_INTENT_EXTRA = "PICTURE_TO_USE";
    private final int ACTION_REQUEST_GALLERY = 1;
    private final int ACTION_REQUEST_CAMERA = 2;
    private Uri initialURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final Button btnGallery = (Button) findViewById(R.id.button5);


        btnGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[] {"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        // GET IMAGE FROM THE GALLERY
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");

                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);

                                        break;

                                    case 1:
                                        Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");

                                        File cameraFolder;

                                        if (android.os.Environment.getExternalStorageState().equals
                                                (android.os.Environment.MEDIA_MOUNTED))
                                            cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),
                                                    "powder_me/");
                                        else
                                            cameraFolder= FirstActivity.this.getCacheDir();
                                        if(!cameraFolder.exists())
                                            cameraFolder.mkdirs();

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                                        String timeStamp = dateFormat.format(new Date());
                                        String imageFileName = "camera_" + timeStamp + ".jpg";

                                        File photo = new File(Environment.getExternalStorageDirectory(),
                                                "powder_me/" + imageFileName);
                                        getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                                        initialURI = Uri.fromFile(photo);

                                        startActivityForResult(getCameraImage, ACTION_REQUEST_CAMERA);
                                        //Make "camera" option open the OpenCV Face=Detection demo instead
                                        //**Requires NDK installed to run C++ code
//                                        Intent i = new Intent(FirstActivity.this, FdActivity.class);
//                                        startActivity(i);

                                        break;

                                    default:
                                        break;
                                }
                            }
                        });

                builder.show();
            }
        });


    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(FirstActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    protected void startMainApp(){
        Intent i = new Intent(FirstActivity.this, MainActivity.class);
        i.putExtra(PICTURE_INTENT_EXTRA, initialURI.toString());
        i.putExtra("FILE", getRealPathFromURI(initialURI));
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)    {

            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:
                    initialURI = data.getData();
                    break;
                case ACTION_REQUEST_CAMERA:
                    // initialURI is already set/
                    //Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");
                    break;
            }
            //Open initialURi with face recognizer
            startMainApp();


        }
    }
}
