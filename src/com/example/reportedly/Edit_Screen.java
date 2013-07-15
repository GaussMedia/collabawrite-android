package com.example.reportedly;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_Screen extends Activity implements OnClickListener {
	Button cancel_edit, edit_save;
	EditText edit_name, edit_website, edit_id;
	String edit_url = "http://reportedly.pnf-sites.info/webservices/api1/updateuser/";
	Intent in;
	TextView edit_username;
	String user_id, get_name, get_website, get_about, get_email, final_url,message, username;
	URI uri = null;
	String url_new;
	EditText edit_about;
	
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;

	protected void onCreate(Bundle saveInstance) {
		
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.edit_screen);
		
		myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	    app_id= myPrefs.getString("UserId","");
	  // prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
		Intent in = getIntent();
		user_id = in.getStringExtra("id");
		username = in.getStringExtra("username");
		System.out.println("userid.... " + user_id);
		cancel_edit = (Button) findViewById(R.id.cancel_edit);
		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_username = (TextView) findViewById(R.id.edit_username);
		edit_website = (EditText) findViewById(R.id.edit_website);
		edit_about = (EditText) findViewById(R.id.edit_about);
		edit_id = (EditText) findViewById(R.id.edit_id);
		edit_save = (Button) findViewById(R.id.edit_save);
		edit_username.setText(username);
		edit_save.setOnClickListener(this);
		cancel_edit.setOnClickListener(this);
		
	}

	@Override
		public void onClick(View v) { 
		
		switch (v.getId()) {
		
		case R.id.cancel_edit:
			in = new Intent(this, Profile_Screen.class);
			startActivity(in);
			finish();
			break;  
			
		case R.id.edit_save:
			get_name = edit_name.getText().toString();
			get_email = edit_id.getText().toString();
			get_about = edit_about.getText().toString();
			get_website = edit_website.getText().toString();
			try {  
			String id=URLEncoder.encode(user_id, "utf-8");
			String name=URLEncoder.encode(get_name, "utf-8");
			String email=URLEncoder.encode(get_email,"utf-8");
			String about=URLEncoder.encode(get_about, "utf-8");
			String website=URLEncoder.encode(get_website, "utf-8");
			
			final_url = edit_url + id + "/" + name + "/" + email
					+ "/" + about + "/" + website;
			
			} 
			catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			System.out.println("final url == " + final_url);
			

			try {
			
			

			System.out.println("rofile url == " + final_url);
			JsonParser jparser = new JsonParser();
			System.out.println("rofile url1 " );
			JSONObject jobj = jparser.getJSONfromUrl(final_url);
			System.out.println("rofile url1 232434" );
			message = jobj.getString("Message");
			System.out.println("messsgae   " + message);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (message.equals("true")) {
				Toast.makeText(Edit_Screen.this,
						"Profile Successfully Updated", Toast.LENGTH_LONG)
						.show();
			}
			else if (message.equals("false")) {
				Toast.makeText(Edit_Screen.this,
						"Profile has not been changes unsucessful",
						Toast.LENGTH_LONG).show();
			}
			
		
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
	    	Intent i = new Intent(Edit_Screen.this,Profile_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
}
