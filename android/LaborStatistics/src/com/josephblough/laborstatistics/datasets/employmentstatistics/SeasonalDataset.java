package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class SeasonalDataset implements DeptOfLaborDataset {

    public String seasonal;
    public String seasonalText;
    
    public String getMethod() {
	return "Statistics/OES2010/Seasonal";
    }

    public String getFields() {
	return "seasonal,seasonal_text";
    }

    public String toString(Map<String, String> results) {
	return results.get("seasonal") + ", " + results.get("seasonal_text");
    }

}
