package com.josephblough.laborstatistics.activities.comparison;

import java.util.ArrayList;
import java.util.List;

import com.josephblough.laborstatistics.ApplicationController;
import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.DocumentationActivity;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.StateLibrary;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ComparisonLocationSelectionActivity extends MapActivity implements OnClickListener {

    // This activity will display a national map
    // Start the map centered at the current location.
    // When the map has been zoomed in enough to be more certain
    //	we know which state they want, pull up the 
    //	more top occupations in that area
    // Maybe provide a way to see top industries?
    
    private static final String TAG = "ComparisonLocationSelectionActivity";
    
    public static final String AREA_TYPE = "ComparisonLocationSelectionActivity.area_type";

    public static final int AREA_TYPE_STATE = 0;
    public static final int AREA_TYPE_CITY = 1;
    
    private List<AreaDataset> states = null;
    private List<AreaDataset> cities = null;
    
    private MapView mapView = null;
    private Button confirmButton = null;
    private ProgressDialog progress = null;
    private int areaType = 0;

    private View tooltipPanel;
    private Handler closeTooltipHandler;
    private Thread closeTooltipThread;
    
    private View stateTooltipPanel;
    private Handler closeStateTooltipHandler;
    private Thread closeStateTooltipThread;
    private boolean displayedStateTooltip = false;
    
    private View cityTooltipPanel;
    private Handler closeCityTooltipHandler;
    private Thread closeCityTooltipThread;
    private boolean displayedCityTooltip = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.map_with_button_layout);

        confirmButton = (Button) findViewById(R.id.confirm_location_selection);
        confirmButton.setVisibility(View.GONE);
        confirmButton.setOnClickListener(this);
        
        areaType = getIntent().getIntExtra(AREA_TYPE, AREA_TYPE_STATE);
        
        mapView = (MapView) findViewById(R.id.mapview);
        /*
        mapView.getController().setZoom(7);
        mapView.setBuiltInZoomControls(true);
        
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            mapView.getController().setCenter(StateLibrary.getPoint(location.getLatitude(), location.getLongitude()));
        }
         */
        
        progress = ProgressDialog.show(this, "Rendering", "Rendering map", true, true);
        new MapLoaderTask().execute();
    }
    
    @Override
    protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
    }

    private class MapLoaderTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... arg0) {
		states = DataLibrary.getAreas(ComparisonLocationSelectionActivity.this, "S");
	        
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

    public void loadStateMunicipalities(final String state) {
	//progress = ProgressDialog.show(this, "Downloading", "Retrieving " + state + " municipalities");
	
	final String stateAbbreviation = StateLibrary.getStateAbbreviation(this, state);
	if (stateAbbreviation != null) {
	    cities =  DataLibrary.getMunicipalities(ComparisonLocationSelectionActivity.this, StateLibrary.getStateAbbreviation(ComparisonLocationSelectionActivity.this, state));
	    Drawable marker = getResources().getDrawable(R.drawable.city_marker);
	    marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

	    Drawable highlightedMarker = getResources().getDrawable(R.drawable.highlighted_city_marker);
	    highlightedMarker.setBounds(0, 0, highlightedMarker.getIntrinsicWidth(), highlightedMarker.getIntrinsicHeight());

	    MunicipalitiesOverlay overlay = new MunicipalitiesOverlay(marker, highlightedMarker, mapView);
	    mapView.getOverlays().add(overlay);
	    mapView.invalidate();
	}	
    }
    
    private class StatesOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private List<OverlayItem> items = new ArrayList<OverlayItem>();
	private Drawable marker = null;
	private Drawable highlightedMarker = null;
	
	public StatesOverlay(Drawable marker, Drawable highlightedMarker, MapView mapView) {
	    super(marker, mapView);
	    this.marker = marker;
	    this.highlightedMarker = highlightedMarker;
	    setBalloonBottomOffset(32);
	    
	    boundCenterBottom(marker);
	    boundCenterBottom(highlightedMarker);
	    
	    Log.i(TAG, "Looping through " + states.size() + " states");
	    for (AreaDataset state : states) {
		//Log.i(TAG, "Value: " + stateSalary.value);
		GeoPoint point = DataLibrary.getGeoPoint(ComparisonLocationSelectionActivity.this, state.areaCode);
		
		if (point != null) {
		    //Log.i(TAG, "Adding marker for " + stateName);
		    items.add(new OverlayItem(point, state.areaName, (areaType == AREA_TYPE_CITY) ? "Tap balloon to retrieve " + state.areaName + " cities" : ""));
		}
		else {
		    Log.e(TAG, "State " + state.areaName + " has no coordinates");
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
	    ((ApplicationController)ComparisonLocationSelectionActivity.this.getApplicationContext()).selectedState = 
			DataLibrary.getStateAreaCode(ComparisonLocationSelectionActivity.this, tappedItem.getTitle());
	    
	    if (areaType == AREA_TYPE_STATE) {
		confirmButton.setText("Select " + getItem(i).getTitle());
		confirmButton.setVisibility(View.VISIBLE);
	    }
	    
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
	    
	    if (areaType == AREA_TYPE_CITY) {
		ComparisonLocationSelectionActivity.this.loadStateMunicipalities(item.getTitle());
	    }
	    return true;
	}
    }

    private class MunicipalitiesOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private List<OverlayItem> items = new ArrayList<OverlayItem>();
	private Drawable marker = null;
	private Drawable highlightedMarker = null;
	
	public MunicipalitiesOverlay(Drawable marker, Drawable highlightedMarker, MapView mapView) {
	    super(marker, mapView);
	    this.marker = marker;
	    this.highlightedMarker = highlightedMarker;
	    setBalloonBottomOffset(32);
	    
	    boundCenterBottom(marker);
	    boundCenterBottom(highlightedMarker);
	    
	    Log.i(TAG, "Looping through " + cities.size() + " municipalities");
	    for (AreaDataset city : cities) {
		GeoPoint point = DataLibrary.getGeoPoint(ComparisonLocationSelectionActivity.this, city.areaCode);
		if (point != null) {
		    //Log.i(TAG, "Adding marker for " + cityName);
		    items.add(new OverlayItem(point, city.areaName, ""));
		}
		else {
		    Log.e(TAG, "City " + city.areaName + " has no coordinates");
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
	    ((ApplicationController)ComparisonLocationSelectionActivity.this.getApplicationContext()).selectedMunicipality = 
		    DataLibrary.getMunicipalityAreaCode(ComparisonLocationSelectionActivity.this, getItem(i).getTitle());

	    if (areaType == AREA_TYPE_CITY) {
		confirmButton.setText("Select " + getItem(i).getTitle());
		confirmButton.setVisibility(View.VISIBLE);
	    }
	    
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
	    
	    if (!displayedCityTooltip) {
		displayCityTooltip();
		displayedCityTooltip = true;
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

    public void onClick(View v) {
	String areaName = confirmButton.getText().toString().replaceFirst("Select ", "");
	AreaDataset area = DataLibrary.getAreaByName(this, areaName);
	if (area != null) {
	    // A location was selected.  Return the location area code
	    Intent data = new Intent();
	    data.putExtra(ComparisonActivity.AREA_CODE, area.areaCode);
	    setResult(RESULT_OK, data);
	    finish();
	}
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
	    intent.putExtra(DocumentationActivity.DEFAULT_PAGE, DocumentationActivity.COMPARE_PAGE);
	    startActivity(intent);
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    // Tooltip methods
    private void displayTooltip() {
	mapView.setBuiltInZoomControls(false); // The built in zoom controls hide the tooltip

        if (tooltipPanel == null) {
            tooltipPanel = ((ViewStub) findViewById(R.id.stub_tooltip)).inflate();
        }
        ((TextView) tooltipPanel.findViewById(R.id.tooltip_overlay_text)).
        setText(R.string.compare_map_tooltip);
        tooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonLocationSelectionActivity.this,
        	R.anim.slide_in));
        
	closeTooltipHandler = new Handler();
	closeTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
        	tooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonLocationSelectionActivity.this,
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
	
        if (stateTooltipPanel == null) {
            stateTooltipPanel = ((ViewStub) findViewById(R.id.stub_state_tooltip)).inflate();
        }
        ((TextView) stateTooltipPanel.findViewById(R.id.tooltip_overlay_text)).
        setText( (areaType == AREA_TYPE_CITY) ? R.string.compare_city_map_select_state_tooltip : R.string.compare_state_map_select_state_tooltip);
        stateTooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonLocationSelectionActivity.this,
        	R.anim.slide_in));
        
	closeStateTooltipHandler = new Handler();
	closeStateTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
		stateTooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonLocationSelectionActivity.this,
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
	
	//mapView.setBuiltInZoomControls(true);
    }
    
    private void displayCityTooltip() {
	mapView.setBuiltInZoomControls(false); // The built in zoom controls hide the tooltip
	
	// Hide the first tooltip if it's showing
	closeTooltipHandler.removeCallbacks(closeTooltipThread);
	tooltipPanel.setVisibility(View.GONE);
	
	closeStateTooltipHandler.removeCallbacks(closeStateTooltipThread);
	stateTooltipPanel.setVisibility(View.GONE);
	
        if (cityTooltipPanel == null) {
            cityTooltipPanel = ((ViewStub) findViewById(R.id.stub_city_tooltip)).inflate();
        }
        ((TextView) cityTooltipPanel.findViewById(R.id.tooltip_overlay_text)).
        setText(R.string.compare_city_map_select_city_tooltip);
        cityTooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonLocationSelectionActivity.this,
        	R.anim.slide_in));
        
	closeCityTooltipHandler = new Handler();
	closeCityTooltipThread = new Thread(new Runnable() {
	    
	    public void run() {
		cityTooltipPanel.startAnimation(AnimationUtils.loadAnimation(ComparisonLocationSelectionActivity.this,
        		R.anim.slide_out));
		cityTooltipPanel.setVisibility(View.GONE);
	    }
	});
        
	postHideCityTooltop();

	// Hide the state tooltip on tap
        cityTooltipPanel.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		closeCityTooltipHandler.removeCallbacks(closeCityTooltipThread);
		cityTooltipPanel.setVisibility(View.GONE);
	    }
	});
    }

    private void postHideCityTooltop() {
        // Start a time to hide the panel after 10 seconds
	closeCityTooltipHandler.removeCallbacks(closeCityTooltipThread);
	closeCityTooltipHandler.postDelayed(closeCityTooltipThread, 10000);
	
	mapView.setBuiltInZoomControls(true);
    }
}
