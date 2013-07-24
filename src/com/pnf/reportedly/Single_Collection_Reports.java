package com.pnf.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pnf.reportedly.Home_Screen.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class Single_Collection_Reports extends Activity
{
	ListView report_list;
	String id;
	Button single_cancel,collection_report_edit;
	int counter=0;
	String id_one;
	String url="http://reportedly.pnf-sites.info/webservices/api1/collectionreport/";
	String title,image,post,message,post_id;
	ArrayList<HashMap<String, String>> single_arraylist;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id,col_name,author;
	TextView collection_name;
	
	
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
	collection_report_edit=(Button)findViewById(R.id.collection_report_edit);
	collection_name=(TextView)findViewById(R.id.collection_name);
	
	Intent in=getIntent();
	//author=in.getStringExtra("author");
	id=in.getStringExtra("collection_id");
	col_name=in.getStringExtra("collection_name");
	collection_name.setText(col_name);
	
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
					System.out.println("message id = "+ message);
					author=jobj.getString("author");
					System.out.println("author id = "+ author);
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
				else
				{
					author=jobj.getString("author");
					Toast.makeText(Single_Collection_Reports.this, "No post here !", Toast.LENGTH_LONG).show();
					
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
			LazyAdatpter adapter = new LazyAdatpter(Single_Collection_Reports.this, single_arraylist,"select_collection_report");
			System.out.println("-----collection_arraylist adapter-----"+single_arraylist);
			report_list.setAdapter(adapter);
		}
		}
	}.execute();
	
	single_cancel.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	       Intent in=new Intent(Single_Collection_Reports.this,Home_Screen.class);
	         startActivity(in);
	         finish();
		}
	});
	
	collection_report_edit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			final AlertDialog alertdialog;
			System.out.println("app id here = "+ app_id);
			System.out.println("author id here = "+ author);
			if(author.equals(app_id))
			{
				
				Intent in=new Intent(Single_Collection_Reports.this,EditCollection.class);
				
				in.putExtra("collection_id", id);
				in.putExtra("user_id", app_id);
				startActivity(in);
				finish();
				
			}else
			{
				alertdialog = new AlertDialog.Builder(Single_Collection_Reports.this).create();
				alertdialog.setTitle("Reportedly");
				alertdialog.setMessage("You can't edit it !!");
				alertdialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								alertdialog.dismiss();
							}
						});
				alertdialog.show();
		}
		}
	});
	
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