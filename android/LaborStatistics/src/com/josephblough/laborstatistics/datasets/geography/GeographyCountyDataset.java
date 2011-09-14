package com.josephblough.laborstatistics.datasets.geography;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class GeographyCountyDataset implements DeptOfLaborDataset {

    public int countyCode;
    public String countyName;
    
    public String getMethod() {
	return "Geography/County";
    }

    public String getFields() {
	return "CountyCode,CountyName";
    }

    public String toString(Map<String, String> results) {
	return results.get("CountyCode") + ", " + results.get("CountyName");
    }

}
