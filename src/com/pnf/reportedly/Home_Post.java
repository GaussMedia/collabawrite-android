package com.pnf.reportedly;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Home_Post extends Activity
{
	 WebView post_web;
	    String post_id,message,post,title,user_pic,user_name;
	     TextView report_title,name;
	     ImageView image;
		String url="http://reportedly.pnf-sites.info/webservices/api1/report/";
		
		
		private int color;
		 private Paint paint;
		    private Rect rect;
		    private Bitmap result;
		    private RectF rectF;
		    private Canvas canvas;
		    private float roundPx;
		    public static boolean _exit;
		    SharedPreferences myPrefs;
			SharedPreferences.Editor prefsEditor;
			String app_id;
		protected void onCreate(Bundle savedInstance)
		{
			
			super.onCreate(savedInstance);
			setContentView(R.layout.single_post);
			
			myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_WORLD_READABLE);
		    prefsEditor = myPrefs.edit();
		  app_id= myPrefs.getString("UserId","");
		   prefsEditor.commit();
		   System.out.println("my getting id ===" +app_id);
			
		   post_web=(WebView)findViewById(R.id.post_web);
			report_title=(TextView)findViewById(R.id.top_single_text);
			image=(ImageView)findViewById(R.id.single_circle_view);
			name=(TextView)findViewById(R.id.name);
			 Intent in=getIntent();
	System.out.println("---intent in");
			post_id=in.getStringExtra("id_one");
			System.out.println("---post id is---"+post_id);
			new AsyncTask<String, Integer, String>()
			{

				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					
					JsonParser jparser=new JsonParser();
					JSONObject jobj=jparser.getJSONfromUrl(url+post_id);
			        try {
			        	System.out.println("url is---"+url+post_id);
						message=jobj.getString("Message");
						if(message.equals("true"))
						{
							user_pic=jobj.getString("User_image");	    
				            user_name=jobj.getString("User");
							JSONArray jarray=jobj.getJSONArray("data");
							for(int j=0;j<jarray.length();j++)
							{
								
								JSONObject obj=jarray.getJSONObject(j);
								title=obj.getString("title");
							    post=obj.getString("post");
							    		   
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
					report_title.setText(title);
					System.out.println("userrrr  = " + user_name);
					name.setText(user_name);
					new AsyncTask<String, Void, Bitmap>() {
						String url = "";			
						
						
						@Override
						protected Bitmap doInBackground(String... params) {
							url = params[0];
							Bitmap myBitmap = null;
							try {
					            URL Purl = new URL(url);
					            HttpURLConnection connection = (HttpURLConnection) Purl.openConnection();
					            connection.setDoInput(true);
					            connection.connect();
					            InputStream input = connection.getInputStream();
					            myBitmap = BitmapFactory.decodeStream(input);		            
					        } catch (IOException e) {
					            e.printStackTrace();
					        }
							return myBitmap;
						}
						
						protected void onPostExecute(Bitmap result) {
							if(!url.equals("")) {
								image.setImageBitmap(getRoundedRectBitmap(result, 1000));
								
							}
						};
						
					}.execute(user_pic);
					 byte[] data = Base64.decode(post, Base64.DEFAULT);
					    String string;
						try {
							string = new String(data, "UTF-8");
			
					    post_web.loadData(string,"text/html","utf-8");
			              
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}

			
			}.execute();
		}
		public  Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels)
	    {
	        result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(result);
	        color = 0xff424242;
	        paint = new Paint();
	        rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	        rectF = new RectF(rect);
	        roundPx = pixels;
	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	        return result;
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
		    	Intent i = new Intent(Home_Post.this,Home_Screen.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
		    }
			return false;
		}

}
