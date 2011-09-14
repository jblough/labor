package com.josephblough.laborstatistics.tasks;

import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.data.StatisticalSummary;
import com.josephblough.laborstatistics.data.StatisticalSummaryCallback;

import android.os.AsyncTask;

public class NationalSummaryRetrieverTask extends AsyncTask<String, Void, StatisticalSummary> {

    private StatisticalSummaryCallback callback;
    
    public NationalSummaryRetrieverTask(final StatisticalSummaryCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected StatisticalSummary doInBackground(String... occupations) {
	return DataTransport.getNationalStatisticalSummary(occupations[0]);
    }

    @Override
    protected void onPostExecute(StatisticalSummary summary) {
        super.onPostExecute(summary);
        
        if (summary != null)
            this.callback.success(summary);
        else
            this.callback.error("Error retrieving summary");
    }
}
