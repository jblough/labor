package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class DataDataset implements DeptOfLaborDataset {

    public String seriesId;
    public String year;
    public String period;
    public String value;
    public String footnoteCodes;
    
    public String getMethod() {
	return "Statistics/OES2010/data";
    }

    public String getMinimumFields() {
	return "series_id,value";
    }

    public String getFields() {
	return "series_id,year,period,value,footnote_codes";
    }

    public String toString(Map<String, String> results) {
	return results.get("series_id") + ", " + results.get("year") + ", " + 
		results.get("period") + ", " + results.get("value") + ", " + results.get("footnote_codes");
    }

}
