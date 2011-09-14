package com.josephblough.laborstatistics.activities.industry;

import java.text.NumberFormat;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.StatisticalSummary;
import com.josephblough.laborstatistics.data.StatisticalSummaryCallback;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDefinitionsDataset;
import com.josephblough.laborstatistics.tasks.AreaSummaryRetrieverTask;
import com.josephblough.laborstatistics.tasks.NationalSummaryRetrieverTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class IndustryOccupationDetailsActivity extends Activity {

    private static final String TAG = "IndustryOccupationDetailsActivity";
    
    public static final String OCCUPATION_CODE = "IndustryOccupationDetailsActivity.occupation_code";
    
    // This activity will display all of the statistics for an occupation
    // National stats will always be shown, but state and municipality
    //	stats may also be displayed if requested

    private NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    private NumberFormat formatter = NumberFormat.getIntegerInstance();
    private StatisticalSummary nationalSummary = null;
    private StatisticalSummary stateSummary = null;
    private StatisticalSummary citySummary = null;
    private String occupationCode;
    
    private View tooltipPanel;
    private Handler closeTooltipHandler;
    private Thread closeTooltipThread;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate");
        
        setContentView(R.layout.industry_occupation_details);
        
	occupationCode = getIntent().getStringExtra(OCCUPATION_CODE);
	if (occupationCode == null)
	    occupationCode = ((ApplicationController)getApplication()).selectedOccupation;
	
        final OccupationDataset occupation = DataLibrary.getOccupation(this, occupationCode);
        ((TextView)findViewById(R.id.occupation_name)).setText(occupation.occupationName);
        
        OccupationDefinitionsDataset definition = DataLibrary.getOccupationDefinition(this, occupationCode);
        ((TextView)findViewById(R.id.occupation_description)).setText((definition == null) ? "" : definition.definition);
        
        displayTooltip();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        findViewById(R.id.regional_stats_available_label).setVisibility(View.GONE);
        
        // National
        loadNationalStats();
        
        ApplicationController app = (ApplicationController)getApplication();
        
        // State
        if (app.selectedState != null) {
            AreaDataset state = DataLibrary.getArea(this, app.selectedState);
            if (state != null) {
        	((TextView)findViewById(R.id.state_stats_header_label)).setText(state.areaName + " Statistics:");
            }
            loadStateStats(app.selectedState);
        }
        else {
            findViewById(R.id.state_stats_heading).setVisibility(View.GONE);
        }
        
        // Municipality
        if (app.selectedMunicipality != null) {
            AreaDataset city = DataLibrary.getArea(this, app.selectedMunicipality);
            if (city != null) {
        	((TextView)findViewById(R.id.municipality_stats_header_label)).setText(city.areaName + " Statistics:");
            }
            loadMunicipalityStats(app.selectedMunicipality);
        }
        else {
            findViewById(R.id.municipality_stats_heading).setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }
    
    private void loadNationalStats() {
	if (nationalSummary == null) {
	    NationalSummaryRetrieverTask retriever = new NationalSummaryRetrieverTask(new StatisticalSummaryCallback() {

		public void success(StatisticalSummary summary) {
		    nationalSummary = summary;
		    updateNationalStats(nationalSummary);
		}

		public void error(String error) {
		    findViewById(R.id.national_stats_busy_indicator).setVisibility(View.GONE);
		    Toast.makeText(IndustryOccupationDetailsActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    findViewById(R.id.national_occupation_stats).setVisibility(View.GONE);
	    findViewById(R.id.national_stats_busy_indicator).setVisibility(View.VISIBLE);

	    retriever.execute(occupationCode);
	}
	else {
	    updateNationalStats(nationalSummary);
	}
    }
    
    private void updateNationalStats(final StatisticalSummary summary) {
	synchronized (this) {
	    updateIntegerLabel((TextView)findViewById(R.id.national_occupation_employment), summary.employment);
	    updateMoneyLabel((TextView)findViewById(R.id.national_occupation_mean_salary), summary.annualMeanWage);
	    updateMoneyLabel((TextView)findViewById(R.id.national_occupation_median_salary), summary.annualMedianWage);
	    updateMoneyLabel((TextView)findViewById(R.id.national_occupation_mean_wage), summary.hourlyMeanWage);
	    updateMoneyLabel((TextView)findViewById(R.id.national_occupation_median_wage), summary.hourlyMedianWage);
	}
	
	findViewById(R.id.national_occupation_stats).setVisibility(View.VISIBLE);
	findViewById(R.id.national_stats_busy_indicator).setVisibility(View.GONE);
	
	nationalSummary = null;
    }
    
    private void loadStateStats(final String stateAreaCode) {
        findViewById(R.id.state_stats_heading).setVisibility(View.VISIBLE);
        
	if (stateSummary == null) {
	    AreaSummaryRetrieverTask retriever = new AreaSummaryRetrieverTask(stateAreaCode, new StatisticalSummaryCallback() {

		public void success(StatisticalSummary summary) {
		    stateSummary = summary;
		    updateStateStats(stateSummary);
		}

		public void error(String error) {
		    findViewById(R.id.state_stats_busy_indicator).setVisibility(View.GONE);
		    Toast.makeText(IndustryOccupationDetailsActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    findViewById(R.id.state_stats_no_data_available_label).setVisibility(View.GONE);
	    findViewById(R.id.state_occupation_stats).setVisibility(View.GONE);
	    findViewById(R.id.state_stats_busy_indicator).setVisibility(View.VISIBLE);

	    retriever.execute(occupationCode);
	}
	else {
	    updateStateStats(stateSummary);
	}
    }
    
    private void updateStateStats(final StatisticalSummary summary) {
	if (summary.employment != null) {
	    synchronized (this) {
		updateIntegerLabel((TextView)findViewById(R.id.state_occupation_employment), summary.employment);
		updateMoneyLabel((TextView)findViewById(R.id.state_occupation_mean_salary), summary.annualMeanWage);
		updateMoneyLabel((TextView)findViewById(R.id.state_occupation_median_salary), summary.annualMedianWage);
		updateMoneyLabel((TextView)findViewById(R.id.state_occupation_mean_wage), summary.hourlyMeanWage);
		updateMoneyLabel((TextView)findViewById(R.id.state_occupation_median_wage), summary.hourlyMedianWage);
	    }

	    findViewById(R.id.state_stats_no_data_available_label).setVisibility(View.GONE);
	    findViewById(R.id.state_occupation_stats).setVisibility(View.VISIBLE);
    	}
	else {
	    findViewById(R.id.state_stats_no_data_available_label).setVisibility(View.VISIBLE);
	    findViewById(R.id.state_occupation_stats).setVisibility(View.GONE);
	}
	
	findViewById(R.id.state_stats_busy_indicator).setVisibility(View.GONE);
	
	stateSummary = null;
    }
    
    private void loadMunicipalityStats(final String areaCode) {
        findViewById(R.id.municipality_stats_heading).setVisibility(View.VISIBLE);
        
	if (citySummary == null) {
	    AreaSummaryRetrieverTask retriever = new AreaSummaryRetrieverTask(areaCode, new StatisticalSummaryCallback() {

		public void success(StatisticalSummary summary) {
		    citySummary = summary;
		    updateMunicipalityStats(citySummary);
		}

		public void error(String error) {
		    findViewById(R.id.municipality_stats_busy_indicator).setVisibility(View.GONE);
		    Toast.makeText(IndustryOccupationDetailsActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    findViewById(R.id.municipality_stats_no_data_available_label).setVisibility(View.GONE);
	    findViewById(R.id.municipality_occupation_stats).setVisibility(View.GONE);
	    findViewById(R.id.municipality_stats_busy_indicator).setVisibility(View.VISIBLE);

	    retriever.execute(occupationCode);
	}
	else {
	    updateMunicipalityStats(citySummary);
	}
    }
    
    private void updateMunicipalityStats(final StatisticalSummary summary) {
	if (summary.employment != null) {
	    synchronized (this) {
		updateIntegerLabel((TextView)findViewById(R.id.municipality_occupation_employment), summary.employment);
		updateMoneyLabel((TextView)findViewById(R.id.municipality_occupation_mean_salary), summary.annualMeanWage);
		updateMoneyLabel((TextView)findViewById(R.id.municipality_occupation_median_salary), summary.annualMedianWage);
		updateMoneyLabel((TextView)findViewById(R.id.municipality_occupation_mean_wage), summary.hourlyMeanWage);
		updateMoneyLabel((TextView)findViewById(R.id.municipality_occupation_median_wage), summary.hourlyMedianWage);
	    }

	    findViewById(R.id.municipality_stats_no_data_available_label).setVisibility(View.GONE);
	    findViewById(R.id.municipality_occupation_stats).setVisibility(View.VISIBLE);
	}
	else {
	    findViewById(R.id.municipality_stats_no_data_available_label).setVisibility(View.VISIBLE);
	    findViewById(R.id.municipality_occupation_stats).setVisibility(View.GONE);
	}
	findViewById(R.id.municipality_stats_busy_indicator).setVisibility(View.GONE);
	
	citySummary = null;
    }
    
    private void updateIntegerLabel(final TextView label, final String value) {
	if ("-".equals(value) || value == null)
	    label.setText("");
	else
	    label.setText(formatter.format(Integer.valueOf(value)));
    }

    private void updateMoneyLabel(final TextView label, final String value) {
	if ("-".equals(value) || value == null)
	    label.setText("");
	else
	    label.setText(moneyFormatter.format(Double.valueOf(value)));
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
    
    // Tooltip methods
    private void displayTooltip() {
	//Toast.makeText(this, R.string.occupation_selection_toolip, Toast.LENGTH_LONG).show();
        if (tooltipPanel == null) {
            tooltipPanel = ((ViewStub) findViewById(R.id.stub_tooltip)).inflate();
            ((TextView) tooltipPanel.findViewById(R.id.tooltip_overlay_text)).
            	setText(R.string.industry_occupation_selection_tooltip);
            tooltipPanel.startAnimation(AnimationUtils.loadAnimation(IndustryOccupationDetailsActivity.this,
        	    R.anim.slide_in));
        }
        
	closeTooltipHandler = new Handler();
	closeTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
        	tooltipPanel.startAnimation(AnimationUtils.loadAnimation(IndustryOccupationDetailsActivity.this,
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
        // Start a time to hide the panel after 5 seconds
	closeTooltipHandler.removeCallbacks(closeTooltipThread);
	closeTooltipHandler.postDelayed(closeTooltipThread, 5000);
    }
}
