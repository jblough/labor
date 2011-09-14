package com.josephblough.laborstatistics.activities.comparison;


import java.util.List;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationGroupDataset;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ComparisonOccupationSelectionActivity extends ListActivity implements OnItemSelectedListener, OnEditorActionListener, TextWatcher {

    // This activity lets the user select the occupation for which they 
    //	would like to see the stats

    public static final String RETURN_SELECTION_FLAG = "ComparisonOccupationSelectionActivity.return_selection_flag";

    private View searchView;
    private Spinner occupationGroupSpinner;

    private View tooltipPanel;
    private Handler closeTooltipHandler;
    private Thread closeTooltipThread;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.occupation_selection);

        occupationGroupSpinner = (Spinner) findViewById(R.id.occupation_group);
        occupationGroupSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<OccupationGroupDataset> adapter = new ArrayAdapter<OccupationGroupDataset>(this, android.R.layout.simple_spinner_item, 
		DataLibrary.getOccupationGroups(this));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	occupationGroupSpinner.setAdapter(adapter);
	
	final boolean returnSelection = getIntent().getBooleanExtra(RETURN_SELECTION_FLAG, false);
	
	ListView lv = getListView();
	lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
		// Close the keyboard if it's open
		if (searchView != null) {
		    EditText input = ((EditText) searchView.findViewById(R.id.input_search_query));
		    if (input != null) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
			input.clearFocus();
		    }
		}

		ApplicationController app = (ApplicationController)getApplicationContext();
		final String occupationCode = ((OccupationDataset)adapter.getItemAtPosition(position)).occupationCode;
		app.selectedOccupation = occupationCode;
		
		if (returnSelection) {
		    // An occupation was selected and the intent was flagged to return
		    Intent data = new Intent();
		    data.putExtra(ComparisonActivity.OCCUPATION_CODE, occupationCode);
		    setResult(RESULT_OK, data);
		    finish();
		}
		else {
		    Intent intent = new Intent(ComparisonOccupationSelectionActivity.this, ComparisonTabbedActivity.class);
		    startActivity(intent);
		}
	    }
	});
	
	lv.setFastScrollEnabled(true);
        lv.setTextFilterEnabled(true);
        
        displayTooltip();
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        // Clear out the entered salary
	ApplicationController app = (ApplicationController)getApplicationContext();
	app.enteredComparisonSalary = null;
    }
    
    public void onItemSelected(AdapterView<?> adapter, View parent, int position, long id) {
	List<OccupationDataset> occupations = 
		DataLibrary.getOccupations(ComparisonOccupationSelectionActivity.this, ((OccupationGroupDataset)adapter.getItemAtPosition(position)));
	//setListAdapter(new ArrayAdapter<OccupationDataset>(ComparisonOccupationSelectionActivity.this, android.R.layout.simple_list_item_1, occupations));
	setListAdapter(new ArrayAdapter<OccupationDataset>(ComparisonOccupationSelectionActivity.this, R.layout.occupation_row, R.id.occupation_name, occupations));
	getListView().clearTextFilter();
    }

    public void onNothingSelected(AdapterView<?> adapter) {
    }

    /*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return onSearchRequested();
        }
        return super.onKeyUp(keyCode, event);
    }
    */

    @Override
    public boolean onSearchRequested() {
	// Hide the tooltip if it's visible
	if (closeTooltipHandler != null && closeTooltipThread != null)
	    closeTooltipHandler.removeCallbacks(closeTooltipThread);

	if (tooltipPanel != null)
	    tooltipPanel.setVisibility(View.GONE);
	
	// Display the search fields
	toggleSearch();
        
        // Returning true indicates that we did launch the search, instead of blocking it.
        return true;
    }

    private void toggleSearch() {
	if (searchView == null) {
	    searchView = ((ViewStub) findViewById(R.id.stub_search)).inflate();
	    EditText input = ((EditText) searchView.findViewById(R.id.input_search_query));
	    input.setOnEditorActionListener(this);
	    input.addTextChangedListener(this);
	    Button searchButton = ((Button) searchView.findViewById(R.id.button_go));
	    searchButton.setOnClickListener(new View.OnClickListener() {
	        
	        public void onClick(View v) {
	            toggleSearch();
	        }
	    });
	    
	    searchView.setVisibility(View.VISIBLE);
	    findViewById(R.id.occupation_group_label).setVisibility(View.GONE);
	    occupationGroupSpinner.setVisibility(View.GONE);
	    findViewById(R.id.occupation_label).setVisibility(View.GONE);

	    input.requestFocus();
	    CharSequence filter = getListView().getTextFilter();
	    input.setText((filter != null) ? filter : "");

	    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	else if (searchView.isShown()) {
	    searchView.setVisibility(View.GONE);
	    findViewById(R.id.occupation_group_label).setVisibility(View.VISIBLE);
	    occupationGroupSpinner.setVisibility(View.VISIBLE);
	    findViewById(R.id.occupation_label).setVisibility(View.VISIBLE);
	    
	    EditText input = ((EditText) searchView.findViewById(R.id.input_search_query));
	    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	    input.clearFocus();
	}
	else {
	    searchView.setVisibility(View.VISIBLE);
	    findViewById(R.id.occupation_group_label).setVisibility(View.GONE);
	    occupationGroupSpinner.setVisibility(View.GONE);
	    findViewById(R.id.occupation_label).setVisibility(View.GONE);

	    EditText input = ((EditText) searchView.findViewById(R.id.input_search_query));
	    input.requestFocus();
	    CharSequence filter = getListView().getTextFilter();
	    input.setText((filter != null) ? filter : "");

	    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	toggleSearch();
	v.clearFocus();

	return false;
    }

    public void afterTextChanged(Editable s) {
	if ("".equals(s.toString())) {
	    getListView().clearTextFilter();
	}
	else {
	    getListView().setFilterText(s.toString());
	}
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
	    int after) {
	// TODO Auto-generated method stub
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
    }

    // Cancel the filter on BACK key press if there is a filter
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
	    if (getListView().getTextFilter() != null &&
		    !"".equals(getListView().getTextFilter().toString())) {
		getListView().clearTextFilter();
	        ((EditText) findViewById(R.id.input_search_query)).setText("");

	        // Hide the search bar if visible
	        if (searchView != null && searchView.isShown()) {
	            toggleSearch();
	        }

		return true;
	    }
	    else if (searchView != null && searchView.isShown()) {
		// Hide the search bar if visible when no search term has been entered yet
		toggleSearch();
	        
		return true;
	    }
	}
	else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return onSearchRequested();
        }

	return super.onKeyDown(keyCode, event);
    }
    
    // Tooltip methods
    private void displayTooltip() {
	//Toast.makeText(this, R.string.occupation_selection_toolip, Toast.LENGTH_LONG).show();
        if (tooltipPanel == null) {
            tooltipPanel = ((ViewStub) findViewById(R.id.stub_tooltip)).inflate();
            ((TextView) tooltipPanel.findViewById(R.id.tooltip_overlay_text)).
            	setText(R.string.occupation_selection_tooltip);
            tooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonOccupationSelectionActivity.this,
        	    R.anim.slide_in));
        }
        
	closeTooltipHandler = new Handler();
	closeTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
        	tooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonOccupationSelectionActivity.this,
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
	    intent.putExtra(DocumentationActivity.DEFAULT_PAGE, DocumentationActivity.COMPARE_PAGE);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
}
