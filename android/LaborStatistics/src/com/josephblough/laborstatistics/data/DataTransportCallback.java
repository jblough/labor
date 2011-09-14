package com.josephblough.laborstatistics.data;

import java.util.List;

import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;

public interface DataTransportCallback {

    //Return results
    public void success(List<DataDataset> results);

    //Error Callback
    public void error(String error);

}
