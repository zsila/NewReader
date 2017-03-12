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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;


public class ParsingXML extends Activity {
    private ListView listView_xml;
    private ArrayList<List_item> xmlList = new ArrayList<List_item>();
    private Button lastClickedButton;

    private ProgressDialog dialog;

    Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(reader.android.news.R.layout.maintwo);

        activity = this;

        final Button bTurkiye = (Button) findViewById(reader.android.news.R.id.bTurkiye);
        final Button bDunya = (Button) findViewById(reader.android.news.R.id.bDunya);
        final Button bSondakika = (Button) findViewById(reader.android.news.R.id.bSondakika);
        final Button bKultur = (Button) findViewById(reader.android.news.R.id.bKultursanat);
        final Button bBilim = (Button) findViewById(reader.android.news.R.id.bBilim);
        final Button bYasam = (Button) findViewById(reader.android.news.R.id.bYasam);
        final Button bMagazin = (Button) findViewById(reader.android.news.R.id.bMagazin);
        final Button bSpor = (Button) findViewById(reader.android.news.R.id.bSpor);
        final Button bEkonomi = (Button) findViewById(reader.android.news.R.id.bEkonomi);
        final Button bSaglik = (Button) findViewById(reader.android.news.R.id.bSaglik);


        listView_xml = (ListView) findViewById(reader.android.news.R.id.listView_xml);
        bTurkiye.setTextColor(Color.parseColor("#D60B22"));
        lastClickedButton = (Button) findViewById(reader.android.news.R.id.bTurkiye);


        bTurkiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getNews(bTurkiye, "http://www.cnnturk.com/feed/rss/turkiye/news");
            }
        });


        bSondakika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bSondakika, "http://www.cnnturk.com/feed/rss/all/news");
            }
        });

        bDunya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bDunya, "http://www.cnnturk.com/feed/rss/dunya/news");
            }
        });


        bKultur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bKultur, "http://www.cnnturk.com/feed/rss/kultur-sanat/news");
            }
        });



        bBilim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bBilim, "http://www.cnnturk.com/feed/rss/bilim-teknoloji/news");
            }
        });


        bYasam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bYasam, "http://www.cnnturk.com/feed/rss/yasam/news");
            }
        });


        bMagazin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bMagazin, "http://www.cnnturk.com/feed/rss/magazin/news");
            }
        });


        bSpor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bSpor, "http://www.cnnturk.com/feed/rss/spor/news");
            }
        });


        bSaglik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews(bSaglik, "http://www.cnnturk.com/feed/rss/saglik/news");
            }
        });


        bEkonomi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getNews(bEkonomi, "http://www.cnnturk.com/feed/rss/ekonomi/news");
            }
        });

        new GetNewsAsync().execute("http://www.cnnturk.com/feed/rss/turkiye/news");


        listView_xml.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ParsingXML.this, NewsDetail_Act.class);
                i.putExtra("key_detail", xmlList.get(position).detail);
                i.putExtra("key_title", xmlList.get(position).title);
                i.putExtra("key_date", xmlList.get(position).date);
                i.putExtra("key_id", xmlList.get(position).id);
                i.putExtra("key_photo", xmlList.get(position).image_url);

                startActivity(i);
            }

        });


        final ImageView img_yazar = (ImageView) findViewById(reader.android.news.R.id.yazar_pasif);
        img_yazar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog = ProgressDialog.show(ParsingXML.this, "", "Loading...", true);
                Intent i = new Intent(ParsingXML.this, Yazarlar.class);
                startActivity(i);
            }
        });


        /*final ImageView img_photoGallery = (ImageView) findViewById(R.id.foto_pasif);
        img_photoGallery.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                *//*dialog = ProgressDialog.show(ParsingXML.this, "", "Loading...", true);
                Intent i = new Intent(ParsingXML.this, Photo_Gallery.class);
                startActivity(i);*//*
            }
        });


        final ImageView img_videoGallery = (ImageView) findViewById(R.id.video_pasif);
        img_videoGallery.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                *//*dialog = ProgressDialog.show(ParsingXML.this, "", "Loading...", true);
                Intent i = new Intent(ParsingXML.this, Video_Act.class);
                startActivity(i);*//*
            }
        });*/


    }


    public class GetNewsAsync extends AsyncTask<String, Void, ArrayList<List_item>> {

        @Override
        protected ArrayList<List_item> doInBackground(String... urls) {
            xmlList = getListFromXml(urls[0]);
            return xmlList;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(ParsingXML.this);
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

                NodeList nodeListDate = elementMain.getElementsByTagName("pubDate");
                Element elementDate = (Element) nodeListDate.item(0);
                temp.date = elementDate.getChildNodes().item(0).getNodeValue();

                NodeList nodeListId = elementMain.getElementsByTagName("link");
                Element elementId = (Element) nodeListId.item(0);
                temp.id = elementId.getChildNodes().item(0).getNodeValue();

                NodeList nodeListNewsDetail = elementMain.getElementsByTagName("description");
                Element elementValue = (Element) nodeListNewsDetail.item(0);
                temp.detail = elementValue.getChildNodes().item(0).getNodeValue();

                NodeList nodeListNewsImage = elementMain.getElementsByTagName("image");
                Element elementImage = (Element) nodeListNewsImage.item(0);
                temp.image_url = elementImage.getChildNodes().item(0).getNodeValue();

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


    public void getNews(Button bttn, String url){
        lastClickedButton.setTextColor(Color.BLACK);
        bttn.setTextColor(Color.parseColor("#D60B22"));
        lastClickedButton = bttn;
        listView_xml = (ListView) findViewById(reader.android.news.R.id.listView_xml);
        new GetNewsAsync().execute(url);
    }


}
	
	      
	
