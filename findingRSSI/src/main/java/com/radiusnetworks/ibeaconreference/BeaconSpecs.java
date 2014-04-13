package com.radiusnetworks.ibeaconreference;

import android.os.Bundle;
import android.app.Activity;
import android.os.RemoteException;
import android.view.Menu;
import android.widget.TextView;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import java.util.Collection;

public class BeaconSpecs extends Activity implements IBeaconConsumer {
	String uuid;
    private IBeaconManager iBeaconManager = IBeaconManager
            .getInstanceForApplication(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_specs);
         
		String distance = getIntent().getExtras().getString("distance");
	    uuid = getIntent().getExtras().getString("uuid");
		String proximity = getIntent().getExtras().getString("proximity");
		String RSSI = getIntent().getExtras().getString("RSSI");

        updateDisplay(distance, proximity, RSSI);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beacon_specs, menu);
		return true;
	}

    @Override
    public void onIBeaconServiceConnect() {

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons,
                                                Region region) {
                if (iBeacons.size() > 0) {
                    IBeacon beacon = iBeacons.iterator().next();

                    String distance = ""+beacon.getAccuracy();
                    String proximity = ""+beacon.getProximity();
                    String RSSI = ""+beacon.getRssi();

                    updateDisplay(distance, proximity, RSSI);
                }
            }

        });
        try {
            String major = getIntent().getExtras().getString("major");
            String minor = getIntent().getExtras().getString("minor");

            iBeaconManager.startRangingBeaconsInRegion(new Region(
                    "myRangingUniqueId", uuid, Integer.valueOf(major), Integer.valueOf(minor)));

            } catch (RemoteException e) {
        }
    }

    private void updateDisplay(final String distance, final String proximity, final String RSSI) {
        final String minor = getIntent().getExtras().getString("minor");
        final String major = getIntent().getExtras().getString("major");

        runOnUiThread(new Runnable() {
            public void run() {
                TextView uuidTv = (TextView) BeaconSpecs.this.findViewById(R.id.UUID);
                uuidTv.setText("UUID:" + uuid);
                TextView minorTv = (TextView) BeaconSpecs.this.findViewById(R.id.minor);
                minorTv.setText("Minor:" + minor);
                TextView majorTv = (TextView) BeaconSpecs.this.findViewById(R.id.major);
                majorTv.setText("Major:" + major);
                TextView distanceTv = (TextView) BeaconSpecs.this
                        .findViewById(R.id.distance);
                distanceTv.setText("Distance:" + distance + "meters");
                TextView proximityTv = (TextView) BeaconSpecs.this
                        .findViewById(R.id.proximity);
                proximityTv.setText("Proximity:" + proximity);
                TextView rssitv = (TextView) BeaconSpecs.this.findViewById(R.id.rssi);
                rssitv.setText("RSSI:" + RSSI);
            }
        });
    }
}
