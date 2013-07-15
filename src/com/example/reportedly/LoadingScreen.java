package com.example.reportedly;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.reportedly.Home_Screen.HomeAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class LoadingScreen extends AsyncTask<String, Integer, String>{

	ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
	Context context;
	Activity activity;
	String rqst,message;
	ProgressDialog	progressdialog1;
	public LoadingScreen(Context context,Activity activity)
	{
		this.activity=activity;
		this.context=context;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		super.onPreExecute();
		progressdialog1 = new ProgressDialog(activity);
		progressdialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog1.setMessage("Loading...");
		progressdialog1.show();
	}
	@Override
	protected String doInBackground(String... value) {
		// TODO Auto-generated method stub
		rqst=value[0];
		if(rqst.equals("recent_report"))
		{
			String url=value[1];
			JsonParser jparser=new JsonParser();
			
			try {
				data.clear();
				JSONObject jobj=jparser.getJSONfromUrl(url);
				System.out.println("----jobj----"+jobj);
				message=jobj.getString("Message");
				if(message.equals("true"))
				{
				JSONArray jsonarray=jobj.getJSONArray("data");
				String jobjnext=jobj.getString("next");
				System.out.println("nexttttt = " +jobjnext);
				for(int i=0;i<jsonarray.length();i++)
				{
					JSONObject jdata=jsonarray.getJSONObject(i);
					//String id=jdata.getString("id");
					String image=jdata.getString("image");
					String title=jdata.getString("title");
					String post=jdata.getString("post");
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("image", image);
					map.put("title", title);
					map.put("next_id", jobjnext);
					map.put("post", post);
					data.add(map);	System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"+ data);
					//loadmore.setVisibility(View.INVISIBLE);
				}
			}	
				else
				{
					Home_Screen.loadmore.setVisibility(View.GONE);
					Toast.makeText(activity, "NO Record Found", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(rqst.equals("most_recommended"))
		{
			String url=value[1];
			JsonParser jparser=new JsonParser();
			
			try {
				data.clear();
				JSONObject jobj=jparser.getJSONfromUrl(url);
				System.out.println("----jobj----"+jobj);
				message=jobj.getString("Message");
				if(message.equals("true"))
				{
				JSONArray jsonarray=jobj.getJSONArray("data");
				String jobjnext=jobj.getString("next");
				System.out.println("nexttttt = " +jobjnext);
				for(int i=0;i<jsonarray.length();i++)
				{
					JSONObject jdata=jsonarray.getJSONObject(i);
					//String id=jdata.getString("id");
					String image=jdata.getString("image");
					String title=jdata.getString("title");
					String post=jdata.getString("post");
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("image", image);
					map.put("title", title);
					map.put("next_id", jobjnext);
					map.put("post", post);
					data.add(map);	System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii"+ data);
					//loadmore.setVisibility(View.INVISIBLE);
				}
			}	
				else
				{
					Home_Screen.loadmore.setVisibility(View.GONE);
					Toast.makeText(activity, "NO Record Found", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(rqst.equals("recent_report"))
		{
			progressdialog1.dismiss();
			
			if(message.equals("true"))
			{
			LazyAdatpter adapter = new LazyAdatpter(activity, data,"recent_report");
			System.out.println("-----call adapter-----"+data);
			
			Home_Screen.list.setAdapter(adapter);
			//loadmore.setVisibility(View.INVISIBLE);
			Home_Screen.loadmore=new Button(activity);
			Home_Screen.loadmore.setText("Load more");
			Home_Screen.list.addFooterView(Home_Screen.loadmore);


	        
			Home_Screen.loadmore.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String next_id = Home_Screen.recent_url + data.get(0).get("next_id");
					
					new LoadingScreen(context, activity).execute("recent_report",next_id);
					
					
				}
			});
			
			
			
			}
			else
			{
				Home_Screen.loadmore.setVisibility(View.GONE);
				Toast.makeText(activity, "NO More Record Found", Toast.LENGTH_LONG).show();
			}
			
		}
		if(rqst.equals("most_recommended"))
		{
			progressdialog1.dismiss();
			
			if(message.equals("true"))
			{
			LazyAdatpter adapter = new LazyAdatpter(activity, data,"most_recommended");
			System.out.println("-----call adapter-----"+data);
			
			Home_Screen.list.setAdapter(adapter);
			//loadmore.setVisibility(View.INVISIBLE);
			Home_Screen.loadmore=new Button(activity);
			Home_Screen.loadmore.setText("Load more");
			Home_Screen.list.addFooterView(Home_Screen.loadmore);


	        
			Home_Screen.loadmore.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String next_id = Home_Screen.most_recommended_url + data.get(0).get("next_id");
					
					new LoadingScreen(context, activity).execute("most_recommended",next_id);
					
					
				}
			});
			
			
			
			}
			else
			{
				Home_Screen.loadmore.setVisibility(View.GONE);
				Toast.makeText(activity, "NO More Record Found", Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
	

}
