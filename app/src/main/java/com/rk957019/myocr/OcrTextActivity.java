package com.rk957019.myocr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OcrTextActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_text);
        TextView text = findViewById(R.id.detectedText);
        text.setText(ActualText.mOcrText);
    }
}
