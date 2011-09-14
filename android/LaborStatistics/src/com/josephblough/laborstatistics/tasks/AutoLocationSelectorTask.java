package com.josephblough.laborstatistics.tasks;

import java.io.IOException;
import java.util.List;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.data.DataLibrary;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

public class AutoLocationSelectorTask extends AsyncTask<Void, Void, Void> {

    private final static String TAG = "AutoLocationSelectorTask";
    
    private ApplicationController app;
    private String selectedMunicipality = null;
    private String selectedState = null;
    
    public AutoLocationSelectorTask(ApplicationController app) {
	this.app = app;
    }
    
    @Override
    protected Void doInBackground(Void... arg0) {
	LocationManager locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
	Criteria criteria = new Criteria();
	String provider = locationManager.getBestProvider(criteria, false);
	Location location = locationManager.getLastKnownLocation(provider);
	if (location != null) {
	    this.selectedMunicipality = DataLibrary.getClosestMunicipality(app, location);
	    //Log.i(TAG, "Closest municipality: " + DataLibrary.getArea(this, selectedMunicipality).areaName);
	    Geocoder geocoder = new Geocoder(app);
	    try {
		List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
		if (addresses.size() > 0) {
		    //Log.i(TAG, "Current state: " + addresses.get(0).getAdminArea());
		    this.selectedState = DataLibrary.getStateAreaCode(app, addresses.get(0).getAdminArea());
		}
	    }
	    catch (IOException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}

	return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        
        if (app.selectedState == null)
            app.selectedState = this.selectedState;
        if (app.selectedMunicipality == null)
            app.selectedMunicipality = this.selectedMunicipality;
    }
}
