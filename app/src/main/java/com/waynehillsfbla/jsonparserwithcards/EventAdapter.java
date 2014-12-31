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
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
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

    public void onBindViewHolder(EventViewHolder eventViewHolder, int i)
    {
        EventInfo ei = eventList.get(i);
        eventViewHolder.vTitle.setText(ei.title);
        eventViewHolder.vDate.setText(ei.date);
        //eventViewHolder.vPicture.setImageBitmap(ei.picture);
        DownloadImageTask dit = new DownloadImageTask(eventViewHolder.vPicture);
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

        public EventViewHolder(View v){
            super(v);
            vTitle = (TextView) v.findViewById(R.id.txtTitle);
            vDate = (TextView) v.findViewById(R.id.txtDate);
            vPicture = (ImageView) v.findViewById(R.id.picture);
            vType = (TextView) v.findViewById(R.id.txtType);
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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

}
