package com.rk957019.myocr;

import java.util.ArrayList;

public class KeyWords
{
    public static  ArrayList<String>mkeywords = new ArrayList<String>();
    public static ArrayList<ArrayList<String>> m2d_keywords = new ArrayList<ArrayList<String>>();

    public KeyWords()
    {
        setmKeyWords();
        setM2d_keywords();
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
        mkeywords.add("somanath");
        mkeywords.add("sourav");
        mkeywords.add("suman kumar");
        mkeywords.add("rajiv");
        mkeywords.add("joydeep");
        mkeywords.add("sriparna");
        mkeywords.add("hod,cse");
        mkeywords.add("hod, cse");
        mkeywords.add("director");
        mkeywords.add("director_office");
        mkeywords.add("associate dean");
        mkeywords.add("r&d");
        mkeywords.add("inspection");
        mkeywords.add("assistant registrar");
        mkeywords.add("registrar");
        mkeywords.add("Junior Superintendent");
        mkeywords.add("Rakesh Kumar");
        mkeywords.add("Vishwa Ranjan");
        mkeywords.add("Jeetendra Kumar");
        mkeywords.add("Akhilendra");
        mkeywords.add("Ashish Kumar");
        mkeywords.add("Ranganathan");
        mkeywords.add("Sanjoy Kumar");
        mkeywords.add("Assistant Librarian");
        mkeywords.add("Mritunjay Anand");
        mkeywords.add("Maneesh Patel");
        mkeywords.add("ashutosh kumar sinha");
        mkeywords.add("neeraj kumar");
        mkeywords.add("shailendra kr. verma");
        mkeywords.add("sanjay");
       // mkeywords.add("purchase");

        mkeywords.add("asim kumar maiti");
        // for testing
       // mkeywords.add("sociology");
        mkeywords.add("jts");
        mkeywords.add("scholarship");
       return;
    }

    public static void setM2d_keywords()
    {
        ArrayList<String> temp = new ArrayList<String>();
        temp.add("academic");
        temp.add("course");
        temp.add("faculty in-charge");
        temp.add("tuition fee");
        temp.add("faculty advisor");
        temp.add("registration");
        temp.add("1701CS");
        temp.add("1801CS");
        temp.add("1601CS");
        temp.add("1501CS");
        temp.add("1811CS");
        temp.add("1711CS");
        temp.add("PCS1");
        temp.add("roll no");
        temp.add("gian");
        temp.add("midsem");
        temp.add("endsem");
        temp.add("research");
        temp.add("attendance");
        m2d_keywords.add(temp);
        ArrayList<String> temp1 = new ArrayList<String>();
        temp1.add("accounts");
        temp1.add("purchase");
        temp1.add("purchasing");
        temp1.add("financial");
        temp1.add("finance");
        temp1.add("payment");
        temp1.add("reimbursement");
        temp1.add("amount");
        m2d_keywords.add(temp1);
        ArrayList<String> temp2 = new ArrayList<String>();
        temp2.add("hospital");
        temp2.add("medical");
        temp2.add("health");
        m2d_keywords.add(temp2);

    }

    public ArrayList<String> getMkeywords()
    {
        return mkeywords;
    }

    public  ArrayList<ArrayList<String>> getM2d_keywords() {
        return m2d_keywords;
    }

}
