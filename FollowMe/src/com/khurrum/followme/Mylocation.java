package com.khurrum.followme;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
/**
 * 
 * @author khurrumchaudhry
 *
 */
public class Mylocation extends SherlockFragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	LocationRequest lr;
	LocationClient lc;
	SupportMapFragment fragment;
	BitmapDescriptor pin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylocation);
		try {
			MapsInitializer.initialize(getApplicationContext());
			pin = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fragment = new SupportMapFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragCont, fragment).commit();

		lr = LocationRequest.create();
		lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lc = new LocationClient(this.getApplicationContext(), this, this);
		lc.connect();
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.mylocation, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng currentLocation = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
				currentLocation, 15);
		fragment.getMap().animateCamera(cameraUpdate);

		if (pin == null)
			fragment.getMap().addMarker(
					new MarkerOptions().position(currentLocation).title(
							"Mapping not initialized"));
		else
			fragment.getMap().addMarker(
					new MarkerOptions().position(currentLocation).icon(pin));

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		lc.requestLocationUpdates(lr, this);// start updates
		mapHandler.sendEmptyMessage(5);//intentional gps is too fast to simulate for my testing :)

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
	
	private final Handler mapHandler = new Handler() {
	    public void handleMessage(Message msg) {
	    	//hide wait text
	    	Mylocation.this.findViewById(R.id.txtLoading).setVisibility(View.GONE);
	    	
	        // enable buttong
	    	Button share = (Button) Mylocation.this.findViewById(R.id.btnShare);
	    	//add linteners for sending a share intent
	    	
	    	share.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// fire a intent to share your location and all the apps for general share intent will get listed
					
					String uri = String.format(getResources().getString(R.string.uri),lc.getLastLocation().getLatitude(),lc.getLastLocation().getLongitude());
					String share = String.format(getResources().getString(R.string.url), uri);
					
					Intent intent = new Intent(android.content.Intent.ACTION_SEND);
					intent.setType("text/plain");  
					intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(share));
					
					
					startActivity(intent);
					
				}
			});
	    	
	    	share.setVisibility(View.VISIBLE);
	    	
	    }
	};

}
