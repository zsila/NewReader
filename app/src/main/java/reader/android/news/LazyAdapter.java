package reader.android.news;

/**
 * Created by zisan on 3.03.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LazyAdapter extends BaseAdapter {

    private ArrayList<List_item> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ArrayList<List_item> d) {
        data = d;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(a.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(reader.android.news.R.layout.rowlayout, null);

        TextView text = (TextView) vi.findViewById(reader.android.news.R.id.label);
        TextView title = (TextView) vi.findViewById(reader.android.news.R.id.title);
        ImageView image = (ImageView) vi.findViewById(reader.android.news.R.id.image);
        text.setText(Html.fromHtml(data.get(position).detail));
        title.setText(Html.fromHtml(data.get(position).title));
        imageLoader.DisplayImage(data.get(position).image_url, image);
        return vi;
    }
}