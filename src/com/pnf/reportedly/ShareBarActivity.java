/*
 ===========================================================================
 Copyright (c) 2012 Three Pillar Global Inc. http://threepillarglobal.com

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================
 */

package com.pnf.reportedly;

import java.io.File;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ShareBarActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	boolean status;

	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	// Android Components
	Button update;
	EditText edit;
  String key_value,newlink,newpost,url;
  Intent in;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
		    prefsEditor = myPrefs.edit();
		  app_id= myPrefs.getString("UserId","12");
		   prefsEditor.commit();
		   System.out.println("my getting id ===" +app_id);
in=getIntent();
		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		//textview.setText("Welcome to SocialAuth Demo. Connect any provider and then press Update button to Share Update.");
      // Button fb=(Button)findViewById(R.id.fb);
		LinearLayout bar = (LinearLayout) findViewById(R.id.linearbar);
		bar.setBackgroundResource(R.drawable.bar_gradient);
     key_value=in.getStringExtra("key_value");
		// Add Bar to library
     if(key_value.equals("facebook"))
     {
		adapter = new SocialAuthAdapter(new ResponseListener());

		
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		newlink=in.getStringExtra("link");
		//newpost=in.getStringExtra("post");
		url="Link is: "+newlink;
     }
     else if(key_value.equals("twitter"))
     {
 		adapter = new SocialAuthAdapter(new ResponseListener());

 		
 		adapter.addProvider(Provider.TWITTER, R.drawable.twitter_new);
 		newlink=in.getStringExtra("linkin");
 		//newpost=in.getStringExtra("postin");
 		url="Link is: "+newlink;
      }
	
		//adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
		//adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);

		// Add email and mms providers
		//adapter.addProvider(Provider.EMAIL, R.drawable.email);
		//adapter.addProvider(Provider.MMS, R.drawable.mms);

		// For twitter use add callback method. Put your own callback url here.
		adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		adapter.enable(bar);

	}

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			// Variable to receive message status
			Log.d("Share-Bar", "Authentication Successful");
			 System.out.println("----hellloooo layout----");
			// Get name of provider after authentication
			final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("Share-Bar", "Provider Name = " + providerName);
			Toast.makeText(ShareBarActivity.this, providerName + " connected", Toast.LENGTH_SHORT).show();

			update = (Button) findViewById(R.id.update);
			edit = (EditText) findViewById(R.id.editTxt);
               edit.setText(url);
			// Please avoid sending duplicate message. Social Media Providers
			// block duplicate messages.

			update.setOnClickListener(new OnClickListener() {

				@Override
				
				public void onClick(View v) {
					// Call updateStatus to share message via oAuth providers
					// adapter.updateStatus(edit.getText().toString(), new
					// MessageListener(), false);
                      System.out.println("----hellloooo upadte----");
					// call to update on all connected providers at once
					adapter.updateStatus(edit.getText().toString(), new MessageListener(), true);
				}
			});

			// Share via Email Intent
			if (providerName.equalsIgnoreCase("share_mail")) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
						"vineet.aggarwal@3pillarglobal.com", null));
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test");
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
						"image5964402.png");
				Uri uri = Uri.fromFile(file);
				emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(emailIntent, "Test"));
			}

			// Share via mms intent
			if (providerName.equalsIgnoreCase("share_mms")) {
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
						"image5964402.png");
				Uri uri = Uri.fromFile(file);

				Intent mmsIntent = new Intent(Intent.ACTION_SEND, uri);
				mmsIntent.putExtra("sms_body", "Test");
				mmsIntent.putExtra(Intent.EXTRA_STREAM, uri);
				mmsIntent.setType("image/png");
				startActivity(mmsIntent);
			}
		}

		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			Log.d("Share-Bar", error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("Share-Bar", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Bar", "Dialog Closed by pressing Back Key");

		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {     
		System.out.println("Home presed");
	    if(keyCode == KeyEvent.KEYCODE_HOME)
	    {
	    	System.out.println("Home presed  in side");
	       finish();
	       System.exit(0);
	    }
	    if(keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	Intent i = new Intent(ShareBarActivity.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
				Toast.makeText(ShareBarActivity.this, "Message posted on " + provider, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(ShareBarActivity.this, "Message not posted on" + provider, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}
}