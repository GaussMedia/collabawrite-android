package com.pnf.reportedly;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class Create_Story extends Activity implements OnClickListener
{
	Button cancel_story,camera_btn,library_btn,text_btn;
	Intent in;
	final static int CAMERA_DATA=0;
	final static int IMAGE_GET=1;
	Bitmap bitmap;
	Button preview_btn;
	EditText title,subtitle,collection_name;
  
	String value ;
	ArrayList<HashMap<String, String>> story_arraylist;
	ListView story_list;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	String app_id;
	
	protected void  onCreate(Bundle saveInstance)
	{
	   super.onCreate(saveInstance);
	   requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   setContentView(R.layout.create_story);
	   myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	  app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
	   library_btn=(Button)findViewById(R.id.library_btn);
	   story_list=(ListView)findViewById(R.id.set_view_image);
	   camera_btn=(Button)findViewById(R.id.camera_btn);
	   cancel_story=(Button)findViewById(R.id.cancel_story);
	  text_btn=(Button)findViewById(R.id.text_btn);
	  preview_btn=(Button)findViewById(R.id.preview_btn);
	  title=(EditText)findViewById(R.id.title);
	  subtitle=(EditText)findViewById(R.id.sub_title);
	  collection_name=(EditText)findViewById(R.id.collection_title);
	    
	   cancel_story.setOnClickListener(this);
	   camera_btn.setOnClickListener(this);
	   library_btn.setOnClickListener(this);
	   preview_btn.setOnClickListener(this);
	   text_btn.setOnClickListener(this);
	   
	   story_arraylist = new ArrayList<HashMap<String, String>>();

		/*for (int i = 0; i < 20; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			
			String myname = "Hi i have done with home custom view. And now  i'm waiting for services";
			map.put("task_name", task_name);
			
			map.put("myname", myname);
			story_arraylist.add(map);
		}*/
		
		//story_list.setOnItemClickListener(this);
 
	}
	class StoryAdapter extends BaseAdapter {
		Activity activity;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater;

		public StoryAdapter(Activity activity,
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

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;

			if (convertView == null) {
				vi = inflater.inflate(R.layout.create_story_custom, null);
				TextView task_name = (TextView) vi.findViewById(R.id.colection_click);
				task_name.setText(data.get(position).get("task_name"));
			}
			return vi;
		}

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel_story:
			in=new Intent(this,Select_Colllection.class);
			startActivity(in);
			finish();
			break;
		case R.id.camera_btn:
			
			in =new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(in, CAMERA_DATA);
			
			
			break;
		case R.id.library_btn:
			
			in=new Intent();
			in.setType("image/*");
			in.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(in, "Select Image"),IMAGE_GET);
			
			break;
		case R.id.preview_btn:
			
			String get_title=title.getText().toString();
			String get_subtitle=subtitle.getText().toString();
			Bundle bundle=new Bundle();
			bundle.putString("title", get_title);
			bundle.putString("subtitle", get_subtitle);
			
			
			in=new Intent(this,Preview_Screen.class);
			in.putExtras(bundle);
			startActivity(in);
			finish();
			
			break;
			
		case R.id.text_btn:
			 AlertDialog.Builder alert = new AlertDialog.Builder(this);
	           
	            alert.setMessage("Enter your text here !!!"); //Message here
	 
	            // Set an EditText view to get user input 
	            final EditText input = new EditText(this);
	            alert.setView(input);
	 
	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	             //You will get as string input data in this variable.
	             // here we convert the input to a string and show in a toast.
	             String srt = input.getEditableText().toString();
	             
	             story_arraylist = new ArrayList<HashMap<String, String>>();
	             HashMap<String, String> map = new HashMap<String, String>();
	 			
	 			
	 			map.put("task_name", srt);
	 			
	 			
	 			story_arraylist.add(map);
	 			
	 			StoryAdapter adapter = new StoryAdapter(Create_Story.this, story_arraylist);
	 			story_list.setAdapter(adapter);
	             //Toast.makeText(context,srt,Toast.LENGTH_LONG).show();                
	            } // End of onClick(DialogInterface dialog, int whichButton)
	        }); //End of alert.setPositiveButton
	            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int whichButton) {
	                // Canceled.
	                  dialog.cancel();
	              }
	        }); //End of alert.setNegativeButton
	            AlertDialog alertDialog = alert.create();
	            alertDialog.show();
		       
		        System.out.println("value"+ value);
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
	    	Intent i = new Intent(Create_Story.this,Select_Colllection.class);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
	    }
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case CAMERA_DATA:
			 
			if(resultCode == RESULT_OK)
			{
			Bundle extras=data.getExtras();
			bitmap=(Bitmap)extras.get("data");
			//set_image.setImageBitmap(bitmap);
			
			
			}
			
			break;
       
		case IMAGE_GET:
			  if (data != null && resultCode == RESULT_OK) 
	          {              
	              
	                Uri selectedImage = data.getData();
	                
	                String[] filePathColumn = {MediaStore.Images.Media.DATA};
	                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	                cursor.moveToFirst();
	                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                String filePath = cursor.getString(columnIndex);
	                cursor.close();
	              
	                if(bitmap != null && !bitmap.isRecycled())
	                {
	                    bitmap = null;                
	                }
	                                
	                bitmap = BitmapFactory.decodeFile(filePath);
	            //    set_image.setBackgroundResource(0);
	               // set_image.setImageBitmap(bitmap);   
	          }
			break;
			
		default:
			
			break;
		}
	   
		
	}

}
