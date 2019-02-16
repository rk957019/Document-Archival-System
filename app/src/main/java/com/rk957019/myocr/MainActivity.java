package com.rk957019.myocr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.ArrayList;
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
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;
    public static boolean permission;

    ImageView imageView ;
    Button galleryBtn;
    Button uploadBtn;
    Uri imageURi;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        galleryBtn = (Button) findViewById(R.id.from_gallery);
        uploadBtn = (Button) findViewById(R.id.upload);
        isWriteStoragePermissionGranted();
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setImageGallery();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("main","meassage");
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {

                            Log.e("main","Message");

                            executeSSHcommand();


                        } catch (Exception e) {
                            e.printStackTrace();
//                            Toast.makeText(MainActivity.this, "Toast Message",
//                                    Toast.LENGTH_LONG).show();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                    }
                }.execute(1);
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
           mCurrentPhotoPath = getRealPathFromURI(getApplicationContext(),imageURi);
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
                Log.e("main","meassage1");
               // executeSSHcommand();

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
    public  boolean isWriteStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("main","Permission is granted2");
                permission = true;
                return true;
            } else
            {

                Log.e("main","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                permission =false;
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            permission = true;
            Log.e("main","Permission is granted2");
            return true;
        }
    }
    public  void executeSSHcommand()
    {
        String user = "rahulkumar.cs17";
        String password = "10/12/1998";
        String host = "172.16.1.3";
        int port=22;

        try {

            JSch jsch = new JSch();
            Log.e("main", "Message1");
            Session session = jsch.getSession(user, host, port);

            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            String text = ActualText.mOcrText.toLowerCase();
            Log.e("main",text);
            KeyWords keyWords = new KeyWords();
            ArrayList<String> KEYWORDS = keyWords.getMkeywords();
            for (int i = 0; i < KEYWORDS.size(); i++)
            {

                String keyword = KEYWORDS.get(i);
                Log.e("main",keyword);

                if(!text.contains(keyword))continue;
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand("cd /home/stud/btech/cse/2017/rahulkumar.cs17/android && mkdir " + keyword);
                channel.connect();

                Log.e("main", "Message2");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.disconnect();
                ChannelSftp channel2 = (ChannelSftp) session.openChannel("sftp");
                channel2.connect();
                try
                {
                    if (isWriteStoragePermissionGranted())
                        channel2.put(mCurrentPhotoPath, "/home/stud/btech/cse/2017/rahulkumar.cs17/android/" + keyword);
                } catch (SftpException e)
                {
                    e.printStackTrace();
                }
                Log.e("main", "Message3");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel2.disconnect();


            }
        }
        catch(JSchException e)
            {

            }

    }
    private String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("main", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
