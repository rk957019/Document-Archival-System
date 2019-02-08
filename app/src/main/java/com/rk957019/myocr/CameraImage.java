package com.rk957019.myocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;

public class CameraImage
{
    ImageView mImageView;
    Bitmap mImageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

   public static void dispatchTakePictureIntent(Context context, Activity activity)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null)
        {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


}
