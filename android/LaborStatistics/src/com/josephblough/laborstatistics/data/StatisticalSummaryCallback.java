package com.josephblough.laborstatistics.data;


public interface StatisticalSummaryCallback {

    //Return results
    public void success(StatisticalSummary summary);

    //Error Callback
    public void error(String error);

}
