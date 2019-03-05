package com.rk957019.myocr;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

public class FileSearching extends AppCompatActivity
{
    String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_searching);
        Button searchBtn = (Button) findViewById(R.id.search);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {

                            Log.e("main","Message");

                            result = executeSSHcommand();


                        } catch (Exception e) {
                            e.printStackTrace();
//                            Toast.makeText(MainActivity.this, "Toast Message",
//                                    Toast.LENGTH_LONG).show();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid)
                    {
                        Toast.makeText(FileSearching.this, "Process Done!\n"+result,
                                Toast.LENGTH_LONG).show();
                        super.onPostExecute(aVoid);
                    }
                }.execute(1);
            }
        });
    }
    public  String executeSSHcommand()
    {
        String user = "rahulkumar.cs17";
        String password = "10/12/1998";
        String host = "172.16.1.3";
        int port = 22;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            JSch jsch = new JSch();
            Log.e("main", "Message1");
            Session session = jsch.getSession(user, host, port);

            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            channel.setOutputStream(baos);
            channel.setCommand("cd /home/stud/btech/cse/2017/rahulkumar.cs17/android && grep -l \"include\" * ");
            channel.connect();
            try{Thread.sleep(1000);}catch(Exception ee){}
            channel.disconnect();

        } catch (JSchException e) {

        }
        Log.e("main",baos.toString());
        return new String(baos.toByteArray());
    }


}
