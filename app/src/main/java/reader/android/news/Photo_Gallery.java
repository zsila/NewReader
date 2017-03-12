package reader.android.news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Photo_Gallery extends Activity {

	final Map<String, Drawable> drawableMap=new HashMap<String, Drawable>();
	
	
	private ListView listView_xml;
    private ArrayList<List_item> xmlList=new ArrayList<List_item>();

    private ProgressDialog dialog;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(reader.android.news.R.layout.photo_gallery_first);
		listView_xml=(ListView) findViewById(reader.android.news.R.id.listView_xml);
		
		
		new MyAsync().execute("http://zaman.com.tr/multimedia-foto.rss");
		
	
        listView_xml.setOnItemClickListener(new OnItemClickListener(){
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    		Intent i = new Intent(Photo_Gallery.this, PhotoDetail.class);
	    		
	    		i.putExtra("key_id", xmlList.get(position).id);    		
	    		startActivity(i);

	    	}  
	   });           
        
        final ImageView img_fotogaleri  = (ImageView) findViewById(reader.android.news.R.id.foto_pasif);
        
        final ImageView img = (ImageView) findViewById(reader.android.news.R.id.haberler_pasif);
	    img.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	dialog = ProgressDialog.show(Photo_Gallery.this, "", "Loading...", true);
	        	Intent i = new Intent(Photo_Gallery.this, ParsingXML.class);
	    		startActivity(i);
	        }
	    });
	          
	    
	    
	    final ImageView img_yazar = (ImageView) findViewById(reader.android.news.R.id.yazar_pasif);
	    img_yazar.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	dialog = ProgressDialog.show(Photo_Gallery.this, "", "Loading...", true);
	        	Intent i = new Intent(Photo_Gallery.this, Yazarlar.class);
	    		startActivity(i);
	        	
	        }
	    });
        
		
	    final ImageView img_video = (ImageView) findViewById(reader.android.news.R.id.video_pasif);
	    img_video.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	dialog = ProgressDialog.show(Photo_Gallery.this, "", "Loading...", true);
	        	Intent i = new Intent(Photo_Gallery.this, Video_Act.class);
	    		startActivity(i);
	        	
	        }
	    });
		
		
	}
	
/**/    public class MyAsync extends AsyncTask<String, Void, ArrayList<List_item>> {
		
    	@Override
		protected ArrayList<List_item> doInBackground(String... urls) {
			
			//ArrayList<List_item> xmlList;
			if(xmlList.size()==0)
		    xmlList=getPhotosFromXml(urls[0]);
			return xmlList;
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	        dialog = new ProgressDialog(Photo_Gallery.this);
	        dialog.setMessage("Loading...");
	        dialog.show();
		}
		
		protected void onPostExecute(ArrayList<List_item> xmlList) {
			MySimpleArrayAdapter araAdapter=new MySimpleArrayAdapter(getApplicationContext(), xmlList);
	        listView_xml.setAdapter(araAdapter);
	        dialog.dismiss();
			
	     }
	
}           //*/
	
	private ArrayList<List_item> getPhotosFromXml(String haber_link)  {
        ArrayList<List_item> list=new ArrayList<List_item>();
        
        try {

        	//<news> <title> ......... </title> <spot> .......... </spot> </news>  şeklinde bir xml elementleri
            URL url=new URL(haber_link);
            DocumentBuilderFactory dFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder=dFactory.newDocumentBuilder();

            //Verilen url de stream açıyor.
            Document document=dBuilder.parse(new InputSource(url.openStream()));
      		
            document.getDocumentElement().normalize();
            
           
            //Ana tag <news> childlar�na for ile Node lar�na eri�iyoruz
            NodeList nodeListNews=document.getElementsByTagName("item");
            for (int i = 0; i < nodeListNews.getLength(); i++) {
            	
            	List_item temp = new List_item();
          
            	Node node=nodeListNews.item(i);
                Element elementMain=(Element) node;
               
               
                //getElementsByTagName() ile title ve spot daki de�erlere eri�iyoruz.
                NodeList nodeListTitle=elementMain.getElementsByTagName("title");
                Element elementTitle=(Element) nodeListTitle.item(0);

   
                NodeList nodeListNewsLink=elementMain.getElementsByTagName("link");
                Element elementLink=(Element) nodeListNewsLink.item(0);
                
                String gallery_id =elementLink.getChildNodes().item(0).getNodeValue();
                temp.id = gallery_id.substring(59);
                
                
                NodeList nodeListNewspubDate=elementMain.getElementsByTagName("pubDate");
                Element elementpubDate=(Element) nodeListNewspubDate.item(0);

                NodeList nodeListImage=elementMain.getElementsByTagName("description");
                Element elementImage=(Element) nodeListImage.item(0);
              
                
                String xml_ = elementImage.getChildNodes().item(0).getNodeValue();
                
                Matcher matcher = Pattern.compile("src=\"([^\"]+)").matcher(xml_);
                while (matcher.find()) {
                    temp.image_url=matcher.group(1);
                }
                

                String desc = xml_.substring(xml_.indexOf("/>")+2);
                

                temp.date = elementpubDate.getChildNodes().item(0).getNodeValue();
                temp.title = elementTitle.getChildNodes().item(0).getNodeValue();
                temp.detail = desc; // elementLink.getChildNodes().item(0).getNodeValue(); 
                
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

	
	
	
    
  
   
    public Drawable loadImageFromUrl(String url) {
    	InputStream inputStream;
    	try {
    	inputStream = new URL(url).openStream();
    	} catch (IOException e) {
    	throw new RuntimeException(e);
    	}
    	return Drawable.createFromStream(inputStream, "src");
    	}
       
    
    @Override 
	public boolean onKeyDown(int keyCode, KeyEvent event)  { 
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


