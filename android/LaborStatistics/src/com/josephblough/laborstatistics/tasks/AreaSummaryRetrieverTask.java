package com.josephblough.laborstatistics.tasks;

import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.data.StatisticalSummary;
import com.josephblough.laborstatistics.data.StatisticalSummaryCallback;

import android.os.AsyncTask;

public class AreaSummaryRetrieverTask extends AsyncTask<String, Void, StatisticalSummary> {

    private StatisticalSummaryCallback callback;
    private String areaCode;
    
    public AreaSummaryRetrieverTask(final String areaCode, final StatisticalSummaryCallback callback) {
	this.areaCode = areaCode;
	this.callback = callback;
    }
    
    @Override
    protected StatisticalSummary doInBackground(String... occupations) {
	return DataTransport.getStatisticalSummary(occupations[0], areaCode);
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
