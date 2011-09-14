package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class AreaDataset implements DeptOfLaborDataset {

    public String areaCode;
    public String areatypeCode;
    public String areaName;
    
    public String getMethod() {
	return "Statistics/OES2010/area";
    }

    public String getFields() {
	return "area_code,areatype_code,area_name";
    }

    public String toString() {
	return areaName;
    }
    
    public String toString(Map<String, String> results) {
	return results.get("area_code") + ", " + results.get("areatype_code") + ", " + results.get("area_name");
    }

}
