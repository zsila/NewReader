package reader.android.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class VideoDetail extends Activity{
	
	String GAnalyticsTrackerId="UA-11015976-26"; 
	private GoogleAnalyticsTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		tracker = GoogleAnalyticsTracker.getInstance();        
        tracker.start(GAnalyticsTrackerId, this);
		
		//setContentView(R.layout.video_detail);
		Intent intent = getIntent();
		String LINK = intent.getExtras().getString("key_videolink");
	
		//String LINK="http://www.ted.com/talks/download/video/8584/talk/761";
	   // String LINK = "http://commonsware.com/misc/test2.3gp";
	   
		
		setContentView(reader.android.news.R.layout.video_detail);
	    VideoView videoView = (VideoView) findViewById(reader.android.news.R.id.videoView1);
	    MediaController mc = new MediaController(this);
	    mc.setAnchorView(videoView);
	    mc.setMediaPlayer(videoView);
	    Uri video = Uri.parse(LINK);
	    videoView.setMediaController(mc);
	    videoView.setVideoURI(video);
	    videoView.start();
		
	} 
	
	public void GAnalyticsTrackPageView(String page){
		tracker.trackPageView(getApplicationInfo().packageName+"/"+page);
		tracker.dispatch();
	}



}


