package com.josephblough.laborstatistics.tasks;

import java.util.List;

import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

import android.os.AsyncTask;

public class TopIndustriesRetrieverTask extends AsyncTask<String, Void, List<DataDataset>> {

    private DataTransportCallback callback;
    private int sort;
    
    public TopIndustriesRetrieverTask(final int sort, final DataTransportCallback callback) {
	this.sort = sort;
	this.callback = callback;
    }
    
    @Override
    protected List<DataDataset> doInBackground(String... occupations) {
	return DataTransport.getTopIndustries(occupations[0], sort);
    }

    @Override
    protected void onPostExecute(List<DataDataset> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving top industries");
    }
}
