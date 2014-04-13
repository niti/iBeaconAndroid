package com.radiusnetworks.ibeaconreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class RangingActivity extends Activity implements IBeaconConsumer {
	protected static final String TAG = "RangingActivity";
	private IBeaconManager iBeaconManager = IBeaconManager
			.getInstanceForApplication(this);

	// create map to store
	Map<String, String> map = new LinkedHashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);

		iBeaconManager.bind(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		iBeaconManager.unBind(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (iBeaconManager.isBound(this))
			iBeaconManager.setBackgroundMode(this, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (iBeaconManager.isBound(this))
			iBeaconManager.setBackgroundMode(this, false);
	}

	@Override
	public void onIBeaconServiceConnect() {
		
		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons,
					Region region) {
				if (iBeacons.size() > 0) {

                    for (IBeacon beacon : iBeacons) {
                        logToDisplay("" + beacon.getAccuracy(), "" + beacon.getProximityUuid(),
                                "" + beacon.getMinor(), "" + beacon.getMajor(), "" + beacon.getProximity(),
                                "" + beacon.getRssi());
                    }
				}
			}

		});
		try {

			iBeaconManager.startRangingBeaconsInRegion(new Region(
					"myRangingUniqueId", "8DEEFBB9-F738-4297-8040-96668BB44281", null, null));

		} catch (RemoteException e) {
		}
		
	}

	private void logToDisplay(final String distance, final String uuid,
			final String minor, final String major, final String proximity,
			final String RSSI) {
		runOnUiThread(new Runnable() {
			public void run() {
				map.put("UUID: " + uuid + " Major: " + major + " Minor: "
						+ minor, proximity + "," + RSSI + "," + distance);
				//listview();

			}
		});

	}

	public void listview() {
		{

			ListView lv = (ListView) findViewById(R.id.list);
			List<String> list = new ArrayList<String>(map.keySet());
			List<String> beacon_list = new ArrayList<String>(list);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					beacon_list);

			// Set the ArrayAdapter as the ListView's adapter.
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String value = (new ArrayList<String>(map.values()))
							.get(position);
					String key = (new ArrayList<String>(map.keySet()))
							.get(position);
					List<String> keyList = Arrays.asList(key.split(" "));
					List<String> valueList = Arrays.asList(value.split(","));

					// Launching new Activity on selecting single List Item
					Intent i = new Intent(getApplicationContext(),
							BeaconSpecs.class);
					// sending data to new activity
					i.putExtra("uuid", keyList.get(1));
					i.putExtra("major", keyList.get(3));
					i.putExtra("minor", keyList.get(5));
					i.putExtra("RSSI", valueList.get(1));
					i.putExtra("proximity", valueList.get(0));
					i.putExtra("distance", valueList.get(2));
					startActivity(i);

				}
			});

		}
	}
}