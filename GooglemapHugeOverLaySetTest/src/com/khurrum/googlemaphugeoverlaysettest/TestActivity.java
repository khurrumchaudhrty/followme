package com.khurrum.googlemaphugeoverlaysettest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.khurrum.googlemaphugeoverlaysettest.R;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 
 * @author khurrumchaudhry
 * 
 */
public class TestActivity extends SherlockFragmentActivity {

	SupportMapFragment fragment;
	List<LatLng> waypoints = new ArrayList<LatLng>();
	TextView status;
	PolylineOptions options = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		status = (TextView) findViewById(R.id.status);
		new Thread() {
			public void run() {
				loadfilefromAssets();
			}
		}.start();
		try {
			MapsInitializer.initialize(getApplicationContext());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}

		fragment = new SupportMapFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragCont, fragment).commit();

	}

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			status.setText("Way Points parsed:" + waypoints.size());
			if (options != null && msg.what != 0)
				fragment.getMap().addPolyline(options);
			fragment.getMap().addPolyline(
					new PolylineOptions()
							.add(new LatLng(51.5, -0.1),
									new LatLng(40.7, -74.0)).width(5)
							.color(Color.RED));
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	private String loadfilefromAssets() {

		// timekeeping
		long start = System.currentTimeMillis();
		try {

			InputStream is = new BufferedInputStream(getApplicationContext()
					.getAssets().open("LocationHistory.json"));
			System.out.println("Data Size:" + is.available());
			JsonFactory jfactory = new JsonFactory();
			double latitude = 0;
			double longitude = 0;

			LatLng ll = new LatLng(0, 0);
			/*** read from file ***/
			JsonParser jParser = jfactory.createParser(is);
			while (jParser.nextToken() != JsonToken.END_OBJECT) {
				String fieldname = jParser.getCurrentName();
				if ("locations".equals(fieldname)) { // got locations
					// got for massive array of 384K+ entries
					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						while (jParser.nextToken() != JsonToken.END_OBJECT) {

							String localentry = jParser.getCurrentName();

							if (localentry != null) {
								if ("activitys".equals(localentry)) {
									jParser.skipChildren();
								} else if ("latitudeE7".equals(localentry)) {
									jParser.nextToken();
									latitude = jParser.getDoubleValue()/10000000;
								} else if ("longitudeE7".equals(localentry)) {
									jParser.nextToken();
									longitude = jParser.getDoubleValue()/10000000;
								}
								
							}
						} // end of single location we should be exausting all
						
						if (latitude != 0 && longitude != 0) {
							LatLng ltlg = new LatLng(latitude,
									longitude);
							waypoints.add(ltlg);
							if(waypoints.size()% 100 == 0){
							System.out.print(ltlg.toString());
							handler.sendEmptyMessage(0);
							}// very high frequency for updating the UI
							// but for sample it is ok
						}
						// the heavy activities
							// Log.d("LATLNG", ll.latitude + ":" +
							// ll.longitude);
						// System.out.println(ll.toString());
						// System.out.print(latitude);
						// System.out.print(":");
						 //System.out.print(ltlg);

					}// end of locations array
				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		long seconds = (end - start) / 1000;
		System.out.println("Seconds to parse:" + seconds);

		options = new PolylineOptions();
		options.addAll(waypoints);
		options.width(5);
		options.color(Color.RED);
		handler.sendEmptyMessage(1);
		return null;

	}
}
