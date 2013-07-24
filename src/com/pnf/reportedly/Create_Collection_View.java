package com.pnf.reportedly;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Create_Collection_View extends Activity implements OnClickListener {
	Button create_collection_cancel, create_collection_save,
			create_collection_image;
	Intent in;
	String Url;
	Bitmap photo;
	EditText create_collection_title, create_collection_description,create_collection_id;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id, imgString;
	RadioGroup radio_grp;
	RadioButton radioButton,radio_two,radio_one;
	String title, description,finalUrl, image,radio_selected,convert,message,inserted_id;
	String uploadUrl = "http://reportedly.pnf-sites.info/webservices/api1/createcollection/";
	String newtitle, newdescription, newvalue;
	String final_url, selectpath = "", str, resp;
	Uri picUri;
	String gettitle,status;
	int titlelastindex;
	Animation anm; 
	TextView create_collection_invite,add_more;
	int checkedRadioButton;
	 RadioButton radiobutton;
	
	
	String radioButtonSelected = "", getvalue, getimage;
	private static final int CAMERA_CAPTURE = 0;
	private static final int IMAGE_GET = 1;
	private static final int PIC_CROP = 2;
	AsyncTask<Void, Void, Void> mRegisterTask;

	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.create_collection);

		myPrefs = this.getSharedPreferences("myPrefs",
				Context.MODE_WORLD_READABLE);
		prefsEditor = myPrefs.edit();
		app_id = myPrefs.getString("UserId", "");
		prefsEditor.commit();
		System.out.println("my getting id ===" + app_id);

		create_collection_title = (EditText) findViewById(R.id.create_collection_title);
		create_collection_description = (EditText) findViewById(R.id.create_collection_description);
		create_collection_image = (Button) findViewById(R.id.create_collection_image);
		radio_grp = (RadioGroup) findViewById(R.id.radio_grp);
		
      
		
		
        create_collection_image.setOnClickListener(new OnClickListener() {
		
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder srch = new AlertDialog.Builder(
						Create_Collection_View.this);
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

									break;

								case 1:
									Intent intent = new Intent();
									intent.setType("image/*");
									intent.setAction(Intent.ACTION_GET_CONTENT);
									startActivityForResult(Intent
											.createChooser(intent,
													"Select Image"), IMAGE_GET);

									break;
								}
							}
						});
				srch.show();
			}
		});
	
		
		 
		create_collection_cancel = (Button) findViewById(R.id.create_collection_cancel);
		create_collection_cancel.setOnClickListener(this);
		create_collection_save = (Button) findViewById(R.id.create_collection_save);
		create_collection_save.setOnClickListener(this);
		
		
		radiobutton=(RadioButton)findViewById(R.id.radio_one);
		   radio_selected=radiobutton.getText().toString();
		   if(radio_selected.equals("Anyone"))
	       {
	        getvalue="Anyone";
	       }
	       else
	       {
	    	  getvalue="Invite";
		          
	       }
		// This overrides the radiogroup onCheckListener
		radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(RadioGroup rGroup, int position)
		    {
		       
		       radiobutton= (RadioButton)rGroup.findViewById(position);
			   radio_selected=radiobutton.getText().toString();
		        if(radio_selected.equals("Invite Only"))
			       {
			        getvalue="Invite";
			       }
			       else
			       {
			    	  getvalue="Anyone";
				          
			       }
		       
		     
		       
		       }
		});
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.create_collection_cancel:

			in = new Intent(this, Home_Screen.class);
			startActivity(in);
			finish();
			break;

		case R.id.create_collection_save:
			try {

				/*
				radioButton=(RadioButton)findViewById(checkedRadioButton);
				radio_selected=radioButton.getText().toString();
				System.out.println("radio_selected==" +radio_selected);*/
				

				

				
				if (TextUtils.isEmpty(create_collection_title.getText()
						.toString())) {
					create_collection_title.setError("Please give a title");
					anm=AnimationUtils.loadAnimation(this, R.anim.shake);
					create_collection_save.setAnimation(anm);
					

				} else {
					System.out.println("tiltle length = " +create_collection_title.getText().toString().length());
					if(!(create_collection_title.getText().toString().substring(0, create_collection_title.getText().toString().length())).contains(" ")) 
					{
						create_collection_title.setError("Please enter atleast two words");
						anm=AnimationUtils.loadAnimation(this, R.anim.shake);
						create_collection_save.setAnimation(anm);
					}
					
					else
					{
						title = create_collection_title.getText().toString();
					}
					
					
				}
				
				
				if (TextUtils.isEmpty(create_collection_description.getText()
						.toString())) {
					create_collection_description.setError("Enter some words");
					anm=AnimationUtils.loadAnimation(this, R.anim.shake);
					create_collection_save.setAnimation(anm);
					
				} else {

					description = create_collection_description.getText()
							.toString();

				}
				if((create_collection_image.getText().toString()).equals("Attach image"))
				{
					create_collection_image.setError("Upload image");
					
					Toast.makeText(getApplicationContext(), "Upload Image ",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
					
					create_collection_image.setText("Successfully Uploaded");
				}

			

				if(title.equals("") || description.equals("") || getvalue.equals("") || (create_collection_image.getText().toString().equals("Attach image")) )
				{
					Toast.makeText(getApplicationContext(), "fill complete information",
							Toast.LENGTH_SHORT).show();
					
				}
				else{
					System.out.println("Image Value = " +create_collection_image.getText().toString());
					newtitle = URLEncoder.encode(title, "utf-8");
					newdescription = URLEncoder.encode(description, "utf-8");
				    newvalue=URLEncoder.encode(getvalue,"utf-8");
					 System.out.println("get value =" +getvalue);
					
					 finalUrl = uploadUrl+app_id+"/"+ newtitle + "/" + newdescription
					 + "/" + newvalue ;
					 System.out.println("Final url is=== " +finalUrl);
					
					 createCollection();
					 
					 
					
				}
			

				

			} catch (Exception e)

			{
				System.out.println("excepiton == "+ e);
				
			}

			break;

		default:
			break;
		}

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		
		return cursor.getString(column_index);
	}

	private void performCrop() {
		// take care of exceptions
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			System.out.println("---picUri----" + picUri);
			cropIntent.setDataAndType(picUri, "image/*");
			cropIntent.putExtra("crop", "true");
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			cropIntent.putExtra("return-data", true);
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("Home presed");http://reportedly.pnf-sites.info/webservices/api1/report/371
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			System.out.println("Home presed  in side");
			finish();
			System.exit(0);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Create_Collection_View.this,
					Home_Screen.class);
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
				System.out.println("---picUri----" + picUri);
				performCrop();
			}
			break;
		case IMAGE_GET:
			if (resultCode == RESULT_OK) {
				picUri = data.getData();
				performCrop();
			}
			break;
		case PIC_CROP:
			try {
				Bundle extras = data.getExtras();
				Bitmap bmp = extras.getParcelable("data");
				ContentValues values = new ContentValues();
				values.put(Images.Media.TITLE, "title");
				values.put(Images.Media.BUCKET_ID, "test");
				values.put(Images.Media.DESCRIPTION, "test Image taken");
				values.put(Images.Media.MIME_TYPE, "image/jpeg");
				Uri uri = getContentResolver().insert(
						Media.EXTERNAL_CONTENT_URI, values);
				photo = (Bitmap) data.getExtras().get("data");
				OutputStream outstream;
				try {
					   
					outstream = getContentResolver().openOutputStream(uri);
					photo.compress(Bitmap.CompressFormat.PNG, 100, outstream);
					
					outstream.close();
				} catch (FileNotFoundException e) {

				} catch (IOException e) {
				}
				selectpath = getPath(uri);
				
				create_collection_image.setText("Successfully Uploaded");
				
			} catch (NullPointerException e) {

			}
			break;
		default:
			break;
		}
	}

	private byte[] getBytesFromBitmap(Bitmap photo2) {
		// TODO Auto-generated method stub

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo2.compress(CompressFormat.JPEG, 70, stream);
		return stream.toByteArray();

	}

	 public void createCollection()
	 {
		 new AsyncTask<Void, Void, Void>()
		 {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try
				{
					JsonParser jparser=new JsonParser();
					System.out.println("final url  = " +finalUrl);
					JSONObject jobj=jparser.getJSONfromUrl(finalUrl);
					message=jobj.getString("Message");
					System.out.println("my new message = " + message);
					if(message.equals("true"))
					{
						inserted_id=jobj.getString("id");
						System.out.println("iddd = " +inserted_id);
					}
					else
					{
						status=jobj.getString("status");
						
					}
					
					
				}
				catch (Exception e) {
					// TODO: handle exception
					System.out.println("exception in data = "+ e );
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(message.equals("true"))
				{
					System.out.println("iddd in post = " + inserted_id);
					imageUpload(inserted_id);
				    
				}
				else
				{
				Toast.makeText(Create_Collection_View.this, status, Toast.LENGTH_LONG).show();
				}
				}
			 
		 }.execute();

			
	 }
	
	public void imageUpload(final String insert) {
		mRegisterTask = new AsyncTask<Void, Void, Void>() {
			

			@Override
			protected Void doInBackground(Void... params) {
				
				System.out.println("Final Url = " + Url);
				HttpURLConnection conn = null;
				DataOutputStream dos = null;
				DataInputStream inStream = null;
				String lineEnd = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";
				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 2 * 1024 * 1024;

				try {
					System.out.println("Pathhh = " + selectpath);
					FileInputStream fileInputStream = new FileInputStream(
							new File(selectpath));
					URL url = new URL("http://reportedly.pnf-sites.info/webservices/api1/imageUpload.php?id="+insert);
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Content-Type",
							"multipart/form-data;boundary=" + boundary);
					dos = new DataOutputStream(conn.getOutputStream());
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"userfile\";filename=\""
							+ selectpath+ "\"" + lineEnd);
				
					dos.writeBytes(lineEnd);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					Log.v("buff size", String.valueOf(bufferSize));
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					// //bytesRead = 0;
					while (bytesRead > 0)
					// while ((bytesRead = fileInputStream.read(buffer))
					// != -1)
					{
						Log.v("byte", String.valueOf(bytesRead));
						// dos.write(buffer, 0, bytesRead);
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					Log.v("Debug", "File is written");
					fileInputStream.close();
					dos.flush();
					dos.close();
				} catch (SocketException ex) {
					Log.v("Error : ",
							"Error on soapPrimitiveData() " + ex.getMessage());
					ex.printStackTrace();
				} catch (MalformedURLException ex) {
					Log.v("Debug", "error: " + ex.getMessage(), ex);
				} catch (IOException ioe) {
					Log.v("Debug", "error: " + ioe.getMessage(), ioe);
				}

				try {
					inStream = new DataInputStream(conn.getInputStream());
					Log.v("Debug",
							"Server Response " + String.valueOf(inStream));
					while ((str = inStream.readLine()) != null) {
						Log.v("Debug", "Server Response " + str);
						if (str.contains("http"))
							resp = str;
						else if (str.contains("true"))
							resp = "true";
						else if (str.contains("false"))
							resp = "false";
					}
					inStream.close();
				} catch (IOException ioex) {
					
					System.out.println("exception 1 = " +ioex);

				} catch (NullPointerException e) {
					System.out.println("exception 2 = " +e);
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(resp.equals("true"))
				{
					
					System.out.println("Intent Response =" + message +" " + resp);
					Intent in=new Intent(Create_Collection_View.this,EditCollection.class);
					in.putExtra("collection_id", inserted_id);
					in.putExtra("selectpath", selectpath);
					startActivity(in);
					overridePendingTransition(0, 0);
					finish();
				}
			
			}

		};
		mRegisterTask.execute(null,null,null);
		System.out.println("My Response out=" + resp);
		 //return resp;
	}

}