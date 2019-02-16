package com.rk957019.myocr;

import java.util.ArrayList;

public class KeyWords
{
    public static  ArrayList<String>mkeywords = new ArrayList<String>();

    public KeyWords()
    {
        setmKeyWords();
    }

    public void setmKeyWords()
    {
        mkeywords.add("jimson");
        mkeywords.add("mayank");
        mkeywords.add("pushpak");
        mkeywords.add("abyayananda");
        mkeywords.add("arijit");
        mkeywords.add("asif");
        mkeywords.add("raju");
        mkeywords.add("samrat");
        mkeywords.add("somnath");
        mkeywords.add("sourav");
        mkeywords.add("suman kumar");
        mkeywords.add("rajiv");
        mkeywords.add("joydeep");
        mkeywords.add("sriparna");
        // for testing
        mkeywords.add("sociology");
        // for testing
        mkeywords.add("esearch");


       return;
    }

    public ArrayList<String> getMkeywords()
    {
        return mkeywords;
    }
}
