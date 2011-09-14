package com.josephblough.laborstatistics.activities.industry;

import java.util.List;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.datasets.employmentstatistics.IndustryDataset;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class IndustrySelectionActivity extends ListActivity implements OnEditorActionListener, TextWatcher {

    private static final String TAG = "IndustrySelectionActivity";
    
    // This activity lets the user select the occupation for which they 
    //	would like to see the available occupations

    /*
     * SELECT * FROM industry ORDER BY industry_name
     */
    
    // Not sure what this SQL was for...
    /* SELECT i.industry_name, d.value FROM Data d
     * JOIN series s ON d.series_id = s.series_id
     * JOIN industry i ON i.industry_code = s.industry_code
     * WHERE s.industry_code = ? AND s.datatype_code = '04'
     * AND s.areatype_code = 'N' AND s.area_code = '0000000'      -- **
     * AND i.industry_code != '000000'
     * AND d.value != '-'
     * ORDER BY d.value DESC
     */
    
    // ** Also might want to allow for always using the most specific locality available
    //	i.e. - state/municipality
    
    // This list can get big so be sure to enable filtering and fast scrolling

    private View tooltipPanel;
    private Handler closeTooltipHandler;
    private Thread closeTooltipThread;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.industry_selection);

        Log.d(TAG, "onCreate");
        
        //View searchView = ((ViewStub) findViewById(R.id.stub_search)).inflate();
        //final EditText input = ((EditText) searchView.findViewById(R.id.input_search_query));
        final EditText input = ((EditText) findViewById(R.id.input_search_query));
        input.setOnEditorActionListener(this);
        input.addTextChangedListener(this);
        //Button searchButton = ((Button) searchView.findViewById(R.id.button_go));
        Button searchButton = ((Button) findViewById(R.id.button_go));
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
        	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        //searchView.setVisibility(View.VISIBLE);

        List<IndustryDataset> industries = DataLibrary.getIndustries(this);
        //setListAdapter(new ArrayAdapter<IndustryDataset>(this, android.R.layout.simple_list_item_1, industries));
        setListAdapter(new ArrayAdapter<IndustryDataset>(this, R.layout.industry_row, R.id.industry_name, industries));

        ListView lv = getListView();
        lv.setFastScrollEnabled(true);
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		final String selectedIndustry = ((IndustryDataset)adapter.getItemAtPosition(position)).industryCode;
		
		// If there isn't a selected State/Municipality, maybe the intent should be
		//	a National OccupationIndustryComparisonActivity instead
		Intent intent = new Intent(IndustrySelectionActivity.this, IndustryTopOccupationsActivity.class);
		intent.putExtra(IndustryTopOccupationsActivity.INDUSTRY, selectedIndustry);
		startActivity(intent);
	    }
	});
        
        displayTooltip();
    }

    // Close the keyboard on enter
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	if (event == null || ( event.getAction() == 1)) {
	    // Hide the keyboard
	    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	return true;
    }

    public void afterTextChanged(Editable s) {
	if ("".equals(s.toString())) {
	    getListView().clearTextFilter();
	}
	else {
	    getListView().setFilterText(s.toString());
	}
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	
    }

    
    // Cancel the filter on BACK key press if there is a filter
    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	if (pKeyCode == KeyEvent.KEYCODE_BACK && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	    if (getListView().getTextFilter() != null &&
		    !"".equals(getListView().getTextFilter().toString())) {
		getListView().clearTextFilter();
	        ((EditText) findViewById(R.id.input_search_query)).setText("");

		return true;
	    }
	}

	return super.onKeyDown(pKeyCode, pEvent);
    }

    // Tooltip methods
    private void displayTooltip() {
	//Toast.makeText(this, R.string.occupation_selection_toolip, Toast.LENGTH_LONG).show();
        if (tooltipPanel == null) {
            tooltipPanel = ((ViewStub) findViewById(R.id.stub_tooltip)).inflate();
            ((TextView) tooltipPanel.findViewById(R.id.tooltip_overlay_text)).
            	setText(R.string.industry_selection_tooltip);
            tooltipPanel.startAnimation(AnimationUtils.loadAnimation(IndustrySelectionActivity.this,
        	    R.anim.slide_in));
        }
        
	closeTooltipHandler = new Handler();
	closeTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
        	tooltipPanel.startAnimation(AnimationUtils.loadAnimation(IndustrySelectionActivity.this,
        		R.anim.slide_out));
		tooltipPanel.setVisibility(View.GONE);
	    }
	});
	postHideTooltop();

	// Hide the tooltip on tap
        tooltipPanel.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		closeTooltipHandler.removeCallbacks(closeTooltipThread);
		tooltipPanel.setVisibility(View.GONE);
	    }
	});
    }

    private void postHideTooltop() {
        // Start a time to hide the panel after 10 seconds
	closeTooltipHandler.removeCallbacks(closeTooltipThread);
	closeTooltipHandler.postDelayed(closeTooltipThread, 10000);
    }

    // Menu methods
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
