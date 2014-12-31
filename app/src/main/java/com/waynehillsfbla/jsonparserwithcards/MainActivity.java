package com.waynehillsfbla.jsonparserwithcards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends ActionBarActivity {
    JSONArray jarr = null;
    JSONObject jobj = null;
    ClientServerInterface clientServerInterface = new ClientServerInterface();
    TextView titleTextView;
    ImageView pictureImageView;
    TextView dateTextView;
    TextView typeTextView;
    String ab = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = (TextView) findViewById(R.id.txtTitle);
        pictureImageView = (ImageView) findViewById(R.id.picture);
        dateTextView = (TextView) findViewById(R.id.txtDate);
        typeTextView = (TextView) findViewById(R.id.txtType);

        RetrieveData rd = new RetrieveData();
        rd.execute();

        try {
            rd.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        EventAdapter ea = new EventAdapter(createList(jarr.length()));
        recList.setAdapter(ea);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<EventInfo> createList(int size) {

        List<EventInfo> result = new ArrayList<EventInfo>();
        for (int i = 0; i < size; i++) {
            EventInfo ei = new EventInfo();
            try {
                jobj = jarr.getJSONObject(i);
                ei.title = jobj.getString("title");
                ei.date = jobj.getString("date");
                //DownloadImageTask dit = new DownloadImageTask(pictureImageView);
                String picURL = jobj.getString("pictureURL");
                //dit.execute(picURL);
                ei.pictureURL = jobj.getString("pictureURL");
                //ei.picture = (new DownloadImageTask(pictureImageView))
                //        .execute(jobj.getString("pictureURL"));
                ei.type = jobj.getString("type");
            }catch (JSONException e){
                e.printStackTrace();
            }
            /*
            ei.title = "tit";
            ei.date = "dat";
            ei.type = "typ";
            */
            result.add(ei);
        }

        return result;
    }

    class RetrieveData extends AsyncTask<String,String,JSONArray>
    {
        protected JSONArray doInBackground(String... arg0) {
            jarr = clientServerInterface.makeHttpRequest("http://54.164.136.46/printresult.php");
            return jarr;
        }

        protected void onPostExecute(String ab){

            /*try{

                ab = jobj.getString("title");
                titleTextView.setText(ab);
                ab = jobj.getString("pictureURL");
                //new DownloadImageTask((ImageView) findViewById(R.id.picture))
                //        .execute(ab);
                ab = jobj.getString("date");
                dateTextView.setText(ab);
                ab = jobj.getString("type");
                typeTextView.setText(ab);
            }catch(JSONException e){
                e.printStackTrace();
            }*/
        }
    }
/*
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }
    }
    */
}
