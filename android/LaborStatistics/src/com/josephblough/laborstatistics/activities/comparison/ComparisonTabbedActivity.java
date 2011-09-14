package com.josephblough.laborstatistics.activities.comparison;

import java.util.List;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationGroupDataset;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class ComparisonTabbedActivity extends TabActivity implements OnTabChangeListener {

    // This activity needs to check if an occupation has been selected
    //	and prompt if one has not.
    // On occupation selection, display a dialog with the description
    //	and ask for confirmation with something like
    //	"Does this sound like what you do?"
    // After confirmation, take the user to a three tab activity
    //	"National", "State", and "Municipality"
    // National tab is default and State/Municipality tabs
    //	show the state/municipality that has already been selected
    //	elsewhere.  Otherwise it pops up the map activity allowing the
    //	user to select a state/municipality
    // State can probably be preselected by the users location
    // State/Municipality screens need a way to allow the user to
    //	change the current selection (and cancel as well)
    
    // The activity should display mean and median salaries/wages
    //	in a graph format.  An edit field should be on the activity
    //	to allow the user to plot their salary on the graph
    // Any value entered should transfer to the corresponding
    //	edit field in the other tabs.
    // The percentile of the entered salary should be computed
    //	and displayed to the user (for help in salary negotiations?)

    private static final String TAG = "ComparisonTabbedActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabhost_with_footer);
        
        Log.i(TAG, "onCreate");
        
        // Create the tabs
        Resources res = getResources();
	TabHost tabHost = getTabHost();
	TabHost.TabSpec spec;
	Intent intent;

	ApplicationController app = (ApplicationController)getApplication();
	
	intent = new Intent().setClass(this, ComparisonActivity.class);
	intent.putExtra(ComparisonActivity.AREA_CODE, "0000000");
	spec = tabHost.newTabSpec("national").setIndicator("National", res.getDrawable(R.drawable.ic_tab_usa)).setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, ComparisonActivity.class);
	if (app.selectedState != null) {
	    intent.putExtra(ComparisonActivity.AREA_CODE, app.selectedState);
	}
	intent.putExtra(ComparisonLocationSelectionActivity.AREA_TYPE, ComparisonLocationSelectionActivity.AREA_TYPE_STATE);
	spec = tabHost.newTabSpec("state").setIndicator("State", res.getDrawable(R.drawable.ic_tab_map)).setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, ComparisonActivity.class);
	if (app.selectedMunicipality != null) {
	    intent.putExtra(ComparisonActivity.AREA_CODE, app.selectedMunicipality);
	}
	intent.putExtra(ComparisonLocationSelectionActivity.AREA_TYPE, ComparisonLocationSelectionActivity.AREA_TYPE_CITY);
	spec = tabHost.newTabSpec("city").setIndicator("City", res.getDrawable(R.drawable.ic_tab_city)).setContent(intent);
	tabHost.addTab(spec);
	
	tabHost.setOnTabChangedListener(this);
	
	// If no occupation has been preselected present a dialog
	if (app.selectedOccupation == null) {
	    promptForOccupation();
	}
    }
    
    public void promptForOccupation() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Select your occupation");
	View view = getLayoutInflater().inflate(R.layout.occupation_selection, null);
	TypedArray array = getTheme().obtainStyledAttributes(new int[] { android.R.attr.colorBackground }); 
	int backgroundColor = array.getColor(0, 0xFFFFFF); 
	array.recycle();
	view.setBackgroundColor(backgroundColor);
	builder.setView(view);
	final AlertDialog dialog = builder.create();
	
	final ListView occupationList = (ListView) view.findViewById(android.R.id.list);
        final Spinner occupationGroupSpinner = (Spinner) view.findViewById(R.id.occupation_group);
        occupationGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> adapter, View parent, int position, long id) {
		List<OccupationDataset> occupations = 
			DataLibrary.getOccupations(ComparisonTabbedActivity.this, ((OccupationGroupDataset)adapter.getItemAtPosition(position)));
		occupationList.setAdapter(new ArrayAdapter<OccupationDataset>(ComparisonTabbedActivity.this, android.R.layout.simple_list_item_1, occupations));
	    }

	    public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	    }
	});

        ArrayAdapter<OccupationGroupDataset> adapter = new ArrayAdapter<OccupationGroupDataset>(this, android.R.layout.simple_spinner_item, 
		DataLibrary.getOccupationGroups(this));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	occupationGroupSpinner.setAdapter(adapter);
	
	occupationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
		// Set the app selected occupation to the occupation currently selected in the list
		ApplicationController app = (ApplicationController)getApplicationContext();
		app.selectedOccupation = ((OccupationDataset)adapter.getItemAtPosition(position)).occupationCode;
		
		// Close the dialog
		dialog.dismiss();
	    }
	});
	
	occupationList.setFastScrollEnabled(true);
	
	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
		// Close the dialog
		dialog.dismiss();

		// Go back to the main menu
		finish();
	    }
	});
	
	dialog.show();
    }

    public void onTabChanged(String tabId) {
	//Log.i(TAG, getTabHost().getCurrentTabTag() + " -> " + tabId);
	//Log.i(TAG, getTabHost().getCurrentTabView().getClass().getName());
    }
}
