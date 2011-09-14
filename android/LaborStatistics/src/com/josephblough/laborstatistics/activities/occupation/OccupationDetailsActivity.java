package com.josephblough.laborstatistics.activities.occupation;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.StatisticalSummary;
import com.josephblough.laborstatistics.data.StatisticalSummaryCallback;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDefinitionsDataset;
import com.josephblough.laborstatistics.tasks.AreaSummaryRetrieverTask;
import com.josephblough.laborstatistics.tasks.NationalSummaryRetrieverTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OccupationDetailsActivity extends Activity {

    private static final String TAG = "OccupationDetailsActivity";
    
    // This activity will display all of the statistics for an occupation
    // National stats will always be shown, but state and municipality
    //	stats may also be displayed if requested

    private NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    private NumberFormat formatter = NumberFormat.getIntegerInstance();
    private StatisticalSummary nationalSummary = null;
    private Map<String, StatisticalSummary> stateSummaries = new HashMap<String, StatisticalSummary>();
    private Map<String, StatisticalSummary> citySummaries = new HashMap<String, StatisticalSummary>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate");
        
        setContentView(R.layout.occupation_details);
        
	final String occupationCode = ((ApplicationController)getApplication()).selectedOccupation;
        final OccupationDataset occupation = DataLibrary.getOccupation(this, occupationCode);
        ((TextView)findViewById(R.id.occupation_name)).setText(occupation.occupationName);
        
        OccupationDefinitionsDataset definition = DataLibrary.getOccupationDefinition(this, occupationCode);
        ((TextView)findViewById(R.id.occupation_description)).setText((definition == null) ? "" : definition.definition);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        
        // National
        loadNationalStats();
        
        ApplicationController app = (ApplicationController)getApplication();
        
        if (app.selectedState == null && app.selectedMunicipality == null) {
            findViewById(R.id.regional_stats_available_label).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.regional_stats_available_label).setVisibility(View.GONE);
        }
        
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
		    Toast.makeText(OccupationDetailsActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    findViewById(R.id.national_occupation_stats).setVisibility(View.GONE);
	    findViewById(R.id.national_stats_busy_indicator).setVisibility(View.VISIBLE);

	    final String occupation = ((ApplicationController)getApplication()).selectedOccupation;
	    retriever.execute(occupation);
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
    }
    
    private void loadStateStats(final String stateAreaCode) {
        findViewById(R.id.state_stats_heading).setVisibility(View.VISIBLE);
        
        StatisticalSummary stateSummary = stateSummaries.get(stateAreaCode);
	if (stateSummary == null) {
	    AreaSummaryRetrieverTask retriever = new AreaSummaryRetrieverTask(stateAreaCode, new StatisticalSummaryCallback() {

		public void success(StatisticalSummary summary) {
		    stateSummaries.put(stateAreaCode, summary);
		    updateStateStats(summary);
		}

		public void error(String error) {
		    findViewById(R.id.state_stats_busy_indicator).setVisibility(View.GONE);
		    Toast.makeText(OccupationDetailsActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    findViewById(R.id.state_stats_no_data_available_label).setVisibility(View.GONE);
	    findViewById(R.id.state_occupation_stats).setVisibility(View.GONE);
	    findViewById(R.id.state_stats_busy_indicator).setVisibility(View.VISIBLE);

	    final String occupation = ((ApplicationController)getApplication()).selectedOccupation;
	    retriever.execute(occupation);
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
    }
    
    private void loadMunicipalityStats(final String areaCode) {
        findViewById(R.id.municipality_stats_heading).setVisibility(View.VISIBLE);
        
        StatisticalSummary citySummary = citySummaries.get(areaCode);
	if (citySummary == null) {
	    AreaSummaryRetrieverTask retriever = new AreaSummaryRetrieverTask(areaCode, new StatisticalSummaryCallback() {

		public void success(StatisticalSummary summary) {
		    citySummaries.put(areaCode, summary);
		    updateMunicipalityStats(summary);
		}

		public void error(String error) {
		    findViewById(R.id.municipality_stats_busy_indicator).setVisibility(View.GONE);
		    Toast.makeText(OccupationDetailsActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    findViewById(R.id.municipality_stats_no_data_available_label).setVisibility(View.GONE);
	    findViewById(R.id.municipality_occupation_stats).setVisibility(View.GONE);
	    findViewById(R.id.municipality_stats_busy_indicator).setVisibility(View.VISIBLE);

	    final String occupation = ((ApplicationController)getApplication()).selectedOccupation;
	    retriever.execute(occupation);
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
}
