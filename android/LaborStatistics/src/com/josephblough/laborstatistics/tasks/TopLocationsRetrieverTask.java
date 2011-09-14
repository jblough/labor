package com.josephblough.laborstatistics.tasks;

import java.util.List;

import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

import android.os.AsyncTask;

public class TopLocationsRetrieverTask extends AsyncTask<String, Void, List<DataDataset>> {

    private DataTransportCallback callback;
    private int areatype;
    private int sort;
    
    public TopLocationsRetrieverTask(final int areatype, final int sort, final DataTransportCallback callback) {
	this.areatype = areatype;
	this.sort = sort;
	this.callback = callback;
    }
    
    @Override
    protected List<DataDataset> doInBackground(String... occupations) {
	return DataTransport.getTopLocations(occupations[0], areatype, sort);
    }

    @Override
    protected void onPostExecute(List<DataDataset> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving top locations");
    }
}
