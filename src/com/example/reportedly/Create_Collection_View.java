package com.example.reportedly;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Create_Collection_View extends Activity implements OnClickListener
{
	 Button create_collection_cancel,create_collection_save,create_collection_image;
	 Intent in;
	 TextView create_collection_title,create_collection_description;
	 SharedPreferences myPrefs;
		SharedPreferences.Editor prefsEditor;
		String app_id;
	 RadioGroup radio_grp;
	 RadioButton radio_one,radio_two;
	 String title,description,image;
     String url="http://reportedly.pnf-sites.info/webservices/api1/createcollection/";
     String final_url;
     Uri picUri;
     String radioButtonSelected = "",getvalue,getimage;
 	private static final int CAMERA_CAPTURE = 0;
 	private static final int IMAGE_GET = 1;
	protected void onCreate(Bundle saveInstance) 
	{
		super.onCreate(saveInstance);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.create_collection);
		
		 myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
		    prefsEditor = myPrefs.edit();
		  app_id= myPrefs.getString("UserId","");
		   prefsEditor.commit();
		   System.out.println("my getting id ===" +app_id);
		   
		create_collection_title=(TextView)findViewById(R.id.create_collection_title);
		create_collection_description=(TextView)findViewById(R.id.create_collection_description);
		create_collection_image=(Button)findViewById(R.id.create_collection_image);
		radio_grp=(RadioGroup)findViewById(R.id.radio_grp);
		radio_one=(RadioButton)findViewById(R.id.radio_one);
		radio_two=(RadioButton)findViewById(R.id.radio_two);
		radio_one.setText("Anyone");
		radio_two.setText("Invite");
		
		create_collection_image.setOnClickListener(this);
		create_collection_cancel=(Button)findViewById(R.id.create_collection_cancel);
		create_collection_cancel.setOnClickListener(this);
		create_collection_save=(Button)findViewById(R.id.create_collection_save);
		create_collection_save.setOnClickListener(this);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_grp);
		 
		 
		int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
		 
	
		switch (checkedRadioButton) {
		  case R.id.radio_one :
			  radioButtonSelected = "radio_one";
		          break;
		  case R.id.radio_two :
			  radioButtonSelected = "radio_two";
				    break;
		  
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.create_collection_cancel:
			
			in= new Intent(this,Home_Screen.class);
			startActivity(in);
			finish();
			break;
         case R.id.create_collection_image:
        
        		Builder srch = new AlertDialog.Builder(getApplicationContext());
				srch.setTitle("Profile Picture");
				srch.setItems(R.array.prflPic,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Log.v("Log",
										"search select value: "
												+ String.valueOf(which));
								switch (which) {
								case 0:
									Intent cameraIntent = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(cameraIntent,
											CAMERA_CAPTURE);
                                     /*getimage=cameraIntent.toString();*/

									break;

								case 1:
									Intent intent = new Intent();
									intent.setType("image/*");
									intent.setAction(Intent.ACTION_GET_CONTENT);
									startActivityForResult(Intent
											.createChooser(intent,
													"Select Image"), IMAGE_GET);
									/*getimage=i*/
									break;
								}
							}
						});
				srch.show();                  

			
			
        	 
			break;
       
        	
			
			
        case R.id.create_collection_save:
			
        	try{
        		
        		
        		if(TextUtils.isEmpty(create_collection_title.getText().toString()))
        		{
        			create_collection_title.setError("Please give a title");
        			
        			
        		}
        		else
        		{
        			title=create_collection_title.getText().toString();
        		}
        		if(TextUtils.isEmpty(create_collection_description.getText().toString()))
        		{
        			create_collection_description.setError("Enter some words");
        			
        		}
        		else
        		{
        			
        			 description=create_collection_description.getText().toString();
        			
        		
        		}
        		if(TextUtils.isEmpty(radioButtonSelected))
        		{
        		Toast.makeText(getApplicationContext(), "Select one value", Toast.LENGTH_SHORT).show();
        		
        		}
        		else 
        		{
        			getvalue=radioButtonSelected;
        		}
        	
        		
        		
        		
        		
        		
        		try {  
        			String newtitle=URLEncoder.encode(title, "utf-8");
        			String newdescription=URLEncoder.encode(description, "utf-8");
        			String newvalue=URLEncoder.encode(getvalue,"utf-8");
        			//String loc_ation=URLEncoder.encode(set_location, "utf-8");
        			
        			
        			final_url = url + newtitle + "/" + newdescription  
        					+ "/" + newvalue ;
        			
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
        			if(message.equals("values Inserted successfully"))
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
        		
        	
        	catch(NullPointerException e)
        	{
        		
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
	    	Intent i = new Intent(Create_Collection_View.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CAMERA_CAPTURE:
			if (resultCode == RESULT_OK) {
				picUri = data.getData();
				System.out.println("---picUri----"+picUri);
				
			}
			break;
		case IMAGE_GET:
			if (resultCode == RESULT_OK) {
				picUri = data.getData();
				
			}
			break;
			default:
				break;
	}
	}

}
