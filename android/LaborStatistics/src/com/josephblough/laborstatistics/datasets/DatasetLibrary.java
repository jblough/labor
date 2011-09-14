package com.josephblough.laborstatistics.datasets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import gov.dol.doldata.api.DOLDataContext;
import gov.dol.doldata.api.DOLDataRequest;
import gov.dol.doldata.api.DOLDataRequestCallback;

public class DatasetLibrary {

    public static List<AreaDataset> getStates() {
	final List<AreaDataset> states = new ArrayList<AreaDataset>();
	final ProgressDialog progress = ProgressDialog.show(null, null, "Downloading data", true, true);

	DOLDataContext context = new DOLDataContext(ApplicationController.API_KEY, ApplicationController.SHARED_SECRET, 
		ApplicationController.API_HOST, ApplicationController.API_URI);

	//Instantiate new request object. Pass the context var that contains all the API key info
	//Set this as the callback responder
	DOLDataRequest request = new DOLDataRequest(new DOLDataRequestCallback() {

	    public void DOLDataResultsCallback(List<Map<String, String>> results) {
		// Iterate thru List of results. Add each field to the display List		
		for (Map<String, String> result : results) {
		    AreaDataset area = new AreaDataset();
		    area.areaCode = result.get("area_code");
		    area.areaName = result.get("area_name");
		}
		progress.dismiss();
	    }

	    public void DOLDataErrorCallback(String error) {
		
		progress.dismiss();
	    }
	}, context);
	
	// Hashmap to store the arguments
	HashMap<String, String> args = new HashMap<String, String>(3);

	// Populate the args into the HashMap
	args.put("select", "area_code,area_name");
	args.put("filter", "areatype_code eq 'S'");
	args.put("orderby", "area_name");

	// Call the API method
	request.callAPIMethod("Statistics/OES2010/area", args);

	return states;
    }
    
    public static void getMunicipalities(final int state) {
	
    }
    
    public static void getIndustries() {
	
    }
    
    public static void getOccupations() {
	
    }
    
    public static void getOccupations(final int industry) {
	
    }
}
