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

public class GetStarted_Second extends Activity implements OnClickListener
{
	Button continue_second;
	Intent in;
	protected void  onCreate(Bundle saveInsatnce)
	{
		super.onCreate(saveInsatnce);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.getstarted_second);
		continue_second=(Button)findViewById(R.id.continue_second);
		continue_second.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubirst
		switch (v.getId()) {
		case R.id.continue_second:
			in=new Intent(this,GetStarted_Third.class);
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
	    	Intent i = new Intent(GetStarted_Second.this,GetStarted_First.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
	}
