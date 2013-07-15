package com.example.reportedly;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.reportedly.R;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Profile_Screen extends Activity implements OnClickListener
{
	ListView profile_list;
	Button edit_myprofile_btn,cancel_profile_btn;
	Intent in;
	ArrayList<HashMap<String,String>> profile_arraylist;
	String url="http://reportedly.pnf-sites.info/webservices/api1/user/";
	String report_url="http://reportedly.pnf-sites.info/webservices/api1/userreports/";
	String message,full_name,description,website,image,total_Posts,total_recommends,title,post,post_image,id,username,link;
	TextView edit_name,edit_website,edit_my_site,report_posted_counter,recommendation_counter;
	ImageLoader imageLoader;
	RelativeLayout userpic_layout;
	Bitmap bitmap;
	int next=0;
	private static String APP_ID = "637189502961426";
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String filename = "FileName";
	private SharedPreferences mprefs;
	static String facebookId;
	String accessToken;
	
	String response;
	Bundle bundle;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	
	byte[] data;
	ImageView my_image;
	protected void onCreate(Bundle saveInstance)
	{
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.profile_screen);
	    myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	    
	    facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
	
		app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
	    profile_list=(ListView)findViewById(R.id.edit_profile_list);
	    edit_myprofile_btn=(Button)findViewById(R.id.edit_myprofile_btn);
	    cancel_profile_btn=(Button)findViewById(R.id.cancel_profile_btn);
	    edit_name=(TextView)findViewById(R.id.edit_name);
	    edit_website=(TextView)findViewById(R.id.edit_location);
	    edit_my_site=(TextView)findViewById(R.id.edit_my_site);
	    userpic_layout=(RelativeLayout)findViewById(R.id.userpic_layout);
	    report_posted_counter=(TextView)findViewById(R.id.report_posted_counter);
	    recommendation_counter=(TextView)findViewById(R.id.recommendation_counter);
	    imageLoader = new ImageLoader(this);
	    my_image=(ImageView)findViewById(R.id.my_image);
	    cancel_profile_btn.setOnClickListener(this);
	    edit_myprofile_btn.setOnClickListener(this);
	    
	/*    for(int i=0;i<20;i++)
		{
			HashMap<String, String> map =new HashMap<String, String>();
			String task_name="Train Windows";
			String myname="Hi i have done with home custom view. And now  i'm waiting for services";
				map.put("task_name", task_name);
				map.put("myname", myname);
				profile_arraylist.add(map);
		}
	*/	
	    new AsyncTask<String, Integer, String>() 
	    {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				JsonParser jparser=new JsonParser();
				
				try {
					System.out.println("user_profile = " +  url + app_id );
					
					JSONObject jobj=jparser.getJSONfromUrl(url + app_id);
					System.out.println("----jobj----"+jobj);
					message=jobj.getString("Message");
					System.out.println("Messageeeee = " +message);
					JSONObject user=jobj.getJSONObject("User");
					full_name=user.getString("fullname");
					id=user.getString("id");
					username=user.getString("username");
					website=user.getString("website");
					description=user.getString("description");
					image=user.getString("image");
					
					
					
					
					
				
					
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
				
				//int current_position=list.getFirstVisiblePosition();

				edit_name.setText(full_name);
				edit_website.setText(website);
				edit_my_site.setText(description);
				try {

			        URL url = new URL(image);
			        System.out.println("----url----"+url);
			        InputStream in = url.openConnection().getInputStream(); 
			        BufferedInputStream bis = new BufferedInputStream(in,1024*8);
			        ByteArrayOutputStream out = new ByteArrayOutputStream();

			        int len=0;
			        byte[] buffer = new byte[1024];
			        while((len = bis.read(buffer)) != -1){
			            out.write(buffer, 0, len);
			        }
			        out.close();
			        bis.close();

			           data = out.toByteArray();
			         bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			         //System.out.println("---bitmap---"+my_image.getHeight() +"   ===   " +my_image.getWidth());
			        
			         my_image.setImageBitmap(bitmap);
			       
			    }
			    catch (Exception e) {
			        e.printStackTrace();
			    }	
			
		}
		
		
	    	
		}.execute();
	   
	    
		profileLoading();
	}
	
	public void profileLoading()
	{
		
		new AsyncTask<String, Integer, String>()
		{
			ProgressDialog progressdialog;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressdialog = new ProgressDialog(Profile_Screen.this);
				progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressdialog.setMessage("Loading...");
				progressdialog.show();
				
			}
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				profile_arraylist=new ArrayList<HashMap<String,String>>();
				JsonParser jparser=new JsonParser();
				
				try {
					profile_arraylist.clear();
					JSONObject jobj=jparser.getJSONfromUrl(report_url + app_id+"/"+next);
					System.out.println("----jobj----"+jobj);
					message=jobj.getString("Message");
					System.out.println("Messageeeee = " +message);
					if(message.equals("true"))
					{
						total_Posts=jobj.getString("Posts");
						total_recommends =jobj.getString("Total-recommends");
						
				       JSONArray data=jobj.getJSONArray("data");
				       for(int i=0;i<data.length();i++)
					   {
						JSONObject jdata=data.getJSONObject(i);
						//String id=jdata.getString("id");
						post_image=jdata.getString("image");
						title=jdata.getString("title");
						 post=jdata.getString("post");
						link=jdata.getString("link");
						 
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("message", message);
						map.put("post_image", post_image);
						map.put("title", title);
						map.put("post", post);
						map.put("link", link);
						
						
						profile_arraylist.add(map);
						System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii helloooo"+ profile_arraylist);
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
				progressdialog.dismiss();
				if(message.equals("false"))
				{
					report_posted_counter.setText("0");
				}
				else
				{
					report_posted_counter.setText(total_Posts);	
				}
				if(message.equals("false"))
				{
					recommendation_counter.setText("0");
				}
				else
				{
				recommendation_counter.setText(total_recommends);
				}
				ProfileAdapter adapter = new ProfileAdapter(Profile_Screen.this, profile_arraylist);
				System.out.println("-----call adapter-----"+profile_arraylist);
				profile_list.setAdapter(adapter);
			}
			
		}.execute();
		
	}
	
	class ProfileAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		LayoutInflater inflater;
		
		
		
		public ProfileAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
			this.activity = activity;
			this.data = data;
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      System.out.println("kkkkkkk   = " + data);
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
			
			if (convertView == null) {
				vi = inflater.inflate(R.layout.profile_post_list, null);
			    TextView title=(TextView)vi.findViewById(R.id.profile_task_name);
			    TextView post=(TextView)vi.findViewById(R.id.profile_myname);
			    
			    ImageView task_image=(ImageView)vi.findViewById(R.id.profile_task_image);
			    ImageView facebook_click=(ImageView)vi.findViewById(R.id.profile_facebook);
			    ImageView twitter_click=(ImageView)vi.findViewById(R.id.profile_twitter);
			    
			    ImageView recommend_click=(ImageView)vi.findViewById(R.id.profile_recommend);
			    
			    
			    
			    title.setText(data.get(position).get("title"));
			    post.setText(data.get(position).get("post"));
			    imageLoader.DisplayImage(data.get(position).get("post_image"), task_image);	
			     
			    facebook_click
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						 in=new Intent(getApplicationContext(),ShareBarActivity.class);
						String	share_link = data.get(position).get("link");
							
						in.putExtra("link", share_link);
						in.putExtra("key_value", "facebook");
						startActivity(in);
/*
						loginToFacebook();
						postMessageOnWall(share_link, my_post);
						HomeLoading(getUrl+next);*/
					}
				});
		twitter_click
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		
			 in=new Intent(getApplicationContext(),ShareBarActivity.class);
			 String	share_link = data.get(position).get("link");
				
					
				in.putExtra("linkin", share_link);
				
				in.putExtra("key_value", "twitter");
				startActivity(in);
			}
		});
			     
			    
			}
			return vi;
		}

        }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edit_myprofile_btn:
			in=new Intent(this,Edit_Screen.class);
			in.putExtra("id", id);
			in.putExtra("username", username);
			startActivity(in);
			finish();
			
			break;
		case R.id.cancel_profile_btn:
			in=new Intent(this,Home_Screen.class);
			startActivity(in);
			finish();
			break;
			
			

		default:
			break;
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
	    	Intent i = new Intent(Profile_Screen.this,Home_Screen.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
}
