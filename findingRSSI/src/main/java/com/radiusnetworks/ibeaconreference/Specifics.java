package com.radiusnetworks.ibeaconreference;

public class Specifics {
	String majorSpec;
	String minorSpec;
	String proximitySpec;
	String rssiSpec;
	String distanceSpec;
	String uuidSpec;
public Specifics(String major,String minor,String proximity,String rssi,String distance){
	majorSpec=major;
	minorSpec=minor;
	proximitySpec=proximity;
	rssiSpec=rssi;
	distanceSpec=distance;
}


public Specifics(String uuid,String major,String minor){
	majorSpec=major;
	minorSpec=minor;
	uuidSpec=uuid;
	
}
}
