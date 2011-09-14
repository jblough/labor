package com.josephblough.laborstatistics.activities.comparison;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.androidplot.xy.YValueMarker;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.StatisticalSummary;
import com.josephblough.laborstatistics.data.StatisticalSummaryCallback;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.tasks.AreaSummaryRetrieverTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ComparisonActivity extends Activity {
    
    private static final String TAG = "ComparisonActivity";

    // There are various options for how to pass the location into the activity
    // Pass area code in and determine the state/municipality status from
    //	the database.
    // Pass in the scope National/State/Municipality and use the selected
    //	area code from the ApplicationController
    // In all cases, assume national if nothing is passed in
    public static final String AREA_CODE = "ComparisonActivity.area_code";
    public static final String OCCUPATION_CODE = "ComparisonActivity.occupation_code";
    
    private NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    private AreaDataset area = null;
    private StatisticalSummary summary = null;
    private Double salary = null;
    private XYPlot plot = null;
    private ProgressDialog progress = null;
    private TextView occupationNameLabel;
    
    private View tooltipPanel;
    private Handler closeTooltipHandler;
    private Thread closeTooltipThread;
    private boolean displayedTooltip = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate");
        
        setContentView(R.layout.comparison);

	occupationNameLabel = (TextView)findViewById(R.id.comparison_occupation_name);
        
	final ApplicationController app = (ApplicationController)getApplication();
	OccupationDataset occupation = DataLibrary.getOccupation(this, app.selectedOccupation);
	occupationNameLabel.setText(occupation.occupationName);
        
	final EditText salaryField = (EditText)findViewById(R.id.comparison_salary);
	salaryField.setOnEditorActionListener(new OnEditorActionListener() {
	    
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (event == null || ( event.getAction() == 1)) {
		    // Hide the keyboard
		    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(salaryField.getWindowToken(), 0);
		    
		    // Plot the salary
		    try {
			app.enteredComparisonSalary = salaryField.getText().toString();
			
			salary = Double.valueOf(salaryField.getText().toString());
			updateStats(summary);
		    }
		    catch (NumberFormatException e) {
			salary = null;
		    }
		}
		return true;
	    }
	});
        
        ((Button)findViewById(R.id.comparison_plot_salary)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		// Hide the keyboard
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(salaryField.getWindowToken(), 0);
		
		// Plot the salary
		try {
		    ApplicationController app = (ApplicationController)getApplication();
		    app.enteredComparisonSalary = salaryField.getText().toString();
		    
		    salary = Double.valueOf(salaryField.getText().toString());
		    updateStats(summary);
		}
		catch (NumberFormatException e) {
		    salary = null;
		}
	    }
	});

        if (app.enteredComparisonSalary != null) {
            try {
        	salary = Double.valueOf(app.enteredComparisonSalary);
        	salaryField.setText(app.enteredComparisonSalary);
            }
            catch (NumberFormatException e) {
        	salary = null;
            }
        }

        moneyFormatter.setMaximumFractionDigits(0);
	
        plot = (XYPlot)findViewById(R.id.mySimpleXYPlot); 
	plot.setRangeLabel("Annual Wages");
	plot.setDomainLabel("Percentile");
	plot.getLegendWidget().setVisible(false);
	plot.getRangeLabelWidget().setVisible(false);
	plot.getDomainLabelWidget().setVisible(false);
	plot.disableAllMarkup();
	plot.setRangeValueFormat(moneyFormatter);
	
	NumberFormat domainFormat = NumberFormat.getInstance();
	domainFormat.setMaximumFractionDigits(0);
	plot.setDomainValueFormat(domainFormat);

	plot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
	plot.setRangeBoundaries(0, 5000, BoundaryMode.FIXED);
	plot.setDomainBoundaries(0, 100, BoundaryMode.FIXED);
	
	plot.getGraphWidget().setPaddingRight(10);
	plot.getGraphWidget().setPaddingLeft(20);
	plot.getGraphWidget().setPaddingTop(10);
	//plot.getGraphWidget().setPaddingBottom(2);
	
	// Retrieve the area, defaulting to National
        final String areaCode = getIntent().getStringExtra(AREA_CODE);
        //area = (areaCode == null) ? DataLibrary.getArea(this, "0000000") : DataLibrary.getArea(this, areaCode);
        area = DataLibrary.getArea(this, areaCode);
        
	if (area != null) {
	    TextView areaNameLabel = (TextView) findViewById(R.id.comparison_area_name);
	    areaNameLabel.setText(area.areaName);
	    areaNameLabel.setVisibility(View.VISIBLE);
	    
	    loadStats();
	}
	else {
	    Intent intent = new Intent(this, ComparisonLocationSelectionActivity.class);
	    intent.putExtra(ComparisonLocationSelectionActivity.AREA_TYPE, 
		    getIntent().getIntExtra(ComparisonLocationSelectionActivity.AREA_TYPE, ComparisonLocationSelectionActivity.AREA_TYPE_STATE));
	    startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onResume() {
	super.onResume();

	Log.i(TAG, "onResume");
	
	ApplicationController app = (ApplicationController)getApplication();
	
	// Check if the salary was entered on another tab
        if (app.enteredComparisonSalary != null) {
            try {
        	salary = Double.valueOf(app.enteredComparisonSalary);
        	final EditText salaryField = (EditText)findViewById(R.id.comparison_salary);
        	salaryField.setText(app.enteredComparisonSalary);
            }
            catch (NumberFormatException e) {
        	salary = null;
            }
        }
        
        // Check if the selected occupation was changed on another tab
	OccupationDataset selectedOccupation = DataLibrary.getOccupation(this, app.selectedOccupation);
	if (selectedOccupation != null && !selectedOccupation.occupationName.equals(occupationNameLabel.getText().toString())) {
	    occupationNameLabel.setText(selectedOccupation.occupationName);
	    this.summary = null;
	    loadStats();
	}
	else if (this.summary != null) {
            loadStats();
        }
    }

    private void loadStats() {
	if (this.summary == null) {
	    AreaSummaryRetrieverTask retriever = new AreaSummaryRetrieverTask(area.areaCode, new StatisticalSummaryCallback() {

		public void success(StatisticalSummary summary) {
		    ComparisonActivity.this.summary = summary;
		    updateStats(ComparisonActivity.this.summary);
		}

		public void error(String error) {
		    if (progress != null)
			progress.dismiss();

		    //findViewById(R.id.national_stats_busy_indicator).setVisibility(View.GONE);
		    Toast.makeText(ComparisonActivity.this, error, Toast.LENGTH_LONG).show();
		}
	    });

	    //findViewById(R.id.national_occupation_stats).setVisibility(View.GONE);
	    //findViewById(R.id.national_stats_busy_indicator).setVisibility(View.VISIBLE);

	    progress = ProgressDialog.show(this, "Downloading", "Retrieving " + area.areaName + " Statistics", true, true);
	    final String occupation = ((ApplicationController)getApplication()).selectedOccupation;
	    retriever.execute(occupation);
	}
	else {
	    updateStats(this.summary);
	}
    }
    
    private void updateStats(final StatisticalSummary summary) {
	if (progress != null)
	    progress.dismiss();

	updateLabel((TextView)findViewById(R.id.percentile_10th_value), summary.annual10thPercentileWage);
	updateLabel((TextView)findViewById(R.id.percentile_25th_value), summary.annual25thPercentileWage);
	updateLabel((TextView)findViewById(R.id.median_value), summary.annualMedianWage);
	updateLabel((TextView)findViewById(R.id.percentile_75th_value), summary.annual75thPercentileWage);
	updateLabel((TextView)findViewById(R.id.percentile_90th_value), summary.annual90thPercentileWage);
	
	findViewById(R.id.comparison_percentiles).setVisibility(View.VISIBLE);
	
	plotValues(summary);
	
	if (this.salary != null) {
	    calculateSalaryPercentile(this.salary, summary);
	}
	else {
	    // Don't bother displaying the tooltip if there is already a salary entered
	    // Also only show the tooltip on the National screen
	    if (!displayedTooltip && area != null && "0000000".equals(area.areaCode)) {
		displayTooltip();
		displayedTooltip = true;
	    }
	}
    }
    
    private void updateLabel(final TextView label, final String value) {
	if ("-".equals(value) || value == null)
	    label.setText("");
	else
	    label.setText(moneyFormatter.format(Integer.valueOf(value)));
    }
    
    private void plotValues(final StatisticalSummary summary) {
	List<Number> xValues = new ArrayList<Number>();
	List<Number> yValues = new ArrayList<Number>();
	
	if (!"-".equals(summary.annual10thPercentileWage) && summary.annual10thPercentileWage != null) {
	    xValues.add(10);
	    yValues.add(Integer.valueOf(summary.annual10thPercentileWage));
	}
	if (!"-".equals(summary.annual25thPercentileWage) && summary.annual25thPercentileWage != null) {
	    xValues.add(25);
	    yValues.add(Integer.valueOf(summary.annual25thPercentileWage));
	}
	if (!"-".equals(summary.annualMedianWage) && summary.annualMedianWage != null) {
	    xValues.add(50);
	    yValues.add(Integer.valueOf(summary.annualMedianWage));
	}
	if (!"-".equals(summary.annual75thPercentileWage) && summary.annual75thPercentileWage != null) {
	    xValues.add(75);
	    yValues.add(Integer.valueOf(summary.annual75thPercentileWage));
	}
	if (!"-".equals(summary.annual90thPercentileWage) && summary.annual90thPercentileWage != null) {
	    xValues.add(90);
	    yValues.add(Integer.valueOf(summary.annual90thPercentileWage));
	}

	com.androidplot.series.XYSeries series = new SimpleXYSeries(xValues, yValues, "");

	LineAndPointFormatter formatter = new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(200, 0, 0), Color.TRANSPARENT);
	plot.clear();
	plot.addSeries(series, formatter);
	
	plot.removeMarkers();
	if (salary != null) {
	    plot.addMarker(new YValueMarker(salary, moneyFormatter.format(salary)));
	}
	
	plot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
	if (!"-".equals(summary.annual10thPercentileWage) && summary.annual10thPercentileWage != null)
	    plot.setRangeLowerBoundary(Integer.valueOf(summary.annual10thPercentileWage) - 5000, BoundaryMode.FIXED);
	else
	    plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
	
	if (!"-".equals(summary.annual90thPercentileWage) && summary.annual90thPercentileWage != null)
	    plot.setRangeUpperBoundary(Integer.valueOf(summary.annual90thPercentileWage) + 5000, BoundaryMode.FIXED);
	else
	    plot.setRangeUpperBoundary(5000, BoundaryMode.FIXED);
	    
	plot.setDomainBoundaries(0, 100, BoundaryMode.FIXED);
	plot.redraw();
    }

    private void calculateSalaryPercentile(final double salary, final StatisticalSummary summary) {
	try {
	    TextView label = (TextView)findViewById(R.id.comparison_computed_salary_percentile);
	    int percentile10th = Integer.valueOf(summary.annual10thPercentileWage);
	    int percentile25th = Integer.valueOf(summary.annual25thPercentileWage);
	    int percentile50th = Integer.valueOf(summary.annualMedianWage);
	    int percentile75th = Integer.valueOf(summary.annual75thPercentileWage);
	    int percentile90th = Integer.valueOf(summary.annual90thPercentileWage);

	    String percentile = "";
	    if (salary < percentile10th) {
		double diff = (percentile10th - 0) / 10;
		double value = salary / diff;
		percentile = formatPercentile((int)(value));
		label.setText(moneyFormatter.format(salary) + " is in the " + percentile + " percentile");
	    }
	    else if (salary == percentile10th) {
		label.setText(moneyFormatter.format(salary) + " is in the 10th percentile");
	    }
	    else if (salary > percentile10th && salary < percentile25th) {
		double diff = (percentile25th - percentile10th) / 15;
		double value = (salary - percentile10th) / diff;
		percentile = formatPercentile((int)(value + 10));
		label.setText(moneyFormatter.format(salary) + " is in the " + percentile + " percentile");
	    }
	    else if (salary == percentile25th) {
		label.setText(moneyFormatter.format(salary) + " is in the 25th percentile");
	    }
	    else if (salary > percentile25th && salary < percentile50th) {
		double diff = (percentile50th - percentile25th) / 25;
		double value = (salary - percentile25th) / diff;
		percentile = formatPercentile((int)(value + 25));
		label.setText(moneyFormatter.format(salary) + " is in the " + percentile + " percentile");
	    }
	    else if (salary == percentile50th) {
		label.setText(moneyFormatter.format(salary) + " is in the 50th percentile");
	    }
	    else if (salary > percentile50th && salary < percentile75th) {
		double diff = (percentile75th - percentile50th) / 15;
		double value = (salary - percentile50th) / diff;
		percentile = formatPercentile((int)(value + 50));
		label.setText(moneyFormatter.format(salary) + " is in the " + percentile + " percentile");
	    }
	    else if (salary == percentile75th) {
		label.setText(moneyFormatter.format(salary) + " is in the 75th percentile");
	    }
	    else if (salary > percentile75th && salary < percentile90th) {
		double diff = (percentile90th - percentile75th) / 15;
		double value = (salary - percentile75th) / diff;
		percentile = formatPercentile((int)(value + 75));
		label.setText(moneyFormatter.format(salary) + " is in the " + percentile + " percentile");
	    }
	    else if (salary == percentile90th) {
		label.setText(moneyFormatter.format(salary) + " is in the 90th percentile");
	    }
	    else {
		label.setText(moneyFormatter.format(salary) + " is higher than the 90th percentile");
	    }

	    label.setVisibility(View.VISIBLE);
	}
	catch (NumberFormatException e) {
	    Toast.makeText(this, "Unable to calculate salary percentile", Toast.LENGTH_LONG).show();
	}
    }
    
    private String formatPercentile(final int percentile) {
	// 40 - th
	// 41 - st
	// 42 - nd
	// 43 - rd
	// 44 - th
	// 45 - th
	// 46 - th
	// 47 - th
	// 48 - th
	// 49 - th
	// 50 - th
	String value = Integer.toString(percentile);
	char firstCharater = value.charAt(0);
	char lastCharater = value.charAt(value.length()-1);
	if (value.length() == 2 && firstCharater == '1') {
	    return value + "th";
	}
	else if (lastCharater == '1') {
	    return value + "st";
	}
	else if (lastCharater == '2') {
	    return value + "nd";
	}
	else if (lastCharater == '3') {
	    return value + "rd";
	}
	else {
	    return value + "th";
	}
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	if (area != null && "0000000".equals(area.areaCode))
	    inflater.inflate(R.menu.national_comparison_menu, menu);
	else
	    inflater.inflate(R.menu.area_comparison_menu, menu);
	
	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.change_occupation_menu_item:
	    changeOccupation();
	    return true;
	case R.id.change_location_menu_item:
	    changeLocation();
	    return true;
	case R.id.help_menu_item:
	    Intent intent = new Intent(this, DocumentationActivity.class);
	    intent.putExtra(DocumentationActivity.DEFAULT_PAGE, DocumentationActivity.COMPARE_PAGE);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }

    private void changeLocation() {
	Intent intent = new Intent(this, ComparisonLocationSelectionActivity.class);
	intent.putExtra(ComparisonLocationSelectionActivity.AREA_TYPE, 
		getIntent().getIntExtra(ComparisonLocationSelectionActivity.AREA_TYPE, ComparisonLocationSelectionActivity.AREA_TYPE_STATE));
	startActivityForResult(intent, 0);
    }
    
    private void changeOccupation() {
	Intent intent = new Intent(this, ComparisonOccupationSelectionActivity.class);
	intent.putExtra(ComparisonOccupationSelectionActivity.RETURN_SELECTION_FLAG, true);
	startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // If a location has been selected then load the details data
	if (resultCode == RESULT_OK && data != null) {
	    String areaCode = data.getStringExtra(AREA_CODE);
	    if (areaCode != null) {
		area = DataLibrary.getArea(this, areaCode);

		if (area != null) {
		    TextView areaNameLabel = (TextView) findViewById(R.id.comparison_area_name);
		    areaNameLabel.setText(area.areaName);
		    areaNameLabel.setVisibility(View.VISIBLE);
		}
	    }
	    /*else if (this.area == null) {
		area = DataLibrary.getArea(this, "0000000");

		if (area != null) {
		    TextView areaNameLabel = (TextView) findViewById(R.id.comparison_area_name);
		    areaNameLabel.setText(area.areaName);
		    areaNameLabel.setVisibility(View.VISIBLE);
		}
	    }*/
	    
	    String occupationCode = data.getStringExtra(OCCUPATION_CODE);
	    if (occupationCode != null) {
		final ApplicationController app = (ApplicationController)getApplication();
		app.selectedOccupation = occupationCode;
		OccupationDataset occupation = DataLibrary.getOccupation(this, occupationCode);
	        occupationNameLabel.setText(occupation.occupationName);
	    }
	    
	    this.summary = null;
	    if (area != null) {
		loadStats();
	    }
	}
    }

    @Override
    protected void onPause() {
	// Clear out any saved salaries
	if (this.isFinishing()) {
	    final ApplicationController app = (ApplicationController)getApplication();
	    app.enteredComparisonSalary = null;

	    final EditText salaryField = (EditText)findViewById(R.id.comparison_salary);
	    salaryField.setText("");
	}
        super.onPause();
    }
    
    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	if (pKeyCode == KeyEvent.KEYCODE_BACK && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	    // Clear out any saved salaries
	    final ApplicationController app = (ApplicationController)getApplication();
	    app.enteredComparisonSalary = null;

	    final EditText salaryField = (EditText)findViewById(R.id.comparison_salary);
	    salaryField.setText("");
	}

	return super.onKeyDown(pKeyCode, pEvent);
    }
    // Tooltip methods
    private void displayTooltip() {
	//Toast.makeText(this, R.string.occupation_selection_toolip, Toast.LENGTH_LONG).show();
        if (tooltipPanel == null) {
            tooltipPanel = ((ViewStub) findViewById(R.id.stub_tooltip)).inflate();
        }
        ((TextView) tooltipPanel.findViewById(R.id.tooltip_overlay_text)).
        	setText(R.string.compare_tooltip);
        tooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonActivity.this,
        	R.anim.slide_in));
        
	closeTooltipHandler = new Handler();
	closeTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
        	tooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonActivity.this,
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
}
