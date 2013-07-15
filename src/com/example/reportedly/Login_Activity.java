package com.example.reportedly;



import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Login_Activity extends Activity implements OnClickListener
{
	Button cancel,signnp,signin,facebook_btn,twitter_btn;
	EditText username,password;
	TextView forgot_pass;
	Intent in;
	InputMethodManager ipm;
	String message,user,pass,id,status;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	static String url="http://reportedly.pnf-sites.info/webservices/api1/login/";
	
	private static Animation anm;
	ConnectionStatus conn;
	boolean conn_status;
	
   protected void onCreate(Bundle savedInstance)
   {
	   super.onCreate(savedInstance);
	   requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   setContentView(R.layout.login_screen);
	    myPrefs = Login_Activity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
	    prefsEditor = myPrefs.edit();
	    
	   cancel=(Button)findViewById(R.id.cancel_login);
	   signnp=(Button)findViewById(R.id.signup);
	   signin=(Button)findViewById(R.id.signin_btn);
	 //  facebook_btn=(Button)findViewById(R.id.facebook_btn);
	  // twitter_btn=(Button)findViewById(R.id.twitter_btn);
	   username=(EditText)findViewById(R.id.username);
	    password=(EditText)findViewById(R.id.password);
	    forgot_pass=(TextView)findViewById(R.id.forgot_password);
	    conn=new ConnectionStatus(this);
	    
	   
	    forgot_pass.setOnClickListener(this);
	   cancel.setOnClickListener(this);
	   signin.setOnClickListener(this);
	   signnp.setOnClickListener(this);
	  /* facebook_btn.setOnClickListener(this);
	   twitter_btn.setOnClickListener(this);*/
	   ipm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	   
   }
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	final AlertDialog alertDialog;
	
	conn_status=conn.isConnected();
	switch (v.getId()) {
	case R.id.cancel_login:
		ipm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		in = new Intent(this,SplashActivity.class);
		startActivity(in);
		finish();
		break;
	case R.id.signup:
		 in =new Intent(Login_Activity.this,SignUp_Screen.class);
		startActivity(in);
		finish();
		break;
case R.id.signin_btn:
		
	try
	{
		System.out.println("connection status = " + conn_status);
		if(conn_status)
		{
	if(TextUtils.isEmpty(username.getText().toString()))
	{
		username.setError("Enter Your Username");
	    anm=AnimationUtils.loadAnimation(this, R.anim.shake);
	    signin.setAnimation(anm);
	}
	else
	{
		user=username.getText().toString();	
		
	}
	
	if(TextUtils.isEmpty(password.getText().toString()))
	{
		password.setError("Enter Your Username");
	    anm=AnimationUtils.loadAnimation(this, R.anim.shake);
	    signin.setAnimation(anm);
	}
	else
	{
	 pass=password.getText().toString();
	}
		
	
	 if (user.equals("") || pass.equals(""))
		{
			final AlertDialog alertdialog = new AlertDialog.Builder(Login_Activity.this).create();
			alertdialog.setTitle("Reportedly");
			alertdialog.setMessage("plz enter your values");
			alertdialog.setButton("OK", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					alertdialog.dismiss();
			
				    username.setText("");
				    password.setText("");
						
				}
			});
			alertdialog.show();
		
		}
	 else{
	new AsyncTask<String, Integer, String>()
	{
        
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			  
			
			try {
				String furl=url+user+"/"+pass;
				JsonParser json=new JsonParser();
			JSONObject jobj=json.getJSONfromUrl(furl);
          			
			
				message=jobj.getString("Message");
				
				
				if(message.equals("true"))
				{
					id=jobj.getString("id");
					status=jobj.getString("status");
						System.out.println("Status = " +status);
						//Reportedly_Pref.ID_DEF = id;
						System.out.println("my login id: "+id);
						prefsEditor.putString("UserId", id);
						prefsEditor.commit();
					
					
				}
				
				
					
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("message == " +message);
			
			return message;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(message.equals("true"))
			{
				if(status.equals("1"))
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(Login_Activity.this).create();
					alertDialog.setTitle("Reportedly");
					alertDialog.setMessage("Successfully Signin");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							alertDialog.dismiss();
							
							in =new Intent(Login_Activity.this,Home_Screen.class);
							
							startActivity(in);
							finish();
						    
								
						}
					});
					alertDialog.show();
					
				}
				else 
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(Login_Activity.this).create();
					alertDialog.setTitle("Reportedly");
					alertDialog.setMessage("You have to verify your account");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							alertDialog.dismiss();
							
						    username.setText("");
						    password.setText("");
								
						}
					});
					alertDialog.show();
				}
			}
			
			else
			{
				 
				final AlertDialog alertDialog = new AlertDialog.Builder(Login_Activity.this).create();
				alertDialog.setTitle("Reportedly");
				alertDialog.setMessage("UserName or Password is not correct");
				alertDialog.setButton("OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						alertDialog.dismiss();
						
					    username.setText("");
					    password.setText("");
							
					}
				});
				alertDialog.show();
			}
		}

		
	}.execute();
	
	 }
	}
	else
	{
		 alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Reportedly");
		alertDialog.setMessage("No Internet Connection Available");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{						
			}
		});
		alertDialog.show();
	}
	}
	catch (NullPointerException e) {
		// TODO: handle exception
	}
		break;
/*			
case R.id.facebook_btn:
	
	alertDialog = new AlertDialog.Builder(this).create();
	alertDialog.setTitle("Reportedly");
	alertDialog.setMessage("Login with Facebook");
	alertDialog.setButton("OK", new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			alertDialog.dismiss();
			

		    username.setText("");
		    password.setText("");
		}
	});
	alertDialog.show();
	
		break;
case R.id.twitter_btn:
	
	alertDialog = new AlertDialog.Builder(this).create();
	alertDialog.setTitle("Reportedly");
	alertDialog.setMessage("Login with Twitter");
	alertDialog.setButton("OK", new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			alertDialog.dismiss();

		    username.setText("");
		    password.setText("");
				
		}
	});
	alertDialog.show();
	
		break;*/
case R.id.forgot_password:
	alertDialog = new AlertDialog.Builder(this).create();
	alertDialog.setTitle("Reportedly");
	alertDialog.setMessage("Send Via Email");
	final EditText input = new EditText(this);
	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	        LinearLayout.LayoutParams.FILL_PARENT,
	        LinearLayout.LayoutParams.FILL_PARENT);
	input.setLayoutParams(lp);
	alertDialog.setView(input);
	alertDialog.setButton("Send", new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			alertDialog.dismiss();

		    username.setText("");
		    password.setText("");
				
		}
	});
	alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			alertDialog.dismiss();

		    username.setText("");
		    password.setText("");
				
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
    	Intent i = new Intent(Login_Activity.this,SplashActivity.class);
		startActivity(i);
		overridePendingTransition(0, 0);
		finish();
    }
	return false;
}
}
 