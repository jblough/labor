package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class AreaTypeDataset implements DeptOfLaborDataset {

    public String areatypeCode;
    public String areatypeName;
    
    public String getMethod() {
	return "Statistics/OES2010/areatype";
    }

    public String getFields() {
	return "areatype_code,areatype_name";
    }

    public String toString() {
	return areatypeName;
    }
    
    public String toString(Map<String, String> results) {
	return results.get("areatype_code") + ", " + results.get("areatype_name");
    }

}
