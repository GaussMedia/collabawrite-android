package com.example.reportedly;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class Getstarted_Five extends Activity implements android.view.View.OnClickListener  {


	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	Button continue_fourth;
	TextView just_browse;
	Intent in;
	GPSTracker gps;
	Geocoder geocoder;
	List<Address> addresses;
	public String country,address,city;
	protected void  onCreate(Bundle saveInsatnce)
	{
		super.onCreate(saveInsatnce);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.getstarted_five);
		continue_fourth=(Button)findViewById(R.id.continue_fifth);
		just_browse=(TextView)findViewById(R.id.just_browse);
		just_browse.setOnClickListener(this);
	    continue_fourth.setOnClickListener(this);
	    myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	    app_id=myPrefs.getString("UserId","");
	
	    
	}

	

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		final AlertDialog alertDialog;
		switch (v.getId()) {
		case R.id.just_browse:
			/*gps = new GPSTracker(this);

			// check if GPS enabled		
	        
	        if(gps.canGetLocation()){
	        	
	        	double latitude = gps.getLatitude();
	        	double longitude = gps.getLongitude();
	        	
	        	geocoder = new Geocoder(this, Locale.getDefault());
	        	try {
					addresses = geocoder.getFromLocation(latitude, longitude, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


	        	       String cityname = addresses.get(0).getAddressLine(1);
	                   System.out.println(" cityyyy = "+ cityname);
	                   String cityname1=cityname.replace(",", "");
	                   String cityname2=cityname1.replace(" ", "");
	                   city=cityname2.replaceAll("[0-9]", "");
	                   
	                   
	                   
	                   
	        	// \n is for new line
	        	Toast.makeText(getApplicationContext(), "Your Location is city " + city , Toast.LENGTH_LONG).show();	
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        }*/
			if(app_id.equals(""))
			{
			Intent in=new Intent(this,Home_Screen.class);
			in.putExtra("city", city);
			//in.putExtra("address", address);
			in.putExtra("country", country);
			startActivity(in);
			finish();
			}
			else
			{
				prefsEditor.remove("UserId");
				prefsEditor.clear();
				prefsEditor.commit();
				Intent in=new Intent(this,Home_Screen.class);
				in.putExtra("city", city);
				//in.putExtra("address", address);
				in.putExtra("country", country);
				startActivity(in);
				finish();
				Toast.makeText(this, "Your last Session destroyed", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.continue_fifth:
		
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Reportedly");
			alertDialog.setMessage("Are you sure want to signin ? ");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					Intent in =new Intent(Getstarted_Five.this,Login_Activity.class);
					startActivity(in);
					finish();
					
				}
			});
			
			alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
			
			break;

		default:
			break;
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
	    	Intent i = new Intent(Getstarted_Five.this,GetStarted_Fourth.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
	
}

