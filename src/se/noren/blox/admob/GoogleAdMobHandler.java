package se.noren.blox.admob;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * Handle Google AdMob advertising within Android app.
 * Adds an ad to a linear layout resource. 
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class GoogleAdMobHandler {

	public static AdView addAdsToLinearLayout(Activity activity, int index, int linearLayoutResource) {
	    // Create the adView
		// TODO: Create new! This is Othellos
        String MY_AD_UNIT_ID = "a14ee4c32acdfe1";
        AdView adView = new AdView(activity, AdSize.BANNER, MY_AD_UNIT_ID);

        LinearLayout layout = (LinearLayout) activity.findViewById(linearLayoutResource);

        // Add the adView to it
        layout.addView(adView, index, new ViewGroup.LayoutParams(480, 75));

        // Initiate a generic request to load it with an ad
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);               // Emulator
        adView.loadAd(adRequest);
        
        return adView;
	}
}
