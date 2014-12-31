package com.waynehillsfbla.jsonparserwithcards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kartik on 12/30/2014.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventInfo> eventList;

    public EventAdapter(List<EventInfo> eventList)
    {
        this.eventList = eventList;
    }

    public int getItemCount()
    {
        return eventList.size();
    }

    private String getDisplayDate(String date) throws ParseException {
        String day, month, year, result;

        SimpleDateFormat simpForm = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dispForm = new SimpleDateFormat("EEEE, MMMM dd yyyy");

        day = date.substring(8,10);
        month = date.substring(5,7);
        year = date.substring(0,4);
        result = month + "/" + day + "/" + year;

        result = dispForm.format(simpForm.parse(result));

        return result;
    }

    public void onBindViewHolder(EventViewHolder eventViewHolder, int i)
    {
        EventInfo ei = eventList.get(i);
        eventViewHolder.vTitle.setText(ei.title);
        try {
            eventViewHolder.vDate.setText(getDisplayDate(ei.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DownloadImageTask dit = new DownloadImageTask(eventViewHolder.vPicture, eventViewHolder.vProgressBar);
        dit.execute(ei.pictureURL);
        eventViewHolder.vType.setText(ei.type);
    }

    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new EventViewHolder(itemView);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{
        protected TextView vTitle;
        protected TextView vDate;
        protected ImageView vPicture;
        protected TextView vType;
        protected ProgressBar vProgressBar;



        public EventViewHolder(View v){
            super(v);
            vTitle = (TextView) v.findViewById(R.id.txtTitle);
            vDate = (TextView) v.findViewById(R.id.txtDate);
            vPicture = (ImageView) v.findViewById(R.id.picture);
            vType = (TextView) v.findViewById(R.id.txtType);
            vProgressBar = (ProgressBar) v.findViewById(R.id.imgLoad);
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ProgressBar pb;

        public DownloadImageTask(ImageView bmImage, ProgressBar pb){

            this.bmImage = bmImage;
            this.pb = pb;
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
            pb.setVisibility(View.INVISIBLE);
            bmImage.setVisibility(View.VISIBLE);
        }
    }

}
