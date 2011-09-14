package com.josephblough.laborstatistics.activities.occupation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.DataRetriever;
import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.data.StateLibrary;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.FootnoteDataset;
import com.josephblough.laborstatistics.tasks.MunicipalityRetrieverTask;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class OccupationMapActivity extends MapActivity {
    
    private final static String TAG = "OccupationMapActivity";

    // This activity will display a national map with all the mean annual salaries
    //	for an occupation.  Start the map centered at the current location.
    // Tapping on a map to zoom in will load more localized stats
    
    private String occupationCode;
    private MapView mapView = null;
    private List<DataDataset> stateSalaries = null;
    private List<DataDataset> municipalitiesSalaries = null;
    
    private ProgressDialog progress = null;

    private View tooltipPanel;
    private Handler closeTooltipHandler;
    private Thread closeTooltipThread;
    
    private View stateTooltipPanel;
    private Handler closeStateTooltipHandler;
    private Thread closeStateTooltipThread;
    private boolean displayedStateTooltip = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.map_layout);

        mapView = (MapView) findViewById(R.id.map);

        ApplicationController app = (ApplicationController)getApplication();
	occupationCode = app.selectedOccupation;
	
        //if (savedInstanceState == null || !savedInstanceState.containsKey("restore.map")) {
            progress = ProgressDialog.show(this, "Rendering", "Rendering map", true, true);
            new MapLoaderTask().execute();
        //}
    }
    
    @Override
    protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
    }

    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("restore.map", 1);
    }

    public void loadStateMunicipalities(final String state) {
	progress = ProgressDialog.show(this, "Downloading", "Retrieving " + state + " cities", true, true);
	
	final String stateAbbreviation = StateLibrary.getStateAbbreviation(this, state);
	if (stateAbbreviation != null) {
	    MunicipalityRetrieverTask retriever = new MunicipalityRetrieverTask(stateAbbreviation, new DataTransportCallback() {
	        
	        public void success(List<DataDataset> results) {
		    if (OccupationMapActivity.this.progress != null)
			OccupationMapActivity.this.progress.dismiss();
		    
		    municipalitiesSalaries = results; // Set the member variable to the results
		    
		    Log.i(TAG, "Retrieved " + municipalitiesSalaries.size() + " municipalities");
		    //plotMunicipalities(results);
		    Drawable marker = getResources().getDrawable(R.drawable.city_marker);
		    marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

		    Drawable highlightedMarker = getResources().getDrawable(R.drawable.highlighted_city_marker);
		    highlightedMarker.setBounds(0, 0, highlightedMarker.getIntrinsicWidth(), highlightedMarker.getIntrinsicHeight());

		    MunicipalitiesOverlay overlay = new MunicipalitiesOverlay(marker, highlightedMarker, mapView);
		    mapView.getOverlays().add(overlay);
		    mapView.invalidate();
			
		    if (results.size() == 0)
			Toast.makeText(OccupationMapActivity.this, R.string.no_data, Toast.LENGTH_LONG).show();
	        }
	        
	        public void error(String error) {
		    if (OccupationMapActivity.this.progress != null)
			OccupationMapActivity.this.progress.dismiss();
		    
		    Toast.makeText(OccupationMapActivity.this, error, Toast.LENGTH_LONG).show();
	        }
	    });
	    
	    retriever.execute(occupationCode);
	}
	else {
	    if (progress != null)
		progress.dismiss();
	    
	    Toast.makeText(this, "State " + state + " not found", Toast.LENGTH_LONG).show();
	}
    }
    
    private class StatesOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private List<OverlayItem> items = new ArrayList<OverlayItem>();
	private Drawable marker = null;
	private Drawable highlightedMarker = null;
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	
	public StatesOverlay(Drawable marker, Drawable highlightedMarker, MapView mapView) {
	    super(marker, mapView);
	    this.marker = marker;
	    this.highlightedMarker = highlightedMarker;
	    setBalloonBottomOffset(32);
	    
	    boundCenterBottom(marker);
	    boundCenterBottom(highlightedMarker);
	    
	    Log.i(TAG, "Looping through " + stateSalaries.size() + " states");
	    for (DataDataset stateSalary : stateSalaries) {
		//Log.i(TAG, "Value: " + stateSalary.value);
		String areaCode = 
			DataRetriever.extractAreaCodeFromSeriesId(stateSalary.seriesId);
		final AreaDataset state = DataLibrary.getArea(OccupationMapActivity.this, areaCode);
		final String stateName = (state != null) ? state.areaName : "Unknown";
		GeoPoint point = DataLibrary.getGeoPoint(OccupationMapActivity.this, areaCode);
		
		if (point != null) {
		    //Log.i(TAG, "Adding marker for " + stateName);
		    if (stateSalary.value != null && !"-".equals(stateSalary.value)) {
			synchronized (this) {
			    items.add(new OverlayItem(point, stateName, 
				    formatter.format(Double.valueOf(stateSalary.value)) + "\nTap balloon to load " + stateName + " city statistics"));
			}
		    }
		    else {
			if (stateSalary.footnoteCodes != null && !"".equals(stateSalary)) {
			    FootnoteDataset footnote = 
				    DataLibrary.getFootnote(OccupationMapActivity.this, stateSalary.footnoteCodes);
			    if (footnote != null && footnote.footnoteText != null &&
				    !"".equals(footnote.footnoteText)) {

				items.add(new OverlayItem(point, stateName, 
					footnote.footnoteText + "\nTap here to load " + stateName + " city statistics"));
			    }
			    else {
				items.add(new OverlayItem(point, stateName, 
					"No mean annual salary available\nTap here to load " + stateName + " city statistics"));
			    }
			}
			else {
			    items.add(new OverlayItem(point, stateName, 
				    "No mean annual salary available\nTap here to load " + stateName + " city statistics"));
			}
		    }
		}
		else {
		    Log.e(TAG, "State " + stateName + " has no coordinates");
		}
	    }
	    
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
	    // TODO Auto-generated method stub
	    return items.get(i);
	}

	@Override
	public int size() {
	    // TODO Auto-generated method stub
	    return items.size();
	}

	@Override
	protected boolean onTap(int i) {
	    final OverlayItem tappedItem = items.get(i);
		((ApplicationController)OccupationMapActivity.this.getApplicationContext()).selectedState = 
			DataLibrary.getStateAreaCode(OccupationMapActivity.this, tappedItem.getTitle());
	    
	    for (int ctr=0; ctr<items.size(); ctr++) {
		OverlayItem item = items.get(ctr);
		item.setMarker((ctr == i) ? highlightedMarker : marker);
	    }
	    
	    if (!displayedStateTooltip) {
		displayStateTooltip();
		displayedStateTooltip = true;
	    }
	    
	    // super.onTap(i) draws the balloon
	    return super.onTap(i);
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
	    hideBalloon();
	    
	    OccupationMapActivity.this.loadStateMunicipalities(item.getTitle());
	    return true;
	}
    }

    // City overlay ???
    private class MunicipalitiesOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private List<OverlayItem> items = new ArrayList<OverlayItem>();
	private Drawable marker = null;
	private Drawable highlightedMarker = null;
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	
	public MunicipalitiesOverlay(Drawable marker, Drawable highlightedMarker, MapView mapView) {
	    super(marker, mapView);
	    this.marker = marker;
	    this.highlightedMarker = highlightedMarker;
	    setBalloonBottomOffset(32);
	    
	    boundCenterBottom(marker);
	    boundCenterBottom(highlightedMarker);
	    
	    Log.i(TAG, "Looping through " + municipalitiesSalaries.size() + " municipalities");
	    for (DataDataset municipalitySalary : municipalitiesSalaries) {
		//Log.i(TAG, "Value: " + stateSalary.value);
		String areaCode = 
			DataRetriever.extractAreaCodeFromSeriesId(municipalitySalary.seriesId);
		final AreaDataset city = DataLibrary.getArea(OccupationMapActivity.this, areaCode);
		final String cityName = (city != null) ? city.areaName : "Unknown";
		GeoPoint point = DataLibrary.getGeoPoint(OccupationMapActivity.this, areaCode);
		if (point != null) {
		    //Log.i(TAG, "Adding marker for " + cityName);
		    if (municipalitySalary.value != null && !"-".equals(municipalitySalary.value)) {
			synchronized (this) {
			    items.add(new OverlayItem(point, cityName, 
				    formatter.format(Double.valueOf(municipalitySalary.value))));
			}
		    }
		    else {
			if (municipalitySalary.footnoteCodes != null && !"".equals(municipalitySalary)) {
			    FootnoteDataset footnote = 
				    DataLibrary.getFootnote(OccupationMapActivity.this, municipalitySalary.footnoteCodes);
			    if (footnote != null && footnote.footnoteText != null &&
				    !"".equals(footnote.footnoteText)) {

				items.add(new OverlayItem(point, cityName, 
					footnote.footnoteText));
			    }
			    else {
				items.add(new OverlayItem(point, cityName, 
					"No mean annual salary available"));
			    }
			}
			else {
			    items.add(new OverlayItem(point, cityName, 
				    "No mean annual salary available"));
			}
		    }
		}
		else {
		    Log.e(TAG, "City " + cityName + " has no coordinates");
		}
	    }
	    
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
	    // TODO Auto-generated method stub
	    return items.get(i);
	}

	@Override
	public int size() {
	    // TODO Auto-generated method stub
	    return items.size();
	}

	@Override
	protected boolean onTap(int i) {
	    ((ApplicationController)OccupationMapActivity.this.getApplicationContext()).selectedMunicipality = 
		    DataLibrary.getMunicipalityAreaCode(OccupationMapActivity.this, getItem(i).getTitle());

	    // De-select other municipalities
	    for (Overlay overlay : getMapView().getOverlays()) {
		if (overlay instanceof MunicipalitiesOverlay) {
		    List<OverlayItem> otherItems = ((MunicipalitiesOverlay)overlay).items;
		    for (OverlayItem otherItem : otherItems) {
			otherItem.setMarker(marker);
		    }
		}
	    }
	    
	    for (int ctr=0; ctr<items.size(); ctr++) {
		OverlayItem item = items.get(ctr);
		item.setMarker((ctr == i) ? highlightedMarker : marker);
	    }
	    
	    // super.onTap(i) draws the balloon
	    return super.onTap(i);
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
	    hideBalloon();
	    return true;
	}
    }

    private class MapLoaderTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... arg0) {
		stateSalaries = DataLibrary.getStateMeanSalaries(OccupationMapActivity.this, occupationCode);
	        
		Drawable marker = getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

		Drawable highlightedMarker = getResources().getDrawable(R.drawable.highlighted_marker);
		highlightedMarker.setBounds(0, 0, highlightedMarker.getIntrinsicWidth(), highlightedMarker.getIntrinsicHeight());
		
	        StatesOverlay overlay = new StatesOverlay(marker, highlightedMarker, mapView);

		//StatesOverlay overlay = new StatesOverlay();
	        mapView.getOverlays().add(overlay);
	        //mapView.setBuiltInZoomControls(true);

	        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, false);
	        Location location = locationManager.getLastKnownLocation(provider);
	        if (location != null) {
	            mapView.getController().setCenter(StateLibrary.getPoint(location.getLatitude(), location.getLongitude()));
	        }

	    return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
	    super.onPostExecute(result);
	    
	    if (progress != null)
		progress.dismiss();
	    
            mapView.getController().setZoom(7);
            // Don't turn on the built in zoom controls until after the tooltip has closed
	    //mapView.setBuiltInZoomControls(true);
	    mapView.invalidate();
	    
	    displayTooltip();
	}
    }
    
    // Tooltip methods
    private void displayTooltip() {
	mapView.setBuiltInZoomControls(false); // The built in zoom controls hide the tooltip

        if (tooltipPanel == null) {
            tooltipPanel = ((ViewStub) findViewById(R.id.stub_tooltip)).inflate();
        }
        ((TextView) tooltipPanel.findViewById(R.id.tooltip_overlay_text)).
        setText(R.string.occupation_map_tooltip);
        tooltipPanel.startAnimation(AnimationUtils.loadAnimation(OccupationMapActivity.this,
        	R.anim.slide_in));
        
	closeTooltipHandler = new Handler();
	closeTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
        	tooltipPanel.startAnimation(AnimationUtils.loadAnimation(OccupationMapActivity.this,
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
    
    private void displayStateTooltip() {
	mapView.setBuiltInZoomControls(false); // The built in zoom controls hide the tooltip
	
	// Hide the first tooltip if it's showing
	closeTooltipHandler.removeCallbacks(closeTooltipThread);
	tooltipPanel.setVisibility(View.GONE);
	
	//Toast.makeText(this, R.string.occupation_selection_toolip, Toast.LENGTH_LONG).show();
        if (stateTooltipPanel == null) {
            stateTooltipPanel = ((ViewStub) findViewById(R.id.stub_state_tooltip)).inflate();
        }
        ((TextView) stateTooltipPanel.findViewById(R.id.tooltip_overlay_text)).
        setText(R.string.occupation_map_state_tooltip);
        stateTooltipPanel.startAnimation(AnimationUtils.loadAnimation(OccupationMapActivity.this,
        	R.anim.slide_in));
        
	closeStateTooltipHandler = new Handler();
	closeStateTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
		stateTooltipPanel.startAnimation(AnimationUtils.loadAnimation(OccupationMapActivity.this,
        		R.anim.slide_out));
		stateTooltipPanel.setVisibility(View.GONE);
	    }
	});
        
	postHideStateTooltop();

	// Hide the state tooltip on tap
        stateTooltipPanel.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		closeStateTooltipHandler.removeCallbacks(closeStateTooltipThread);
		stateTooltipPanel.setVisibility(View.GONE);
	    }
	});
    }

    private void postHideStateTooltop() {
        // Start a time to hide the panel after 10 seconds
	closeStateTooltipHandler.removeCallbacks(closeStateTooltipThread);
	closeStateTooltipHandler.postDelayed(closeStateTooltipThread, 10000);
	
	mapView.setBuiltInZoomControls(true);
    }
}
