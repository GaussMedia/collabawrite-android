package com.example.reportedly;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.reportedly.Home_Screen.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LazyAdatpter extends BaseAdapter{

	Activity activity;
	ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
	LayoutInflater inflater;
	public ImageLoader imageLoader;
	String rqst;
	TextView task_name;
	TextView myname;
	ImageView task_image;
	
	String share_link, my_post;
	public LazyAdatpter(Activity activity,ArrayList<HashMap<String, String>> arraylist, String rqst)
	{
		this.activity=activity;
		this.arraylist=arraylist;
	    this.rqst=rqst;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
System.out.println("dataaaaa= "+arraylist);
imageLoader = new ImageLoader(activity.getApplicationContext());
				
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
 ViewHolder holder;
		System.out.println("vallll out=="+ arraylist.get(position).get("post"));
		
		if(rqst.equals("recent_report"))
		{	if (convertView == null) {
			vi = inflater.inflate(R.layout.recent_custom_list, null);
			holder = new ViewHolder();

			holder.recent_facebook_img = (ImageView) vi
					.findViewById(R.id.recent_facebook_img);
			holder.recent_twitter_img = (ImageView) vi
					.findViewById(R.id.twitter_img);
			
			holder.recent_twitter_img = (ImageView) vi
					.findViewById(R.id.recent_twitter_img);
			holder.recent_recommend_it = (ImageView) vi
					.findViewById(R.id.recent_recommend_it);

			holder.recent_task_name = (TextView) vi.findViewById(R.id.recent_task_name);
			holder.recent_myname = (TextView) vi.findViewById(R.id.recent_myname);
			holder.recent_task_image = (ImageView) vi
					.findViewById(R.id.recent_task_image);
			holder.recent_task_name.setText(arraylist.get(position).get("title"));
			holder.recent_myname.setText(arraylist.get(position).get("post") + ".....");
			System.out.println("vallll in=="
					+ arraylist.get(position).get("post"));
			imageLoader.DisplayImage(arraylist.get(position).get("image"),
					holder.recent_task_image);

			share_link = arraylist.get(position).get("link");
			my_post = arraylist.get(position).get("post");

			holder.recent_task_image
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							Intent in = new Intent(activity,
									Single_post.class);
							String id1 = arraylist.get(position).get("id");
							System.out.println("idnew  =  " + id1);
							in.putExtra("id_one", id1);
							activity.startActivity(in);
						}
					});

			holder.recent_facebook_img
			.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					
					Intent in=new Intent(activity,ShareBarActivity.class);
					String	share_link = arraylist.get(position).get("link");
					//String	my_post = data1.get(position).get("post");
					System.out.println("---link post facebbok "+share_link);	
					in.putExtra("link", share_link);
					//in.putExtra("post", my_post);
					in.putExtra("key_value", "facebook");
					activity.startActivity(in);
/*
					loginToFacebook();
					postMessageOnWall(share_link, my_post);
					HomeLoading(getUrl+next);*/
				}
			});
	holder.recent_twitter_img
	.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

	
		Intent in=new Intent(activity,ShareBarActivity.class);
		 String	share_link = arraylist.get(position).get("link");
			//String	my_post = data1.get(position).get("post");
			System.out.println("---link post twitter "+share_link);	
			in.putExtra("linkin", share_link);
			//in.putExtra("postin", my_post);
			in.putExtra("key_value", "twitter");
			activity.startActivity(in);
		}
	});


		}
		}
		if(rqst.equals("most_recommended"))
		{
			if (convertView == null) {
				vi = inflater.inflate(R.layout.most_custom_list, null);
				holder = new ViewHolder();

				holder.most_facebook_img = (ImageView) vi
						.findViewById(R.id.most_facebook_img);
				
				
				holder.most_twitter_img = (ImageView) vi
						.findViewById(R.id.most_twitter_img);
				holder.most_recommend_it = (ImageView) vi
						.findViewById(R.id.most_recommend_it);

				holder.most_task_name = (TextView) vi.findViewById(R.id.most_task_name);
				holder.most_myname = (TextView) vi.findViewById(R.id.most_myname);
				holder.most_task_image = (ImageView) vi
						.findViewById(R.id.most_task_image);
				holder.most_task_name.setText(arraylist.get(position).get("title"));
				holder.most_myname.setText(arraylist.get(position).get("post") + ".....");
				System.out.println("vallll in=="
						+ arraylist.get(position).get("post"));
				imageLoader.DisplayImage(arraylist.get(position).get("image"),
						holder.most_task_image);

				share_link = arraylist.get(position).get("link");
				my_post = arraylist.get(position).get("post");

				holder.most_task_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Intent in = new Intent(activity,
										Single_post.class);
								String id1 = arraylist.get(position).get("id");
								System.out.println("idnew  =  " + id1);
								in.putExtra("id_one", id1);
								activity.startActivity(in);
							}
						});
		
				holder.most_facebook_img
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						Intent in=new Intent(activity,ShareBarActivity.class);
						String	share_link = arraylist.get(position).get("link");
						//String	my_post = data2.get(position).get("post");
						System.out.println("---link post facebbok "+share_link);	
						in.putExtra("link", share_link);
						//in.putExtra("post", my_post);
						in.putExtra("key_value", "facebook");
						activity.startActivity(in);
