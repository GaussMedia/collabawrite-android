package com.pnf.reportedly;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp_Screen extends Activity implements OnClickListener
{
	Button signup,cancel;
	EditText username_sign,password_sign,email_sign,full_name;
	AutoCompleteTextView location;
	Intent in;
	String url="http://reportedly.pnf-sites.info/webservices/api1/signup/";
	String final_url;
	String fullname,user,password,email,set_location;
	ArrayList<String> locationlist=new ArrayList<String>();
	private static Animation anm;
	ConnectionStatus conn;
	boolean conn_status;
	InputMethodManager ipm;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
protected void  onCreate(Bundle savedInstance) 
{ 
	
	super.onCreate(savedInstance);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	setContentView(R.layout.signup_screen);
	

	myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	  app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
	username_sign=(EditText)findViewById(R.id.username_signup);
	password_sign=(EditText)findViewById(R.id.password_signup);
	email_sign=(EditText)findViewById(R.id.email_signup);
	location=(AutoCompleteTextView)findViewById(R.id.location);
	full_name=(EditText)findViewById(R.id.fullname_signup);
	
	conn=new ConnectionStatus(this);
	cancel=(Button)findViewById(R.id.cancel_signup);
	signup=(Button)findViewById(R.id.signup_btn);
	cancel.setOnClickListener(this);
	signup.setOnClickListener(this);
	location.setOnClickListener(this);
	 ipm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	 
	
}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	switch (v.getId()) {
	case R.id.signup_btn:
		ipm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); 
		conn_status=conn.isConnected();
	
	try{
		
		if(conn_status)
		{
			
			
			
			if(TextUtils.isEmpty(full_name.getText().toString()))
			{
				full_name.setError("Enter Your FullName");
				anm=AnimationUtils.loadAnimation(this, R.anim.shake);
				signup.setAnimation(anm);
				
			}
			else
			{
				fullname=full_name.getText().toString();
			}
				
			
		if(TextUtils.isEmpty(username_sign.getText().toString()))
		{
			username_sign.setError("Enter Your UserName");
			anm=AnimationUtils.loadAnimation(this, R.anim.shake);
			signup.setAnimation(anm);
			
		}
		else
		{
			user=username_sign.getText().toString();
		}
		if(TextUtils.isEmpty(password_sign.getText().toString()))
		{
			password_sign.setError("Enter Your Password");
			anm=AnimationUtils.loadAnimation(this, R.anim.shake);
			signup.setAnimation(anm);
		}
		else
		{
			if(password_sign.getText().toString().length() >=6)
			{
				password=password_sign.getText().toString();
			}
			else
			{
				password_sign.setError("Password must be 6 character long");
				anm=AnimationUtils.loadAnimation(this, R.anim.shake);
				signup.setAnimation(anm);
			
			}
		}
		if(TextUtils.isEmpty(email_sign.getText().toString()))
		{
			email_sign.setError("Enter Your Email");
			anm=AnimationUtils.loadAnimation(this, R.anim.shake);
			signup.setAnimation(anm);
		}
		else 
		{
			if(email_sign.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
		{
			email=email_sign.getText().toString();
			
		}
		else
		{
			email_sign.setError("Enter Valid Email");
			anm=AnimationUtils.loadAnimation(this, R.anim.shake);
			signup.setAnimation(anm);
			   
		}
		
		}
		if(TextUtils.isEmpty(location.getText().toString()))
		{
			location.setError("Enter Your Location");
			anm=AnimationUtils.loadAnimation(this, R.anim.shake);
			signup.setAnimation(anm);
			
		}
		else
		{
			set_location=location.getText().toString();
		}
		
		
		
		try {  
			String user_name=URLEncoder.encode(user, "utf-8");
			String full_name=URLEncoder.encode(fullname, "utf-8");
			String pass_word=URLEncoder.encode(password, "utf-8");
			String email_id=URLEncoder.encode(email,"utf-8");
			String loc_ation=URLEncoder.encode(set_location, "utf-8");
			
			
			final_url = url + user_name + "/" + full_name+ "/" + email_id + "/" + pass_word
					+ "/" + loc_ation ;
			
			} 
			catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		try
		{
			
			System.out.println("final url == "+ final_url);
			JsonParser jparser=new JsonParser();
			JSONObject jobj=jparser.getJSONfromUrl(final_url);
			String message=jobj.getString("Message");
			if(message.equals("User Inserted successfully"))
			{
				Toast.makeText(this, message, Toast.LENGTH_LONG).show();
				Intent in = new Intent(this,Login_Activity.class);
				startActivity(in);
				finish();
			}
			else
			{
				String status=jobj.getString("status");
				Toast.makeText(this, status, Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e)
		{
			
		}
		}
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
	catch(NullPointerException e)
	{
		
	}
		
		/*
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Reportedly");
		alertDialog.setMessage("Successfully SignUp");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				alertDialog.dismiss();
				username_sign.setText("");
				password_sign.setText("");
				email_sign.setText("");
			}
		});
		alertDialog.show();*/
		break;
case R.id.cancel_signup:
		in=new Intent(this,Login_Activity.class);
		startActivity(in);
		finish();
		break;
case R.id.location:
	locationlist=new ArrayList<String>();
	location.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			String temp = null;
			try
			{
				temp = new MapLoader().execute(String.valueOf(s)).get();
				
				if(temp != null) {
					if(!locationlist.contains(temp))
						locationlist.add(temp);
					System.out.println("location: "+location);
					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp_Screen.this, android.R.layout.simple_spinner_item, locationlist);
					adapter.setNotifyOnChange(true);
					location.setThreshold(1);
					location.setAdapter(adapter);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
			
			location.setMovementMethod(new ScrollingMovementMethod());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
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
    	Intent i = new Intent(SignUp_Screen.this,Login_Activity.class);
		startActivity(i);
		overridePendingTransition(0, 0);
		finish();
    }
	return false;
}
class MapLoader extends AsyncTask<String, String, String> {


	@Override
	protected String doInBackground(String... params) {
		String s = params[0];
		String formatted_address = null;
		JsonParser jParser = new JsonParser();
		try
		{
			JSONObject json = jParser.getJSONfromUrl("http://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(s.toString(), "UTF-8")+"&sensor=false");
			JSONArray results = json.getJSONArray("results");
			for(int i=0; i<results.length(); i++)
			{
				JSONObject r = results.getJSONObject(i);
				formatted_address = r.getString("formatted_address");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return formatted_address;
	}
	
}


}
