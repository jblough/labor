package com.josephblough.laborstatistics.activities.industry;

import java.util.List;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.adapters.IndustryOccupationsAdapter;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.DataRetriever;
import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.IndustryDataset;
import com.josephblough.laborstatistics.tasks.TopOccupationsRetrieverTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class IndustryTopOccupationsActivity extends ListActivity implements OnItemSelectedListener {

    /*
     * This Activity will display the top 10 or 100 occupations
     * 	for the industry based on either then mean annual salary
     * 	or employment number.  Also allow for pulling the data
     * 	at a National, State, or Municipality level. 
     */
    
    /* -- All Occupations listed alphabetically 
     * SELECT o.* FROM occupation o
     * JOIN series s ON o.occupation_code = s.occupation_code
     * JOIN industry i ON i.industry_code = s.industry_code
     * WHERE s.industry_code = ? AND s.datatype_code = '04'
     * AND s.areatype_code = 'N' AND s.area_code = '0000000'
     * AND o.occupation_code != '000000'
     * ORDER BY o.occupation_name
     */
    
    /* -- Top 30 occupations by annual salary
     * SELECT o.*, d.value FROM occupation o
     * JOIN series s ON o.occupation_code = s.occupation_code
     * JOIN industry i ON i.industry_code = s.industry_code
     * JOIN data d ON d.series_id = s.series_id
     * WHERE s.industry_code = '212100' AND s.datatype_code = '04'
     * AND s.areatype_code = 'N' AND s.area_code = '0000000'
     * AND o.occupation_code != '000000'
     * AND d.value != '-'
     * ORDER BY CAST(d.value AS NUMERIC) DESC -- o.occupation_name
     * LIMIT 30
     */

    /* -- Top 30 occupations by employment
     * SELECT o.*, d.value FROM occupation o
     * JOIN series s ON o.occupation_code = s.occupation_code
     * JOIN industry i ON i.industry_code = s.industry_code
     * JOIN data d ON d.series_id = s.series_id
     * WHERE s.industry_code = '212100' AND s.datatype_code = '01'
     * AND s.areatype_code = 'N' AND s.area_code = '0000000'
     * AND o.occupation_code != '000000'
     * AND d.value != '-'
     * ORDER BY CAST(d.value AS NUMERIC) DESC -- o.occupation_name
     * LIMIT 30
     */

    public static final String INDUSTRY = "IndustryTopOccupationsActivity.industry";
    
    private IndustryDataset industry = null;
    private Spinner sortOrderSpinner = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.industry_top_occupations);
        
        String industryCode = getIntent().getStringExtra(INDUSTRY);

        // All industries if not specified
        industry = DataLibrary.getIndustry(this, (industryCode != null) ? industryCode : "000000");

        TextView industryNameLabel = (TextView) findViewById(R.id.industry_name);
        industryNameLabel.setText(industry.industryName);
        industryNameLabel.setVisibility(View.VISIBLE);
        
        sortOrderSpinner = 
        	(Spinner)findViewById(R.id.industry_occupation_sort_order_spinner);
        sortOrderSpinner.setOnItemSelectedListener(this);
        
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
		DataDataset data = (DataDataset)adapter.getItemAtPosition(position);
		
		Intent intent = new Intent(IndustryTopOccupationsActivity.this, IndustryOccupationDetailsActivity.class);
		intent.putExtra(IndustryOccupationDetailsActivity.OCCUPATION_CODE, 
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
	final ProgressDialog progress = ProgressDialog.show(this, "Downloading", "Retrieving Top Occupations", true, true);

	new TopOccupationsRetrieverTask(sort, new DataTransportCallback() {

	    public void success(List<DataDataset> occupations) {

		// Convert list of DataDataset into industry list, by looking up each
		// 	industry code and retrieving the name
		// Fill a ArrayAdapter<String> and set the ListView adapter
		// to that adapter
		IndustryOccupationsAdapter adapter = new IndustryOccupationsAdapter(IndustryTopOccupationsActivity.this, 
			occupations, sortOrderSpinner.getSelectedItemPosition());
		setListAdapter(adapter);

		if (progress != null)
		    progress.dismiss();
		
		if (occupations.size() == 0)
		    Toast.makeText(IndustryTopOccupationsActivity.this, R.string.no_data, Toast.LENGTH_LONG).show();
	    }

	    public void error(String error) {
		if (progress != null)
		    progress.dismiss();
		
		Toast.makeText(IndustryTopOccupationsActivity.this, error, Toast.LENGTH_LONG).show();
	    }
	}).execute(industry.industryCode);
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
	    intent.putExtra(DocumentationActivity.DEFAULT_PAGE, DocumentationActivity.INDUSTRIES_PAGE);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
}
