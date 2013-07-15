package com.example.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.reportedly.Home_Screen.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Single_Collection_Reports extends Activity
{
	ListView report_list;
	String id;
	Button single_cancel;
	int counter=0;
	String id_one;
	String url="http://reportedly.pnf-sites.info/webservices/api1/collectionreport/";
	String title,image,post,message,post_id;
	ArrayList<HashMap<String, String>> single_arraylist;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
protected void onCreate(Bundle savedInstance)
{
	
	super.onCreate(savedInstance);
	setContentView(R.layout.single_collection_report);
	
	myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
    prefsEditor = myPrefs.edit();
  app_id= myPrefs.getString("UserId","");
   prefsEditor.commit();
   
   System.out.println("my getting id ===" +app_id);
	report_list=(ListView)findViewById(R.id.collection_report_list);
	single_cancel=(Button)findViewById(R.id.collection_report_cancel);
	
	Intent in=getIntent();
	id=in.getStringExtra("collection_id");
	new AsyncTask<String, Integer, String>()
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			JsonParser jparser=new JsonParser();
			JSONObject jobj=jparser.getJSONfromUrl(url+id+"/" +counter);
	        try {
	        	single_arraylist = new ArrayList<HashMap<String, String>>();
				message=jobj.getString("Message");
				if(message.equals("true"))
				{
					JSONArray jarray=jobj.getJSONArray("data");
					for(int j=0;j<jarray.length();j++)
					{
						
						JSONObject obj=jarray.getJSONObject(j);
						title=obj.getString("title");
					    post=obj.getString("post");
						image=obj.getString("image");
						post_id=obj.getString("id");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("message", message);
						map.put("title", title);
						map.put("image", image);
						map.put("post", post);
						map.put("post_id", post_id);
						
						single_arraylist.add(map);
						System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"+ single_arraylist);
					}
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
			if(message.equals("true"))
			{
			SingleAdapter adapter = new SingleAdapter(Single_Collection_Reports.this, single_arraylist);
			System.out.println("-----collection_arraylist adapter-----"+single_arraylist);
			report_list.setAdapter(adapter);
		}
		}
	}.execute();
	
	single_cancel.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	         Intent in =new Intent(Single_Collection_Reports.this,Home_Screen.class);
	         startActivity(in);
	         finish();
		}
	});
	
}
class SingleAdapter extends BaseAdapter {
	Activity activity;
	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	LayoutInflater inflater;
	ImageLoader imageLoadernew;

	public SingleAdapter(Activity activity,
			ArrayList<HashMap<String, String>> data) {
		this.activity = activity;
		this.data = data;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
System.out.println("dataaaaa= "+data);
imageLoadernew = new ImageLoader(activity.getApplicationContext());

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
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		final ViewHolder holder; 
		System.out.println("vallll out=="+ data.get(position).get("post"));
		if (convertView == null) {
			vi = inflater.inflate(R.layout.home_custom_list, null);
			holder = new ViewHolder();
			holder.title = (TextView) vi.findViewById(R.id.task_name);
			holder.post = (TextView) vi.findViewById(R.id.myname);
			holder.image=(ImageView) vi.findViewById(R.id.task_image);
		
			holder.title.setText(data.get(position).get("title"));
			holder.post .setText(data.get(position).get("post")+"....");
		 
		
			imageLoadernew.DisplayImage(data.get(position).get("image"), holder.image);	
			holder.image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 id_one=data.get(position).get("post_id");
				   	Intent in=new Intent(getApplicationContext(),Single_post.class);
				   in.putExtra("id_one", id_one);
				  System.out.println("--call here"+id_one); 
				   	startActivity(in);
				  
				}
			});

		}
		return vi;
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
    	Intent i = new Intent(Single_Collection_Reports.this,Home_Screen.class);
		startActivity(i);
		overridePendingTransition(0, 0);
		finish();
    }
	return false;
}
static class ViewHolder {
	TextView post;
	TextView title;
	ImageView image;
	
}
}