package com.example.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.reportedly.Create_Story;
import com.example.reportedly.Home_Screen.ViewHolder;
import com.example.reportedly.ImageLoader;
import com.example.reportedly.R;

import com.navdrawer.SimpleSideDrawer;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
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
	
	ListViewAdapter adapter;
	ProgressDialog pDialog;
	SharedPreferences myPrefs;
	EditText search_box;
	SharedPreferences.Editor prefsEditor;
	String app_id,message,Selection_id,total_drafts,testUrl;
	String url="http://reportedly.pnf-sites.info/webservices/api1/collections/";
	ArrayList<HashMap<String, String>> select_arraylist;
	static Button loadmore;
	Button create_btn, left_button, homeview_btn,current_location_btn,top_recommended_btn,create_location_btn,add_btn,profile_screen_btn,states_btn,profile_settings_btn,map_btn;



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
			testUrl=url+current_page;
		System.out.println("----texturl is----- "+testUrl);
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

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("message", message);
				map.put("total_drafts", total_drafts);
				map.put("image", image);
				map.put("Selection_name", Selection_name);
				map.put("id", Selection_id);
				map.put("User", User);
				select_arraylist.add(map);
				System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"
						+ select_arraylist);
		}
		}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		// LoadMore button
		Button btnLoadMore = new Button(this);
		btnLoadMore.setText("Load More");

		// Adding Load More button to lisview at bottom
		select_list.addFooterView(btnLoadMore);
		
		// Getting adapter
		System.out.println("--select_arraylist-- "+select_arraylist);
		adapter = new ListViewAdapter(this, select_arraylist);
	
		select_list.setAdapter(adapter);

		/**
		 * Listening to Load More button click event
		 * */
		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				new loadMoreListView().execute();
			}
		});
		/*search_box.addTextChangedListener(new TextWatcher() {

	        public void onTextChanged(CharSequence cs, int arg1, int arg2,
	                int arg3) {
	            // When user changed the Text
	            adapter.getFilter(task_name).filter(cs);
	        }

	        public void beforeTextChanged(CharSequence arg0, int arg1,
	                int arg2, int arg3) {
	            // TODO Auto-generated method stub

	        }

	        public void afterTextChange(Editable arg0) {
	            // TODO Auto-generated method stub

	        }

	        public void afterTextChanged(Editable s) {
	            // TODO Auto-generated method stub

	        }
	    });
	
	*/

	}

	
	
	 class ListViewAdapter extends BaseAdapter {
	    	Activity activity;
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			LayoutInflater inflater;

			public ListViewAdapter(Activity activity,
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
					holder.Select_post_no = (TextView) vi
							.findViewById(R.id.collection_post_no);
					holder.Select_post = (TextView) vi
							.findViewById(R.id.collection_post);

					holder.task_name.setText(data.get(position).get(
							"Selection_name"));
			
					holder.myname.setText(data.get(position).get("User"));
					holder.Select_post_no.setText(data.get(position).get(
							"total_drafts"));
					int no_of_post = Integer.parseInt(data.get(position).get(
							"total_drafts"));
					if (no_of_post != 0) {
						holder.Select_post.setText(" posts");
					}

					imageLoader.DisplayImage(data.get(position).get("image"),
							holder.task_image);
					holder.task_image
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							Intent in=new Intent(Select_Colllection.this,Create_Story.class);
							startActivity(in);
							finish();
						}
					});

		

				}
				return vi;
			

	    }}
	 
	 
	private class loadMoreListView extends AsyncTask<Void, Void, Void> {

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

								HashMap<String, String> map = new HashMap<String, String>();
								map.put("message", message);
								map.put("total_drafts", total_drafts);
								map.put("image", image);
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
					adapter = new ListViewAdapter(Select_Colllection.this,select_arraylist);
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
	
	

	@Override
	public void onClick(View v) {
		AlertDialog alertdialog;
		// TODO Auto-generated method stub
		switch (v.getId()) {
	case R.id.add_btn:
		in = new Intent(this, Create_Collection_View.class);
		startActivity(in);
		finish();
		break;
	case R.id.select_slide_btn:
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