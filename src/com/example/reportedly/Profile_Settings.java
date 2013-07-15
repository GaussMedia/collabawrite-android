package com.example.reportedly;


import org.json.JSONException;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class Profile_Settings extends Activity implements OnClickListener
{
	
	Button profile_settings_cancel_login,logout,edit_your_profile;
	Intent in;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	
	protected void onCreate(Bundle saveInstance)
	{
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.profile_settings);
		profile_settings_cancel_login=(Button)findViewById(R.id.profile_settings_cancel_login);
		profile_settings_cancel_login.setOnClickListener(this);
		logout=(Button)findViewById(R.id.logout);
		edit_your_profile=(Button)findViewById(R.id.edit_your_profile);
		
		
		  myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
		    prefsEditor = myPrefs.edit();
		    myPrefs.getString("UserId","");
		    logout.setOnClickListener(this);
		    edit_your_profile.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.profile_settings_cancel_login:
			in= new Intent(this,Home_Screen.class);
			startActivity(in);
			overridePendingTransition(0, 0);
			finish();
			
			break;
		case R.id.logout:
			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				Profile_Settings.this);
		alertDialog2.setTitle("Reportedly");
		alertDialog2.setMessage(" Are you sure you want to Logout?");
		alertDialog2.setIcon(R.drawable.alert);
		alertDialog2.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
							
						prefsEditor.remove("UserId");
						prefsEditor.clear();
						prefsEditor.commit();
						in= new Intent(Profile_Settings.this,Login_Activity.class);
						startActivity(in);
						overridePendingTransition(0, 0);
						finish();
						
											
						System.gc();
						
					}	
					});
					
		
			
		alertDialog2.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
		});
		alertDialog2.show();
					
		
			
			break;
		case R.id.edit_your_profile:
			Intent in=new Intent(this,Edit_Screen.class);
			startActivity(in);
			overridePendingTransition(0, 0);
			finish();
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
	    	Intent i = new Intent(Profile_Settings.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
}
