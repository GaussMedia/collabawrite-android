package com.example.reportedly;

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

	public static String collection_url = "http://reportedly.pnf-sites.info/webservices/api1/collections/";
	
	
	private static String APP_ID = "637189502961426";
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String filename = "FileName";
	private SharedPreferences mprefs;
	static String facebookId;
	String accessToken;
	
	String response;
	Bundle bundle;
	public static boolean exit;

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
	static Button loadmore;
	
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

			HomeLoading(new_home + next);
			System.out.println("url issss-----------------" + new_home);
		} else {
			getUrl("Home");
			HomeLoading(home_url + next);
			System.out.println("url issss-----------------else ");
		}
		// list.setOnItemClickListener(this);

		loadmore = new Button(getApplicationContext());
		loadmore.setText("Load more");
		list.addFooterView(loadmore);
		

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
					HomeAdapter adapter = new HomeAdapter(Home_Screen.this,
							home_arraylist);
					System.out.println("-----call adapter-----"
							+ home_arraylist);

					list.setAdapter(adapter);
					// loadmore.setVisibility(View.INVISIBLE);
					//list.setSelectionFromTop(currentPosition + 1, 0);

					loadmore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							next += 1;
							HomeLoading(getUrl + next);

						}
					});

				} else 
				{
					
						  
	                       Toast.makeText(Home_Screen.this, "NO More Record Found",
								Toast.LENGTH_LONG).show();
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
				// TODO Auto-generated method stub

				super.onPostExecute(result);
				progressdialog1.dismiss();

				if (message.equals("true")) {
					//int currentPosition = list.getFirstVisiblePosition();
					LazyAdatpter adapter = new LazyAdatpter(Home_Screen.this,
							recent_arraylist,"recent_report");
					System.out.println("-----call adapter-----"
							+ recent_arraylist);

					list.setAdapter(adapter);

					//list.setSelectionFromTop(currentPosition + 1, 0);

					loadmore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							next += 1;
							RecentLoading(getUrl + next);

						}
					});

				}
				else {
                   
                    Toast.makeText(Home_Screen.this, "NO More Record Found",
							Toast.LENGTH_LONG).show();
				}
				

			}
		}.execute();
	}
	public void MostLoading(final String url) {

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
					LazyAdatpter adapter = new LazyAdatpter(Home_Screen.this,
							most_arraylist,"most_recommended");
					System.out.println("-----call adapter-----"
							+ most_arraylist);

					list.setAdapter(adapter);

					//list.setSelectionFromTop(currentPosition + 1, 0);

					loadmore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							next += 1;
							MostLoading(getUrl + next);

						}
					});

				}else
				{
					
	                
					Toast.makeText(Home_Screen.this, "No Record Found", Toast.LENGTH_LONG).show();
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
					LazyAdatpter adapter = new LazyAdatpter(
							Home_Screen.this, collection_arraylist,"collection");
					System.out.println("-----collection_arraylist adapter-----"
							+ collection_arraylist);
					list.setAdapter(adapter);

					loadmore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							System.out.println("collection url out=  " + getUrl
									+ next_collection);

							next_collection += 1;

							CollectionLoading(getUrl + next_collection);

						}
					});
				} else {
					Toast.makeText(Home_Screen.this, "NO Record Found",
							Toast.LENGTH_LONG).show();
				}

			}
		}.execute();

	}

	

	/*public void CollectionMoreLoading(final String url) {

		new AsyncTask<String, Integer, String>() {

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
			protected String doInBackground(String... params) {

				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				System.out.println("--calll async");
				collection_arraylist = new ArrayList<HashMap<String, String>>();
				JsonParser jparser = new JsonParser();

				try {
					collection_arraylist.clear();
					System.out.println("collection url ddd=  " + url);
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

							// String post=jdata.getString("post");

							HashMap<String, String> map = new HashMap<String, String>();
							map.put("message", message);
							map.put("total_drafts", total_drafts);
							map.put("image", image);
							map.put("collection_name", collection_name);
							map.put("id", collection_id);
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

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub

				super.onPostExecute(result);
				progressdialog.dismiss();
				if (message.equals("true")) {
					int currentposition = list.getFirstVisiblePosition();
					CollectionAdapter adapter = new CollectionAdapter(
							Home_Screen.this, collection_arraylist);
					System.out.println("-----collection_arraylist adapter-----"
							+ collection_arraylist);
					list.setAdapter(adapter);
					list.setSelectionFromTop(currentposition + 1, 0);

				} else {
					Toast.makeText(Home_Screen.this, "NO Record Found",
							Toast.LENGTH_LONG).show();
				}

			}
		}.execute();

	}*/

	/*class CollectionAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater;

		public CollectionAdapter(Activity activity,
				ArrayList<HashMap<String, String>> data) {
			this.activity = activity;
			this.data = data;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("dataaaaa= " + data);
			imageLoader = new ImageLoader(activity.getApplicationContext());

		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			final ViewHolder holder;
			System.out.println("vallll out==" + data.get(position).get("post"));
			if (convertView == null) {
				vi = inflater.inflate(R.layout.collection_custom_list, null);
				holder = new ViewHolder();
				holder.task_name = (TextView) vi
						.findViewById(R.id.collection_task_name);
				holder.myname = (TextView) vi
						.findViewById(R.id.collection_myname);
				holder.task_image = (ImageView) vi
						.findViewById(R.id.collection_task_image);
				holder.collection_post_no = (TextView) vi
						.findViewById(R.id.collection_post_no);
				holder.collection_post = (TextView) vi
						.findViewById(R.id.collection_post);

				holder.task_name.setText(data.get(position).get(
						"collection_name"));
				holder.myname.setText(data.get(position).get("User"));
				holder.collection_post_no.setText(data.get(position).get(
						"total_drafts"));
				int no_of_post = Integer.parseInt(data.get(position).get(
						"total_drafts"));
				if (no_of_post != 0) {
					holder.collection_post.setText(" posts");
				}

				imageLoader.DisplayImage(data.get(position).get("image"),
						holder.task_image);
				holder.task_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Intent in = new Intent(Home_Screen.this,
										Single_Collection_Reports.class);
								String id = data.get(position).get("id");
								System.out.println("idnew  =  " + id);
								in.putExtra("collection_id", id);
								startActivity(in);
							}
						});

			}
			return vi;
		}

	}
*/
	class HomeAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater;

		public HomeAdapter(Activity activity,
				ArrayList<HashMap<String, String>> data) {
			this.activity = activity;
			this.data = data;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("dataaaaa= " + data);
			imageLoader = new ImageLoader(activity.getApplicationContext());

		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			final ViewHolder holder;
			System.out.println("vallll out==" + data.get(position).get("post"));
			if (convertView == null) {
				vi = inflater.inflate(R.layout.home_custom_list, null);
				holder = new ViewHolder();

				holder.facebook_img = (ImageView) vi
						.findViewById(R.id.facebook_img);
				holder.twitter_img = (ImageView) vi
						.findViewById(R.id.twitter_img);
				
				holder.twitter_img = (ImageView) vi
						.findViewById(R.id.twitter_img);
				holder.recommend_it = (ImageView) vi
						.findViewById(R.id.recommend_it);

				holder.task_name = (TextView) vi.findViewById(R.id.task_name);
				holder.myname = (TextView) vi.findViewById(R.id.myname);
				holder.task_image = (ImageView) vi
						.findViewById(R.id.task_image);
				holder.task_name.setText(data.get(position).get("title"));
				holder.myname.setText(data.get(position).get("post") + ".....");
				System.out.println("vallll in=="
						+ data.get(position).get("post"));
				imageLoader.DisplayImage(data.get(position).get("image"),
						holder.task_image);

				share_link = data.get(position).get("link");
				my_post = data.get(position).get("post");

				holder.task_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Intent in = new Intent(Home_Screen.this,
										Single_post.class);
								String id1 = data.get(position).get("id");
								System.out.println("idnew  =  " + id1);
								in.putExtra("id_one", id1);
								startActivity(in);
							}
						});
				holder.facebook_img
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						 in=new Intent(getApplicationContext(),ShareBarActivity.class);
						String	share_link = data.get(position).get("link");
						//String	my_post = data.get(position).get("post");
						System.out.println("---link post facebbok "+share_link);	
						in.putExtra("link", share_link);
						//in.putExtra("post", my_post);
						in.putExtra("key_value", "facebook");
						startActivity(in);
