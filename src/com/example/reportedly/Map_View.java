package com.example.reportedly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

public class Map_View extends FragmentActivity
{
	protected void onCreate(Bundle saved)
     {
		super.onCreate(saved);
		setContentView(R.layout.map_view);
		
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
	    	Intent i = new Intent(Map_View.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
}
