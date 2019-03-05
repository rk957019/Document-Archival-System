package com.rk957019.myocr;

public class ActualText
{
   public static String mOcrText;

    public ActualText(String mOcrText)
    {
        this.mOcrText = mOcrText.toLowerCase();
    }

    public String getmOcrText() {
        return mOcrText;
    }
}
