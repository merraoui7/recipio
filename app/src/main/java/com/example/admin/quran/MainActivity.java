package com.example.admin.quran;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Surah> datalist;
    RecyclerView recyclerView;
    SurahAdapter surahAdapter;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datalist=new ArrayList <>();
        datalist.clear();
        datalist=new GetDatabase().getdata();
        surahAdapter = new SurahAdapter(datalist,this);

        recyclerView = (RecyclerView) findViewById(R.id.surahlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(surahAdapter);



        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "quran-saad-elghamidi");
        if (!folder.exists()) {
            folder.mkdirs();
        }

    }

    public boolean checkFileIsExist(String path){
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "quran-saad-elghamidi");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file=new File(folder,path);
        if(file.exists()){
            return true;
        }
        else {
            return false;
        }

    }
    public  class DownloadSurah extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... urls) {
            int index=surahAdapter.index;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                String root = Environment.getExternalStorageDirectory()+"/quran-saad-elghamidi";

                URL url= new URL(urls[0]);
                URLConnection urlConnection=url.openConnection();
                urlConnection.connect();

                int filelenght=urlConnection.getContentLength();

                inputStream= new BufferedInputStream(url.openStream(),8192);

                outputStream =new FileOutputStream(root+"/"+datalist.get(index).getFilename()+".mp3");
                byte data[]= new byte[1024];

                int count;
                long total=0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                }
                outputStream.flush();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try{
                outputStream.close();
                inputStream.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
                return null;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            surahAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(Integer.parseInt(values[0]));
        }
    }
    public void downloadfile(String url){
        new DownloadSurah().execute(url);
    }
}