/*
						loginToFacebook();
						postMessageOnWall(share_link, my_post);
						HomeLoading(getUrl+next);*/
					}
				});
		holder.most_twitter_img
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		
			Intent in=new Intent(activity,ShareBarActivity.class);
			 String	share_link = arraylist.get(position).get("link");
				//String	my_post = data2.get(position).get("post");
				System.out.println("---link post twitter "+share_link);	
				in.putExtra("linkin", share_link);
				//in.putExtra("postin", my_post);
				in.putExtra("key_value", "twitter");
				activity.startActivity(in);
			}
		});
			}
		
		}	
			if(rqst.equals("collection"))
			{
				
				if (convertView == null) {
					vi = inflater.inflate(R.layout.collection_custom_list, null);
					holder = new ViewHolder();
					holder.task_name = (TextView) vi
							.findViewById(R.id.collection_task_name);
					holder.myname = (TextView) vi
							.findViewById(R.id.collection_myname);
					holder.task_image = (ImageView) vi
							.findViewById(R.id.collection_task_image);
					holder.collection_post_no = (TextView) vi
							.findViewById(R.id.collection_post_no);
					holder.collection_post = (TextView) vi
							.findViewById(R.id.collection_post);

					holder.task_name.setText(arraylist.get(position).get(
							"collection_name"));
					holder.myname.setText(arraylist.get(position).get("User"));
					holder.collection_post_no.setText(arraylist.get(position).get(
							"total_drafts"));
					int no_of_post = Integer.parseInt(arraylist.get(position).get(
							"total_drafts"));
				/*	if (no_of_post != 0) {
						holder.collection_post.setText(" posts");
					}*/
				
					imageLoader.DisplayImage(arraylist.get(position).get("image"),
							holder.task_image);
					holder.task_image
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

									Intent in = new Intent(activity,
											Single_Collection_Reports.class);
									String id = arraylist.get(position).get("id");
									System.out.println("idnew  =  " + id);
									in.putExtra("collection_id", id);
									activity.startActivity(in);
										}
							
		});
				}
			}
			
	
			return vi;
	}

	static class ViewHolder {
		TextView task_name;
		TextView myname;
		ImageView task_image;
		TextView collection_post_no;
		TextView collection_post;
		ImageView facebook_img;
		ImageView twitter_img;
		ImageView recommend_it;
		
		
		TextView most_task_name;
		TextView most_myname;
		ImageView most_task_image;
		TextView most_collection_post_no;
		TextView most_collection_post;
		ImageView most_facebook_img;
		ImageView most_twitter_img;
		ImageView most_recommend_it;
		
		
		TextView recent_task_name;
		TextView recent_myname;
		ImageView recent_task_image;
		TextView recent_collection_post_no;
		TextView recent_collection_post;
		ImageView recent_facebook_img;
		ImageView recent_twitter_img;
		ImageView recent_recommend_it;

	}

	
}

