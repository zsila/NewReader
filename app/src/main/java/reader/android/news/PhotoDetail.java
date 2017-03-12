package reader.android.news;

import java.io.IOException;
import java.io.InputStream;
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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;


public class PhotoDetail extends Activity implements OnClickListener {

    /** Called when the activity is first created. */

    int image_index = 0;
    
    String GAnalyticsTrackerId="UA-11015976-26";
	private GoogleAnalyticsTracker tracker;

    private ArrayList<item_photos> photoList=new ArrayList<item_photos>();
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tracker = GoogleAnalyticsTracker.getInstance();        
        tracker.start(GAnalyticsTrackerId, this);
        
        
        setContentView(reader.android.news.R.layout.photo_detail);

        Intent intent = getIntent();
		String photo_id = intent.getExtras().getString("key_id");
	
		  
		photoList = getGalleryPhotos("http://zaman.com.tr/photoxml.do?" + photo_id);
	
        
        ImageView btnPrevious = (ImageView)findViewById(reader.android.news.R.id.image_prev1);
        btnPrevious.setOnClickListener(this);       
        ImageView btnNext = (ImageView)findViewById(reader.android.news.R.id.image_next1);
        btnNext.setOnClickListener(this);

        showImage();        

    }

    private void showImage() {

        ImageView imgView = (ImageView) findViewById(reader.android.news.R.id.imageView1);
        imgView.setImageDrawable(loadImageFromUrl(photoList.get(image_index).image_url));       

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case (reader.android.news.R.id.image_prev1):

                image_index--;

                if (image_index == -1) {                    
                    image_index = photoList.size() - 1;                  
                }

                showImage();

            break;

            case (reader.android.news.R.id.image_next1):

                image_index++;

                if (image_index == photoList.size()) {               
                image_index = 0;                       
            }

                showImage();

            break;      

        }

    }
    
    
private ArrayList<item_photos> getGalleryPhotos(String haber_link)  {
    	
   
        ArrayList<item_photos> list=new ArrayList<item_photos>();
        
        try {

        	
            URL url=new URL(haber_link);
            DocumentBuilderFactory dFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder=dFactory.newDocumentBuilder();

            
            Document document=dBuilder.parse(new InputSource(url.openStream()));
      		
            document.getDocumentElement().normalize();
            
           
            
            NodeList nodeListNews=document.getElementsByTagName("image");
            for (int i = 0; i < nodeListNews.getLength(); i++) {
            	
            	item_photos temp = new item_photos();
          
            	Node node=nodeListNews.item(i);
                Element elementMain=(Element) node;
               
                
                NodeList nodeListNewsLink=elementMain.getElementsByTagName("link");
                Element elementLink=(Element) nodeListNewsLink.item(0);
            
                NodeList nodeListTitle=elementMain.getElementsByTagName("title");
                Element elementTitle=(Element) nodeListTitle.item(0);

              

                temp.image_url=elementLink.getChildNodes().item(0).getNodeValue();
               // temp.mydrawable=loadImageFromUrl(temp.image_url); 
                
                          
                temp.title = elementTitle.getChildNodes().item(0).getNodeValue();
                list.add(temp);
}
                  
 
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e) {
       e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
   } catch (IOException e) {
            e.printStackTrace();
        }
 
        return list;
    }
	  
	public Drawable loadImageFromUrl(String url) {
    	InputStream inputStream;
    	try {
    	inputStream = new URL(url).openStream();
    	} catch (IOException e) {
    	throw new RuntimeException(e);
    	}
    	return Drawable.createFromStream(inputStream, "src");
    	}

	public void GAnalyticsTrackPageView(String page){
		tracker.trackPageView(getApplicationInfo().packageName+"/"+page);
		tracker.dispatch();
	}
    
    
    
}
