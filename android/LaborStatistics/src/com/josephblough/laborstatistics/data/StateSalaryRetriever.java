package com.josephblough.laborstatistics.data;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import gov.dol.doldata.api.DOLDataContext;
import gov.dol.doldata.api.DOLDataRequest;
import gov.dol.doldata.api.DOLDataRequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class StateSalaryRetriever {

    private static final String TAG = "StateSalaryRetriever";
    
    private Context context;
    private String occupationCode;
    private DOLDataRequestCallback callback;
    private List<AreaDataset> states;
    private int currentIndex;
    private List<Map<String, String>> results;
    
    public StateSalaryRetriever(final Context context, final String occupationCode, final DOLDataRequestCallback callback) {
	this.context = context;
	this.occupationCode = occupationCode;
	this.callback = callback;
	this.currentIndex = 0;
    }
    
    public void getStateMeanSalaries(final ProgressDialog progress) {
	if (this.states == null)
	    this.states = DataLibrary.getAreas(context, "S");

	final StringBuilder builder = new StringBuilder();
	//for (AreaDataset state : states) {
	final String startingState = this.states.get(this.currentIndex).areaName;
	for (;(this.currentIndex < this.states.size()) && (builder.length() < 1000); this.currentIndex++) {
	    if (builder.length() > 0) {
		builder.append(" or ");
	    }

	    builder.append("(series_id eq 'OEUS" + states.get(this.currentIndex).areaCode + "000000" + occupationCode + "04')");
	}

	Log.d(TAG, "filter has " + builder.toString().length() + " characters");
	final String endingState = this.states.get(this.currentIndex-1).areaName;
	progress.setMessage("Downloading " + startingState + " through " + endingState);

	// Instantiate context object
	DOLDataContext dataContext = new DOLDataContext(ApplicationController.API_KEY, ApplicationController.SHARED_SECRET, 
		ApplicationController.API_HOST, ApplicationController.API_URI);

	//Instantiate new request object. Pass the context var that contains all the API key info
	//Set this as the callback responder
	DOLDataRequest request = new DOLDataRequest(new DOLDataRequestCallback() {
	    
	    public void DOLDataResultsCallback(List<Map<String, String>> results) {
		if (StateSalaryRetriever.this.results == null) {
		    StateSalaryRetriever.this.results = new ArrayList<Map<String,String>>();
		}
		
		StateSalaryRetriever.this.results.addAll(results);
		
		if (currentIndex >= states.size()) {
		    callback.DOLDataResultsCallback(StateSalaryRetriever.this.results);
		}
		else {
		    StateSalaryRetriever.this.getStateMeanSalaries(progress);
		}
	    }
	    
	    public void DOLDataErrorCallback(String error) {
		callback.DOLDataErrorCallback(error);
	    }
	}, dataContext);

	final DataDataset dataRequest = new DataDataset();
	HashMap<String, String> args = new HashMap<String, String>(3);
	args.put("select", dataRequest.getMinimumFields());
	args.put("filter", builder.toString());

	// Call the API method
	request.callAPIMethod(dataRequest.getMethod(), args);
    }
}
