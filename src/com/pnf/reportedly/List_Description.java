package com.pnf.reportedly;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class List_Description extends Activity implements OnClickListener
{
	
	Button list_description_cancel,most_story_btn;
	TextView top_list_text,below_image_text,below_image_second_text;
	 WebSettings settings;
	 SharedPreferences myPrefs;
		SharedPreferences.Editor prefsEditor;
		String app_id;
	protected void onCreate(Bundle saveInstance)
	{
		
		super.onCreate(saveInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.list_description);
		
		myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
	    prefsEditor = myPrefs.edit();
	  app_id= myPrefs.getString("UserId","");
	   prefsEditor.commit();
	   System.out.println("my getting id ===" +app_id);
		most_story_btn=(Button)findViewById(R.id.most_story_btn);
		top_list_text=(TextView)findViewById(R.id.top_list_text);
		/*String text = "<html><body>"
                + "<p align=\"justify\">"                
                  + getString(R.string.first_paragraph) 
                  + "</p> "
                 + "</body></html>";
      settings= top_list_text.getSettings();
      settings.setTextSize(settings.getTextSize().NORMAL);
      top_list_text.loadData(text, "text/html", "utf-8");*/
		
      
      	below_image_text=(TextView)findViewById(R.id.below_image_text);
		/*String text1 = "<html><body>"
				                + "<p align=\"justify\">"                
				                  + getString(R.string.second_paragraph) 
				                  + "</p> "
				                 + "</body></html>";
		settings= below_image_text.getSettings();
	    settings.setTextSize(settings.getTextSize().NORMAL);
		below_image_text.loadData(text1, "text/html", "utf-8");*/
		
		below_image_second_text=(TextView)findViewById(R.id.below_image_second_text);
		/*String text2 = "<html><body>"
                + "<p align=\"justify\">"                
                  + getString(R.string.third_paragraph) 
                  + "</p> "
                 + "</body></html>";
        settings = below_image_second_text.getSettings();
        settings.setTextSize(settings.getTextSize().NORMAL);
		below_image_second_text.loadData(text2, "text/html", "utf-8");*/
		most_story_btn.setOnClickListener(this);
		list_description_cancel=(Button)findViewById(R.id.cancel_list_description);
		list_description_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent in =new Intent(List_Description.this,Home_Screen.class);
				startActivity(in);
				finish();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.most_story_btn:
			Intent in =new Intent(this,Most_Stories_By.class);
			startActivity(in);
			finish();
			
			break;

		default:
			break;
		}
		
	}

}
