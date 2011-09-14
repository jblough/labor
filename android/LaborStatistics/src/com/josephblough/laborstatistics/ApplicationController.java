package com.josephblough.laborstatistics;

import java.io.IOException;
import java.util.List;

import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.tasks.AutoLocationSelectorTask;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationController";

    // API Key and URL constants
    public static final String API_KEY = "YOUR_DOL_API_KEY_HERE";
    public static final String SHARED_SECRET = "YOUR_DOL_SHARED_SECRET_HERE";
    public static final String API_HOST = "http://api.dol.gov";
    public static final String API_URI = "/V1";
    
    // Saved information that we might want to save as
    //	settings for the user and might want to persist
    //	between Activities
    public String selectedOccupation;
    public String selectedState;
    public String selectedMunicipality;
    //public List<DataDataset> selectedStateMunicipalities; //
    public String enteredComparisonSalary = null;
    
    @Override
    public void onCreate() {
        super.onCreate();

        //Do Application initialization over here
	Log.d(TAG, "onCreate");
	
	//autoSelectClosestLocation();
	new AutoLocationSelectorTask(this).execute();
    }
    
    // Put this functionality in a background ASyncTask to speed up app start
    public void autoSelectClosestLocation() {
	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	Criteria criteria = new Criteria();
	String provider = locationManager.getBestProvider(criteria, false);
	Location location = locationManager.getLastKnownLocation(provider);
	if (location != null) {
	    selectedMunicipality = DataLibrary.getClosestMunicipality(this, location);
	    //Log.i(TAG, "Closest municipality: " + DataLibrary.getArea(this, selectedMunicipality).areaName);
	    Geocoder geocoder = new Geocoder(this);
	    try {
		List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
		if (addresses.size() > 0) {
		    //Log.i(TAG, "Current state: " + addresses.get(0).getAdminArea());
		    selectedState = DataLibrary.getStateAreaCode(this, addresses.get(0).getAdminArea());
		}
	    }
	    catch (IOException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}
    }
}
