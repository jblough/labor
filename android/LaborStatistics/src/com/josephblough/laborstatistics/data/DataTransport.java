package com.josephblough.laborstatistics.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

public class DataTransport {
    private static final String TAG = "DataTransport";

    // Change this line to point to your server host where the php scripts reside
    private static final String HOST_NAME = "http://www.marble-maze.com/labor/";

    private static final String MUNICIPALITIES_URL = HOST_NAME + "municipalities.php";
    private static final String OCCUPATIONS_URL = HOST_NAME + "occupations.php";
    private static final String INDUSTRIES_URL = HOST_NAME + "industries.php";
    private static final String LOCATIONS_URL = HOST_NAME + "locations.php";
    private static final String SUMMARY_URL = HOST_NAME + "summary.php";
    
    public static final int SALARY_CRITERIA = 0;
    public static final int EMPLOYMENT_CRITERIA = 1;

    public static final int STATE_AREA_TYPE = 0;
    public static final int MUNICIPALITY_AREA_TYPE = 1;
    
    public static List<DataDataset> getMunicipalities(final String stateAbbreviation, final String occupationCode) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    final String url = MUNICIPALITIES_URL + "?state=" + stateAbbreviation + "&occupation=" + occupationCode;
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    JSONArray municipalities = json.getJSONArray("municipalities");
	    int count = municipalities.length();
	    List<DataDataset> data = new ArrayList<DataDataset>();
	    for (int i=0; i<count; i++) {
		JSONObject municipality = municipalities.getJSONObject(i);
		DataDataset dataset = new DataDataset();
		dataset.seriesId = municipality.optString("series_id", null);
		dataset.value = municipality.optString("value", null);
		dataset.footnoteCodes = municipality.optString("footnote_codes", null);
		data.add(dataset);
	    }
	    return data;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static List<DataDataset> getTopOccupations(final String industryCode, final int criteria) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    String url = OCCUPATIONS_URL + "?&industry=" + industryCode;
	    if (criteria != SALARY_CRITERIA)
		url = url + "&employment=1";
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    JSONArray occupations = json.getJSONArray("occupations");
	    int count = occupations.length();
	    List<DataDataset> data = new ArrayList<DataDataset>();
	    for (int i=0; i<count; i++) {
		JSONObject occupation = occupations.getJSONObject(i);
		DataDataset dataset = new DataDataset();
		dataset.seriesId = occupation.optString("series_id", null);
		dataset.value = occupation.optString("value", null);
		dataset.footnoteCodes = occupation.optString("footnote_codes", null);
		data.add(dataset);
	    }
	    return data;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static List<DataDataset> getTopIndustries(final String occupationCode, final int criteria) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    String url = INDUSTRIES_URL + "?&occupation=" + occupationCode;
	    if (criteria != SALARY_CRITERIA)
		url = url + "&employment=1";
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    JSONArray industries = json.getJSONArray("industries");
	    int count = industries.length();
	    List<DataDataset> data = new ArrayList<DataDataset>();
	    for (int i=0; i<count; i++) {
		JSONObject industry = industries.getJSONObject(i);
		DataDataset dataset = new DataDataset();
		dataset.seriesId = industry.optString("series_id", null);
		dataset.value = industry.optString("value", null);
		dataset.footnoteCodes = industry.optString("footnote_codes", null);
		data.add(dataset);
	    }
	    return data;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static List<DataDataset> getTopLocations(final String occupationCode, final int areatype, final int criteria) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    String url = LOCATIONS_URL + "?&occupation=" + occupationCode;
	    if (areatype != STATE_AREA_TYPE)
		url = url + "&areatype=M";
	    if (criteria != SALARY_CRITERIA)
		url = url + "&employment=1";
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    JSONArray industries = json.getJSONArray("locations");
	    int count = industries.length();
	    List<DataDataset> data = new ArrayList<DataDataset>();
	    for (int i=0; i<count; i++) {
		JSONObject industry = industries.getJSONObject(i);
		DataDataset dataset = new DataDataset();
		dataset.seriesId = industry.optString("series_id", null);
		dataset.value = industry.optString("value", null);
		dataset.footnoteCodes = industry.optString("footnote_codes", null);
		data.add(dataset);
	    }
	    return data;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static List<DataDataset> getTopOccupationsForArea(final String areaCode, final int criteria) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    String url = OCCUPATIONS_URL + "?&area=" + areaCode;
	    if (criteria != SALARY_CRITERIA)
		url = url + "&employment=1";
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    JSONArray occupations = json.getJSONArray("occupations");
	    int count = occupations.length();
	    List<DataDataset> data = new ArrayList<DataDataset>();
	    for (int i=0; i<count; i++) {
		JSONObject occupation = occupations.getJSONObject(i);
		DataDataset dataset = new DataDataset();
		dataset.seriesId = occupation.optString("series_id", null);
		dataset.value = occupation.optString("value", null);
		dataset.footnoteCodes = occupation.optString("footnote_codes", null);
		data.add(dataset);
	    }
	    return data;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static StatisticalSummary getNationalStatisticalSummary(final String occupationCode) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    final String url = SUMMARY_URL + "?occupation=" + occupationCode;
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    return parseSummary(new JSONObject(response));
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static StatisticalSummary getStatisticalSummary(final String occupationCode, final String areaCode) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    final String url = SUMMARY_URL + "?occupation=" + occupationCode + "&area=" + areaCode;
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    return parseSummary(new JSONObject(response));
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    private static StatisticalSummary parseSummary(final JSONObject json) throws JSONException {
	StatisticalSummary summary = new StatisticalSummary();
	JSONArray values = json.getJSONArray("values");
	int count = values.length();
	for (int i=0; i<count; i++) {
	    JSONObject value = values.getJSONObject(i);
	    final String seriesId = value.optString("series_id", null);
	    final String datatype = DataRetriever.extractDataTypeFromSeriesId(seriesId);
	    if ("01".equals(datatype)) {
		summary.employment = value.getString("value");
		summary.employmentFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("02".equals(datatype)) {
		summary.employmentPercentRelativeStandardError = value.getString("value");
		summary.employmentPercentRelativeStandardErrorFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("03".equals(datatype)) {
		summary.hourlyMeanWage = value.getString("value");
		summary.hourlyMeanWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("04".equals(datatype)) {
		summary.annualMeanWage = value.getString("value");
		summary.annualMeanWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("05".equals(datatype)) {
		summary.wagePercentRelativeStandardError = value.getString("value");
		summary.wagePercentRelativeStandardErrorFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("06".equals(datatype)) {
		summary.hourly10thPercentileWage = value.getString("value");
		summary.hourly10thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("07".equals(datatype)) {
		summary.hourly25thPercentileWage = value.getString("value");
		summary.hourly25thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("08".equals(datatype)) {
		summary.hourlyMedianWage = value.getString("value");
		summary.hourlyMedianWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("09".equals(datatype)) {
		summary.hourly75thPercentileWage = value.getString("value");
		summary.hourly75thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("10".equals(datatype)) {
		summary.hourly90thPercentileWage = value.getString("value");
		summary.hourly90thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("11".equals(datatype)) {
		summary.annual10thPercentileWage = value.getString("value");
		summary.annual10thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("12".equals(datatype)) {
		summary.annual25thPercentileWage = value.getString("value");
		summary.annual25thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("13".equals(datatype)) {
		summary.annualMedianWage = value.getString("value");
		summary.annualMedianWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("14".equals(datatype)) {
		summary.annual75thPercentileWage = value.getString("value");
		summary.annual75thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	    else if ("15".equals(datatype)) {
		summary.annual90thPercentileWage = value.getString("value");
		summary.annual90thPercentileWageFootnoteCodes = value.optString("footnote_codes", null);
	    }
	}
	return summary;
    }
}
