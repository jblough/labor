package com.josephblough.laborstatistics.datasets.consumerexpenditure;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class ConsumerExpenditureDataDataset implements DeptOfLaborDataset {

    public String seriesId;
    public int year;
    public int period;
    public float value;
    public String footnoteCodes;
    
    public String getMethod() {
	return "Statistics/ConsumerExpenditure/data";
    }

    public String getFields() {
	return "series_id,year,period,value,footnote_codes";
    }

    public String toString(Map<String, String> results) {
	return results.get("series_id") + ", " + results.get("year") + 
		", " + results.get("period") + ", " + results.get("value") + 
		", " + results.get("footnote_codes");
    }

}
