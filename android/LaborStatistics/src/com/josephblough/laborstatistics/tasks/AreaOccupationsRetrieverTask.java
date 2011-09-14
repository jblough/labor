package com.josephblough.laborstatistics.tasks;

import java.util.List;

import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

import android.os.AsyncTask;

public class AreaOccupationsRetrieverTask extends AsyncTask<String, Void, List<DataDataset>> {

    private DataTransportCallback callback;
    private int sort;
    
    public AreaOccupationsRetrieverTask(final int sort, final DataTransportCallback callback) {
	this.sort = sort;
	this.callback = callback;
    }
    
    @Override
    protected List<DataDataset> doInBackground(String... areas) {
	return DataTransport.getTopOccupationsForArea(areas[0], sort);
    }

    @Override
    protected void onPostExecute(List<DataDataset> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving occupations");
    }
}
