package com.rk957019.myocr;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.vision.L;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileSearching extends AppCompatActivity
{
    String result = null;
    String query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_searching);
      //  Button searchBtn = (Button) findViewById(R.id.search);
        SearchView searchView = (SearchView) findViewById(R.id.searching);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                Toast.makeText(FileSearching.this, "Working" +
                                "",
                        Toast.LENGTH_SHORT).show();
                query = s;
               Log.e("query",query);
               Log.e("s",s);

                new  AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {

                            Log.e("main","Message");

                            result = executeSSHcommand(query);
                            Log.e("result",result);


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
                      //  Toast.makeText(FileSearching.this, "Process Done!\n"+result,
                         //       Toast.LENGTH_LONG).show();
                        Log.e("main",result);
                        ArrayList<String>fileNames = new ArrayList<String>();
                        fileNames = processResult(result);
                        updateUI(fileNames);
                        super.onPostExecute(aVoid);
                    }
                }.execute(1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {

                return false;
            }
        });

    }

    private void updateUI(final ArrayList<String> fileNames)
    {
        ListView fileNamesListView = (ListView) findViewById(R.id.listview);
        fileNamesListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fileNames));
        fileNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view,final int i, long l)
            {
               //  Toast.makeText(FileSearching.this,fileNames.get(i),Toast.LENGTH_LONG).show();

                new  AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {

                            downloadFile(adapterView.getItemAtPosition(i).toString());
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
                          Toast.makeText(FileSearching.this, "File Downloaded!",
                               Toast.LENGTH_LONG).show();

                        super.onPostExecute(aVoid);
                    }
                }.execute(1);
            }
        });
    }

    private void downloadFile(String fileName) {
        String user = "rahulkumar.cs17";
        String password = "10/12/1998";
        String host = "172.16.1.3";
        int port = 22;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

       try
       {
            JSch jsch = new JSch();

        Session session = jsch.getSession(user, host, port);

        session.setPassword(password);
        Log.e("main", "Message1");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.setTimeout(10000);

        session.connect();
           try {
               download(session," /home/stud/btech/cse/2017/rahulkumar.cs17/android/",fileName,"/storage/emulated/0/downloads/");
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    catch (JSchException e)
    {

    }

    }


    private ArrayList<String> processResult(String result)
    {
        ArrayList<String>fileNames =new ArrayList<String>();
        String names ="";
        for(int i=0;i<result.length();i++)
        {
            if(result.charAt(i)!='\n')
            {
                names = names  + result.charAt(i);
            }
            else
            {
               fileNames.add(names);
               names = "";
            }
        }
        //fileNames.add(names);
        return  fileNames;
    }

    public  String executeSSHcommand(String s)
    {
        String user = "rahulkumar.cs17";
        String password = "10/12/1998";
        String host = "172.16.1.3";
        int port = 22;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {

            JSch jsch = new JSch();

            Session session = jsch.getSession(user, host, port);

            session.setPassword(password);
            Log.e("main", "Message1");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.setTimeout(10000);

            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            channel.setOutputStream(baos);

            channel.setCommand("cd /home/stud/btech/cse/2017/rahulkumar.cs17/android/ && fgrep -l -R -i "+s + " * ");

            channel.connect();

            try{Thread.sleep(1000);}catch(Exception ee){}

            channel.disconnect();

            session.disconnect();

            Log.e("main",baos.toString()+"hello");

        } catch (JSchException e) {
           e.printStackTrace();
        }
        Log.e("main",baos.toString());

        return new String(baos.toByteArray());
    }
    public void download(Session s,String remotePath,String remoteFileName,String localPath)
            throws Exception
    {
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        BufferedOutputStream bos = null;
        ChannelSftp channelSftp = getChannelToSftpServer(s);
        if(channelSftp != null){
            try{
            //   channelSftp.cd(remotePath);
                bis = new BufferedInputStream(channelSftp.get("/home/stud/btech/cse/2017/rahulkumar.cs17/android/"+remoteFileName));
                File newFile = new File(localPath );
                os = new FileOutputStream(newFile);
                bos = new BufferedOutputStream(os);
                int count;
                while( (count = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, count);
                }
                // This line is very important, other wise
                // you will get a 0KB file and when ever you want to
                // open the file , you will get EOF Exception....
                bos.flush();
            }catch (SftpException e) {
                String message = "SFTP Error while retreiving the file'" + e.getMessage();
                throw new IOException(message);
            }
            catch(Exception ex){
                String message = "Error while retreiving the file'" + ex.getMessage();
                throw new Exception(message);
            }
            finally {
                try {
                   channelSftp.disconnect();
//                    if( os!=null ) {
//                        os.close();
//                    }
//                    if( bis!=null ) {
//                        bis.close();
//                    }
//                    if( bos!=null ) {
//                        bos.close();
//                    }
                } catch(Exception e) {
                    String message = "Error while closing the streams" + e.getMessage();
                    throw new Exception(message);
                }
            }
        }
    }
    private ChannelSftp getChannelToSftpServer(Session session)throws IOException
    {
        ChannelSftp channelSftp = null;

            try{
                Channel channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp)channel;
            }
            catch (JSchException e) {

            }

        return channelSftp;
    }
}
