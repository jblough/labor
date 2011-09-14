package com.josephblough.laborstatistics.tasks;

import java.util.List;

import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

import android.os.AsyncTask;

public class TopOccupationsRetrieverTask extends AsyncTask<String, Void, List<DataDataset>> {

    private DataTransportCallback callback;
    private int sort;
    
    public TopOccupationsRetrieverTask(final int sort, final DataTransportCallback callback) {
	this.sort = sort;
	this.callback = callback;
    }
    
    @Override
    protected List<DataDataset> doInBackground(String... industries) {
	return DataTransport.getTopOccupations(industries[0], sort);
    }

    @Override
    protected void onPostExecute(List<DataDataset> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving top occupations");
    }
}
