package se.noren.blox;

import se.noren.blox.admob.GoogleAdMobHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class AboutActivity extends Activity {
	

	 private AdView adView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.about);    	
    	
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