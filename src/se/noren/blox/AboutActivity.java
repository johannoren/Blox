package se.noren.blox;

import se.noren.blox.admob.GoogleAdMobHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/**
 * Activity to show 'About' info. Also holds advertisement.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class AboutActivity extends Activity {
	
	 private AdView adView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.about);    	
    	
    	/*
    	 * Setup Google Analytics tracking of this view
    	 */
    	GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.trackPageView("/aboutpage");
    	tracker.dispatch();
    	
        Button next = (Button) findViewById(R.id.aboutGoBackButton);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
    			finish();
            }
        });
        
        adView = GoogleAdMobHandler.addAdsToLinearLayout(this, 0, R.id.aboutLinearLayout);
    }   
    
    
    @Override
    public void onDestroy() {
      adView.destroy();
      super.onDestroy();
    }
}