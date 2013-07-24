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
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditCollection extends Activity implements OnClickListener {
	EditText edit_collection_title, edit_collection_description;
	String url = "http://reportedly.pnf-sites.info/webservices/api1/usercollection/";
	String edit_url = "http://reportedly.pnf-sites.info/webservices/api1/editcollection/";
	String invite_url = "http://reportedly.pnf-sites.info/webservices/api1/addinvitee/";
	String delete_url="http://reportedly.pnf-sites.info/webservices/api1/deletecollection/";
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	RadioGroup radio_grp;
	RadioButton anyone, invite, radiobutton;
	Button edit_collection_delete, edit_collection_save, edit_collection_image;
	Intent in;
	TextView edit_collection_id, add_more, edit_collection_invite;
	private static final int CAMERA_CAPTURE = 0;
	private static final int IMAGE_GET = 1;
	private static final int PIC_CROP = 2;
	Animation anm;
	Bitmap photo;
	Uri picUri;
	AsyncTask<Void, Void, Void> mRegisterTask;
	AlertDialog alertdialog;
	String app_id, collection_id, final_url, message, msg,delete_msg,delete_status,title, description,
			contriubute_type, selectpath ="", getvalue, radio_selected, rqst,
			edittitle, editdescription, editvalue, editUrl, str, resp, author,
			status, invite_final_url, enter_email;

	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.edit_collection);
		myPrefs = this.getSharedPreferences("myPrefs",
				Context.MODE_WORLD_READABLE);
		prefsEditor = myPrefs.edit();
		app_id = myPrefs.getString("UserId", "");
		prefsEditor.commit();

		edit_collection_title = (EditText) findViewById(R.id.edit_collection_title);
		edit_collection_description = (EditText) findViewById(R.id.edit_collection_description);
		edit_collection_image = (Button) findViewById(R.id.edit_collection_image);

		edit_collection_delete = (Button) findViewById(R.id.edit_collection_delete);
		edit_collection_save = (Button) findViewById(R.id.edit_collection_save);

		anyone = (RadioButton) findViewById(R.id.edit_radio_one);
		invite = (RadioButton) findViewById(R.id.edit_radio_two);
		radio_grp = (RadioGroup) findViewById(R.id.edit_radio_grp);

		edit_collection_invite = (TextView) findViewById(R.id.edit_collection_invite);
		add_more = (TextView) findViewById(R.id.add_more);
		edit_collection_id = (EditText) findViewById(R.id.edit_collection_id);

		edit_collection_delete.setOnClickListener(this);
		edit_collection_save.setOnClickListener(this);
		add_more.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (TextUtils.isEmpty(edit_collection_id.getText()
						.toString())) {
					edit_collection_id.setError("Please enter some email");
					anm = AnimationUtils.loadAnimation(EditCollection.this, R.anim.shake);
					add_more.setAnimation(anm);

				}else
				{
				enter_email = edit_collection_id.getText()
						.toString().replace(" ", "");
				
				invite_final_url = invite_url + app_id + "/"
						+ collection_id + "/" + enter_email;
				invite_loading(invite_final_url);
				}
			}
		});
		edit_collection_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder srch = new AlertDialog.Builder(
						EditCollection.this);
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

		
		
		Intent in = getIntent();

		collection_id = in.getStringExtra("collection_id");

		final_url = url + app_id + "/" + collection_id;

		selectpath = in.getStringExtra("selectpath");

		System.out.println("final url in edit collection = " + final_url);

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					JsonParser jparser = new JsonParser();
					JSONObject jobj = jparser.getJSONfromUrl(final_url);
					message = jobj.getString("Message");
					if (message.equals("true")) {
						JSONArray jarray = jobj.getJSONArray("data");
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject obj = jarray.getJSONObject(i);
							title = obj.getString("collection_name");
							description = obj.getString("collection");
							contriubute_type = obj.getString("contribute_type");

						}
					}
					else
					{
						System.out.println("notthing here");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				try {
					if (message.equals("true")) {
						edit_collection_title.setText(title);
						edit_collection_description.setText(description);
						System.out.println("contribute typeeeee = "
								+ contriubute_type);
						if (contriubute_type.equals("Invite")) {
							System.out.println("---call anyone");

							anyone.setChecked(false);
							invite.setChecked(true);

							edit_collection_invite.setVisibility(View.VISIBLE);
							edit_collection_id.setVisibility(View.VISIBLE);
							add_more.setVisibility(View.VISIBLE);
							

						} else {
							System.out.println("--else-call anyone");

							anyone.setChecked(true);
							invite.setChecked(false);
						}

					}
					else
					{
						System.out.println("message = " +message);
					}
				}

				catch (Exception ex) {
					System.out.println("Exception = " + ex);
				}

			}

		}.execute();

		radio_grp
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup rGroup, int position) {

						radiobutton = (RadioButton) rGroup
								.findViewById(position);
						radio_selected = radiobutton.getText().toString();
						if (radio_selected.equals("Invite Only")) {
							getvalue = "Invite";
							edit_collection_invite.setVisibility(View.VISIBLE);
							edit_collection_id.setVisibility(View.VISIBLE);
							add_more.setVisibility(View.VISIBLE);
							
						} else {
							getvalue = "Anyone";
							edit_collection_invite.setVisibility(View.GONE);
							edit_collection_id.setVisibility(View.GONE);
							add_more.setVisibility(View.GONE);
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.edit_collection_delete:
			new AsyncTask<Void, Void, Void>()
			{

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					
							try
							{
							JsonParser jparser = new JsonParser();
							JSONObject jobj = jparser.getJSONfromUrl(delete_url+app_id+"/"+collection_id);
							System.out.println("delet url = "+ (delete_url+app_id+"/"+collection_id));
							delete_msg = jobj.getString("Message");
							delete_status=jobj.getString("status");
							if (message.equals("true")) {
								System.out.println("status if= "+status);
							}
							else
							{
								System.out.println("status else= "+ status);
							}
							}
							catch (Exception e) {
								// TODO: handle exception
								System.out.println("delete collection exception = " + e);
							    
						
							
						}
					
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
				
					try {
						if (message.equals("true")) {
							Toast.makeText(EditCollection.this, delete_status, Toast.LENGTH_LONG).show();
							in= new Intent(EditCollection.this,Select_Colllection.class);
							startActivity(in);
							finish();
						}
						else
						{
							Toast.makeText(EditCollection.this, delete_status, Toast.LENGTH_LONG).show();
						}
					
					} catch (Exception e) {
						// TODO: handle exception
					   System.out.println("exception delete = " +e );
					}
				}

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
				}
				
			}.execute();
			

			break;

		case R.id.edit_collection_save:

			try {

				
				if (TextUtils.isEmpty(edit_collection_title.getText()
						.toString())) {
					edit_collection_title.setError("Please give a title");
					anm = AnimationUtils.loadAnimation(this, R.anim.shake);
					edit_collection_save.setAnimation(anm);

				} else {
					System.out.println("tiltle length = "
							+ edit_collection_title.getText().toString()
									.length());
					if (!(edit_collection_title.getText().toString().substring(
							0, edit_collection_title.getText().toString()
									.length())).contains(" ")) {
						edit_collection_title
								.setError("Please enter atleast two words");
						anm = AnimationUtils.loadAnimation(this, R.anim.shake);
						edit_collection_save.setAnimation(anm);
					}

					else {
						title = edit_collection_title.getText().toString();
					}

				}

				if (TextUtils.isEmpty(edit_collection_description.getText()
						.toString())) {
					edit_collection_description.setError("Enter some words");
					anm = AnimationUtils.loadAnimation(this, R.anim.shake);
					edit_collection_save.setAnimation(anm);

				} else {

					description = edit_collection_description.getText()
							.toString();

				}
				

				if (title.equals("") || description.equals("")
						|| getvalue.equals("")) {
					Toast.makeText(getApplicationContext(),
							"fill complete information", Toast.LENGTH_SHORT)
							.show();

				} else {
					System.out.println("Image Value = "
							+ edit_collection_image.getText().toString());
					edittitle = URLEncoder.encode(title, "utf-8");
					editdescription = URLEncoder.encode(description, "utf-8");
					editvalue = URLEncoder.encode(getvalue, "utf-8");
					System.out.println("get value =" + getvalue);

					editUrl = edit_url + app_id + "/" + collection_id + "/"
							+ edittitle + "/" + editdescription + "/"
							+ editvalue;
					System.out.println("Final url is=== " + editUrl);

					
					System.out.println("Selected path == " + selectpath);
					editCollection();
					

				}

			} catch (Exception e)

			{
				System.out.println("excepiton == " + e);

			}

			break;
		default:
			break;
		}

	}

	public void invite_loading(final String url) {
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
			
				
					try {
						JsonParser jparser = new JsonParser();
						System.out.println("final url  = " + url);
						JSONObject jobj = jparser.getJSONfromUrl(url);
						msg = jobj.getString("Message");
						status = jobj.getString("status");
						System.out.println("my new message = " + msg);
						if(msg.equals("true"))
						{
						System.out.println("---calll if----");						
						
						} else {
							
							System.out.println("----call else");	
						}

					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("exception in data = " + e);
					}

				
		
								return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				try{
				if(msg.equals("true"))
				{
					edit_collection_id.setText("");
									
				Toast.makeText(EditCollection.this, status,Toast.LENGTH_LONG)
							.show();
				} else {
					
					Toast.makeText(EditCollection.this, status, Toast.LENGTH_LONG)
							.show();

					
				}
				}
				catch(Exception e)
				{
					System.out.println("exception in data post = " + e);
				}
				
			}
				   
		  
		}.execute();
		
			   
	
	
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
				/*
				 * imgString = Base64.encodeToString(getBytesFromBitmap(photo),
				 * Base64.NO_WRAP); try { convert=URLEncoder.encode(imgString,
				 * "utf-8"); } catch (UnsupportedEncodingException e) { // TODO
				 * Auto-generated catch block System.out.println("encode = "+
				 * e); }
				 */
				edit_collection_image.setText("Successfully Uploaded");

			} catch (NullPointerException e) {

			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	private byte[] getBytesFromBitmap(Bitmap photo2) {
		// TODO Auto-generated method stub

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo2.compress(CompressFormat.JPEG, 70, stream);
		return stream.toByteArray();

	}

	public void editCollection() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					JsonParser jparser = new JsonParser();
					System.out.println("final url  = " + editUrl);
					JSONObject jobj = jparser.getJSONfromUrl(editUrl);
					message = jobj.getString("Message");
					System.out.println("my new message = " + message);
					if (message.equals("true")) {
						Toast.makeText(EditCollection.this, "Collection edit",
								Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(EditCollection.this,
								"Collection not edit", Toast.LENGTH_LONG)
								.show();
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("exception in data = " + e);
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (message.equals("true")) {
					Toast.makeText(EditCollection.this, "Collection edit",
							Toast.LENGTH_LONG).show();
					if(selectpath!=null)
					{
						imageUpload(collection_id);
						
					}
					else
					{
						Toast.makeText(EditCollection.this, "Collection edit",
								Toast.LENGTH_LONG).show();
						Intent in = new Intent(EditCollection.this,
								Select_Colllection.class);
						
						startActivity(in);
						overridePendingTransition(0, 0);
						finish();
					
					}
					}
			}

		}.execute();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("Home presed");
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			System.out.println("Home presed  in side");
			finish();
			System.exit(0);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(EditCollection.this,
					Select_Colllection.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
		}
		return false;
	}

	public void imageUpload(final String insert) {
		mRegisterTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				// System.out.println("Final Url = " + Url);
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
					URL url = new URL(
							"http://reportedly.pnf-sites.info/webservices/api1/imageUpload.php?id="
									+ insert);
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
							+ selectpath + "\"" + lineEnd);

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

					System.out.println("exception 1 = " + ioex);

				} catch (NullPointerException e) {
					System.out.println("exception 2 = " + e);
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (resp.equals("true")) {

					System.out.println("Intent Response =" + message + " "
							+ resp);
					Intent in = new Intent(EditCollection.this,
							Select_Colllection.class);
					/*
					 * in.putExtra("title", title); in.putExtra("description",
					 * description); in.putExtra("collection_id", inserted_id);
					 */
					startActivity(in);
					overridePendingTransition(0, 0);
					finish();
				} else {
					Toast.makeText(EditCollection.this,
							"please select valid image", Toast.LENGTH_LONG)
							.show();
				}

			}

		};
		mRegisterTask.execute(null, null, null);
		System.out.println("My Response out=" + resp);
		// return resp;
	}
}
