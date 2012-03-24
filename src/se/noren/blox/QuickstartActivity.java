package se.noren.blox;

import se.noren.blox.admob.GoogleAdMobHandler;
import se.noren.blox.googleanalytics.AnalyticsAccount;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class QuickstartActivity extends Activity {
	
	 private AdView adView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.quickstart);    	
    	
    	GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.startNewSession(AnalyticsAccount.ACCOUNT_ID, this);
    	tracker.trackPageView("/quickstart");
    	tracker.dispatch();
    	
        Intent sender = getIntent();
        final String username = sender.getExtras().getString("username");
    	
        Button helpBtn = (Button) findViewById(R.id.startGameBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
    			Intent intent = new Intent(getApplicationContext(), MainBloxActivity.class);
    			intent.putExtra("username", username);
    			startActivity(intent);
            }

        });

        adView = GoogleAdMobHandler.addAdsToLinearLayout(this, 0, R.id.quickstartLinearLayout);
    }


	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
    
}