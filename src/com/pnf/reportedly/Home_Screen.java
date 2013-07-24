package com.pnf.reportedly;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.navdrawer.SimpleSideDrawer;
import com.pnf.reportedly.LazyAdatpter.ViewHolder;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Home_Screen extends Activity implements OnClickListener {
	
	
	public static String home_new_url = "http://reportedly.pnf-sites.info/webservices/api1/guestindex/";
	public static String recent_new_url = "http://reportedly.pnf-sites.info/webservices/api1/guestmostrecent/";
	public static String recommended_new_url = "http://reportedly.pnf-sites.info/webservices/api1/guestrecommend/";

	public static String home_url = "http://reportedly.pnf-sites.info/webservices/api1/";
	public static String recent_url = "http://reportedly.pnf-sites.info/webservices/api1/mostrecent/";
	public static String most_recommended_url = "http://reportedly.pnf-sites.info/webservices/api1/recommend/";
     String status="recent";
     String status1="most";
   //  String status="recent";
	public static String collection_url = "http://reportedly.pnf-sites.info/webservices/api1/collections/";
	ArrayList<HashMap<String, String>> searchResults;
	
		LazyAdatpter adapter;
	private static String APP_ID = "637189502961426";
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String filename = "FileName";
	private SharedPreferences mprefs;
	static String facebookId;
	String accessToken;
	 int current_page = 0;
	 String response;
	Bundle bundle;
	public static boolean exit;
    String hometestUrl,recenttestUrl,mosttestUrl,collectiontestUrl;
	String share_link, my_post;
	public static ListView list;
	Button next_btn, create_btn, map_btn, left_button, homeview_btn,
			current_location_btn, top_recommended_btn, create_location_btn,
			profile_screen_btn, states_btn, profile_settings_btn;
	SimpleSideDrawer slide_me;
	TextView recent_report, collection, most_recommended;
	Intent in;
	int next = 0;
	int next_collection = 0;
	public ImageLoader imageLoader;
	String getUrl = "";
	RelativeLayout rel_left_menu;
	ImageView first_triangle, second_triangle, third_triangle;
	EditText search_box;

	JsonParser parser;
	// public static String
	// user_collection_url="http://reportedly.pnf-sites.info/webservices/api1/usercollections/";

	public String new_city, new_address, new_country, new_home,new_recent,new_recommend;

	public ArrayList<HashMap<String, String>> home_arraylist;
    ArrayList<HashMap<String, String>> recent_arraylist;
	ArrayList<HashMap<String, String>> most_arraylist;
	ArrayList<HashMap<String, String>> collection_arraylist;
	LinearLayout small_triangle;
	String message = null;
	ImageView task_image;
	String total_drafts, collection_id, home_id;
	static Button btnLoadMore;
	ProgressDialog pDialog;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home_screen);
		list = (ListView) findViewById(R.id.home_list);
		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.left_menu);
		
		    myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
		    prefsEditor = myPrefs.edit();
		    app_id= myPrefs.getString("UserId","");
		  // prefsEditor.commit();
		   System.out.println("my getting id ===" +app_id);
		
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		// rel_left_menu=(RelativeLayout)slide_me.findViewById(R.id.relative_left_menu);
		left_button = (Button) findViewById(R.id.slide_btn);
		small_triangle = (LinearLayout) findViewById(R.id.small_triangle);
		first_triangle = (ImageView) findViewById(R.id.first_triangle);
		second_triangle = (ImageView) findViewById(R.id.second_triangle);
		third_triangle = (ImageView) findViewById(R.id.third_triangle);
		search_box = (EditText) findViewById(R.id.searchbox_value);
		homeview_btn = (Button) findViewById(R.id.homeview_btn);
		create_location_btn = (Button) findViewById(R.id.create_location_btn);
		profile_screen_btn = (Button) findViewById(R.id.profile_screen_btn);

		states_btn = (Button) findViewById(R.id.states_btn);
		map_btn = (Button) findViewById(R.id.map_btn);
		profile_settings_btn = (Button) slide_me
				.findViewById(R.id.profile_settings_btn);
		recent_report = (TextView) findViewById(R.id.recent_reports);
		collection = (TextView) findViewById(R.id.collections);
		
		home_arraylist = new ArrayList<HashMap<String, String>>();
		most_arraylist = new ArrayList<HashMap<String, String>>();
		recent_arraylist = new ArrayList<HashMap<String, String>>();
		
		create_btn = (Button) findViewById(R.id.create_btn);
		most_recommended = (TextView) findViewById(R.id.most_recommended);
		recent_report.setOnClickListener(this);
		most_recommended.setOnClickListener(this);
		collection.setOnClickListener(this);
		create_btn.setOnClickListener(this);
		left_button.setOnClickListener(this);
		homeview_btn.setOnClickListener(this);
        search_box.setOnClickListener(this);
		create_location_btn.setOnClickListener(this);
		map_btn.setOnClickListener(this);
		profile_screen_btn.setOnClickListener(this);
		states_btn.setOnClickListener(this);
		profile_settings_btn.setOnClickListener(this);

		/*
		 * Intent in=getIntent(); new_city=in.getStringExtra("city");
		 * System.out.println(" city name = " +new_city);
		 */

		//recent_arraylist.clear();
		///most_arraylist.clear();
		new_home = home_new_url + "Chandigarh" + "/";

		System.out.println("he lloooooo how r u??????????"
				+ app_id);

		if (app_id.equals("")) {
			getUrl("Home_new");

			HomeLoading(new_home + current_page);
			System.out.println("url issss-----------------" + new_home);
		} else {
			getUrl("Home");
			String n_home=home_url + current_page;
			HomeLoading(n_home);
			System.out.println("url issss-----------------else ");
		}
		// list.setOnItemClickListener(this);

		btnLoadMore = new Button(getApplicationContext());
		btnLoadMore.setText("Load more");
		list.addFooterView(btnLoadMore);
		
		
	}

	public void HomeLoading(final String url) {

		new AsyncTask<String, Integer, ArrayList<HashMap<String, String>>>() {
			ProgressDialog progressdialog1;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressdialog1 = new ProgressDialog(Home_Screen.this);
				progressdialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressdialog1.setMessage("Loading...");
				progressdialog1.show();
			}

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(
					String... params) {
				// TODO Auto-generated method stub
				json(url);
   System.out.println("nowwwwwwwww url is"+ url);
				return null;

			}

			@Override
			protected void onPostExecute(
					ArrayList<HashMap<String, String>> result) {
				// TODO Auto-generated method stub

				super.onPostExecute(result);
				progressdialog1.dismiss();
                
				if (message.equals("true")) {
					//int currentPosition = list.getFirstVisiblePosition();
					 adapter = new LazyAdatpter(Home_Screen.this,
							home_arraylist,"home");
					System.out.println("-----call adapter-----"
							+ home_arraylist);

					list.setAdapter(adapter);
					// loadmore.setVisibility(View.INVISIBLE);
					//list.setSelectionFromTop(currentPosition + 1, 0);

					btnLoadMore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// Starting a new async task
							new loadMoreListView().execute();
							System.out.println("----button clicked-----------");
						}
					
					});
				
				}
				
			}
							}.execute();
			
			
			}
	

	public void RecentLoading(final String url) {

		new AsyncTask<String, Integer, ArrayList<HashMap<String, String>>>() {
			ProgressDialog progressdialog1;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressdialog1 = new ProgressDialog(Home_Screen.this);
				progressdialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressdialog1.setMessage("Loading...");
				progressdialog1.show();
			}

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(
					String... params) {
				// TODO Auto-generated method stub

				recent_json(url);
				return null;

			}

			@Override
			protected void onPostExecute(
					ArrayList<HashMap<String, String>> result) {
				

				super.onPostExecute(result);
				progressdialog1.dismiss();

				if (message.equals("true")) {
					
					//int currentPosition = list.getFirstVisiblePosition();
					 adapter = new LazyAdatpter(Home_Screen.this,
							recent_arraylist,"recent_report");
					System.out.println("-----call adapter-----"
							+ recent_arraylist);

					list.setAdapter(adapter);

					//list.setSelectionFromTop(currentPosition + 1, 0);
					btnLoadMore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// Starting a new async task
							new RecentLoad().execute();
							System.out.println("----button clicked-----------");
						}
					
					});
				
				}
				
			}
							}.execute();
							
						
			}
	public void MostLoading(final String url) {

		new AsyncTask<String, Integer, ArrayList<HashMap<String, String>>>() {
			ProgressDialog progressdialog1;

			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();
				progressdialog1 = new ProgressDialog(Home_Screen.this);
				progressdialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressdialog1.setMessage("Loading...");
				progressdialog1.show();
			}

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(
					String... params) {
				// TODO Auto-generated method stub
                  
                  
				most_json(url);
				return null;

			}

			@Override
			protected void onPostExecute(
					ArrayList<HashMap<String, String>> result) {
				// TODO Auto-generated method stub

				super.onPostExecute(result);
				progressdialog1.dismiss();

				if (message.equals("true")) {
					//int currentPosition = list.getFirstVisiblePosition();
					 adapter = new LazyAdatpter(Home_Screen.this,
							most_arraylist,"most_recommended");
					System.out.println("-----call adapter-----"
							+ most_arraylist);

					list.setAdapter(adapter);

					//list.setSelectionFromTop(currentPosition + 1, 0);

					btnLoadMore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// Starting a new async task
							new MostLoad().execute();
							System.out.println("----button clicked-----------");
						}
					
					});
				
				}
			
			}
							}.execute();
			
			
			}
	
	public void CollectionLoading(final String url) {

		new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressdialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressdialog = new ProgressDialog(Home_Screen.this);
				progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressdialog.setMessage("Loading...");
				progressdialog.show();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				collection_json(url);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub

				super.onPostExecute(result);
				progressdialog.dismiss();

				if (message.equals("true")) {
					 adapter = new LazyAdatpter(
							Home_Screen.this, collection_arraylist,"collection");
					System.out.println("-----collection_arraylist adapter-----"
							+ collection_arraylist);
					list.setAdapter(adapter);

					btnLoadMore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// Starting a new async task
							new CollectionLoad().execute();
							System.out.println("----button clicked-----------");
						}
					
					});
				
				}
			
					
			}
							}.execute();
			
			
			}
	


	private class loadMoreListView extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					Home_Screen.this);
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
					if (app_id.equals("")) {
						hometestUrl=new_home+current_page;
					}
					else
					{
					// Next page request
					hometestUrl = home_url + current_page;
                    System.out.println("---texturl is----- "+hometestUrl);
					}
					 // getting XML
					
				json(hometestUrl);

				
						
					// get listview current position - used to maintain scroll position
				
				if (message.equals("true")) {
					int currentPosition = list.getFirstVisiblePosition();
					
					// Appending new data to menuItems ArrayList
					 adapter = new LazyAdatpter(Home_Screen.this,
							home_arraylist,"home");
					System.out.println("-----call adapter-----"
							+ home_arraylist);

					list.setAdapter(adapter);
					// Setting new scroll position
					//select_list.setSelectionFromTop(currentPosition + 1, 0);
				}
		
				}
			});

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
	}
	

	private class RecentLoad extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					Home_Screen.this);
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
					new_recent=recent_new_url+"Chandigarh"+"/";
					
					if (app_id.equals("")) {
						recenttestUrl=new_recent+current_page;
					}
					else
					{
					// Next page request
						recenttestUrl = recent_url + current_page;
                    System.out.println("---recenttestUrl is----- "+recenttestUrl);
					}
					 // getting XML
					
				recent_json(recenttestUrl);

				
						
					// get listview current position - used to maintain scroll position
				
				if (message.equals("true")) {
					int currentPosition = list.getFirstVisiblePosition();
					
					// Appending new data to menuItems ArrayList
					 adapter = new LazyAdatpter(Home_Screen.this,
							recent_arraylist,"recent_report");
					System.out.println("-----call adapter-----"
							+ recent_arraylist);

					list.setAdapter(adapter);
					// Setting new scroll position
					//select_list.setSelectionFromTop(currentPosition + 1, 0);
				}
		
				}
			});

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
	}
	
	private class MostLoad extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					Home_Screen.this);
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
					new_recommend=recommended_new_url+"Chandigarh"+"/";
					
					if (app_id.equals("")) {
						mosttestUrl=new_recommend+current_page;
					}
					else
					{
					// Next page request
						mosttestUrl = most_recommended_url + current_page;
                    System.out.println("---mosttestUrl is----- "+mosttestUrl);
					}
					 // getting XML
					
				most_json(mosttestUrl);

				
						
					// get listview current position - used to maintain scroll position
				
				if (message.equals("true")) {
					int currentPosition = list.getFirstVisiblePosition();
					
					// Appending new data to menuItems ArrayList
					 adapter = new LazyAdatpter(Home_Screen.this,
							most_arraylist,"most_recommended");
					System.out.println("-----call adapter-----"
							+ most_arraylist);

					list.setAdapter(adapter);
					// Setting new scroll position
					//select_list.setSelectionFromTop(currentPosition + 1, 0);
				}
		
				}
			});

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialognew_recommend
			pDialog.dismiss();
		}
	}
	
	private class CollectionLoad extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					Home_Screen.this);
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
					
				
						collectiontestUrl = "http://reportedly.pnf-sites.info/webservices/api1/collections/" + current_page;
                    System.out.println("---collectiontestUrl is----- "+collectiontestUrl);
					
					 // getting XML
					
				collection_json(collectiontestUrl);

				
						
					// get listview current position - used to maintain scroll position
				
				int currentPosition = list.getFirstVisiblePosition();
				
				// Appending new data to menuItems ArrayList
				 adapter = new LazyAdatpter(Home_Screen.this,collection_arraylist,"collection");
				list.setAdapter(adapter);
				list.setSelectionFromTop(currentPosition + 1, 0);
				}
		
				
			});

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
	}
	
	public void recent()
	{
		
		
		recent_arraylist = new ArrayList<HashMap<String, String>>();
		
		 
		 searchResults = new ArrayList<HashMap<String, String>>(recent_arraylist);
			search_box.addTextChangedListener(new TextWatcher() {

	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	              //get the text in the EditText
	             String  searchString=search_box.getText().toString();
	              int textLength=searchString.length();
	              searchResults.clear();
          
	              for(int i=0;i<recent_arraylist.size();i++)
	              {
	             String search=recent_arraylist.get(i).get("title").toString();
	             System.out.println("player name "+search);
	             if(textLength<=search.length()){
	             //compare the String in EditText with Names in the ArrayList
	               if(searchString.equalsIgnoreCase(search.substring(0,textLength))){
	               searchResults.add(recent_arraylist.get(i));
	               System.out.println("the array list is "+recent_arraylist.get(i));
	                adapter = new LazyAdatpter(Home_Screen.this, searchResults,"recent_report");  

	                list.setAdapter(adapter);
	               }

	                 }


	              }
            
           /*
	              if(searchResults.isEmpty()){
	                  Toast toast= Toast.makeText(getApplicationContext(), 
	                             "No Items Matched", Toast.LENGTH_SHORT);  
	                             toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	                             toast.show();
	              }*/
	              adapter.notifyDataSetChanged();
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
	
	
	
	public void collection()
	{
		
		collection_arraylist = new ArrayList<HashMap<String, String>>();
		searchResults = new ArrayList<HashMap<String, String>>(collection_arraylist);
			search_box.addTextChangedListener(new TextWatcher() {

	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	              //get the text in the EditText
	             String  searchString=search_box.getText().toString();
	              int textLength=searchString.length();
	              searchResults.clear();
          
	              for(int i=0;i<collection_arraylist.size();i++)
	              {
	             String search=collection_arraylist.get(i).get("collection_name").toString();
	             System.out.println("player name "+search);
	             if(textLength<=search.length()){
	             //compare the String in EditText with Names in the ArrayList
	               if(searchString.equalsIgnoreCase(search.substring(0,textLength))){
	            	   searchResults.add(collection_arraylist.get(i));
	               System.out.println("the array list is "+collection_arraylist.get(i));
	                adapter = new LazyAdatpter(Home_Screen.this, searchResults,"collection");  

	                list.setAdapter(adapter);
	               }

	                 }


	              }
            
           
	             /* if(searchResults.isEmpty()){
	                  Toast toast= Toast.makeText(getApplicationContext(), 
	                             "No Items Matched", Toast.LENGTH_SHORT);  
	                             toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	                             toast.show();
	              }*/
	              adapter.notifyDataSetChanged();
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
	public void most()
	{

	
		most_arraylist = new ArrayList<HashMap<String, String>>();
		searchResults = new ArrayList<HashMap<String, String>>(most_arraylist);
			search_box.addTextChangedListener(new TextWatcher() {

	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	              //get the text in the EditText
	             String  searchString=search_box.getText().toString();
	              int textLength=searchString.length();
	              searchResults.clear();
          
	              for(int i=0;i<most_arraylist.size();i++)
	              {
	             String search=most_arraylist.get(i).get("title").toString();
	             System.out.println("player name "+search);
	             if(textLength<=search.length()){
	             //compare the String in EditText with Names in the ArrayList
	               if(searchString.equalsIgnoreCase(search.substring(0,textLength))){
	            	   searchResults.add(most_arraylist.get(i));
	               System.out.println("the array list is "+most_arraylist.get(i));
	                adapter = new LazyAdatpter(Home_Screen.this, searchResults,"most_recommended");  

	                list.setAdapter(adapter);
	               }

	                 }


	              }
            
           
	           /*   if(searchResults.isEmpty()){
	                  Toast toast= Toast.makeText(getApplicationContext(), 
	                             "No Items Matched", Toast.LENGTH_SHORT);  
	                             toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	                             toast.show();
	              }*/
	              adapter.notifyDataSetChanged();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		final AlertDialog alertdialog;
		switch (v.getId()) {
		case R.id.recent_reports:
			
			current_page = 0;
			
			
			
			recent_report.setBackgroundResource(R.drawable.selected);
			recent_report.setText("Recent Report");
			recent_report.setTextColor(Color.BLACK);

			collection.setBackgroundResource(R.drawable.bg);
			collection.setText("Collection");
			collection.setTextColor(Color.GRAY);
			collection.setGravity(Gravity.CENTER);

			most_recommended.setBackgroundResource(R.drawable.bg);
			most_recommended.setText("Most Recommended");
			most_recommended.setTextColor(Color.GRAY);
			most_recommended.setGravity(Gravity.CENTER);

			first_triangle.setBackgroundResource(R.drawable.icon);
			second_triangle.setBackgroundResource(R.drawable.icon_invisible);
			third_triangle.setBackgroundResource(R.drawable.icon_invisible);

			small_triangle.setVisibility(View.VISIBLE);
			search_box.setVisibility(View.VISIBLE);
			search_box.setFocusable(true);
     
			new_recent=recent_new_url+"Chandigarh"+"/";
			
			if (app_id.equals("")) {
                getUrl("recent_new");
                
                System.out.println("new Recent url lll = " + new_recent);
                RecentLoading(new_recent+current_page);
                
                
				
			} else {
				
				getUrl("recent");

				System.out.println("recent url lll = " + recent_url + next);

				RecentLoading(recent_url + current_page);
			}
			System.out.println("-------------helllooooooo buuuuddddyyyyyyy-----------");
			recent();
			break;
		case R.id.collections:
			current_page = 0;
			
			
			
			
			collection.setBackgroundResource(R.drawable.selected);
			collection.setText("Collections");
			collection.setTextColor(Color.BLACK);

			recent_report.setBackgroundResource(R.drawable.bg);
			recent_report.setText("Recent Report");
			recent_report.setTextColor(Color.GRAY);
			recent_report.setGravity(Gravity.CENTER);

			most_recommended.setBackgroundResource(R.drawable.bg);
			most_recommended.setText("Most Recommended");
			most_recommended.setTextColor(Color.GRAY);
			most_recommended.setGravity(Gravity.CENTER);

			second_triangle.setBackgroundResource(R.drawable.icon);
			first_triangle.setBackgroundResource(R.drawable.icon_invisible);
			third_triangle.setBackgroundResource(R.drawable.icon_invisible);
			search_box.setVisibility(View.VISIBLE);
			small_triangle.setVisibility(View.VISIBLE);
			search_box.setFocusable(true);

			getUrl("collection");

			String id = app_id;
			System.out.println("idddddd valuuuue = " + id);

			System.out.println("collection url = " + collection_url
					+ current_page);

			CollectionLoading(collection_url + current_page);
			System.out.println("app_id ifff= "
					+ app_id);
			System.out.println("-------------helllooooooo buuuuddddyyyyyyy-----------");

			collection();

			break;
		case R.id.most_recommended:
		
			current_page = 0;
			
			
			
			most_recommended.setBackgroundResource(R.drawable.selected);
			most_recommended.setText("Most Recommended");
			most_recommended.setTextColor(Color.BLACK);

			recent_report.setBackgroundResource(R.drawable.bg);
			recent_report.setText("Recent Report");
			recent_report.setTextColor(Color.GRAY);
			recent_report.setGravity(Gravity.CENTER);

			collection.setBackgroundResource(R.drawable.bg);
			collection.setText("Collections");
			collection.setTextColor(Color.GRAY);
			collection.setGravity(Gravity.CENTER);

			third_triangle.setBackgroundResource(R.drawable.icon);

			second_triangle.setBackgroundResource(R.drawable.icon_invisible);
			first_triangle.setBackgroundResource(R.drawable.icon_invisible);
			search_box.setVisibility(View.VISIBLE);
			small_triangle.setVisibility(View.VISIBLE);
			search_box.setFocusable(true);
			new_recommend=recommended_new_url+"Chandigarh"+"/";
			
			System.out.println("reporttttt = " +app_id);
			if(app_id.equals(""))
			{
				getUrl("recommended_new");
				System.out.println("most new url lll = " + new_recommend);
				MostLoading(new_recommend+current_page);
			}
			else
			{
				getUrl("most_recommended");
				System.out.println("most url lll = " + most_recommended_url);
				MostLoading(most_recommended_url + current_page);
					
			}
			System.out.println("-------------helllooooooo buuuuddddyyyyyyy-----------");

			most();
			break;
		case R.id.create_btn:
			in = new Intent(this, Select_Colllection.class);
			startActivity(in);
			finish();
			break;
		case R.id.slide_btn:
			slide_me.toggleLeftDrawer();
			/*
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
			in = new Intent(this, Create_Collection_View.class);
			startActivity(in);
			finish();
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
								Intent in = new Intent(Home_Screen.this,
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
			if (app_id.equals("")) {
				alertdialog = new AlertDialog.Builder(this).create();
				alertdialog.setTitle("Reportedly");
				alertdialog.setMessage("You have to login first !!");
				alertdialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent in = new Intent(Home_Screen.this,
										Login_Activity.class);
								startActivity(in);
								finish();

							}
						});
				alertdialog.show();
			} else {
				in = new Intent(this, States_View.class);
				startActivity(in);
				finish();
			}
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
								Intent in = new Intent(Home_Screen.this,
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

			break;
	
		default:
			break;
		}
	}

	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
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
	    	finish();
		       System.exit(0);
	    }
		return false;
	}
	public void json(String url) {
		System.out.println("--calll async");
		home_arraylist = new ArrayList<HashMap<String, String>>();
		most_arraylist = new ArrayList<HashMap<String, String>>();
		recent_arraylist = new ArrayList<HashMap<String, String>>();
		JsonParser jparser = new JsonParser();

		try {
			home_arraylist.clear();
			most_arraylist.clear();
			recent_arraylist.clear();
			
		
			JSONObject jobj = jparser.getJSONfromUrl(url);
			System.out.println("----jobj----" + jobj);
			message = jobj.getString("Message");
			if (message.equals("true")) {

				JSONArray jsonarray = jobj.getJSONArray("data");

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jdata = jsonarray.getJSONObject(i);

					String image = jdata.getString("image");
					String title = jdata.getString("title");
					String post = jdata.getString("post");
					String link = jdata.getString("link");
					home_id = jdata.getString("id");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", message);
					map.put("id", home_id);
					map.put("image", image);
					map.put("link", link);
					map.put("title", title);

					map.put("post", post);
					home_arraylist.add(map);
				}

			} else {
				Toast.makeText(Home_Screen.this, "NO More Record Found",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void recent_json(String url) {
		System.out.println("--calll async");
		home_arraylist = new ArrayList<HashMap<String, String>>();
		most_arraylist = new ArrayList<HashMap<String, String>>();
		recent_arraylist = new ArrayList<HashMap<String, String>>();
		JsonParser jparser = new JsonParser();

		try {
			
			recent_arraylist.clear();
			home_arraylist.clear();
			most_arraylist.clear();
			
			JSONObject jobj = jparser.getJSONfromUrl(url);
			System.out.println("----jobj----" + jobj);
			message = jobj.getString("Message");
			if (message.equals("true")) {

				JSONArray jsonarray = jobj.getJSONArray("data");

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jdata = jsonarray.getJSONObject(i);

					String image = jdata.getString("image");
					String title = jdata.getString("title");
					String post = jdata.getString("post");
					home_id = jdata.getString("id");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", message);
					map.put("id", home_id);
					map.put("image", image);
					map.put("title", title);

					map.put("post", post);
					recent_arraylist.add(map);
				}

			} else {
				Toast.makeText(Home_Screen.this, "NO More Record Found",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void most_json(String url) {
		System.out.println("--calll async");
		home_arraylist = new ArrayList<HashMap<String, String>>();
		most_arraylist = new ArrayList<HashMap<String, String>>();
		recent_arraylist = new ArrayList<HashMap<String, String>>();
		JsonParser jparser = new JsonParser();
		

		try {
			
			most_arraylist.clear();
			home_arraylist.clear();
			recent_arraylist.clear();
			
			
			
			JSONObject jobj = jparser.getJSONfromUrl(url);
			System.out.println("----jobj----" + jobj);
			message = jobj.getString("Message");
			if (message.equals("true")) {

				JSONArray jsonarray = jobj.getJSONArray("data");

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jdata = jsonarray.getJSONObject(i);

					String image = jdata.getString("image");
					String title = jdata.getString("title");
					String post = jdata.getString("post");
					home_id = jdata.getString("id");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", message);
					map.put("id", home_id);
					map.put("image", image);
					map.put("title", title);

					map.put("post", post);
					most_arraylist.add(map);
				}

			} else {
				Toast.makeText(Home_Screen.this, "NO More Record Found",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

	}
	public void collection_json(String url)
	{
		System.out.println("--calll async");
		collection_arraylist = new ArrayList<HashMap<String, String>>();
		JsonParser jparser = new JsonParser();

		try {
			System.out.println("---collection---" + url);
			JSONObject jobj = jparser.getJSONfromUrl(url);
			System.out.println("----jobj----" + jobj);
			message = jobj.getString("Message");
			System.out.println("Messageeeee = " + message);
			if (message.equals("true")) {

				JSONArray jsonarray = jobj.getJSONArray("data");

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jdata = jsonarray.getJSONObject(i);
					collection_id = jdata.getString("id");
					total_drafts = jdata.getString("Total_Drafts");
					String collection_name = jdata
							.getString("collection_name");
					String image = jdata.getString("image");
					String User = jdata.getString("User");
					String contribute_type=jdata.getString("contribute_type");
					String author=jdata.getString("author");
					

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", message);
					map.put("total_drafts", total_drafts);
					map.put("image", image);
					map.put("author", author);
					map.put("collection_name", collection_name);
					map.put("id", collection_id);
					map.put("contribute_type", contribute_type);
					map.put("User", User);
					collection_arraylist.add(map);
					System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"
							+ collection_arraylist);

				}
			} else {
				Toast.makeText(Home_Screen.this, "NO Record Found",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		TextView task_name;
		TextView myname;
		ImageView task_image;
		TextView collection_post_no;
		TextView collection_post;
		ImageView facebook_img;
		ImageView twitter_img;
		ImageView recommend_it;
		
		
		TextView most_task_name;
		TextView most_myname;
		ImageView most_task_image;
		TextView most_collection_post_no;
		TextView most_collection_post;
		ImageView most_facebook_img;
		ImageView most_twitter_img;
		ImageView most_recommend_it;
		
		
		TextView recent_task_name;
		TextView recent_myname;
		ImageView recent_task_image;
		TextView recent_collection_post_no;
		TextView recent_collection_post;
		ImageView recent_facebook_img;
		ImageView recent_twitter_img;
		ImageView recent_recommend_it;

	}

	public String getUrl(String url) {
		if (url.equals("recent")) {
			getUrl = recent_url;
		}
		if (url.equals("recent_new")) {
             getUrl=new_recent;
		}
		if (url.equals("most_recommended")) {
			getUrl = most_recommended_url;
		}
		if (url.equals("recommended_new")) {
            getUrl=new_recommend;
		}
		if (url.equals("collection")) {
			getUrl = collection_url;
		}
		if (url.equals("Home")) {
			getUrl = home_url;
		}
		if (url.equals("Home_new")) {
			getUrl = new_home;
		}

		return getUrl;
	}

}
