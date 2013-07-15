package com.example.reportedly;





import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity implements OnClickListener{
   
	AnimationDrawable frameanimation;
	Button go_login;
	ImageButton getstarted_btn;
	Intent in;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		RelativeLayout layout=(RelativeLayout)findViewById(R.id.splashlayout);
		
		go_login=(Button)findViewById(R.id.go_login);
		getstarted_btn=(ImageButton)findViewById(R.id.getstart_button);
		
		frameanimation=(AnimationDrawable)layout.getBackground();
		
		layout.post(new Timer());
		
         go_login.setOnClickListener(this);
		 getstarted_btn.setOnClickListener(this);
	}
	
 	public class  Timer implements Runnable 
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			frameanimation.start();
			
		}
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.go_login:
			in=new Intent(this,Login_Activity.class);
	    	startActivity(in);
			finish();
			break;
		case R.id.getstart_button:
			in=new Intent(this,GetStarted_First.class);
			startActivity(in);
			finish();
			break;

		default:
			break;
		}
		
	}


}
