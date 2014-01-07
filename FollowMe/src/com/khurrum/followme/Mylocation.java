package com.khurrum.followme;

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
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class Mylocation extends FragmentActivity implements
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

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

}
