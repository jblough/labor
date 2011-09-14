package com.josephblough.laborstatistics.activities.occupation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.DataRetriever;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.IndustryDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.tasks.TopIndustriesRetrieverTask;
import com.josephblough.laborstatistics.tasks.TopLocationsRetrieverTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class OccupationTopSalaryActivity extends ListActivity implements OnItemSelectedListener {

    // This activity will show the top items (industries, states, cities for the same occupation
    // based on salary

    private NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    private Spinner topItemSpinner = null;
    private ProgressDialog progress = null;

    private static final int DISPLAY_INDUSTRIES_INDEX = 0;
    private static final int DISPLAY_STATES_INDEX = 1;
    private static final int DISPLAY_CITIES_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.occupation_top_salaries);

	topItemSpinner = 
		(Spinner)findViewById(R.id.occupation_top_salary_items_spinner);
	topItemSpinner.setOnItemSelectedListener(this);
	
	final ApplicationController app = (ApplicationController)getApplication();
	OccupationDataset occupation = DataLibrary.getOccupation(this, app.selectedOccupation);
	((TextView)findViewById(R.id.occupation_name_top_salary_label)).setText(occupation.occupationName);
    }


    public void onItemSelected(AdapterView<?> adapter, View parent, int position, long id) {
	switch(position) {
	case DISPLAY_INDUSTRIES_INDEX:
	    retrieveIndustries();
	    break;
	case DISPLAY_STATES_INDEX:
	    retrieveStates();
	    break;
	case DISPLAY_CITIES_INDEX:
	    retrieveCities();
	    break;
	}
    }

    public void onNothingSelected(AdapterView<?> adapter) {
	// TODO Auto-generated method stub

    }

    private void retrieveIndustries() {
	progress = ProgressDialog.show(this, "Downloading", "Retrieving Top Industries", true, true);
	final ApplicationController app = (ApplicationController)getApplication();
	
	new TopIndustriesRetrieverTask(DataTransport.SALARY_CRITERIA, new SalaryDisplayCallback()).execute(app.selectedOccupation);
	
    }

    private void retrieveStates() {
	progress = ProgressDialog.show(this, "Downloading", "Retrieving Top States", true, true);
	final ApplicationController app = (ApplicationController)getApplication();
	
	new TopLocationsRetrieverTask(DataTransport.STATE_AREA_TYPE, DataTransport.SALARY_CRITERIA, 
		new SalaryDisplayCallback()).execute(app.selectedOccupation);
	
    }

    private void retrieveCities() {
	progress = ProgressDialog.show(this, "Downloading", "Retrieving Top Cities", true, true);
	final ApplicationController app = (ApplicationController)getApplication();
	
	new TopLocationsRetrieverTask(DataTransport.MUNICIPALITY_AREA_TYPE, DataTransport.SALARY_CRITERIA, 
		new SalaryDisplayCallback()).execute(app.selectedOccupation);
	
    }

    private class SalaryDisplayCallback implements DataTransportCallback {

	public void success(List<DataDataset> items) {

	    // Convert list of DataDataset into industry list, by looking up each
	    // 	industry code and retrieving the name
	    // Fill a ArrayAdapter<String> and set the ListView adapter
	    // to that adapter
	    ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();

	    for (final DataDataset item : items) {
		final String name = getName(item);
		if (name != null) {
		    HashMap<String,String> itemMap = new HashMap<String,String>();
		    final String value = moneyFormatter.format(Double.valueOf(item.value));
		    itemMap.put("name", name);
		    itemMap.put("value", value);
		    data.add(itemMap);
		}
	    }

	    SimpleAdapter adapter = new SimpleAdapter(
		    OccupationTopSalaryActivity.this, data, 
		    R.layout.occupation_industry_row, 
		    new String[] {"name", "value"}, 
		    new int[] {R.id.comparison_row_industry_name, R.id.comparison_row_industry_value});
	    setListAdapter(adapter);

	    if (progress != null)
		progress.dismiss();


	    if (items.size() == 0)
		Toast.makeText(OccupationTopSalaryActivity.this, R.string.no_data, Toast.LENGTH_LONG).show();
	}

	public void error(String error) {
	    if (progress != null)
		progress.dismiss();

	    Toast.makeText(OccupationTopSalaryActivity.this, error, Toast.LENGTH_LONG).show();
	}

	private String getName(final DataDataset data) {
	    if (topItemSpinner.getSelectedItemPosition() == DISPLAY_INDUSTRIES_INDEX) {
		final String industryCode = 
			DataRetriever.extractIndustryFromSeriesId(data.seriesId);
		final IndustryDataset industry = DataLibrary.getIndustry(
			OccupationTopSalaryActivity.this, industryCode);
		if (industry != null)
		    return industry.industryName;
	    }
	    else {
		final String areaCode = DataRetriever.extractAreaCodeFromSeriesId(data.seriesId);
		final AreaDataset area = DataLibrary.getArea(OccupationTopSalaryActivity.this, areaCode);
		if (area != null)
		    return area.areaName;
	    }
	    return null;
	}
    };
}