/*
						loginToFacebook();
						postMessageOnWall(share_link, my_post);
						HomeLoading(getUrl+next);*/
					}
				});
		holder.twitter_img
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		
			 in=new Intent(getApplicationContext(),ShareBarActivity.class);
			 String	share_link = data.get(position).get("link");
				//String	my_post = data.get(position).get("post");
				System.out.println("---link post twitter "+share_link);	
				in.putExtra("linkin", share_link);
				//in.putExtra("postin", my_post);
				in.putExtra("key_value", "twitter");
				startActivity(in);
			}
		});


	}
	return vi;
}

}/*
	class RecentAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater;

		public RecentAdapter(Activity activity,
				ArrayList<HashMap<String, String>> data1) {
			this.activity = activity;
			this.data1 = data1;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("dataaaaa= " + data1);
			imageLoader = new ImageLoader(activity.getApplicationContext());

		}

		public int getCount() {
			return data1.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			final ViewHolder holder;
			System.out.println("vallll out==" + data1.get(position).get("post"));
			if (convertView == null) {
				vi = inflater.inflate(R.layout.recent_custom_list, null);
				holder = new ViewHolder();

				holder.recent_facebook_img = (ImageView) vi
						.findViewById(R.id.recent_facebook_img);
				holder.recent_twitter_img = (ImageView) vi
						.findViewById(R.id.twitter_img);
				
				holder.recent_twitter_img = (ImageView) vi
						.findViewById(R.id.recent_twitter_img);
				holder.recent_recommend_it = (ImageView) vi
						.findViewById(R.id.recent_recommend_it);

				holder.recent_task_name = (TextView) vi.findViewById(R.id.recent_task_name);
				holder.recent_myname = (TextView) vi.findViewById(R.id.recent_myname);
				holder.recent_task_image = (ImageView) vi
						.findViewById(R.id.recent_task_image);
				holder.recent_task_name.setText(data1.get(position).get("title"));
				holder.recent_myname.setText(data1.get(position).get("post") + ".....");
				System.out.println("vallll in=="
						+ data1.get(position).get("post"));
				imageLoader.DisplayImage(data1.get(position).get("image"),
						holder.recent_task_image);

				share_link = data1.get(position).get("link");
				my_post = data1.get(position).get("post");

				holder.recent_task_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Intent in = new Intent(Home_Screen.this,
										Single_post.class);
								String id1 = data1.get(position).get("id");
								System.out.println("idnew  =  " + id1);
								in.putExtra("id_one", id1);
								startActivity(in);
							}
						});

				holder.recent_facebook_img
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						 in=new Intent(getApplicationContext(),ShareBarActivity.class);
						String	share_link = data1.get(position).get("link");
						//String	my_post = data1.get(position).get("post");
						System.out.println("---link post facebbok "+share_link);	
						in.putExtra("link", share_link);
						//in.putExtra("post", my_post);
						in.putExtra("key_value", "facebook");
						startActivity(in);

						loginToFacebook();
						postMessageOnWall(share_link, my_post);
						HomeLoading(getUrl+next);
					}
				});
		holder.recent_twitter_img
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		
			 in=new Intent(getApplicationContext(),ShareBarActivity.class);
			 String	share_link = data1.get(position).get("link");
				//String	my_post = data1.get(position).get("post");
				System.out.println("---link post twitter "+share_link);	
				in.putExtra("linkin", share_link);
				//in.putExtra("postin", my_post);
				in.putExtra("key_value", "twitter");
				startActivity(in);
			}
		});


			}
			return vi;
		}

	}
	class MostAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data2 = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater;

		public MostAdapter(Activity activity,
				ArrayList<HashMap<String, String>> data2) {
			this.activity = activity;
			this.data2 = data2;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("dataaaaa= " + data2);
			imageLoader = new ImageLoader(activity.getApplicationContext());

		}

		public int getCount() {
			return data2.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			final ViewHolder holder;
			System.out.println("vallll out==" + data2.get(position).get("post"));
			if (convertView == null) {
				vi = inflater.inflate(R.layout.most_custom_list, null);
				holder = new ViewHolder();

				holder.most_facebook_img = (ImageView) vi
						.findViewById(R.id.most_facebook_img);
				
				
				holder.most_twitter_img = (ImageView) vi
						.findViewById(R.id.most_twitter_img);
				holder.most_recommend_it = (ImageView) vi
						.findViewById(R.id.most_recommend_it);

				holder.most_task_name = (TextView) vi.findViewById(R.id.most_task_name);
				holder.most_myname = (TextView) vi.findViewById(R.id.most_myname);
				holder.most_task_image = (ImageView) vi
						.findViewById(R.id.most_task_image);
				holder.most_task_name.setText(data2.get(position).get("title"));
				holder.most_myname.setText(data2.get(position).get("post") + ".....");
				System.out.println("vallll in=="
						+ data2.get(position).get("post"));
				imageLoader.DisplayImage(data2.get(position).get("image"),
						holder.most_task_image);

				share_link = data2.get(position).get("link");
				my_post = data2.get(position).get("post");

				holder.most_task_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Intent in = new Intent(Home_Screen.this,
										Single_post.class);
								String id1 = data2.get(position).get("id");
								System.out.println("idnew  =  " + id1);
								in.putExtra("id_one", id1);
								startActivity(in);
							}
						});
				holder.most_facebook_img
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						 in=new Intent(getApplicationContext(),ShareBarActivity.class);
						String	share_link = data2.get(position).get("link");
						//String	my_post = data2.get(position).get("post");
						System.out.println("---link post facebbok "+share_link);	
						in.putExtra("link", share_link);
						//in.putExtra("post", my_post);
						in.putExtra("key_value", "facebook");
						startActivity(in);

						loginToFacebook();
						postMessageOnWall(share_link, my_post);
						HomeLoading(getUrl+next);
					}
				});
		holder.most_twitter_img
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		
			 in=new Intent(getApplicationContext(),ShareBarActivity.class);
			 String	share_link = data2.get(position).get("link");
				//String	my_post = data2.get(position).get("post");
				System.out.println("---link post twitter "+share_link);	
				in.putExtra("linkin", share_link);
				//in.putExtra("postin", my_post);
				in.putExtra("key_value", "twitter");
				startActivity(in);
			}
		});


			}
			return vi;
		}

	}
*/
/*
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    facebook.authorizeCallback(requestCode, resultCode, data);
	}
	public void postMessageOnWall(final String link, final String newpost) {
		
		if (facebook.isSessionValid()) {
		    Bundle parameters = new Bundle();
		    parameters.putString("link", link);
		    parameters.putString("postnew", newpost);
		    try {
				String response = facebook.request("feed", parameters,"POST");
				System.out.println("----response is----"+response);
				Toast.makeText(getApplicationContext(), "You have successfully post" , Toast.LENGTH_LONG).show();	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// logout();
		}

	public void loginToFacebook() {

		mprefs = getPreferences(MODE_PRIVATE);
		accessToken= mprefs.getString("access token", null);
		long expires = mprefs.getLong("access expire", 0);
		if (accessToken != null) {
			facebook.setAccessToken(accessToken);

		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub

						}

						public void onError(DialogError e) {
							// TODO Auto-generated method stub

						}

						public void onComplete(Bundle values) {
							// TODO Auto-generated method stub
							SharedPreferences.Editor editor = mprefs.edit();
							editor.putString("access token",
									facebook.getAccessToken());
							editor.putLong("access expire",
									facebook.getAccessExpires());
							editor.commit();
							

						}

						public void onCancel() {
							// TODO Auto-generated method stub

						}
					});
		}
	

	
	}*/
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		final AlertDialog alertdialog;
		switch (v.getId()) {
		case R.id.recent_reports:
			next = 0;
			next_collection = 0;
			
			
			
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
                RecentLoading(new_recent+next);
                
                
				
			} else {
				
				getUrl("recent");

				System.out.println("recent url lll = " + recent_url + next);

				RecentLoading(recent_url + next);
			}
			break;
		case R.id.collections:
			next_collection = 0;
			
			
			
			
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
					+ next_collection);

			CollectionLoading(collection_url + next_collection);
			System.out.println("app_id ifff= "
					+ app_id);

			break;
		case R.id.most_recommended:
			next = 0;
			next_collection = 0;
			
			
			
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
				MostLoading(new_recommend+next);
			}
			else
			{
				getUrl("most_recommended");
				System.out.println("most url lll = " + most_recommended_url);
				MostLoading(most_recommended_url + next);
					
			}
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
	    	Intent i = new Intent(Home_Screen.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
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

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", message);
					map.put("total_drafts", total_drafts);
					map.put("image", image);
					map.put("collection_name", collection_name);
					map.put("id", collection_id);
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
