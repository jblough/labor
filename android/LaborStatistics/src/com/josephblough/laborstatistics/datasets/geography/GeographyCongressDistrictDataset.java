package com.josephblough.laborstatistics.datasets.geography;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class GeographyCongressDistrictDataset implements DeptOfLaborDataset {

    public int countyCode;
    public int stateCode;
    public int district;
    
    public String getMethod() {
	return "Geography/CongressDistrict";
    }

    public String getFields() {
	return "CountyCode,StateCode,District";
    }

    public String toString(Map<String, String> results) {
	return results.get("CountyCode") + ", " + results.get("StateCode") + 
		", " + results.get("District");
    }

}
