package com.josephblough.laborstatistics.tasks;

import java.util.List;

import com.josephblough.laborstatistics.data.DataTransportCallback;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

import android.os.AsyncTask;

public class MunicipalityRetrieverTask extends AsyncTask<String, Void, List<DataDataset>> {

    private DataTransportCallback callback;
    private String state;
    
    public MunicipalityRetrieverTask(final String state, final DataTransportCallback callback) {
	this.state = state;
	this.callback = callback;
    }
    
    @Override
    protected List<DataDataset> doInBackground(String... occupations) {
	return DataTransport.getMunicipalities(state, occupations[0]);
    }

    @Override
    protected void onPostExecute(List<DataDataset> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving state municipalities");
    }
}
