package com.josephblough.laborstatistics.activities.location;

import java.util.List;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.adapters.IndustryOccupationsAdapter;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.DataRetriever;
import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import com.josephblough.laborstatistics.tasks.AreaOccupationsRetrieverTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LocationTopOccupationsActivity extends ListActivity implements OnItemSelectedListener {

    public static final String AREA_CODE = "LocationResultsListActivity.area_code";
    
    private AreaDataset area;
    private Spinner sortOrderSpinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.location_top_occupations);
        
        /* -- Highest paying occupations for a geographic location
         * SELECT o.*, CAST(d.value AS NUMERIC) AS value FROM occupation o
         * JOIN series s ON o.occupation_code = s.occupation_code
         * JOIN data d ON d.series_id = s.series_id
         * WHERE s.datatype_code = '04'
         * AND s.areatype_code = ?
         * AND s.area_code = ?
         * AND s.occupation_code != '000000'
         * AND s.industry_code = '000000'
         * AND d.value != '-'
         * ORDER BY value DESC -- o.occupation_name
         * LIMIT 30
         */

        /* -- Highest employment occupations for a geographic location
         * SELECT o.*, CAST(d.value AS NUMERIC) AS value FROM occupation o
         * JOIN series s ON o.occupation_code = s.occupation_code
         * JOIN data d ON d.series_id = s.series_id
         * WHERE s.datatype_code = '01'
         * AND s.areatype_code = ?
         * AND s.area_code = ?
         * AND s.occupation_code != '000000'
         * AND s.industry_code = '000000'
         * AND d.value != '-'
         * ORDER BY value DESC -- o.occupation_name
         * LIMIT 30
         */
        
        String areaCode = getIntent().getStringExtra(AREA_CODE);
        if (areaCode != null) {
            area = DataLibrary.getArea(this, areaCode);
            
            TextView areaNameLabel = (TextView) findViewById(R.id.location_area_name);
            areaNameLabel.setText(area.areaName);
            areaNameLabel.setVisibility(View.VISIBLE);
        }
        
        sortOrderSpinner = 
        	(Spinner)findViewById(R.id.location_occupation_sort_order_spinner);
        sortOrderSpinner.setOnItemSelectedListener(this);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
		DataDataset data = (DataDataset)adapter.getItemAtPosition(position);
		
		Intent intent = new Intent(LocationTopOccupationsActivity.this, LocationOccupationDetailsActivity.class);
		intent.putExtra(LocationOccupationDetailsActivity.OCCUPATION_CODE, 
			DataRetriever.extractOccupationFromSeriesId(data.seriesId));
		startActivity(intent);
	    }
	});
    }

    public void onItemSelected(AdapterView<?> adapter, View parent, int position, long id) {
	retrieveOccupations(position); // 0 - salary, 1 - employment
    }

    public void onNothingSelected(AdapterView<?> adapter) {
	// TODO Auto-generated method stub
	
    }
    
    private void retrieveOccupations(final int sort) {
	final ProgressDialog progress = ProgressDialog.show(this, "Downloading", "Retrieving Top Occupations for " + area.areaName, true, true);
	
	new AreaOccupationsRetrieverTask(sort, new DataTransportCallback() {
	    
	    public void success(List<DataDataset> occupations) {
		
		// Convert list of DataDataset into industry list, by looking up each
		// 	industry code and retrieving the name
		// Fill a ArrayAdapter<String> and set the ListView adapter
		// to that adapter
		IndustryOccupationsAdapter adapter = new IndustryOccupationsAdapter(LocationTopOccupationsActivity.this, 
			occupations, sortOrderSpinner.getSelectedItemPosition());
		setListAdapter(adapter);

		if (progress != null)
		    progress.dismiss();
		
		if (occupations.size() == 0)
		    Toast.makeText(LocationTopOccupationsActivity.this, R.string.no_data, Toast.LENGTH_LONG).show();
	    }
	    
	    public void error(String error) {
		if (progress != null)
		    progress.dismiss();

		Toast.makeText(LocationTopOccupationsActivity.this, error, Toast.LENGTH_LONG).show();
	    }
	}).execute(area.areaCode);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.generic_menu, menu);
	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.help_menu_item:
	    Intent intent = new Intent(this, DocumentationActivity.class);
	    intent.putExtra(DocumentationActivity.DEFAULT_PAGE, DocumentationActivity.LOCATIONS_PAGE);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
}
