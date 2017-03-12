package reader.android.news;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MySimpleArrayAdapter extends ArrayAdapter<List_item> {


    final Map<String, Drawable> drawableMap = new HashMap<String, Drawable>();

    private final Context context;
    private final ArrayList<List_item> myList;

    public MySimpleArrayAdapter(Context context, ArrayList<List_item> liste) {
        super(context, reader.android.news.R.layout.rowlayout, liste);
        this.context = context;
        this.myList = liste;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (myList.get(position).image_url != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(reader.android.news.R.layout.rowlayout, parent, false);

            TextView titleView = (TextView) rowView.findViewById(reader.android.news.R.id.title);
            TextView textView = (TextView) rowView.findViewById(reader.android.news.R.id.label);
            ImageView imageView = (ImageView) rowView.findViewById(reader.android.news.R.id.result_icon);

            titleView.setText(myList.get(position).title);
            textView.setText(myList.get(position).detail);

            fetchDrawableOnThread(myList.get(position).image_url, imageView);
            if(myList.get(position).mydrawable!=null)
                imageView.setImageDrawable(myList.get(position).mydrawable);
            return rowView;
        } else {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(reader.android.news.R.layout.no_image, parent, false);

            TextView titleView = (TextView) rowView.findViewById(reader.android.news.R.id.title);
            TextView textView = (TextView) rowView.findViewById(reader.android.news.R.id.label);

            titleView.setText(myList.get(position).title);
            textView.setText(myList.get(position).detail);

            return rowView;
        }
    }


    public Drawable fetchDrawable(String urlString) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = Drawable.createFromStream(is, "src");


            if (drawable != null) {
                drawableMap.put(urlString, drawable);
                Log.d(this.getClass().getSimpleName(), "got a thumbnail drawable: " + drawable.getBounds() + ", "
                        + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                        + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
            } else {
                Log.w(this.getClass().getSimpleName(), "could not get thumbnail");
            }

            return drawable;
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return null;
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return null;
        }
    }


    public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
        if (drawableMap.containsKey(urlString)) {
            imageView.setImageDrawable(drawableMap.get(urlString));
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageView.setImageDrawable((Drawable) message.obj);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                Drawable drawable = fetchDrawable(urlString);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }


}
