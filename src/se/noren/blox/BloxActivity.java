package se.noren.blox;

import se.noren.blox.admob.GoogleAdMobHandler;
import se.noren.blox.googleanalytics.AnalyticsAccount;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;


/**
 * <p>Main menu activity of the game. Handle keeping track
 * of username etc.</p>
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class BloxActivity extends Activity {
	public static final String PREFS_NAME = "BloxPrefs";
	
	 private AdView adView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.menu);    	
    	updateGUIComponents();
    	
    	GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.startNewSession(AnalyticsAccount.ACCOUNT_ID, this);
    	tracker.trackPageView("/mainmenu");
    	tracker.dispatch();
    	
        Button helpBtn = (Button) findViewById(R.id.helpButton);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
    			startActivity(intent);
            }

        });

        Button aboutBtn = (Button) findViewById(R.id.aboutButton);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
    			startActivity(intent);
            }

        });

        adView = GoogleAdMobHandler.addAdsToLinearLayout(this, 1, R.id.mainMenuLinearLayout);
    }

	private void updateGUIComponents() {
		// Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String username = settings.getString("username", "");
        System.out.println("username: " + username);
        
        final TextView tv = ((TextView) findViewById(R.id.editText1));
        tv.setText(new StringBuffer(username));
        
        final Button b = (Button) this.findViewById(R.id.playButton);
        if (username == null || username.length() < 3) {
        	b.setEnabled(false);
        } else {
        	b.setEnabled(true);        	
        }
        
        if ("".equals(username)) {
        	tv.setText("enter name");
        	tv.setTypeface(null, Typeface.ITALIC);
        } else {
        	tv.setTypeface(null, Typeface.NORMAL);
        }
        

        tv.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("touch: " + tv.getText() + ".");
				if ("enter name".equals(tv.getText().toString())) {
					System.out.println(tv.getText());
					
					final Runnable task = new Runnable() {
					    public void run() {
					    	System.out.println("inside handler");
							tv.setText("");
							tv.setTypeface(null, Typeface.NORMAL);
					    }
					};
					new Handler().post(task);
				}
				return false;
			}
		});
        
        tv.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				System.out.println("click: " + tv.getText());
				if ("enter name".equals(tv.getText().toString())) {
					tv.setText("");
					tv.setTypeface(null, Typeface.NORMAL);
				}
			}
		});
        
        tv.addTextChangedListener(new TextWatcher() {
		
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				System.out.println("text change: " + s);
				if (s.length() >= 3) {
					b.setEnabled(true);
				} else {
					b.setEnabled(false);      
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
			
			public void afterTextChanged(Editable s) {}
		});
        
        b.setOnClickListener(new ButtonListener());
     
	}
    
	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
    
    private class ButtonListener implements OnClickListener {
    	
    	public ButtonListener() {  	
    		
    	}
    	
		public void onClick(View v) {
			TextView tv = ((TextView) findViewById(R.id.editText1));
			CharSequence usrNameTxt = tv.getText().toString().trim(); 
			
		    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	        Editor edit = settings.edit();
	        edit.putString("username", usrNameTxt.toString());
	        edit.commit();
	        
			Intent intent = new Intent(getApplicationContext(), QuickstartActivity.class);
			intent.putExtra("username", usrNameTxt.toString());
			startActivity(intent);
		}	
    }
}