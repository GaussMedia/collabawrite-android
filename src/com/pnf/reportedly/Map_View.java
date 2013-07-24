package com.pnf.reportedly;


import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;


public class Map_View extends FragmentActivity implements LocationListener{


	 double lat;
     double lng;
	GoogleMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		
		 Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mapmayloadcancelled3);
	        SupportMapFragment mapFragment = (SupportMapFragment)fragment;
	        map = mapFragment.getMap();
	        map.setMyLocationEnabled(true);
	        
	        
	        
	        Geocoder geocoder;
	        String bestProvider;
	        List<Address> user = null;
	       

	       LocationManager lm = (LocationManager) Map_View.this.getSystemService(Context.LOCATION_SERVICE);

	        Criteria criteria = new Criteria();
	        bestProvider = lm.getBestProvider(criteria, false);
	        Location location = lm.getLastKnownLocation(bestProvider);

	        if (location == null){
	            Toast.makeText(Map_View.this,"Location Not found",Toast.LENGTH_LONG).show();
	         }else{
	           geocoder = new Geocoder(Map_View.this);
	           try {
	               user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
	           lat=(double)user.get(0).getLatitude();
	           lng=(double)user.get(0).getLongitude();
	           System.out.println(" DDD lat: " +lat+",  longitude: "+lng);

	           }catch (Exception e) {
	                   e.printStackTrace();
	           }
	        
	        
	        
	        
	        
			/*try {
				
				Location myLocation = map.getMyLocation();

      lat = myLocation.getLatitude();
      lng = myLocation.getLongitude();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	            
			System.out.println("Latitude val "+lat);
			System.out.println("Logitude val "+lat);*/
			 map.addMarker(new MarkerOptions()
	            .position(new LatLng(lat, lng))
	            .title("Hello world"));   
	        
	         } 
		 CameraUpdate center=
			        CameraUpdateFactory.newLatLng(new LatLng(lat,
			        		lng));
			    CameraUpdate zoom=CameraUpdateFactory.zoomTo(3);

			    map.moveCamera(center);
			    map.animateCamera(zoom);
			    
			  
			   
			    
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
