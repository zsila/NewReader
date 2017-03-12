package reader.android.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;


public class NewsDetail_Act extends  Activity {
    public ImageLoader imageLoader;

    List_item _det = new List_item();

    TextView display;
    TextView det_title;
    TextView det_date;
    ImageView det_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader(getApplicationContext());

        Intent intent = getIntent();
        String news_title = intent.getExtras().getString("key_title");
        String news_photo = intent.getExtras().getString("key_photo");
        String news_date = intent.getExtras().getString("key_date");
        //String news_id = intent.getExtras().getString("key_id");
        String news_detail = intent.getExtras().getString("key_detail");

        _det.date = news_date;
        _det.content = news_detail;

        if (news_photo != null) {
            setContentView(reader.android.news.R.layout.detail);

            det_title = (TextView) findViewById(reader.android.news.R.id.detail_title);
            det_title.setText(Html.fromHtml(news_title));
            det_date = (TextView) findViewById(reader.android.news.R.id.detail_date);
            det_date.setText(_det.date);

            det_photo = (ImageView) findViewById(reader.android.news.R.id.detailPhoto);
            imageLoader.DisplayImage(news_photo, det_photo);

            display = (TextView) findViewById(reader.android.news.R.id.detailView);
            display.setText(Html.fromHtml(_det.content));
            display.setMovementMethod(new ScrollingMovementMethod());
        } else {
            setContentView(reader.android.news.R.layout.detail_noimage);


            det_title = (TextView) findViewById(reader.android.news.R.id.textView1);
            det_title.setText(Html.fromHtml(news_title));
            det_date = (TextView) findViewById(reader.android.news.R.id.textView2);
            det_date.setText(Html.fromHtml(_det.date));

            display = (TextView) findViewById(reader.android.news.R.id.textView3);
            display.setText(Html.fromHtml(_det.content));
            display.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Action Bar içinde kullanılacak menü öğelerini inflate edelim
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return super.onCreateOptionsMenu(menu);
    }



    /*//to convert larger font size
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Action Bar öğelerindeki basılmaları idare edelim
        switch (item.getItemId()) {
            case R.id.fontsize:
                display.setTextSize(20);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
