package com.example.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.reportedly.Profile_Screen.ProfileAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Most_Stories_By extends Activity implements OnClickListener
{
	ListView profile_list;
	Button cancel_most_stories;
	ArrayList<HashMap<String,String>> profile_arraylist;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	protected void onCreate(Bundle saveInstance)
	{
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.most_stories_by);
		myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	  app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
	
		cancel_most_stories=(Button)findViewById(R.id.cancel_most_stories);
		cancel_most_stories.setOnClickListener(this);
		profile_list=(ListView)findViewById(R.id.edit_profile_list_most);
		profile_arraylist=new ArrayList<HashMap<String,String>>();
	    for(int i=0;i<20;i++)
		{
			HashMap<String, String> map =new HashMap<String, String>();
			String task_name="Train Windows";
			String myname="Hi i have done with home custom view. And now  i'm waiting for services";
				map.put("task_name", task_name);
				map.put("myname", myname);
				profile_arraylist.add(map);
		}
	    MostAdapter adapter= new MostAdapter(this, profile_arraylist);
		profile_list.setAdapter(adapter);
	}
	class MostAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		LayoutInflater inflater;
		
		
		
		public MostAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
			this.activity = activity;
			this.data = data;
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
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
				vi = inflater.inflate(R.layout.home_custom_list, null);
			    TextView task_name=(TextView)vi.findViewById(R.id.task_name);
			    TextView myname=(TextView)vi.findViewById(R.id.myname);
			    
			    //ImageView task_image=(ImageView)vi.findViewById(R.id.task_image);
			    
			    
			   task_name.setText(data.get(position).get("task_name"));
			     myname.setText(data.get(position).get("myname"));
			    
			     
			    
			     
			    
			}
			return vi;
		}

        }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel_most_stories:
			Intent in=new Intent(this,List_Description.class);
			startActivity(in);
			finish();
			
			break;

		default:
			break;
		}
	}

}
