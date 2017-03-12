package reader.android.news;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;


public class Yazarlar extends Activity {
    private ArrayList<List_item> writers = new ArrayList<List_item>();

    private ListView listView_xml;
    private ProgressDialog dialog;

    Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(reader.android.news.R.layout.writerstwo);

        listView_xml = (ListView) findViewById(reader.android.news.R.id.listWriters);
        new GetWritersAsync().execute("http://www.cnnturk.com/feed/rss/yazarlar");
        listView_xml.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent int_ = new Intent(Yazarlar.this, NewsDetail_Act.class);
                int_.putExtra("key_detail", writers.get(position).detail);
                int_.putExtra("key_title", writers.get(position).title);
                int_.putExtra("key_photo", writers.get(position).image_url);
                int_.putExtra("key_id", writers.get(position).id);

                startActivity(int_);

            }
        });


        final ImageView img = (ImageView) findViewById(reader.android.news.R.id.haberler_pasif);
        img.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog = ProgressDialog.show(Yazarlar.this, "", "Loading...", true);
                Intent i = new Intent(Yazarlar.this, ParsingXML.class);
                startActivity(i);

            }
        });


        /*final ImageView img_foto = (ImageView) findViewById(R.id.foto_pasif);
        img_foto.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                *//*dialog = ProgressDialog.show(Yazarlar.this, "", "Loading...", true);
                Intent i = new Intent(Yazarlar.this, Photo_Gallery.class);
                startActivity(i);*//*

            }
        });


        final ImageView img_video = (ImageView) findViewById(R.id.video_pasif);
        img_video.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                *//*dialog = ProgressDialog.show(Yazarlar.this, "", "Loading...", true);
                Intent i = new Intent(Yazarlar.this, Video_Act.class);
                startActivity(i);*//*
            }
        });*/


    }


    public class GetWritersAsync extends AsyncTask<String, Void, ArrayList<List_item>> {

        @Override
        protected ArrayList<List_item> doInBackground(String... urls) {
            if (writers.size() == 0)
                writers = getListFromXml(urls[0]);
            return writers;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Yazarlar.this);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        protected void onPostExecute(ArrayList<List_item> xmlList) {
            LazyAdapter araAdapter = new LazyAdapter(activity, xmlList);
            listView_xml.setAdapter(araAdapter);
            dialog.dismiss();
        }
    }


    private ArrayList<List_item> getListFromXml(String haber_link) {

        ArrayList<List_item> list = new ArrayList<List_item>();

        try {

            URL url = new URL(haber_link);

            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dFactory.newDocumentBuilder();

            Document document = dBuilder.parse(new InputSource(url.openStream()));

            document.getDocumentElement().normalize();

            NodeList nodeListNews = document.getElementsByTagName("item");
            for (int i = 0; i < nodeListNews.getLength(); i++) {

                List_item temp = new List_item();

                Node node = nodeListNews.item(i);
                Element elementMain = (Element) node;

                NodeList nodeListTitle = elementMain.getElementsByTagName("title");
                Element elementTitle = (Element) nodeListTitle.item(0);
                temp.title = elementTitle.getChildNodes().item(0).getNodeValue();

                NodeList nodeListId = elementMain.getElementsByTagName("guid");
                Element elementId = (Element) nodeListId.item(0);
                temp.id = elementId.getChildNodes().item(0).getNodeValue();

                NodeList nodeListNewsDetail = elementMain.getElementsByTagName("description");
                Element elementValue = (Element) nodeListNewsDetail.item(0);
                temp.detail = elementValue.getChildNodes().item(0).getNodeValue();


                NodeList nodeListImage = elementMain.getElementsByTagName("image");
                if (nodeListImage.getLength() != 0) {
                    Element elementImage = (Element) nodeListImage.item(0);
                    temp.image_url = elementImage.getChildNodes().item(0).getNodeValue();
                }

                list.add(temp);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);

            this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}

	
 		
