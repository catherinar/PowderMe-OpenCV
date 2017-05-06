package com.example.user.cvtest1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);


        Bitmap bitmap = null;

        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            Uri imageUri = Uri.parse(extras.getString(MainActivity.PICTURE_INTENT_EXTRA));
            String filePath = extras.getString("IMAGE");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            InputStream stream = getResources().openRawResource(R.raw.image1);
            bitmap = BitmapFactory.decodeStream(stream);
        }

        ImageView imgOrigIV = (ImageView) findViewById(R.id.withoutColor);
        imgOrigIV.setImageBitmap(bitmap);
    }
}
