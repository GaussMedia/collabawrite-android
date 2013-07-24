package com.pnf.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import com.navdrawer.SimpleSideDrawer;
import com.pnf.reportedly.Create_Story;
import com.pnf.reportedly.ImageLoader;

import com.pnf.reportedly.Home_Screen.ViewHolder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Select_Colllection extends Activity  implements OnClickListener {
	ListView select_list;
	Button select_slide_btn;
	SimpleSideDrawer slide_me;
	Intent in;
	// All variables
	public ImageLoader imageLoader;
	JsonParser parser = new JsonParser();;
	   ArrayList<HashMap<String, String>> searchResults;
	   LazyAdatpter adapter;
	ProgressDialog pDialog;
	SharedPreferences myPrefs;
	EditText search_box;
	SharedPreferences.Editor prefsEditor;
	String app_id,message,Selection_id,total_drafts,testUrl;
	String url="http://reportedly.pnf-sites.info/webservices/api1/collections/";
	static ArrayList<HashMap<String, String>> select_arraylist;
	static Button loadmore;
	Button create_btn, left_button, homeview_btn,current_location_btn,top_recommended_btn,create_location_btn,add_btn,profile_screen_btn,states_btn,profile_settings_btn,map_btn;

	String searchval;

	 int current_page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    	setContentView(R.layout.select_collection);

		select_list = (ListView) findViewById(R.id.select_list);

	
	    	myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
		    prefsEditor = myPrefs.edit();
		  app_id= myPrefs.getString("UserId","");
		   prefsEditor.commit();
		   System.out.println("my getting id ===" +app_id);
	    	slide_me = new SimpleSideDrawer(this);
			slide_me.setLeftBehindContentView(R.layout.left_menu);
			
	  
	    	select_arraylist=new ArrayList<HashMap<String,String>>();
	    	select_slide_btn=(Button)findViewById(R.id.select_slide_btn);
	    	homeview_btn = (Button) findViewById(R.id.homeview_btn);
	    	add_btn=(Button)findViewById(R.id.add_btn);
	    	search_box = (EditText) findViewById(R.id.searchbox_value);
	 
			
	    	map_btn = (Button) findViewById(R.id.map_btn);
			create_location_btn = (Button) findViewById(R.id.create_location_btn);
			profile_screen_btn = (Button) findViewById(R.id.profile_screen_btn);
			states_btn = (Button) findViewById(R.id.states_btn);
			profile_settings_btn = (Button) findViewById(R.id.profile_settings_btn);
			add_btn.setOnClickListener(this);
			states_btn.setOnClickListener(this);
			homeview_btn.setOnClickListener(this);
			select_slide_btn.setOnClickListener(this);
			profile_screen_btn.setOnClickListener(this);
			profile_settings_btn.setOnClickListener(this);
			create_location_btn.setOnClickListener(this);
			map_btn.setOnClickListener(this);
			final Button btnLoadMore = new Button(this);
			btnLoadMore.setText("Load More");

			// Adding Load More button to lisview at bottom
			select_list.addFooterView(btnLoadMore);
			testUrl=url+current_page;
		System.out.println("----texturl is----- "+testUrl);
	// getting XML
		new AsyncTask<String, Integer, ArrayList<HashMap<String, String>>>() {
			ProgressDialog progressdialog1;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressdialog1 = new ProgressDialog(Select_Colllection.this);
				progressdialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressdialog1.setMessage("Loading...");
				progressdialog1.show();
			}

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(
					String... params) {
				// TODO Auto-generated method stub


				try{
					 JSONObject jobj = parser.getJSONfromUrl(testUrl); 
				 message=jobj.getString("Message");
			
				if(message.equals("true"))
				{
					JSONArray jsonarray=jobj.getJSONArray("data");
					
				


					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject jdata = jsonarray.getJSONObject(i);
						Selection_id = jdata.getString("id");
						total_drafts = jdata.getString("Total_Drafts");
						String Selection_name = jdata
								.getString("collection_name");
						String image = jdata.getString("image");
						String User = jdata.getString("User");
		               String contribute_type=jdata.getString("contribute_type");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("message", message);
						map.put("total_drafts", total_drafts);
						map.put("image", image);
						map.put("contribute_type", contribute_type);
						map.put("Selection_name", Selection_name);
						map.put("id", Selection_id);
						map.put("User", User);
						select_arraylist.add(map);
						System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"
								+ select_arraylist);
				}
				}
				else
				{
					System.out.println("------message is falls------------");
				}
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				return null;

			}

			@Override
			protected void onPostExecute(
					ArrayList<HashMap<String, String>> result) {
				

			

			
					
					//int currentPosition = list.getFirstVisiblePosition();
				//
						 adapter = new LazyAdatpter(
								Select_Colllection.this, select_arraylist,"select_collection");
							System.out.println("--select_arraylist-- "+select_arraylist);

							select_list.setAdapter(adapter);


					//list.setSelectionFromTop(currentPosition + 1, 0);
		
				
					progressdialog1.dismiss();
				
				
				}
							}.execute();
			
		

	
		// LoadMore button
	
		
		// Getting adapter	

		/**
		 * Listening to Load More button click event
		 * */
							btnLoadMore.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {	
									
									new AsyncTask<String, Integer, ArrayList<HashMap<String, String>>>() {
										ProgressDialog progressdialog1;

										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method stub
											super.onPreExecute();
											progressdialog1 = new ProgressDialog(Select_Colllection.this);
											progressdialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											progressdialog1.setMessage("Loading...");
											progressdialog1.show();
										}

										@Override
										protected ArrayList<HashMap<String, String>> doInBackground(
												String... params) {
											// TODO Auto-generated method stub

											current_page += 1;
											
											// Next page request
											testUrl = "http://reportedly.pnf-sites.info/webservices/api1/collections/" + current_page;
						                    System.out.println("---texturl is----- "+testUrl);
										
											 // getting XML
											
											try{
												JSONObject jobj = parser.getJSONfromUrl(testUrl); 
												 message=jobj.getString("Message");
												if(message.equals("true"))
												{
													JSONArray jsonarray=jobj.getJSONArray("data");
													
												


													for (int i = 0; i < jsonarray.length(); i++) {
														JSONObject jdata = jsonarray.getJSONObject(i);
														Selection_id = jdata.getString("id");
														total_drafts = jdata.getString("Total_Drafts");
														String Selection_name = jdata
																.getString("collection_name");
														String image = jdata.getString("image");
														String User = jdata.getString("User");
														String contribute_type=jdata.getString("contribute_type");

														HashMap<String, String> map = new HashMap<String, String>();
														map.put("message", message);
														map.put("total_drafts", total_drafts);
														map.put("image", image);
														map.put("contribute_type", contribute_type);
														map.put("Selection_name", Selection_name);
														map.put("id", Selection_id);
														map.put("User", User);
														select_arraylist.add(map);
														System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"
																+ select_arraylist);
												}
												}
												else {
													
													System.out.println("------message is falls------------");
													Toast.makeText(Select_Colllection.this, "NO More Record Found",
														Toast.LENGTH_LONG).show();
												}
												}
												catch (Exception e) {
													// TODO: handle exception
												}
											

											return null;

										}

										@Override
										protected void onPostExecute(
												ArrayList<HashMap<String, String>> result) {
											

										

										
												
												//int currentPosition = list.getFirstVisiblePosition();
											//
											if(message.equals("true"))
											{
											int currentPosition = select_list.getFirstVisiblePosition();
											
											// Appending new data to menuItems ArrayList
											 adapter = new LazyAdatpter(Select_Colllection.this,select_arraylist,"select_collection");
											select_list.setAdapter(adapter);
											


												//list.setSelectionFromTop(currentPosition + 1, 0);
									
											
												progressdialog1.dismiss();
											}
											else {
												
											
												Toast.makeText(Select_Colllection.this, "NO More Record Found",
													Toast.LENGTH_LONG).show();
												progressdialog1.dismiss();
											}
											}
														}.execute();
										
											
									
									// Starting a new async task
									//new loadMoreListView().execute();
									System.out.println("------------------btn success-------");
									
								}
							});
		 searchResults = new ArrayList<HashMap<String, String>>(select_arraylist);
		search_box.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
              //get the text in the EditText
             String  searchString=search_box.getText().toString();
              int textLength=searchString.length();
              searchResults.clear();

              for(int i=0;i<select_arraylist.size();i++)
              {
             String search=select_arraylist.get(i).get("Selection_name").toString();
             System.out.println("player name "+search);
             if(textLength<=search.length()){
             //compare the String in EditText with Names in the ArrayList
               if(searchString.equalsIgnoreCase(search.substring(0,textLength))){
               searchResults.add(select_arraylist.get(i));
               System.out.println("the array list is "+select_arraylist.get(i));
                adapter = new LazyAdatpter(Select_Colllection.this, searchResults,"select_collection");  

              select_list.setAdapter(adapter);
               }

                 }


              }
              if(searchResults.isEmpty()){
                  Toast toast= Toast.makeText(getApplicationContext(), 
                             "No Items Matched", Toast.LENGTH_SHORT);  
                             toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                             toast.show();
              }
             // adapter.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            System.out.println("before changed");

              }

              public void afterTextChanged(Editable s) {


                                          System.out.println("after changed");
              }
             });
		
	}
	
