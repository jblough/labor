package com.josephblough.laborstatistics.activities.occupation;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class OccupationTabbedActivity extends TabActivity implements OnTabChangeListener {

    // This activity holds the other "known occupation"-based activities
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tabhost_with_footer);

        Resources res = getResources();
	final TabHost tabHost = getTabHost();
	TabHost.TabSpec spec;
	Intent intent;

	intent = new Intent().setClass(this, OccupationDetailsActivity.class);
	spec = tabHost.newTabSpec("details").setIndicator("Details", res.getDrawable(R.drawable.ic_tab_info)).setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, OccupationMapActivity.class);
	spec = tabHost.newTabSpec("map").setIndicator("Map", res.getDrawable(R.drawable.ic_tab_usa)).setContent(intent);
	tabHost.addTab(spec);
/*
	intent = new Intent().setClass(this, OccupationTopIndustriesActivity.class);
	spec = tabHost.newTabSpec("industries").setIndicator("Top Industries", res.getDrawable(R.drawable.occupation_industries)).setContent(intent);
	tabHost.addTab(spec);
*/
	intent = new Intent().setClass(this, OccupationTopSalaryActivity.class);
	spec = tabHost.newTabSpec("salary").setIndicator("Salary", res.getDrawable(R.drawable.ic_tab_money)).setContent(intent);
	tabHost.addTab(spec);
	
	intent = new Intent().setClass(this, OccupationTopEmploymentActivity.class);
	spec = tabHost.newTabSpec("employment").setIndicator("Employment", res.getDrawable(R.drawable.ic_tab_employment)).setContent(intent);
	tabHost.addTab(spec);

	tabHost.setOnTabChangedListener(this);
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
	    intent.putExtra(DocumentationActivity.DEFAULT_PAGE, DocumentationActivity.OCCUPATIONS_PAGE);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }

    public void onTabChanged(String tabId) {
	if ("details".equals(tabId)) {
	    setTitle(R.string.occupation_summary);
	}
	else if ("map".equals(tabId)) {
	    setTitle(R.string.select_location);
	}
	else if ("salary".equals(tabId)) {
	    setTitle(R.string.top_salary);
	}
	else if ("employment".equals(tabId)) {
	    setTitle(R.string.top_employment);
	}
    }
}
