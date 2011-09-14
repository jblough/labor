package com.josephblough.laborstatistics.data;

import java.util.HashMap;
import java.util.List;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import gov.dol.doldata.api.DOLDataContext;
import gov.dol.doldata.api.DOLDataRequest;
import gov.dol.doldata.api.DOLDataRequestCallback;
import android.content.Context;
import android.util.Log;

public class DataRetriever {

    private static final String TAG = "DataRetriever";
    
    public static void getStateMeanSalaries(final Context context, final String occupationCode, final DOLDataRequestCallback callback) {
	final List<AreaDataset> states = DataLibrary.getAreas(context, "S");

	final StringBuilder builder = new StringBuilder();
	for (AreaDataset state : states) {
	    if (builder.toString().length() < 1000) {
		//final String seriesId = "OEUS" + state.areaCode + "000000" + occupationCode/*"000000"*/ + "04";
		if (builder.length() > 0) {
		    builder.append(" or ");
		}

		builder.append("(series_id eq 'OEUS" + state.areaCode + "000000" + occupationCode + "04')");
	    }
	}
	Log.d(TAG, "filter has " + builder.toString().length() + " characters");

	// Instantiate context object
	DOLDataContext dataContext = new DOLDataContext(ApplicationController.API_KEY, ApplicationController.SHARED_SECRET, 
		ApplicationController.API_HOST, ApplicationController.API_URI);

	//Instantiate new request object. Pass the context var that contains all the API key info
	//Set this as the callback responder
	DOLDataRequest request = new DOLDataRequest(callback, dataContext);

	final DataDataset dataRequest = new DataDataset();
	HashMap<String, String> args = new HashMap<String, String>(3);
	args.put("select", dataRequest.getMinimumFields());
	args.put("filter", builder.toString());

	// Call the API method
	request.callAPIMethod(dataRequest.getMethod(), args);
    }

    public static void getMunicipalityMeanSalaries(final Context context, final String occupationCode, final String state, final DOLDataRequestCallback callback) {
	List<AreaDataset> municipalities = DataLibrary.getMunicipalities(context, state);

	final StringBuilder builder = new StringBuilder();
	for (AreaDataset municipality : municipalities) {
	    //final String seriesId = "OEUM" + municipality.areaCode + "000000" + occupationCode + "04";
	    if (builder.length() > 0) {
		builder.append(" or ");
	    }

	    builder.append("(series_id eq 'OEUM" + municipality.areaCode + "000000" + occupationCode + "04')");
	}

	// Instantiate context object
	DOLDataContext dataContext = new DOLDataContext(ApplicationController.API_KEY, ApplicationController.SHARED_SECRET, 
		ApplicationController.API_HOST, ApplicationController.API_URI);

	//Instantiate new request object. Pass the context var that contains all the API key info
	//Set this as the callback responder
	DOLDataRequest request = new DOLDataRequest(callback, dataContext);

	final DataDataset dataRequest = new DataDataset();
	HashMap<String, String> args = new HashMap<String, String>(3);
	args.put("select", dataRequest.getFields());
	args.put("filter", builder.toString());

	// Call the API method
	request.callAPIMethod(dataRequest.getMethod(), args);
    }

    // OEUMAAAAAAAIIIIIIOOOOOODD
    // U - seasonal code
    // M - area type code
    // A - area code (4-11)
    // I - industry code (11-17) ?
    // O - occupation code (17-23) ?
    // D - data type code (23-25) ?
    
    public static String extractAreaCodeFromSeriesId(final String seriesId) {
	return seriesId.substring(4, 11);
    }

    public static String extractOccupationFromSeriesId(final String seriesId) {
	//return seriesId.substring(11, 17); // TODO !!!
	return seriesId.substring(17, 23); // TODO !!!
    }

    public static String extractIndustryFromSeriesId(final String seriesId) {
	//return seriesId.substring(17, 23); // TODO !!!
	return seriesId.substring(11, 17); // TODO !!!
    }

    public static String extractDataTypeFromSeriesId(final String seriesId) {
	return seriesId.substring(seriesId.length()-2);
    }
}
