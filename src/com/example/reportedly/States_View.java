package com.example.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.reportedly.Most_Stories_By.MostAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class States_View extends Activity implements OnClickListener {
	Button states_cancel_btn;
	Intent in;
	ListView stats_list;
	String id, title, views, reads, recommendations, post_author,share,tweet;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	String url = "http://reportedly.pnf-sites.info/webservices/api1/stats/";
	static Button loadmore;
	ArrayList<HashMap<String, String>> stats_arraylist;
	StatsAdapter adapter;
	int current_page = 0;

	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.stats_screen);

		myPrefs = this.getSharedPreferences("myPrefs",
				Context.MODE_WORLD_READABLE);
		prefsEditor = myPrefs.edit();
		app_id = myPrefs.getString("UserId", "");
		prefsEditor.commit();
		System.out.println("my getting id ===" + app_id);
		stats_list = (ListView) findViewById(R.id.stats_list);
		states_cancel_btn = (Button) findViewById(R.id.states_cancel_btn);
		states_cancel_btn.setOnClickListener(this);
		stats_arraylist = new ArrayList<HashMap<String, String>>();
		stats_json();

		Button load=new Button(this);
		load.setText("Load More");
		stats_list.addFooterView(load);
		adapter = new StatsAdapter(this, stats_arraylist);
		stats_list.setAdapter(adapter);
		
        load.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new LoadMoreData().execute();
			}
		});
	}

	public void stats_json() {
         System.out.println("first link =" +url + app_id + "/" + current_page);
		try {
			JsonParser jparser = new JsonParser();
			JSONObject jobj = jparser.getJSONfromUrl(url + app_id + "/" + current_page);
			
			String message = jobj.getString("Message");
			if (message.equals("true")) {
				JSONArray jsonarray = jobj.getJSONArray("data");

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jdata = jsonarray.getJSONObject(i);
					id = jdata.getString("id");
					title = jdata.getString("title");
					views = jdata.getString("views");
					reads = jdata.getString("reads");
					recommendations = jdata.getString("recommendations");
					post_author = jdata.getString("Post_author");
                    share=jdata.getString("Facebook_shares");
                    tweet=jdata.getString("Tweets");
                    
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", message);
					map.put("id", id);
					map.put("title", title);
					map.put("views", views);
					map.put("reads", reads);
					map.put("recommendations", recommendations);
					map.put("Post_author", post_author);
					map.put("share", share);
					map.put("tweet", tweet);
					
					stats_arraylist.add(map);
					System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"
							+ stats_list);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	
	class LoadMoreData extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(
					States_View.this);
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
					 
					//current_page=stats_list.getFirstVisiblePosition();
					
					// Next page request
					/*testUrl = "http://reportedly.pnf-sites.info/webservices/api1/collections/" + current_page;*/
                    System.out.println("---texturl is new loadmore----- "+url + app_id +"/"+ current_page);
				
				

				    stats_json();
						
					// get listview current position - used to maintain scroll position
				int	current_new_page = stats_list.getFirstVisiblePosition();
					
					// Appending new data to menuItems ArrayList
					//adapter = new ListViewAdapter(Select_Colllection.this,select_arraylist);
					stats_list.setAdapter(adapter);
					
					// Setting new scroll position
					stats_list.setSelectionFromTop(current_new_page + 1, 0);

				}
			});

			return (null);
		}
		
		
		protected void onPostExecute(Void unused) {
			// closing progress dialog
			pDialog.dismiss();
		}
	}
	class StatsAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater;

		public StatsAdapter(Activity activity,
				ArrayList<HashMap<String, String>> data) {
			this.activity = activity;
			this.data = data;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

			if (convertView == null) {
				vi = inflater.inflate(R.layout.stats_custom_list, null);
				TextView post_title = (TextView) vi.findViewById(R.id.post_title);
				TextView author_name = (TextView) vi.findViewById(R.id.author_name);
				TextView total_views = (TextView) vi.findViewById(R.id.total_views);
				TextView total_reads = (TextView) vi.findViewById(R.id.total_reads);
				TextView total_recommendations = (TextView) vi.findViewById(R.id.total_recommendations);
				TextView total_shares = (TextView) vi.findViewById(R.id.total_shares);
				TextView total_tweets = (TextView) vi.findViewById(R.id.total_tweets);

				

				post_title.setText(data.get(position).get("title"));
				author_name.setText(data.get(position).get("Post_author"));
				total_views.setText(data.get(position).get("views"));
				total_reads.setText(data.get(position).get("reads"));
				total_recommendations.setText(data.get(position).get("recommendations"));
				total_shares.setText(data.get(position).get("share"));
				total_tweets.setText(data.get(position).get("tweet"));
				
			
			}
			return vi;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.states_cancel_btn:

			in = new Intent(this, Home_Screen.class);
			startActivity(in);
			finish();
			break;

		default:
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("Home presed");
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			System.out.println("Home presed  in side");
			finish();
			System.exit(0);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(States_View.this, Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
		}
		return false;
	}

}
