package com.josephblough.laborstatistics.data;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.google.android.maps.GeoPoint;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;

public class StateLibrary {

    private static HashMap<String, GeoPoint> stateCoordinates = null;
    private static HashMap<String, String> areaCodeToStateNameMap = null;
    private static HashMap<String, String> stateLookupMap = null;
    
    public static GeoPoint getStateCoordinates(final Context context, final String stateName) {
	if (stateCoordinates == null) {
	    populateStateCoordinates(context);
	}
	return stateCoordinates.get(stateName);
    }
    
    public static String getStateName(final Context context, final String areaCode) {
	if (areaCodeToStateNameMap == null) {
	    populateStateCoordinates(context);
	}
	return areaCodeToStateNameMap.get(areaCode);
    }
    
    public static String getStateAbbreviation(final Context context, final String stateName) {
	if (stateLookupMap == null) {
	    populateStateCoordinates(context);
	}
	return stateLookupMap.get(stateName);
    }
    
    public static synchronized void populateStateCoordinates(final Context context) {
	if (stateCoordinates == null) {
	    stateCoordinates = new HashMap<String, GeoPoint>();

	    stateCoordinates.put("Alabama", getPoint(32.3182314, -86.902298));
	    stateCoordinates.put("Alaska", getPoint(63.588753, -154.4930619));
	    stateCoordinates.put("Arizona", getPoint(34.0489281, -111.0937311));
	    stateCoordinates.put("Arkansas", getPoint(35.20105, -91.8318334));
	    stateCoordinates.put("California", getPoint(36.778261, -119.4179324));
	    stateCoordinates.put("Colorado", getPoint(39.5500507, -105.7820674));
	    stateCoordinates.put("Connecticut", getPoint(41.6032207, -73.087749));
	    stateCoordinates.put("District of Columbia", getPoint(38.8951118, -77.0363658));
	    stateCoordinates.put("Delaware", getPoint(38.9108325, -75.5276699));
	    stateCoordinates.put("Florida", getPoint(27.6648274, -81.5157535));
	    stateCoordinates.put("Georgia", getPoint(32.1574351, -82.907123));
	    stateCoordinates.put("Hawaii", getPoint(19.8967662, -155.5827818));
	    stateCoordinates.put("Idaho", getPoint(44.0682019, -114.7420408));
	    stateCoordinates.put("Illinois", getPoint(40.6331249, -89.3985283));
	    stateCoordinates.put("Indiana", getPoint(40.5512165, -85.6023643));
	    stateCoordinates.put("Iowa", getPoint(41.8780025, -93.097702));
	    stateCoordinates.put("Kansas", getPoint(39.011902, -98.4842465));
	    stateCoordinates.put("Kentucky", getPoint(37.8393332, -84.2700179));
	    stateCoordinates.put("Louisiana", getPoint(31.2448234, -92.1450245));
	    stateCoordinates.put("Maine", getPoint(45.253783, -69.4454689));
	    stateCoordinates.put("Maryland", getPoint(39.0457549, -76.6412712));
	    stateCoordinates.put("Massachusetts", getPoint(42.4072107, -71.3824374));
	    stateCoordinates.put("Michigan", getPoint(42.732535, -84.5555347));
	    stateCoordinates.put("Minnesota", getPoint(46.729553, -94.6858998));
	    stateCoordinates.put("Mississippi", getPoint(32.3546679, -89.3985283));
	    stateCoordinates.put("Missouri", getPoint(37.9642529, -91.8318334));
	    stateCoordinates.put("Montana", getPoint(46.8796822, -110.3625658));
	    stateCoordinates.put("Nebraska", getPoint(41.4925374, -99.9018131));
	    stateCoordinates.put("Nevada", getPoint(38.8026097, -116.419389));
	    stateCoordinates.put("New Hampshire", getPoint(43.1938516, -71.5723953));
	    stateCoordinates.put("New Jersey", getPoint(40.0583238, -74.4056612));
	    stateCoordinates.put("New Mexico", getPoint(34.9727305, -105.0323635));
	    stateCoordinates.put("New York", getPoint(40.7143528, -74.0059731));
	    stateCoordinates.put("North Carolina", getPoint(35.7595731, -79.0192997));
	    stateCoordinates.put("North Dakota", getPoint(47.5514926, -101.0020119));
	    stateCoordinates.put("Ohio", getPoint(40.4172871, -82.907123));
	    stateCoordinates.put("Oklahoma", getPoint(35.0077519, -97.092877));
	    stateCoordinates.put("Oregon", getPoint(43.8041334, -120.5542012));
	    stateCoordinates.put("Pennsylvania", getPoint(41.2033216, -77.1945247));
	    stateCoordinates.put("Rhode Island", getPoint(41.5800945, -71.4774291));
	    stateCoordinates.put("South Carolina", getPoint(33.836081, -81.1637245));
	    stateCoordinates.put("South Dakota", getPoint(43.9695148, -99.9018131));
	    stateCoordinates.put("Tennessee", getPoint(35.5174913, -86.5804473));
	    stateCoordinates.put("Texas", getPoint(31.9685988, -99.9018131));
	    stateCoordinates.put("Utah", getPoint(39.3209801, -111.0937311));
	    stateCoordinates.put("Vermont", getPoint(44.5588028, -72.5778415));
	    stateCoordinates.put("Virginia", getPoint(37.4315734, -78.6568942));
	    stateCoordinates.put("Washington", getPoint(38.8951118, -77.0363658));
	    stateCoordinates.put("West Virginia", getPoint(38.5976262, -80.4549026));
	    stateCoordinates.put("Wisconsin", getPoint(43.7844397, -88.7878678));
	    stateCoordinates.put("Wyoming", getPoint(43.0759678, -107.2902839));
	    stateCoordinates.put("Guam", getPoint(13.444304, 144.793731));
	    stateCoordinates.put("Puerto Rico", getPoint(18.220833, -66.590149));
	    stateCoordinates.put("Virgin Islands", getPoint(18.335765, -64.896335));
	}
	
	if (areaCodeToStateNameMap == null) {
	    areaCodeToStateNameMap = new HashMap<String, String>();
	    
	    List<AreaDataset> states = DataLibrary.getAreas(context, "S");
	    for (AreaDataset state : states) {
		areaCodeToStateNameMap.put(state.areaCode, state.areaName);
	    }
	}

	if (stateLookupMap == null) {
	    stateLookupMap = new HashMap<String, String>();

	    stateLookupMap.put("Alabama", "AL");
	    stateLookupMap.put("Alaska", "AK");
	    stateLookupMap.put("Arizona", "AZ");
	    stateLookupMap.put("Arkansas", "AR");
	    stateLookupMap.put("California", "CA");
	    stateLookupMap.put("Colorado", "CO");
	    stateLookupMap.put("Connecticut", "CT");
	    stateLookupMap.put("Delaware", "DE");
	    stateLookupMap.put("District of Columbia", "DC");
	    stateLookupMap.put("Florida", "FL");
	    stateLookupMap.put("Georgia", "GA");
	    stateLookupMap.put("Hawaii", "HI");
	    stateLookupMap.put("Idaho", "ID");
	    stateLookupMap.put("Illinois", "IL");
	    stateLookupMap.put("Indiana", "IN");
	    stateLookupMap.put("Iowa", "IA");
	    stateLookupMap.put("Kansas", "KS");
	    stateLookupMap.put("Kentucky", "KY");
	    stateLookupMap.put("Louisiana", "LA");
	    stateLookupMap.put("Maine", "ME");
	    stateLookupMap.put("Maryland", "MD");
	    stateLookupMap.put("Massachusetts", "MA");
	    stateLookupMap.put("Michigan", "MI");
	    stateLookupMap.put("Minnesota", "MN");
	    stateLookupMap.put("Mississippi", "MS");
	    stateLookupMap.put("Missouri", "MO");
	    stateLookupMap.put("Montana", "MT");
	    stateLookupMap.put("Nebraska", "NE");
	    stateLookupMap.put("Nevada", "NV");
	    stateLookupMap.put("New Hampshire", "NH");
	    stateLookupMap.put("New Jersey", "NJ");
	    stateLookupMap.put("New Mexico", "NM");
	    stateLookupMap.put("New York", "NY");
	    stateLookupMap.put("North Carolina", "NC");
	    stateLookupMap.put("North Dakota", "ND");
	    stateLookupMap.put("Ohio", "OH");
	    stateLookupMap.put("Oklahoma", "OK");
	    stateLookupMap.put("Oregon", "OR");
	    stateLookupMap.put("Pennsylvania", "PA");
	    stateLookupMap.put("Rhode Island", "RI");
	    stateLookupMap.put("South Carolina", "SC");
	    stateLookupMap.put("South Dakota", "SD");
	    stateLookupMap.put("Tennessee", "TN");
	    stateLookupMap.put("Texas", "TX");
	    stateLookupMap.put("Utah", "UT");
	    stateLookupMap.put("Vermont", "VT");
	    stateLookupMap.put("Virginia", "VA");
	    stateLookupMap.put("Washington", "WA");
	    stateLookupMap.put("West Virginia", "WV");
	    stateLookupMap.put("Wisconsin", "WI");
	    stateLookupMap.put("Wyoming", "WY");
	}
    }
    
    public static GeoPoint getPoint(double lat, double lon) {
	return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
    }
}