/*	private class loadMoreListView extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					Select_Colllection.this);
			pDialog.setMessage("Please wait..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			runOnUiThread(new Runnable() {
				public void run() {
					// increment current page
					current_page += 1;
					
					// Next page request
					testUrl = "http://reportedly.pnf-sites.info/webservices/api1/collections/" + current_page;
                    System.out.println("---texturl is----- "+testUrl);
				
					 // getting XML
					
					try{
						JSONObject jobj = parser.getJSONfromUrl(testUrl); 
						String message=jobj.getString("Message");
						if(message.equals("true"))
						{
							JSONArray jsonarray=jobj.getJSONArray("data");
							
						


							for (int i = 0; i < jsonarray.length(); i++) {
								JSONObject jdata = jsonarray.getJSONObject(i);
								Selection_id = jdata.getString("id");
								total_drafts = jdata.getString("Total_Drafts");
								String Selection_name = jdata
										.getString("collection_name");
								String image = jdata.getString("image");
								String User = jdata.getString("User");
								String contribute_type=jdata.getString("contribute_type");

								HashMap<String, String> map = new HashMap<String, String>();
								map.put("message", message);
								map.put("total_drafts", total_drafts);
								map.put("image", image);
								map.put("contribute_type", contribute_type);
								map.put("Selection_name", Selection_name);
								map.put("id", Selection_id);
								map.put("User", User);
								select_arraylist.add(map);
								System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"
										+ select_arraylist);
						}
						}
						else {
							Toast.makeText(Select_Colllection.this, "NO More Record Found",
								Toast.LENGTH_LONG).show();
						}
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					

				
						
					// get listview current position - used to maintain scroll position
					int currentPosition = select_list.getFirstVisiblePosition();
					
					// Appending new data to menuItems ArrayList
					 adapter = new LazyAdatpter(Select_Colllection.this,select_arraylist,"select_collection");
					select_list.setAdapter(adapter);
					
					// Setting new scroll position
					//select_list.setSelectionFromTop(currentPosition + 1, 0);

				}
			});
			
			
			

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
		
		
		
	}
	
	*/
	
	@Override
	public void onClick(View v) {
		AlertDialog alertdialog;
		// TODO Auto-generated method stub
		switch (v.getId()) {
	case R.id.add_btn:
		//System.out.println("reportdly===== id  "
				//+ app_id);
		if (app_id.equals("")) {
			alertdialog = new AlertDialog.Builder(this).create();
			alertdialog.setTitle("Reportedly");
			alertdialog.setMessage("You have to login first !!");
			alertdialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Intent in = new Intent(Select_Colllection.this,
									Login_Activity.class);
							startActivity(in);
							finish();

						}
					});
			alertdialog.show();
		} else {
			in = new Intent(this, Create_Collection_View.class);
			startActivity(in);
			finish();
		}
		break;
	case R.id.select_slide_btn:
		slide_me.toggleLeftDrawer();
		/*					int no_of_post = Integer.parseInt(arraylist.get(position).get(
		 * in = new Intent(this, Left_Menu.class); startActivity(in);
		 */
		break;
	case R.id.homeview_btn:

		slide_me.toggleLeftDrawer();
		in = new Intent(this, Home_Screen.class);
		startActivity(in);
		finish();
		break;

	case R.id.create_location_btn:

		slide_me.toggleLeftDrawer();
		System.out.println("reportdly===== id  "
				+ app_id);
		if (app_id.equals("")) {
			alertdialog = new AlertDialog.Builder(this).create();
			alertdialog.setTitle("Reportedly");
			alertdialog.setMessage("You have to login first !!");
			alertdialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Intent in = new Intent(Select_Colllection.this,
									Login_Activity.class);
							startActivity(in);
							finish();

						}
					});
			alertdialog.show();
		} else {
			in = new Intent(this, Create_Collection_View.class);
			startActivity(in);
			finish();
		}
		break;
	case R.id.profile_screen_btn:
		slide_me.toggleLeftDrawer();
		System.out.println("reportdly===== id  "
				+ app_id);
		if (app_id.equals("")) {
			alertdialog = new AlertDialog.Builder(this).create();
			alertdialog.setTitle("Reportedly");
			alertdialog.setMessage("You have to login first !!");
			alertdialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Intent in = new Intent(Select_Colllection.this,
									Login_Activity.class);
							startActivity(in);
							finish();

						}
					});
			alertdialog.show();
		} else {
			in = new Intent(this, Profile_Screen.class);
			startActivity(in);
			finish();
		}
		break;
	case R.id.states_btn:
		slide_me.toggleLeftDrawer();
		in = new Intent(this, States_View.class);
		startActivity(in);
		finish();
		break;
	case R.id.profile_settings_btn:
		slide_me.toggleLeftDrawer();
		System.out.println("Reportedly Id = "+ app_id);
		if (app_id.equals("")) {
			alertdialog = new AlertDialog.Builder(this).create();
			alertdialog.setTitle("Reportedly");
			alertdialog.setMessage("You have to login first !!");
			alertdialog.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Intent in = new Intent(Select_Colllection.this,
									Login_Activity.class);
							startActivity(in);
							finish();

						}
					});
			alertdialog.show();
		} else {
			in = new Intent(this, Profile_Settings.class);
			startActivity(in);
			finish();
		}
		break;

	case R.id.map_btn:
		Intent in = new Intent(this, Map_View.class);
		startActivity(in);
		finish();
		break;

	default:
		break;
	}
		
	}
	
	
	static class ViewHolder {
		TextView task_name;
		TextView myname;
		ImageView task_image;
		TextView Select_post_no;
		TextView Select_post;

		
		


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
	    	Intent i = new Intent(Select_Colllection.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
	
	
}