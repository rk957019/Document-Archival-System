package com.rk957019.myocr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
   static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;

    ImageView imageView ;
    Button galleryBtn;
    Uri imageURi;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        galleryBtn = (Button) findViewById(R.id.from_gallery);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setImageGallery();
            }
        });
    }
    void setImageGallery()
    {
        Intent fromGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(fromGalleryIntent,REQUEST_GALLERY_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK)
        {
            imageURi = data.getData();
            useImageUri(imageURi);
        }
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {

            setPic();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_upload:
                // To do Things;
//                CameraImage.dispatchTakePictureIntent(getApplicationContext(),this);
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
//                {
//                  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null)
                    {
                        Toast.makeText(this, "This is my Toast message!",
                                Toast.LENGTH_LONG).show();
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.rk957019.myocr.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                     startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        galleryAddPic();
                 }
                }
                return  true;
            case R.id.action_help:
                // To do things
                return  true;
            case R.id.action_extract:
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap());
                processText(image);
                return  true;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
    void  useImageUri(Uri uri)
    {
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Log.v("MAIN","Problem in setting bitmap");
        }

    }
    public String processText(FirebaseVisionImage image)
    {
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        final StringBuilder detectedText = new StringBuilder();
//        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
                        {

                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]

//                                TextView text = (TextView) findViewById(R.id.detectText);
//                                text.setText(firebaseVisionText.getText());
                                StringBuilder string = new StringBuilder();
                                for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks())
                                {
                                   String textBlock = block.getText();
                                   string.append(textBlock);
                                   string.append("");
                                }


                                ActualText actualText = new ActualText(string.toString());
                                Intent intent = new Intent(MainActivity.this, OcrTextActivity.class);
                                startActivity(intent);
                                // [END get_text]
                                // [END_EXCLUDE]

                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure( Exception e) {
                                        // Task failed with an exception
                                        // ...

                                        detectedText.append("");
                                        Log.v("main","not detected");
                                    }
                                });

        return  detectedText.toString();
    }
    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void setPic()
    {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
