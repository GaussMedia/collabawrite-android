package com.pnf.reportedly;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Preview_Screen extends Activity implements OnClickListener 
{
	Button preview_cancel;
	TextView preview_title,preview_subtitle;
	String set_title,set_subtitle;

	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	protected void onCreate(Bundle savedInstanceState)
	{
		 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.preview_screen);
	
		myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	  app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
	
		preview_cancel=(Button)findViewById(R.id.preview_screen_cancel);
		preview_title=(TextView)findViewById(R.id.preview_title);
		preview_subtitle=(TextView)findViewById(R.id.preview_subtitle);
		
		preview_cancel.setOnClickListener(this);
		Bundle get_values=getIntent().getExtras();
		set_title=get_values.getString("title");
		set_subtitle=get_values.getString("subtitle");
		
		preview_title.setText(set_title);
		preview_subtitle.setText(set_subtitle);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.preview_screen_cancel:
			Intent in =new Intent(this,Create_Story.class);
			startActivity(in);
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
	    	Intent i = new Intent(Preview_Screen.this,Create_Story.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
}
