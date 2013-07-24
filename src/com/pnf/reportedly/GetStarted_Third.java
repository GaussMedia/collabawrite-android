package com.pnf.reportedly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GetStarted_Third extends Activity implements OnClickListener {
	
	Button continue_third;
	Intent in;
	protected void  onCreate(Bundle saveInsatnce)
	{
		super.onCreate(saveInsatnce);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.getstarted_third);
		continue_third=(Button)findViewById(R.id.continue_third);
		continue_third.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.continue_third:
			in=new Intent(this,GetStarted_Fourth.class);
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
	    	Intent i = new Intent(GetStarted_Third.this,GetStarted_Second.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
}
