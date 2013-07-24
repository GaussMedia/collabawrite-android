package com.pnf.reportedly;


import com.navdrawer.SimpleSideDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Left_Menu extends Activity implements OnClickListener
{ 
	Button homeview_btn,create_location_btn,profile_screen_btn,states_btn,profile_settings_btn;
	Intent in;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.left_menu);
		myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	  app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
		homeview_btn = (Button) findViewById(R.id.homeview_btn);
		create_location_btn = (Button) findViewById(R.id.create_location_btn);
		profile_screen_btn = (Button)findViewById(R.id.profile_screen_btn);
		states_btn = (Button) findViewById(R.id.states_btn);
		profile_settings_btn = (Button) findViewById(R.id.profile_settings_btn);
        homeview_btn.setOnClickListener(this);
		create_location_btn.setOnClickListener(this);
		profile_screen_btn.setOnClickListener(this);
		states_btn.setOnClickListener(this);
		profile_settings_btn.setOnClickListener(this);
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
	/*case R.id.homeview_btn:
		in = new Intent(this, Home_Screen.class);
		
		System.out.println("this is home left menu");
		startActivity(in);
		finish();
		break;
	
	case R.id.create_location_btn:
		in = new Intent(this, Create_Collection_View.class);
		startActivity(in);
		finish();
		break;
	case R.id.profile_screen_btn:
		in = new Intent(this, Profile_Screen.class);
		startActivity(in);
		finish();
		break;
	case R.id.states_btn:
		in = new Intent(this, States_View.class);
		startActivity(in);
		finish();
		break;
	case R.id.profile_settings_btn:
		in = new Intent(this, Profile_Settings.class);
		startActivity(in);
		finish();
		break;*/
	default:
		break;
		}
	}
}