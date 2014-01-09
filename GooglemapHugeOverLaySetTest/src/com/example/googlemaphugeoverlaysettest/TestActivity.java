package com.example.googlemaphugeoverlaysettest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		new Thread() {
			public void run() {
				loadfilefromAssets();
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	private String loadfilefromAssets() {

		//timekeeping
		long start = System.currentTimeMillis();
		try {
			
			InputStream is = new BufferedInputStream(getApplicationContext().getAssets().open(
					"LocationHistory.json"));
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
								if("activitys".equals(localentry)){
									jParser.skipChildren();
								}
								if ("latitudeE7".equals(localentry)) {
									jParser.nextToken();
									latitude = jParser.getDoubleValue();
								}
								if ("longitudeE7".equals(localentry)) {
									jParser.nextToken();
									longitude = jParser.getDoubleValue();
								}
								ll = new LatLng(latitude, longitude);
							}
						} // end of single location we should be exausting all
							// the heavy activities
//						Log.d("LATLNG", ll.latitude + ":" + ll.longitude);
//						System.out.println(ll.toString());
//						System.out.print(latitude);
//						System.out.print(":");
//						System.out.print(longitude);
						
					}// end of locations array
				}

			}

			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		long end = System.currentTimeMillis();
		long seconds = (end-start)/1000;
		System.out.println("Seconds to parse:" + seconds);
		
		
		return null;

	}

}
