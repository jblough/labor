package com.josephblough.laborstatistics.activities;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.comparison.ComparisonOccupationSelectionActivity;
import com.josephblough.laborstatistics.activities.comparison.ComparisonTabbedActivity;
import com.josephblough.laborstatistics.activities.industry.IndustrySelectionActivity;
import com.josephblough.laborstatistics.activities.location.LocationSelectionActivity;
import com.josephblough.laborstatistics.activities.occupation.OccupationSelectionActivity;
import com.josephblough.laborstatistics.activities.tips.TipSelectionActivity;
import com.josephblough.laborstatistics.adapters.MainGridAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainGridActivity extends Activity implements OnItemClickListener {

    private static final int OCCUPATION_INDEX = 0;
    private static final int INDUSTRY_INDEX = 1;
    private static final int LOCATION_INDEX = 2;
    private static final int COMPARE_SALARIES_INDEX = 3;
    private static final int TIPS_INDEX = 4;
    private static final int HELP_INDEX = 5;
    
    private static final String[] items = {"Know your occupation?",
	"Know your preferred industry?",
	"Know where you want to work?",
	"Compare your salary",
    	"What can you do with this information?",
    	"How do you use this app?"};

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_grid_layout);
        
        displayDisclaimer();
        
	gridView = (GridView) findViewById(R.id.gridview);
	MainGridAdapter adapter = new MainGridAdapter(this, items);
	gridView.setAdapter(adapter);

	gridView.setOnItemClickListener(this);
    }

    private void displayDisclaimer() {
	final String disclaimerSetting = "shownDisclaimer";
	final SharedPreferences preferences = this.getSharedPreferences(getClass().getName(), 0);
	final boolean shownDisclaimer = preferences.getBoolean(disclaimerSetting, false);
	if (!shownDisclaimer) {
	    AlertDialog.Builder alert = new AlertDialog.Builder(MainGridActivity.this);
	    alert.setTitle("Disclaimer");
	    alert.setCancelable(false);
	    alert.setMessage(getString(R.string.disclaimer)).
	    setPositiveButton("Accept", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
		    SharedPreferences.Editor editor = preferences.edit();
		    editor.putBoolean(disclaimerSetting, true);
		    editor.commit();
		}
	    }).
	    setNegativeButton("Decline", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
		    finish();
		}
	    });
	    alert.show();
	}
    }
    
    public int getMenuImage(final String title) {
	if ("Know your occupation?".equals(title)) {
	    return R.drawable.occupation2;
	}
	else if ("Know your preferred industry?".equals(title)) {
	    return R.drawable.industry2;
	}
	else if ("Know where you want to work?".equals(title)) {
	    return R.drawable.location3;
	}
	else if ("Compare your salary".equals(title)) {
	    return R.drawable.compare2;
	}
	else if ("What can you do with this information?".equals(title)) {
	    return R.drawable.tips2;
	}
	else if ("How do you use this app?".equals(title)) {
	    return R.drawable.help;
	}
	
	return R.drawable.occupation2;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	switch (position) {
	case OCCUPATION_INDEX:
	    Intent i1 = new Intent(this, OccupationSelectionActivity.class);
	    startActivity(i1);
	    break;
	case INDUSTRY_INDEX:
	    Intent i2 = new Intent(this, IndustrySelectionActivity.class);
	    startActivity(i2);
	    break;
	case LOCATION_INDEX:
	    Intent i3 = new Intent(this, LocationSelectionActivity.class);
	    startActivity(i3);
	    break;
	case COMPARE_SALARIES_INDEX:
	    ApplicationController app = (ApplicationController)getApplication();
	    Intent i4 = new Intent(this, 
		    (app.selectedOccupation == null) ? 
			    ComparisonOccupationSelectionActivity.class : 
				ComparisonTabbedActivity.class);
	    startActivity(i4);
	    break;
	case TIPS_INDEX:
	    Intent i5 = new Intent(this, TipSelectionActivity.class);
	    startActivity(i5);
	    break;
	case HELP_INDEX:
	    Intent i6 = new Intent(this, DocumentationActivity.class);
	    startActivity(i6);
	    break;
	}
    }

    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.main_menu, menu);
	
	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.about_menu_item:
	    Intent intent = new Intent(this, CreditsActivity.class);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
}
