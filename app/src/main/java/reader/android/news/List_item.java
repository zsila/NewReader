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

import android.graphics.drawable.Drawable;

import android.widget.ImageView;


public class List_item {
	
	
	
	
	ArrayList<item_photos> photos;
	//= new ArrayList<String>();
	String content = null;   
	String date= null;
	String id= null;
	String detail= null;
	String title= null;
	String image_url= null;
	String video_link= null;
	ImageView image_view= null;
    Drawable mydrawable= null;
	
    
    
    public void getFromUrl(String id) throws ParserConfigurationException, SAXException, IOException {
	URL url2 = null;
	try {
		url2 = new URL("http://www.zaman.com.tr/newsxml.do?" + id);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
    DocumentBuilderFactory dFactory2=DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder2 = null;
    dBuilder2 = dFactory2.newDocumentBuilder();
    Document document2 = null; 
    document2 = dBuilder2.parse(new InputSource(url2.openStream()));
	
    //Verilen url de stream a�iyor.
    
	
		
    document2.getDocumentElement().normalize();
    
    NodeList nodeListNews2=document2.getElementsByTagName("news");
    
    
  
    Node node2=nodeListNews2.item(0);
    Element elementMain2=(Element) node2;
        
                 
        
    
   NodeList nodeListNewsContent=elementMain2.getElementsByTagName("content");
   if(nodeListNewsContent.getLength()!=0){
   Element elementContent=(Element) nodeListNewsContent.item(0);
   NodeList nl_con= elementContent.getChildNodes();
   if(nl_con!=null){
	   Node node_con=nl_con.item(0);
	   if(node_con != null){
		   String str_con= node_con.getNodeValue(); 
		   if(str_con!=null)
			   this.content=str_con;
		  
		   }
	 
	  
   }
  
   
//   temp.content = elementContent.getChildNodes().item(0).getNodeValue();
   }
   else {
	   this.content=null;
   }             
    
   
   
    NodeList nodeListNewsDate=elementMain2.getElementsByTagName("date");
    if(nodeListNewsDate.getLength()!=0){
        Element elementContent=(Element) nodeListNewsDate.item(0);
        NodeList nl_date= elementContent.getChildNodes();
        if(nl_date!=null){
     	   Node node_date=nl_date.item(0);
     	   if(node_date != null){
     		   String str_date= node_date.getNodeValue(); 
     		   if(str_date!=null)
     			  this.date = str_date.substring(0, 10);
     		  
     		   }
     	   
        }
      
        
//        temp.content = elementContent.getChildNodes().item(0).getNodeValue();
        }
        else {
        	this.date = null;
        }
    }
    
    
}


